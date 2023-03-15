package com.fabric.invoker;

import java.io.File ;
import java.nio.charset.Charset ;
import java.nio.charset.StandardCharsets ;
import java.util.ArrayList ;
import java.util.Base64 ;
import java.util.List ;
import java.util.Properties ;
import java.util.stream.Collectors ;

import org.apache.commons.io.FileUtils ;
import org.hyperledger.fabric.protos.common.Common.Block ;
import org.hyperledger.fabric.protos.common.Common.Payload ;
import org.hyperledger.fabric.protos.peer.TransactionPackage.ProcessedTransaction ;
import org.hyperledger.fabric.protos.peer.TransactionPackage.Transaction ;
import org.hyperledger.fabric.sdk.BlockInfo ;
import org.hyperledger.fabric.sdk.BlockInfo.TransactionEnvelopeInfo ;
import org.hyperledger.fabric.sdk.Channel ;
import org.hyperledger.fabric.sdk.HFClient ;
import org.hyperledger.fabric.sdk.Orderer ;
import org.hyperledger.fabric.sdk.Peer ;
import org.hyperledger.fabric.sdk.TransactionInfo ;
import org.hyperledger.fabric.sdk.security.CryptoSuite ;
import org.json.JSONObject ;

import com.fabric.config.FabricConfig ;
import com.fabric.org.OrgChannel ;
import com.fabric.org.PeerOrganisations ;
import com.fabric.user.UserImpl ;
import com.google.protobuf.ByteString ;

public class QueryTransaction
{
	public JSONObject queryTransactionById(OrgChannel orgChannel, FabricConfig fabric, String txId, UserImpl user) {
	    JSONObject result = null;
	    try {
	        CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
	        HFClient hfClient = HFClient.createNewInstance();
	        hfClient.setCryptoSuite(cryptoSuite);
	        hfClient.setUserContext(user);
	        
	        Channel channel = hfClient.newChannel(orgChannel.getChannelName());
	        
	        ArrayList<PeerOrganisations> peers = (ArrayList<PeerOrganisations>) fabric.getPeers().stream()
	                .filter(peer -> peer.getOrgName().equalsIgnoreCase(user.getAccount())).collect(Collectors.toList());
	        
	        for (PeerOrganisations peerorg : fabric.getPeers()) {
	            File file = new File(peerorg.getTlsca());
	            String pemContent = FileUtils.readFileToString(file);
	            Properties peer_properties = new Properties();
	            peer_properties.put("pemBytes", pemContent.getBytes());
	            peer_properties.setProperty("sslProvider", "openSSL");
	            peer_properties.setProperty("negotiationType", "TLS");
	            Peer peer = hfClient.newPeer(peerorg.getId(), peerorg.getUrl(), peer_properties);
	            channel.addPeer(peer);
	        }
	        File file=new File(fabric.getOrderers().get(0).getTlsca());
	        String 	pemContent=FileUtils.readFileToString(file);
	       	System.out.println("pemContent"+pemContent);			    
		    Properties orderer_properties = new Properties();
		    orderer_properties.put("pemBytes", pemContent.getBytes());
		    orderer_properties.setProperty("sslProvider", "openSSL");
		    orderer_properties.setProperty("negotiationType", "TLS");
		    Orderer orderer = hfClient.newOrderer(fabric.getOrderers().get(0).getId(), fabric.getOrderers().get(0).getUrl(), orderer_properties);  
	        channel.addOrderer(orderer);
	        channel.initialize();

	        TransactionInfo txInfo = channel.queryTransactionByID(txId);
	        
	        System.out.println("Transaction ID: " + txInfo.getTransactionID());
	        System.out.println("Validation code: " + txInfo.getValidationCode());
//	        System.out.println("Envelope payload: " + txInfo.getEnvelopePayload().toStringUtf8());
//	        System.out.println("Created at: " + bytesToHex(txInfo.getProcessedTransaction().getTransactionEnvelope().getPayload().toByteArray()));
	        
	        Payload payload = Payload.parseFrom(txInfo.getEnvelope().getPayload());
	       System.out.println(payload.toString());
	       
	        
	       

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return result;
	}
	
	
	public JSONObject queryBlockById(OrgChannel orgChannel, FabricConfig fabric, String blockId, UserImpl user) {
        JSONObject result = null;
        try {
            CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
            HFClient hfClient = HFClient.createNewInstance();
            hfClient.setCryptoSuite(cryptoSuite);
            hfClient.setUserContext(user);

            Channel channel = hfClient.newChannel(orgChannel.getChannelName());

            List<PeerOrganisations> peers = fabric.getPeers().stream()
                    .filter(peer -> peer.getOrgName().equalsIgnoreCase(user.getAccount())).collect(Collectors.toList());

            for (PeerOrganisations peerorg : peers) {
                File file = new File(peerorg.getTlsca());
                String pemContent = FileUtils.readFileToString(file);
                Properties peer_properties = new Properties();
                peer_properties.put("pemBytes", pemContent.getBytes());
                peer_properties.setProperty("sslProvider", "openSSL");
                peer_properties.setProperty("negotiationType", "TLS");
                Peer peer = hfClient.newPeer(peerorg.getId(), peerorg.getUrl(), peer_properties);
                channel.addPeer(peer);
            }

            File file = new File(fabric.getOrderers().get(0).getTlsca());
            String pemContent = FileUtils.readFileToString(file);
            Properties orderer_properties = new Properties();
            orderer_properties.put("pemBytes", pemContent.getBytes());
            orderer_properties.setProperty("sslProvider", "openSSL");
            orderer_properties.setProperty("negotiationType", "TLS");
            Orderer orderer = hfClient.newOrderer(fabric.getOrderers().get(0).getId(),
                    fabric.getOrderers().get(0).getUrl(), orderer_properties);
            channel.addOrderer(orderer);
            channel.initialize();

            BlockInfo blockInfo = channel.queryBlockByNumber(10);
            Block block = blockInfo.getBlock();
            System.out.println("Block number: " + block.getHeader().getNumber());
//            System.out.println("Previous block hash: " + block.getHeader().getPreviousHash().toStringUtf8());
//            byte[] decodedString = Base64.getDecoder().decode(block.getHeader().getPreviousHash().);
            
            String previousHashString = bytesToHex(block.getHeader().getPreviousHash().toByteArray());
         
            System.out.println(previousHashString);
            String dataHashString = bytesToHex(block.getHeader().getDataHash().toByteArray());
            System.out.println(dataHashString) ;

            // You can access individual transactions in the block like this:
            for (BlockInfo.EnvelopeInfo envelopeInfo : blockInfo.getEnvelopeInfos()) {
            	System.out.println(envelopeInfo.getTransactionID()) ;
            	System.out.println(envelopeInfo.getValidationCode()) ;
                }
}
        catch(Exception ex)
        {
        	ex.printStackTrace();
        }
		return result ;
	}
	
	public static String bytesToHex(byte[] bytes) {
	    StringBuilder sb = new StringBuilder();
	    for (byte b : bytes) {
	        sb.append(String.format("%02x", b));
	    }
	    return sb.toString();
	}
}
