package com.fabric.invoker;

import java.io.File ;
import java.nio.charset.StandardCharsets ;
import java.util.ArrayList ;
import java.util.Collection ;
import java.util.HashMap ;
import java.util.Map ;
import java.util.Properties ;
import java.util.concurrent.CompletableFuture ;

import org.apache.commons.io.FileUtils ;
import org.hyperledger.fabric.protos.common.Common.Block ;
import org.hyperledger.fabric.sdk.BlockEvent ;
import org.hyperledger.fabric.sdk.BlockListener ;
import org.hyperledger.fabric.sdk.ChaincodeID ;
import org.hyperledger.fabric.sdk.ChaincodeResponse.Status ;
import org.hyperledger.fabric.sdk.Channel ;
import org.hyperledger.fabric.sdk.HFClient ;
import org.hyperledger.fabric.sdk.Orderer ;
import org.hyperledger.fabric.sdk.Peer ;
import org.hyperledger.fabric.sdk.ProposalResponse ;
import org.hyperledger.fabric.sdk.QueryByChaincodeRequest ;
import org.hyperledger.fabric.sdk.TransactionProposalRequest ;
import org.hyperledger.fabric.sdk.security.CryptoSuite ;
import org.json.JSONArray ;
import org.json.JSONObject ;

import com.fabric.config.FabricConfig ;
import com.fabric.org.OrgChannel ;
import com.fabric.org.PeerOrganisations ;
import com.fabric.user.UserImpl ;
import com.google.gson.Gson ;

public class InvokePrivateChaincodeData
{

	public String invokePrivateTransaction(OrgChannel orgChannel, FabricConfig fabric, JSONObject args,UserImpl user) {
		 try {
		        CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
		        HFClient hfClient = HFClient.createNewInstance();
		        hfClient.setCryptoSuite(cryptoSuite);

		        hfClient.setUserContext(user);

		        BlockListener blockListener = new BlockListener() {
		            @Override
		            public void received(BlockEvent arg0) {
		                Block block = arg0.getBlock();
		            }
		        };

		        File file = new File(fabric.getOrderers().get(0).getTlsca());
		        String pemContent = FileUtils.readFileToString(file);

		        Properties orderer_properties = new Properties();
		        orderer_properties.put("pemBytes", pemContent.getBytes());
		        orderer_properties.setProperty("sslProvider", "openSSL");
		        orderer_properties.setProperty("negotiationType", "TLS");

		        Orderer orderer = hfClient.newOrderer(
		            fabric.getOrderers().get(0).getId(),
		            fabric.getOrderers().get(0).getUrl(),
		            orderer_properties
		        );

		        Channel channel = hfClient.newChannel(orgChannel.getChannelName());

		        for (PeerOrganisations peerorg: fabric.getPeers()) {
		            file = new File(peerorg.getTlsca());
		            pemContent = FileUtils.readFileToString(file);

		            Properties peer_properties = new Properties();
		            peer_properties.put("pemBytes", pemContent.getBytes());
		            peer_properties.setProperty("sslProvider", "openSSL");
		            peer_properties.setProperty("negotiationType", "TLS");

		            Peer peer = hfClient.newPeer(peerorg.getId(), peerorg.getUrl(), peer_properties);
		            channel.addPeer(peer);
		        }

		        channel.registerBlockListener(blockListener);
		        channel.addOrderer(orderer);
		        channel.initialize();

//		        // create transient map with private data
//		        Map<String, byte[]> transientMap = new HashMap<String, byte[]>();
//		        transientMap.put("key1", "privateData1".getBytes());
//		        transientMap.put("key2", "privateData2".getBytes());
		        
		        
		        Gson gson = new Gson();
		        Map<String, String> jsonMap = gson.fromJson(args.getJSONObject("transient").toString(), HashMap.class);

		        // Create a transient map
		        Map<String, byte[]> transientMap = new HashMap<>();

		        // Insert key-value pairs from JSON object into transient map
		        for (Map.Entry<String, String> entry : jsonMap.entrySet()) {
		            String key = entry.getKey();
		            String value = entry.getValue();
		            byte[] valueBytes = value.getBytes(StandardCharsets.UTF_8);
		            transientMap.put(key, valueBytes);
		        }
		        
		        
		        
		        
		        
		        
		        

		        TransactionProposalRequest request = hfClient.newTransactionProposalRequest();
		        String cc = args.getString("chaincodeName");
		        ChaincodeID ccid = ChaincodeID.newBuilder().setName(cc).build();

		        request.setChaincodeID(ccid);
		        request.setFcn(args.getString("functionName"));
		        JSONArray arrJson = args.getJSONArray("args");
		        String[] arguments = new String[arrJson.length()];
		        for (int i = 0; i < arrJson.length(); i++) {
		            arguments[i] = arrJson.getString(i);
		        }

		        request.setArgs(arguments);
		        request.setProposalWaitTime(3000);
		        
		        // set the transient map in the transaction proposal request
		        request.setTransientMap(transientMap);

		        Collection<ProposalResponse> responses = channel.sendTransactionProposal(request);

		        ArrayList<ProposalResponse> success = new ArrayList<ProposalResponse>();
		        ArrayList<ProposalResponse> failed = new ArrayList<ProposalResponse>();

		        for (ProposalResponse response : responses) {
		            if (response.getStatus() == Status.SUCCESS) {
		                success.add(response);
		            } else {
		                failed.add(response);
		            }
		        }

		        if (failed.size() > 0) {
		            return "Transaction failed";
		        }

		        // send transaction to orderer
		        CompletableFuture<BlockEvent.TransactionEvent> future = channel.sendTransaction(success);

		        BlockEvent.TransactionEvent event = future.get();

		        return "Transaction succeeded";

		    } catch (Exception e) {
		        e.printStackTrace();
		        return "Transaction failed";
		    }	
	
	}


	public String queryPrivateData(OrgChannel orgChannel, FabricConfig fabric, JSONObject args, UserImpl user) {
	    try {
	        CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
	        HFClient hfClient = HFClient.createNewInstance();
	        hfClient.setCryptoSuite(cryptoSuite);

	        hfClient.setUserContext(user);

	        File file = new File(fabric.getOrderers().get(0).getTlsca());
	        String pemContent = FileUtils.readFileToString(file);

	        Properties orderer_properties = new Properties();
	        orderer_properties.put("pemBytes", pemContent.getBytes());
	        orderer_properties.setProperty("sslProvider", "openSSL");
	        orderer_properties.setProperty("negotiationType", "TLS");

	        Orderer orderer = hfClient.newOrderer(
	                fabric.getOrderers().get(0).getId(),
	                fabric.getOrderers().get(0).getUrl(),
	                orderer_properties
	        );

	        Channel channel = hfClient.newChannel(orgChannel.getChannelName());

	        for (PeerOrganisations peerorg : fabric.getPeers()) {
	            file = new File(peerorg.getTlsca());
	            pemContent = FileUtils.readFileToString(file);

	            Properties peer_properties = new Properties();
	            peer_properties.put("pemBytes", pemContent.getBytes());
	            peer_properties.setProperty("sslProvider", "openSSL");
	            peer_properties.setProperty("negotiationType", "TLS");

	            Peer peer = hfClient.newPeer(peerorg.getId(), peerorg.getUrl(), peer_properties);
	            channel.addPeer(peer);
	        }

	        channel.addOrderer(orderer);
	        channel.initialize();

	       
	        // Create a transient map with private data
	        Gson gson = new Gson();
	        Map<String, String> jsonMap = gson.fromJson(args.getJSONObject("transient").toString(), HashMap.class);

	        // Insert key-value pairs from JSON object into transient map
	        Map<String, byte[]> transientMap = new HashMap<>();
	        for (Map.Entry<String, String> entry : jsonMap.entrySet()) {
	            String key = entry.getKey();
	            String value = entry.getValue();
	            byte[] valueBytes = value.getBytes(StandardCharsets.UTF_8);
	            transientMap.put(key, valueBytes);
	        }

	        

	        // Use the QueryByChaincodeRequest object to query private data
	        QueryByChaincodeRequest queryRequest = hfClient.newQueryProposalRequest();
	        String cc = args.getString("chaincodeName");
	        ChaincodeID ccid = ChaincodeID.newBuilder().setName(cc).build();
	        queryRequest.setChaincodeID(ccid);

	        queryRequest.setFcn(args.getString("functionName"));
	        
	        JSONArray arrJson = args.getJSONArray("args");
	        String[] arguments = new String[arrJson.length()];
	        for (int i = 0; i < arrJson.length(); i++) {
	            arguments[i] = arrJson.getString(i);
	        }

	        queryRequest.setArgs(arguments);       
	        queryRequest.setTransientMap(transientMap);
	        queryRequest.setProposalWaitTime(3000);

	        Collection<ProposalResponse> queryResponses = channel.queryByChaincode(queryRequest);

	       

	        for (ProposalResponse res : queryResponses) {
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
