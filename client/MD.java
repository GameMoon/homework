package client;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class MD {
	public static byte[] Mdhash(String text) throws UnsupportedEncodingException, NoSuchAlgorithmException{
		byte[] premessage;

		premessage = text.getBytes("UTF-8");
		//System.out.println(premessage);
	
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		md.update(premessage);
		byte [] message= md.digest();
		
		StringBuffer hexString = new StringBuffer();
    	for (int i=0;i<message.length;i++) {
    		String hex=Integer.toHexString(0xff & message[i]);
   	     	if(hex.length()==1) hexString.append('0');
   	     	hexString.append(hex);
    	}
    	System.out.println(hexString);
		return message;
	}
}
