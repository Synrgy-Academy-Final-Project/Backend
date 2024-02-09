package com.example.finalProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class FinalProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinalProjectApplication.class, args);

		Path rootDir = Paths.get("").toAbsolutePath();

		// Print the root directory
		System.out.println("Root Directory: " + rootDir.toString());
	}

}
