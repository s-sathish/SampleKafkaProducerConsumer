package com.sathish.KPC;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static com.sathish.KPC.utils.Constants.DLQ_TOPIC_NAMES_AND_PARTITIONS;
import static com.sathish.KPC.utils.LoggingUtils.doLogInfoWithMessageAndObject;

@SpringBootApplication
public class KpcApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(KpcApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) {
		doLogInfoWithMessageAndObject("DLQ topic partition map created = {}", DLQ_TOPIC_NAMES_AND_PARTITIONS());
	}
}
