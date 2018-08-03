package com.pankaj.test.testdyndb;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TestdyndbApplication {

	static{
		//AwsDynamoDbHelper.initSqLite();
	}

	public static void main(String[] args) {
		SpringApplication.run(TestdyndbApplication.class, args);
	}

	@Bean
	public CommandLineRunner get() {
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				try {
					System.setProperty("sqlite4java.debug", "true");
					System.out.println("running dynamo db local");
					DynamoDbLocal.main(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
	}
}
