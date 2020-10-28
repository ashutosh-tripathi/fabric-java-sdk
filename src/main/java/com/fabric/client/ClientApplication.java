package com.fabric.client;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fabric.config.FabricConfig;
import com.google.gson.Gson;

@SpringBootApplication(scanBasePackages = {"com.*"})
public class ClientApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(ClientApplication.class, args);
	
		
	}

}
