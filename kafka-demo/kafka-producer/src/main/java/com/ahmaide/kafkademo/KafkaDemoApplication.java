package com.ahmaide.kafkademo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import java.util.Scanner;

@SpringBootApplication
public class KafkaDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaDemoApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(KafkaTemplate<String, String> kafkaTemplate){
			return args -> {
				Scanner in = new Scanner(System.in);
				String data = in.next();
				while(!data.equals("Finish")) {
					kafkaTemplate.send("ahmaide", data);
					System.out.print("Enter Data: ");
					data = in.next();
				}
			};
	}

}
