package com.accenture.academico;

import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringBootTest
@SpringJUnitConfig
public class BankApplicationTests {

	@Test
	void contextLoads() {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(BankApplication.class);
		builder.run();
	}
}