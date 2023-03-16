package com.fabric.client;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager ;
import org.apache.logging.log4j.Logger ;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fabric.config.FabricConfig;
import com.google.gson.Gson;

@SpringBootApplication(scanBasePackages = {"com.*"})
public class ClientApplication {
	
	private static final Logger logger = LogManager.getLogger(ClientApplication.class);
	public static void main(String[] args) throws IOException {
		SpringApplication.run(ClientApplication.class, args);
		logger.debug("Fabric Java SDK started");
	
		
	}

}
