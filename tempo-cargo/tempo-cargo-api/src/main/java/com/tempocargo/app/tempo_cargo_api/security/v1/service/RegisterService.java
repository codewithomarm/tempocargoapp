package com.tempocargo.app.tempo_cargo_api.security.v1.service;

import com.tempocargo.app.tempo_cargo_api.auth.v1.role.model.TempoRole;
import com.tempocargo.app.tempo_cargo_api.auth.v1.role.repository.TempoRoleRepository;
import com.tempocargo.app.tempo_cargo_api.auth.v1.roleuser.model.TempoRoleUser;
import com.tempocargo.app.tempo_cargo_api.auth.v1.roleuser.repository.TempoRoleUserRepository;
import com.tempocargo.app.tempo_cargo_api.auth.v1.user.model.TempoUser;
import com.tempocargo.app.tempo_cargo_api.auth.v1.user.repository.TempoUserRepository;
import com.tempocargo.app.tempo_cargo_api.client.v1.business.model.Business;
import com.tempocargo.app.tempo_cargo_api.client.v1.business.repository.BusinessRepository;
import com.tempocargo.app.tempo_cargo_api.client.v1.client.model.Client;
import com.tempocargo.app.tempo_cargo_api.client.v1.client.repository.ClientRepository;
import com.tempocargo.app.tempo_cargo_api.client.v1.clienttype.model.ClientType;
import com.tempocargo.app.tempo_cargo_api.client.v1.clienttype.repository.ClientTypeRepository;
import com.tempocargo.app.tempo_cargo_api.client.v1.identitytype.repository.IdentityTypeRepository;
import com.tempocargo.app.tempo_cargo_api.client.v1.individual.model.Individual;
import com.tempocargo.app.tempo_cargo_api.client.v1.individual.repository.IndividualRepository;
import com.tempocargo.app.tempo_cargo_api.common.v1.exception.*;
import com.tempocargo.app.tempo_cargo_api.security.v1.dto.request.EmailVerificationRequest;
import com.tempocargo.app.tempo_cargo_api.security.v1.dto.request.OtpVerificationRequest;
import com.tempocargo.app.tempo_cargo_api.security.v1.dto.request.RegisterRequest;
import com.tempocargo.app.tempo_cargo_api.security.v1.dto.response.EmailVerificationResponse;
import com.tempocargo.app.tempo_cargo_api.security.v1.dto.response.OtpVerificationResponse;
import com.tempocargo.app.tempo_cargo_api.security.v1.dto.response.RegisterResponse;
import com.tempocargo.app.tempo_cargo_api.security.v1.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class RegisterService {

    private final ClientRepository clientRepository;
    private final ClientTypeRepository clientTypeRepository;
    private final IdentityTypeRepository identityTypeRepository;
    private final IndividualRepository individualRepository;
    private final BusinessRepository businessRepository;
    private final TempoUserRepository tempoUserRepository;
    private final TempoRoleRepository tempoRoleRepository;
    private final TempoRoleUserRepository tempoRoleUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MailService mailService;

    public EmailVerificationResponse emailVerification(EmailVerificationRequest request) throws IOException {
        String email = request.getEmail().trim().toLowerCase();

        if (tempoUserRepository.existsByEmail(email)) {
            throw new ResourceAlreadyExistsException("Email already exists: " + email);
        }

        String otp = String.format("%06d", new SecureRandom().nextInt(999999));

        String token = jwtTokenProvider.generateEmailVerificationToken(email, otp);

        mailService.sendEmailVerificationOtp(email, otp);

        return new EmailVerificationResponse(
                "Verification code sent to your email", token,
                jwtTokenProvider.getEmailVerificationTokenExpiryInstant().getEpochSecond());
    }

    public OtpVerificationResponse otpVerification(OtpVerificationRequest request) {
        Jws<Claims> jws = jwtTokenProvider.parseToken(request.getEmailVerificationToken());
        Claims claims = jws.getBody();
        String emailFromToken = claims.getSubject();
        String otpFromToken = claims.get("otp", String.class);
        String type = claims.get("type", String.class);

        if (!"EMAIL_VERIFICATION".equals(type)) {
            throw new InvalidVerificationTokenException("Invalid token type: " + type);
        }

        String cleanRequestEmail = request.getEmail().trim().toLowerCase();

        if (!emailFromToken.equals(cleanRequestEmail) || !otpFromToken.equals(request.getOtp())) {
            throw new InvalidVerificationTokenException("Invalid email or OTP");
        }

        String verifiedToken = jwtTokenProvider.generateEmailVerifiedToken(cleanRequestEmail);

        return new OtpVerificationResponse("Email verified successfully", verifiedToken);
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request, HttpServletRequest servletRequest) {
        String email = jwtTokenProvider.validateEmailVerifiedToken(servletRequest);

        if (tempoUserRepository.existsByEmail(email)) {
            throw new ResourceAlreadyExistsException("Email already exists: " + email);
        }

        Client clientBuilder = buildClient(request);
        Client clientEntity = clientRepository.save(clientBuilder);

        Individual individualEntity = null;
        Business businessEntity = null;

        String clientTypeCode = String.valueOf(request.getClient().getClientTypeCode());
        if (Objects.equals(clientTypeCode, "INDV")) {
            Individual individualBuilder = buildIndividual(request.getIndividual(), clientEntity);
            individualEntity = individualRepository.save(individualBuilder);
        } else if (Objects.equals(clientTypeCode, "BUSN")) {
            Business businessBuilder = buildBusiness(request.getBusiness(), clientEntity);
            businessEntity = businessRepository.save(businessBuilder);
        }

        TempoUser userBuilder = buildUser(request.getUser(), clientEntity, email);
        TempoUser userEntity = tempoUserRepository.save(userBuilder);

        TempoRole customerRole = tempoRoleRepository.findByName("CUSTOMER")
                .orElseThrow();
        TempoRoleUser roleUser = TempoRoleUser.builder()
                .user(userEntity)
                .role(customerRole)
                .build();
        tempoRoleUserRepository.save(roleUser);

        return new RegisterResponse(
                new RegisterResponse.ClientResponse(
                        clientEntity.getId(), clientEntity.getPoBoxNumber(), clientEntity.getPhoneNumberPrimary(),
                        clientEntity.getPhoneNumberSecondary(), clientEntity.getType().getCode(),
                        clientEntity.getReferredBy() != null ? clientEntity.getReferredBy().getId() : null,
                        clientEntity.getCreatedAt().toInstant(ZoneOffset.UTC)
                ),
                individualEntity != null ? new RegisterResponse.IndividualResponse(
                        individualEntity.getFirstName(), individualEntity.getLastName(),
                        individualEntity.getIdentityType().getCode(), individualEntity.getIdentityNumber(),
                        individualEntity.getDateOfBirth(), individualEntity.getNameCode(),
                        individualEntity.getCreatedAt().toInstant(ZoneOffset.UTC)
                ) : null,
                businessEntity != null ? new RegisterResponse.BusinessResponse(
                        businessEntity.getName(), businessEntity.getRucNumber(), businessEntity.getContactName(),
                        businessEntity.getNameCode(), businessEntity.getCreatedAt().toInstant(ZoneOffset.UTC)
                ) : null,
                new RegisterResponse.UserResponse(
                        userEntity.getId(), userEntity.getUsername(), userEntity.getEmail(),
                        userEntity.getCreatedAt().toInstant(ZoneOffset.UTC)
                )
        );
    }

    private Client buildClient(RegisterRequest request){
        Client.ClientBuilder builder = Client.builder()
                .poBoxNumber(Math.toIntExact(clientRepository.getNextPoBoxNumber()))
                .phoneNumberPrimary(request.getClient().getPhoneNumberPrimary());

        if (request.getClient().getPhoneNumberSecondary() != null) {
            builder.phoneNumberSecondary(request.getClient().getPhoneNumberSecondary());
        }

        ClientType clientType = clientTypeRepository
                .findByCode(String.valueOf(request.getClient().getClientTypeCode()))
                .orElseThrow(() -> new ResourceNotFoundException("ClientType not found: " +
                        String.valueOf(request.getClient().getClientTypeCode())));
        builder.type(clientType);

        if (request.getClient().getReferredByClientId() != null) {
            Client referredByClientEntity = clientRepository.findById(request.getClient().getReferredByClientId())
                    .orElseThrow(() -> new ResourceNotFoundException("ReferredByClientId not found: " +
                            request.getClient().getReferredByClientId()));

            builder.referredBy(referredByClientEntity);
        }

        return builder.build();
    }

    private Individual buildIndividual(RegisterRequest.IndividualDTO request, Client client) {
        if (individualRepository.existsByIdentityNumber(request.getIdentityNumber())) {
            throw new ResourceAlreadyExistsException("Identity Number already exists: "
                    + request.getIdentityNumber());
        }

        return Individual.builder()
                .client(client)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dateOfBirth(request.getDateOfBirth())
                .nameCode(generateIndividualNameCode(request.getFirstName(), request.getLastName()))
                .identityType(identityTypeRepository.findById(request.getIdentityTypeId())
                        .orElseThrow(() -> new ResourceNotFoundException("IdentityType not found with id: "
                                + request.getIdentityTypeId())))
                .identityNumber(request.getIdentityNumber())
                .build();
    }

    private String generateIndividualNameCode(String firstName, String lastName) {
        String individualNameCode;

        // Concatenate firstName + " " + lastName
        String fullName = String.join(" ", firstName, lastName);

        if (fullName.length() < 15) {
            individualNameCode = validateIndividualNameCode(fullName);
        } else {
            // Truncate firstName to only first character
            char firstNameTruncated = firstName.charAt(0);
            // New fullName with truncated firstName
            String fullNameWithFirstNameTruncated = String.join(" ", String.valueOf(firstNameTruncated), lastName);
            // Validate truncated fullName length
            if (fullNameWithFirstNameTruncated.length() < 15) {
                individualNameCode = validateIndividualNameCode(fullNameWithFirstNameTruncated);
            } else {
                String lastNameTruncated = lastName.substring(0, 13).trim();
                String fullNameTruncated = String.join(" ", String.valueOf(firstNameTruncated), lastNameTruncated);
                individualNameCode = validateIndividualNameCode(fullNameTruncated);
            }
        }
        return individualNameCode;
    }

    private Business buildBusiness(RegisterRequest.BusinessDTO request, Client client) {
        if (businessRepository.existsByName(request.getName())) {
            throw new ResourceAlreadyExistsException("Business Name already exists: " + request.getName());
        }

        if (businessRepository.existsByRucNumber(request.getRucNumber())) {
            throw new ResourceAlreadyExistsException("Business rucNumber already exists: " + request.getRucNumber());
        }

        return Business.builder()
                .client(client)
                .name(request.getName())
                .rucNumber(request.getRucNumber())
                .contactName(request.getContactName())
                .nameCode(generateBusinessNameCode(request.getName()))
                .build();
    }

    private String generateBusinessNameCode(String name) {
        String businessNameCode;

        if (name.length() < 15) {
            businessNameCode = validateBusinessNameCode(name);
        } else {
            String truncatedName = name.substring(0, 15).trim();
            businessNameCode = validateBusinessNameCode(truncatedName);
        }
        return businessNameCode;
    }

    private String validateIndividualNameCode(String individualNameCode){
        List<String> existing = individualRepository.findAllByNameCodeStartingWith(individualNameCode);

        if (existing.isEmpty()) {
            return individualNameCode;
        }

        int maxSuffix = 0;
        for (String code : existing) {
            if (code.equals(individualNameCode)) {
                maxSuffix = Math.max(maxSuffix, 1);
            } else {
                String suffix = code.replace(individualNameCode, "").trim();
                if (suffix.matches("\\d+")) {
                    maxSuffix = Math.max(maxSuffix, Integer.parseInt(suffix));
                }
            }
        }
        return individualNameCode + " " + (maxSuffix + 1);
    }

    private String validateBusinessNameCode(String businessNameCode){
        List<String> existing = businessRepository.findAllByNameCodeStartingWith(businessNameCode);

        if (existing.isEmpty()) {
            return businessNameCode;
        }

        int maxSuffix = 0;
        for (String code : existing) {
            if (code.equals(businessNameCode)) {
                maxSuffix = Math.max(maxSuffix, 1);
            } else {
                String suffix = code.replace(businessNameCode, "").trim();
                if (suffix.matches("\\d+")) {
                    maxSuffix = Math.max(maxSuffix, Integer.parseInt(suffix));
                }
            }
        }
        return businessNameCode + " " + (maxSuffix + 1);
    }

    private TempoUser buildUser(RegisterRequest.UserDTO request, Client client, String email) {
        String username = request.getUsername().trim().toLowerCase();
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            throw new InvalidUsernameException("Username contains invalid characters");
        }
        if (tempoUserRepository.existsByUsername(username)) {
            throw new ResourceAlreadyExistsException("Username already exists: " + request.getUsername());
        }

        String password = request.getPassword();
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#.])[A-Za-z\\d@$!%*?&#.]{8,64}$")) {
            throw new WeakPasswordException("Password must contain upper, lower, digit, and special character");
        }

        return TempoUser.builder()
                .client(client)
                .username(username)
                .passwordHash(passwordEncoder.encode(password))
                .email(email)
                .build();
    }
}
