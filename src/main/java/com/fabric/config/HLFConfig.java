package com.fabric.config;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@Component
public class HLFConfig {
	FabricConfig fabricConfig;
	
	
	@Bean
	public void loadConfig() throws IOException {
		Gson gsonInstance=new Gson();
		 File file = new File("config/config.json");
	       	String config=FileUtils.readFileToString(file);
		this.setFabricConfig(gsonInstance.fromJson(config, FabricConfig.class));
		System.out.println(fabricConfig.getOrderers().get(0).toString());
		System.out.println(fabricConfig.getPeers().get(0).toString());
		System.out.println(fabricConfig.getPeers().get(0).getCaOrganisation().toString());
	}


	public FabricConfig getFabricConfig() {
		return fabricConfig;
	}


	public void setFabricConfig(FabricConfig fabricConfig) {
		this.fabricConfig = fabricConfig;
	}

}
