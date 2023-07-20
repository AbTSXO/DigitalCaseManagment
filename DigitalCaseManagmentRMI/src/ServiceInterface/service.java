/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServiceInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import javax.jws.WebParam;

/**
 *@author Abenezer Ashenafi
 */
public interface service extends Remote {
public String registerUser(String personid,
             String firstname,
             String lastname,
             String address,
             String speciallization,
             String gender,
             String accounttype,
            String birthdate,
            String phonenumber)throws RemoteException;
public ArrayList loginUser(String username,String password)throws RemoteException;
public boolean updatepassword(String username,String password)throws RemoteException;
public ArrayList viewPerson(String username)throws RemoteException;
public ArrayList viewCase(@WebParam(name = "casenumber")String casenumber)throws RemoteException;
public ArrayList viewReport (String value1,String value2,String value3)throws RemoteException;
public String updatecase(String casenumber,String title,String desc,String judge)throws RemoteException;
public String regcase(String casenumber,
            String casetitle,
            String casedescription,
            String cata,
            String remark,
            String startingdate,
            String progressdate,
            String judge,
            String degree,
            int planti,
            int def)throws RemoteException;
public String checkcase(String casenumber,String cata)throws RemoteException;
public String updatecasedetail(String casenumber,
            String cata,
            String fname,
            String lname,
            String address,
            String personid,
            String phone)throws RemoteException ; 
public ArrayList closecasecheck(String casenumber,String cata)throws RemoteException;
public String updateclosecasedecision(String casenumber,String personid, String decsion)throws RemoteException;
public String registerfile(String casenumber,String fileNumber,String descriptionOfFile,String filecata,String fileType,String fileName)throws RemoteException;
public String checkattachfile(String casenumber)throws RemoteException;
public String closecasedecision(String casenumber)throws RemoteException;
public String changestatus(String uname)throws RemoteException;
 public String updateUser(String fname, String lname, String address, String phone,String gender,String personid)throws RemoteException;
public void logresgister(String ipaddress,String username,String operation, String datacode )throws RemoteException;
public ArrayList load()throws RemoteException;
}
