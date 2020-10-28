package com.fabric.invoker;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.hyperledger.fabric.protos.common.Common.Block;
import org.hyperledger.fabric.sdk.BlockEvent;
import org.hyperledger.fabric.sdk.BlockListener;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.TransactionProposalRequest;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdk.ChaincodeResponse.Status;
import org.json.JSONArray;
import org.json.JSONObject;

import com.fabric.config.FabricConfig;
import com.fabric.org.OrgChannel;
import com.fabric.org.PeerOrganisations;
import com.fabric.user.UserImpl;

public class ChaincodeInvoker {

	public String invokeChaincode(OrgChannel orgChannel,FabricConfig fabric,JSONObject args,UserImpl user)
	{
		try {
			CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
			HFClient hfClient = HFClient.createNewInstance();
			hfClient.setCryptoSuite(cryptoSuite);
			hfClient.setUserContext(user);
			
			ArrayList<PeerOrganisations> peers=(ArrayList<PeerOrganisations>) fabric.getPeers().stream().filter(peer->peer.getOrgName().equalsIgnoreCase(user.getAccount())).collect(Collectors.toList());
			
			 File file=new File(peers.get(0).getTlsca());
	       	String pemContent=FileUtils.readFileToString(file);
     	System.out.println("pemString"+pemContent);
			Properties peer_properties = new Properties();
			 peer_properties.put("pemBytes", pemContent.getBytes());
			 peer_properties.setProperty("sslProvider", "openSSL");
			 peer_properties.setProperty("negotiationType", "TLS");
			 Peer peer = hfClient.newPeer(peers.get(0).getId(), peers.get(0).getUrl(), peer_properties);
			 
			 
			 BlockListener blockListener = new BlockListener() {         
			        @Override
			        public void received(BlockEvent arg0) {
			            Block block = arg0.getBlock();

			            System.out.println("BLock All FIelds :" + block.getAllFields());
			            System.out.println("BLock Number :" + arg0.getBlockNumber());               

			            System.out.println("In block Listener..");
			        }
			    };


			    file=new File(fabric.getOrderers().get(0).getTlsca());
		       	pemContent=FileUtils.readFileToString(file);
		       	System.out.println("pemContent"+pemContent);			    
			    Properties orderer_properties = new Properties();
			    orderer_properties.put("pemBytes", pemContent.getBytes());
			    orderer_properties.setProperty("sslProvider", "openSSL");
			    orderer_properties.setProperty("negotiationType", "TLS");
			    Orderer orderer = hfClient.newOrderer(fabric.getOrderers().get(0).getId(), fabric.getOrderers().get(0).getUrl(), orderer_properties);
			    
			    Channel channel = hfClient.newChannel(orgChannel.getChannelName());

			    channel.addPeer(peer);
			    channel.registerBlockListener(blockListener);
			    channel.addOrderer(orderer);
			    channel.initialize();
			    
			    TransactionProposalRequest request = hfClient.newTransactionProposalRequest();
			    String cc = args.getString("chaincodeName"); // Chaincode name
			    ChaincodeID ccid = ChaincodeID.newBuilder().setName(cc).build();

			    request.setChaincodeID(ccid);
			    request.setFcn(args.getString("functionName")); // Chaincode invoke funtion name
			    JSONArray arrJson = args.getJSONArray("args");
			    String[] arguments = new String[arrJson.length()];
			    for(int i = 0; i < arrJson.length(); i++)
			    	arguments[i] = arrJson.getString(i);
			    
			    request.setArgs(arguments);
			    request.setProposalWaitTime(3000);
			    Collection<ProposalResponse> responses = channel.sendTransactionProposal(request);
			    ArrayList<ProposalResponse> success=new ArrayList<ProposalResponse>();
			    ArrayList<ProposalResponse> failed=new ArrayList<ProposalResponse>();
			    Set<ProposalResponse> consistencySet = null;
			    for (ProposalResponse res : responses) {
			      // Process response from transaction proposal
			    	if(res.getStatus().equals(Status.SUCCESS))
			    	{
			    		success.add(res);
			    		consistencySet.add(res);
			    	}
			    	else
			    		failed.add(res);
			    }
			    
			    if(failed.size()==0 && consistencySet.size()==1)
			    {
			    	return success.get(0).getProposalResponse().getPayload().toStringUtf8();
			    }
			    else
			    {
			    	return failed.get(0).getMessage();
			    }
			    
			
			
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		
		return null;
	}
	
	
	
	
}
