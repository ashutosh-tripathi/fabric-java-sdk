package com.fabric.user.controller;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;

import com.fabric.config.FabricConfig;
import com.fabric.org.CAOrganisation;
import com.fabric.org.OrgChannel;
import com.fabric.org.PeerOrganisations;
import com.fabric.user.UserImpl;

public class UserController {
	
	public UserImpl getUser(String username,String orgname,CAOrganisation ca,PeerOrganisations peer) throws IOException
	{
		ArrayList<UserImpl> userList = new ArrayList<UserImpl>();
		 FileInputStream fis=null;
		 ObjectInputStream ois = null;
		 FileOutputStream fos=null;
		 ObjectOutputStream oos=null;

				System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
			
        try
        {
        	
        	
         InputStream filen = new FileInputStream("config/users.ser");
          InputStream buffer = new BufferedInputStream(filen);
          
       
                try {
                	 if(buffer.available()>0)
                	 {
                	 ois = new ObjectInputStream (buffer);
                	
                	userList=(ArrayList<UserImpl>)ois.readObject();
                	 }
//                	System.out.println(userList.toString());
           
                } catch (EOFException e) {
                    
                    e.printStackTrace();
			} 
 

            System.out.println("userList"+userList);
          
           ArrayList<UserImpl> appUser=  (ArrayList<UserImpl>) userList.stream().filter(user-> user.getName().equalsIgnoreCase(username) && user.getAccount().equalsIgnoreCase(orgname)).collect(Collectors.toList());
           
           File file=new File(ca.getTlsca());
           
//           File file = new File("config/ca."+orgname+".com-cert.pem");
	       	String pemContent=FileUtils.readFileToString(file);
//     	System.out.println("pemString"+pemContent);
		  
           Properties properties = new Properties();
           properties.put("pemBytes", pemContent.getBytes());
           properties.put("allowAllHostNames", "true");
           
           CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
      		HFCAClient caClient = HFCAClient.createNewInstance(ca.getUrl(), properties);
      		caClient.setCryptoSuite(cryptoSuite);
           if(appUser.size()==0)
           {
        	   ArrayList<UserImpl> adminUsers=(ArrayList<UserImpl>) userList.stream().filter(user-> user.getName().equalsIgnoreCase("admin") && user.getAccount().equalsIgnoreCase(orgname)).collect(Collectors.toList());
        	   UserImpl adminUser=null;
        	   if(adminUsers.size()>0)
        	   {
        		   adminUser=adminUsers.get(0);
        	   
        	   }
        	   
        	   if(adminUsers.size()==0)
        	   {

                   fos=new FileOutputStream("config/users.ser");
                   oos=new ObjectOutputStream(fos);
               		
               		Enrollment adminEnrollment = caClient.enroll(ca.getEnrollmentId(), ca.getEnrollmentSecret());
               		UserImpl admin = new UserImpl("admin",orgname, peer.getPeerMSP(), adminEnrollment);
               		System.out.println("admin"+admin.toString());
               		RegistrationRequest rr = new RegistrationRequest(username, orgname);
               		String userSecret = caClient.register(rr, admin);
               		Enrollment userEnrollment = caClient.enroll(username, userSecret);
               		UserImpl nappUser = new UserImpl(username, orgname,peer.getPeerMSP(), userEnrollment);
               		userList.add(admin);
               		System.out.println("in admin not enrolled"+admin.toString());
               		userList.add(nappUser);
               		System.out.println("nappuser in "+nappUser.toString());
               		oos.writeObject(userList);
               		return nappUser;
        	   }
        	   else
        	   {

                   fos=new FileOutputStream("config/users.ser");
                   oos=new ObjectOutputStream(fos);
        		   RegistrationRequest rr = new RegistrationRequest(username, orgname);
              		String userSecret = caClient.register(rr, adminUser);
              		Enrollment userEnrollment = caClient.enroll(username, userSecret);
              		UserImpl nappUser = new UserImpl(username, orgname,peer.getPeerMSP(), userEnrollment);
              		userList.add(nappUser);  
              		System.out.println("in admin enrolled"+nappUser.toString());
              		oos.writeObject(userList);
              		return nappUser;
        	   }
           
           
           }
           else
           {
        	   System.out.println("in present"+appUser.toString());
        	   return appUser.get(0);
           }
          
 
        } 
        catch (Exception ex) 
        {
            ex.printStackTrace();
            if(ois!=null)
            ois.close();
           
        	if(oos!=null)
            oos.close();
        
        } 
        
        finally {
        	if(ois!=null)
        	 ois.close();
        	if(oos!=null)
             oos.close();
             
        }
		return null;
         
	}

	
		

}
