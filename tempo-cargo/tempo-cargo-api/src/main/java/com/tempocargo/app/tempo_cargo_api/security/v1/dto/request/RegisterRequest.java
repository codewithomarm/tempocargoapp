package com.tempocargo.app.tempo_cargo_api.security.v1.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class RegisterRequest {

    @NotNull(message = "RegisterRequest's client should not be null")
    private ClientDTO client;

    private IndividualDTO individual;

    private BusinessDTO business;

    @NotNull(message = "RegisterRequest's user should not be null")
    private UserDTO user;

    @AssertTrue(message = "Individual data required for clientType INDIVIDUAL; Business data required for clientType BUSINESS")
    public boolean isValidClientData() {
        if (client == null || client.getClientTypeCode() == null) {
            return false;
        }

        return switch (client.getClientTypeCode()) {
            case INDV -> individual != null && business == null;
            case BUSN -> business != null && individual == null;
        };
    }

    @Data
    @Builder
    public static class ClientDTO {
        @NotBlank(message = "RegisterRequest's Client's phoneNumberPrimary should not be blank")
        @Size(min = 13, max = 14, message = "RegisterRequest's Client's phoneNumberPrimary should be 13 chars min and 14 chars max")
        private String phoneNumberPrimary;

        @Size(min = 13, max = 14, message = "RegisterRequest's Client's phoneNumberSecondary should be 13 chars min and 14 chars max")
        private String phoneNumberSecondary;

        @NotNull(message = "RegisterRequest's Client's clientTypeCode should not be null")
        private ClientTypeCode clientTypeCode;

        private Long referredByClientId;

        public enum ClientTypeCode {
            INDV,
            BUSN;

            @JsonCreator
            public static ClientTypeCode fromString(String value) {
                if (value == null) return null;
                return switch (value.toUpperCase()) {
                    case "INDV" -> INDV;
                    case "BUSN" -> BUSN;
                    default -> throw new IllegalArgumentException("Invalid clientType code: " + value);
                };
            }
        }
    }

    @Data
    @Builder
    public static class IndividualDTO {
        @NotBlank(message = "RegisterRequest's Individual's firstName should not be blank")
        @Size(min = 2, max = 50, message = "RegisterRequest's Individual's firstName should be 2 chars min and 50 chars max")
        private String firstName;

        @NotBlank(message = "RegisterRequest's Individual's lastName should not be blank")
        @Size(min = 2, max = 50, message = "RegisterRequest's Individual's lastName should be 2 chars min and 50 chars max")
        private String lastName;

        @NotNull(message = "RegisterRequest's Individual's identityTypeId should not be null")
        private Long identityTypeId;

        @NotBlank(message = "RegisterRequest's Individual's identityNumber should not be blank")
        private String identityNumber;

        @NotNull(message = "RegisterRequest's Individual's dateOfBirth should not be null")
        @Past(message = "RegisterRequest's Individual's dateOfBirth must be on the past")
        @JsonFormat(pattern = "dd-MM-yyyy")
        private LocalDate dateOfBirth;
    }

    @Data
    @Builder
    public static class BusinessDTO {
        @NotBlank(message = "RegisterRequest's Business's name should not be blank")
        @Size(min = 3, max = 100, message = "RegisterRequest's Business's name should be between 3 chars and 100 chars")
        private String name;

        @NotBlank(message = "RegisterRequest's Business's rucNumber should not be blank")
        @Size(min = 7, max = 100, message = "RegisterRequest's Business's rucNumber should be between 7 chars and 100 chars")
        private String rucNumber;

        @NotBlank(message = "RegisterRequest's Business's contactName should not be blank")
        @Size(min = 5, max = 100, message = "RegisterRequest's Business's contactName should be 2 chars min and 100 chars max")
        private String contactName;
    }

    @Data
    @Builder
    public static class UserDTO {
        @NotBlank(message = "RegisterRequest's User's username should not be blank")
        @Size(min = 5, max = 30, message = "RegisterRequest's User's username should be 5 chars min and 30 chars max")
        private String username;

        @NotBlank(message = "RegisterRequest's User's password should not be blank")
        private String password;

        @Email
        private String email;
    }
}
