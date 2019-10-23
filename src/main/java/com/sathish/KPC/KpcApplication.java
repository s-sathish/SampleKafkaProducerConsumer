package com.sathish.KPC;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static com.sathish.KPC.utils.Constants.DLQ_TOPIC_NAMES_AND_PARTITIONS;

@SpringBootApplication
@Log4j2
public class KpcApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(KpcApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) {
		log.info("DLQ topic partition map created = {}", DLQ_TOPIC_NAMES_AND_PARTITIONS());
	}
}
