/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Encryption;


import java.security.MessageDigest;
import javax.xml.bind.DatatypeConverter;
/**
 *
 * @author AbeXo
 */
public class encrypt {
    public static String getHashValue(String pswd)
    {
        byte[]input;
        input=pswd.getBytes();
        String hashValue="";
       
        try{
            MessageDigest message=MessageDigest.getInstance("SHA-1");
            message.update(input);
            byte[] digestValue=message.digest();
            hashValue=DatatypeConverter.printHexBinary(digestValue).toLowerCase();
        }catch(Exception e){System.out.println("message is not hashed");}
    
    return hashValue;
    }
    
    
}
