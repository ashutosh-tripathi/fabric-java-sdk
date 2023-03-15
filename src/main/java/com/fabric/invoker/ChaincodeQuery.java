package com.fabric.invoker;

import java.io.File ;
import java.util.ArrayList ;
import java.util.Collection ;
import java.util.Properties ;
import java.util.stream.Collectors ;

import org.apache.commons.io.FileUtils ;
import org.hyperledger.fabric.sdk.ChaincodeID ;
import org.hyperledger.fabric.sdk.ChaincodeResponse.Status ;
import org.hyperledger.fabric.sdk.Channel ;
import org.hyperledger.fabric.sdk.HFClient ;
import org.hyperledger.fabric.sdk.Orderer ;
import org.hyperledger.fabric.sdk.Peer ;
import org.hyperledger.fabric.sdk.ProposalResponse ;
import org.hyperledger.fabric.sdk.QueryByChaincodeRequest ;
import org.hyperledger.fabric.sdk.security.CryptoSuite ;
import org.json.JSONArray ;
import org.json.JSONObject ;

import com.fabric.config.FabricConfig ;
import com.fabric.org.OrgChannel ;
import com.fabric.org.PeerOrganisations ;
import com.fabric.user.UserImpl ;

public class ChaincodeQuery
{
	
	
	public String queryChaincode(OrgChannel orgChannel,FabricConfig fabric,JSONObject args,UserImpl user)
	{
try {
	// Create HFClient and set user context
	CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
	HFClient hfClient = HFClient.createNewInstance();
	hfClient.setCryptoSuite(cryptoSuite);
	hfClient.setUserContext(user);

	// Set up the peer and channel
	ArrayList<PeerOrganisations> peers = (ArrayList<PeerOrganisations>) fabric.getPeers().stream()
	    .filter(peer -> peer.getOrgName().equalsIgnoreCase(user.getAccount())).collect(Collectors.toList());
	File peerTlsCaFile = new File(peers.get(0).getTlsca());
	String pemContent = FileUtils.readFileToString(peerTlsCaFile);
	Properties peerProperties = new Properties();
	peerProperties.put("pemBytes", pemContent.getBytes());
	peerProperties.setProperty("sslProvider", "openSSL");
	peerProperties.setProperty("negotiationType", "TLS");
	Peer peer = hfClient.newPeer(peers.get(0).getId(), peers.get(0).getUrl(), peerProperties);
	File ordererTlsCaFile = new File(fabric.getOrderers().get(0).getTlsca());
	pemContent = FileUtils.readFileToString(ordererTlsCaFile);
	Properties ordererProperties = new Properties();
	ordererProperties.put("pemBytes", pemContent.getBytes());
	ordererProperties.setProperty("sslProvider", "openSSL");
	ordererProperties.setProperty("negotiationType", "TLS");
	Orderer orderer = hfClient.newOrderer(fabric.getOrderers().get(0).getId(), fabric.getOrderers().get(0).getUrl(), ordererProperties);
	Channel channel = hfClient.newChannel(orgChannel.getChannelName());
	channel.addPeer(peer);
	channel.addOrderer(orderer);
	channel.initialize();

	// Create the query request
	QueryByChaincodeRequest queryRequest = hfClient.newQueryProposalRequest();
	ChaincodeID ccid = ChaincodeID.newBuilder().setName(args.getString("chaincodeName")).build();
	queryRequest.setChaincodeID(ccid);
	queryRequest.setFcn(args.getString("functionName")); // Chaincode query function name
	JSONArray arrJson = args.getJSONArray("args");
	String[] arguments = new String[arrJson.length()];
	for (int i = 0; i < arrJson.length(); i++) {
	    arguments[i] = arrJson.getString(i);
	}
	queryRequest.setArgs(arguments);

	// Send the query request and process the response
	Collection<ProposalResponse> responses = channel.queryByChaincode(queryRequest);
	for (ProposalResponse res : responses) {
	    if (res.getStatus().equals(Status.SUCCESS)) {
	        String payload = res.getProposalResponse().getResponse().getPayload().toStringUtf8();
	        // Process the query result here
	        System.out.println("Query Result: " + payload);
	        return payload;
	    } else {
	        // Handle query error
	        System.out.println("Query Error: " + res.getMessage());
	        return "Failed";
	    }
	}

}
catch(Exception ex)
{
	ex.printStackTrace();
}
return null ;


}
}
