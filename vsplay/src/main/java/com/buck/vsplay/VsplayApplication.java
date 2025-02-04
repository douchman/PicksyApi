package com.buck.vsplay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableAsync
@SpringBootApplication
public class VsplayApplication {

	public static void main(String[] args) {
		SpringApplication.run(VsplayApplication.class, args);
	}

}
