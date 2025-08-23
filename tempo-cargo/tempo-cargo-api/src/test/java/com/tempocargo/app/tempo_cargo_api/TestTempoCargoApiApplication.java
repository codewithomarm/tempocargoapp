package com.tempocargo.app.tempo_cargo_api;

import org.springframework.boot.SpringApplication;

public class TestTempoCargoApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(TempoCargoApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
