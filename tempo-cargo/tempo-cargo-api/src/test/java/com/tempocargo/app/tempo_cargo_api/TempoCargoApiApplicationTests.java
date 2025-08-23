package com.tempocargo.app.tempo_cargo_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class TempoCargoApiApplicationTests {

	@Test
	void contextLoads() {
	}

}
