package com.fabric.user.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fabric.config.HLFConfig;
import com.fabric.invoker.ChaincodeInvoker;
import com.fabric.invoker.ChaincodeQuery ;
import com.fabric.invoker.InvokePrivateChaincodeData ;
import com.fabric.invoker.QueryTransaction ;
import com.fabric.org.PeerOrganisations;
import com.fabric.user.UserImpl;
import com.fabric.user.controller.UserController;

@RestController
public class UserAPI {
	
	@Autowired
	HLFConfig hlfconfig;
	
	@PostMapping("/user")
	public String User(@RequestBody String req) throws IOException
	{
		UserController userController=new UserController();
		JSONObject requestObj=new JSONObject(req);
		ArrayList<PeerOrganisations> peer=(ArrayList<PeerOrganisations>) hlfconfig.getFabricConfig().getPeers().stream().filter(peerOrg-> peerOrg.getOrgName().equalsIgnoreCase(requestObj.getString("orgname"))).collect(Collectors.toList());
		System.out.println("SelectedPeer"+peer.get(0).toString());
		UserImpl user= userController.getUser(requestObj.getString("username"),requestObj.getString("orgname"),peer.get(0).getCaOrganisation(),peer.get(0));
//		return "user1";
		System.out.println(hlfconfig.getFabricConfig().getOrderers().get(0).toString());
		
		return user.toString();
	}
	
	@PostMapping("/invokeChaincode")
	public String invokeChaincode(@RequestBody String req) throws IOException
	{
		UserController userController=new UserController();
		JSONObject requestObj=new JSONObject(req);
		ArrayList<PeerOrganisations> peer=(ArrayList<PeerOrganisations>) hlfconfig.getFabricConfig().getPeers().stream().filter(peerOrg-> peerOrg.getOrgName().equalsIgnoreCase(requestObj.getString("orgname"))).collect(Collectors.toList());
		System.out.println("SelectedPeer"+peer.get(0).toString());
		UserImpl user= userController.getUser(requestObj.getString("username"),requestObj.getString("orgname"),peer.get(0).getCaOrganisation(),peer.get(0));
//		return "user1";
		System.out.println("user fetched"+user.toString());
		ChaincodeInvoker chaincodeInvoker=new ChaincodeInvoker();
		String response=chaincodeInvoker.invokeChaincode(hlfconfig.getFabricConfig().getChannels().get(0), hlfconfig.getFabricConfig(), requestObj, user);
		
		System.out.println("response"+response);
		JSONObject jsonObject=new JSONObject();
		if(response.equalsIgnoreCase("Failed"))
			jsonObject.put("Status", "Failed");
		else
		{
			jsonObject.put("TransactionId", response);
			jsonObject.put("Status","Success");
		}
		return jsonObject.toString();
	}
	
	@PostMapping("/queryChaincode")
	public String queryChaincode(@RequestBody String req) throws IOException
	{
		UserController userController=new UserController();
		JSONObject requestObj=new JSONObject(req);
		ArrayList<PeerOrganisations> peer=(ArrayList<PeerOrganisations>) hlfconfig.getFabricConfig().getPeers().stream().filter(peerOrg-> peerOrg.getOrgName().equalsIgnoreCase(requestObj.getString("orgname"))).collect(Collectors.toList());
		System.out.println("SelectedPeer"+peer.get(0).toString());
		UserImpl user= userController.getUser(requestObj.getString("username"),requestObj.getString("orgname"),peer.get(0).getCaOrganisation(),peer.get(0));
//		return "user1";
		System.out.println("user fetched"+user.toString());
		ChaincodeQuery chaincodeQuery=new ChaincodeQuery();
		String response=chaincodeQuery.queryChaincode(hlfconfig.getFabricConfig().getChannels().get(0), hlfconfig.getFabricConfig(), requestObj, user);
		
		System.out.println("response"+response);
		JSONObject jsonObject=new JSONObject();
		if(response.equalsIgnoreCase("Failed"))
			jsonObject.put("Status", "Failed");
		else
		{
			jsonObject.put("Payload:", response);
			jsonObject.put("Status","Success");
		}
		return jsonObject.toString();
	}
	
	@PostMapping("/queryTransactionById")
	public JSONObject queryTransactionById(@RequestBody String req) throws IOException
	{
		UserController userController=new UserController();
		JSONObject requestObj=new JSONObject(req);
		ArrayList<PeerOrganisations> peer=(ArrayList<PeerOrganisations>) hlfconfig.getFabricConfig().getPeers().stream().filter(peerOrg-> peerOrg.getOrgName().equalsIgnoreCase(requestObj.getString("orgname"))).collect(Collectors.toList());
		System.out.println("SelectedPeer"+peer.get(0).toString());
		UserImpl user= userController.getUser(requestObj.getString("username"),requestObj.getString("orgname"),peer.get(0).getCaOrganisation(),peer.get(0));
//		return "user1";
		System.out.println("user fetched"+user.toString());
		QueryTransaction queryTransaction=new QueryTransaction();
//		QueryTransaction chaincodeQuery=new ChaincodeQuery();
		JSONObject response=queryTransaction.queryTransactionById(hlfconfig.getFabricConfig().getChannels().get(0), hlfconfig.getFabricConfig(), requestObj.getString("txId"), user);
		return response;
	}
	
	
	@PostMapping("/queryBlockById")
	public JSONObject queryBlockById(@RequestBody String req) throws IOException
	{
		UserController userController=new UserController();
		JSONObject requestObj=new JSONObject(req);
		ArrayList<PeerOrganisations> peer=(ArrayList<PeerOrganisations>) hlfconfig.getFabricConfig().getPeers().stream().filter(peerOrg-> peerOrg.getOrgName().equalsIgnoreCase(requestObj.getString("orgname"))).collect(Collectors.toList());
		System.out.println("SelectedPeer"+peer.get(0).toString());
		UserImpl user= userController.getUser(requestObj.getString("username"),requestObj.getString("orgname"),peer.get(0).getCaOrganisation(),peer.get(0));
//		return "user1";
		System.out.println("user fetched"+user.toString());
		QueryTransaction queryTransaction=new QueryTransaction();
//		QueryTransaction chaincodeQuery=new ChaincodeQuery();
		JSONObject response=queryTransaction.queryBlockById(hlfconfig.getFabricConfig().getChannels().get(0), hlfconfig.getFabricConfig(), requestObj.getString("blockId"), user);
		return response;
	}

	
	
	
	@PostMapping("/invokePrivateTransaction")
	public String invokePrivateTransaction(@RequestBody String req) throws IOException
	{
		UserController userController=new UserController();
		JSONObject requestObj=new JSONObject(req);
		ArrayList<PeerOrganisations> peer=(ArrayList<PeerOrganisations>) hlfconfig.getFabricConfig().getPeers().stream().filter(peerOrg-> peerOrg.getOrgName().equalsIgnoreCase(requestObj.getString("orgname"))).collect(Collectors.toList());
		System.out.println("SelectedPeer"+peer.get(0).toString());
		UserImpl user= userController.getUser(requestObj.getString("username"),requestObj.getString("orgname"),peer.get(0).getCaOrganisation(),peer.get(0));
//		return "user1";
		System.out.println("user fetched"+user.toString());
		InvokePrivateChaincodeData chaincodeInvoker=new InvokePrivateChaincodeData();
		String response=chaincodeInvoker.invokePrivateTransaction(hlfconfig.getFabricConfig().getChannels().get(0), hlfconfig.getFabricConfig(), requestObj, user);
		
		System.out.println("response"+response);
		JSONObject jsonObject=new JSONObject();
		if(response.equalsIgnoreCase("Failed"))
			jsonObject.put("Status", "Failed");
		else
		{
			jsonObject.put("TransactionId", response);
			jsonObject.put("Status","Success");
		}
		return jsonObject.toString();
	}
	
	
	@PostMapping("/queryPrivateData")
	public String queryPrivateData(@RequestBody String req) throws IOException
	{
		UserController userController=new UserController();
		JSONObject requestObj=new JSONObject(req);
		ArrayList<PeerOrganisations> peer=(ArrayList<PeerOrganisations>) hlfconfig.getFabricConfig().getPeers().stream().filter(peerOrg-> peerOrg.getOrgName().equalsIgnoreCase(requestObj.getString("orgname"))).collect(Collectors.toList());
		System.out.println("SelectedPeer"+peer.get(0).toString());
		UserImpl user= userController.getUser(requestObj.getString("username"),requestObj.getString("orgname"),peer.get(0).getCaOrganisation(),peer.get(0));
//		return "user1";
		System.out.println("user fetched"+user.toString());
		InvokePrivateChaincodeData chaincodeQuery=new InvokePrivateChaincodeData();
		String response=chaincodeQuery.queryPrivateData(hlfconfig.getFabricConfig().getChannels().get(0), hlfconfig.getFabricConfig(), requestObj, user);
		
		System.out.println("response"+response);
		JSONObject jsonObject=new JSONObject();
		if(response.equalsIgnoreCase("Failed"))
			jsonObject.put("Status", "Failed");
		else
		{
			jsonObject.put("Payload:", response);
			jsonObject.put("Status","Success");
		}
		return jsonObject.toString();
	}
	
	
	
}
