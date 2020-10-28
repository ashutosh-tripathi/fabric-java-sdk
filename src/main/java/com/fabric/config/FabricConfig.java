package com.fabric.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.fabric.org.OrgChannel;
import com.fabric.org.OrdererOrganisations;
import com.fabric.org.PeerOrganisations;
import com.google.gson.Gson;


public class FabricConfig {

	public ArrayList<OrdererOrganisations> orderers;
	public ArrayList<PeerOrganisations> peers;
	public ArrayList<OrgChannel> channels;
	public ArrayList<OrdererOrganisations> getOrderers() {
		return orderers;
	}
	public void setOrderers(ArrayList<OrdererOrganisations> orderers) {
		this.orderers = orderers;
	}
	public ArrayList<PeerOrganisations> getPeers() {
		return peers;
	}
	public void setPeers(ArrayList<PeerOrganisations> peers) {
		this.peers = peers;
	}
	public ArrayList<OrgChannel> getChannels() {
		return channels;
	}
	public void setChannels(ArrayList<OrgChannel> channels) {
		this.channels = channels;
	}
	
	
	
	
	
	
}

 






