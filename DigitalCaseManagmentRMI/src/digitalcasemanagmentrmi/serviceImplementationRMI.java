/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitalcasemanagmentrmi;

import ServiceInterface.service;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebParam;
import java.io.*;

/**
 *
 * @author Abenezer Ashenafi
 */
public class serviceImplementationRMI extends UnicastRemoteObject implements service {
    /*
    
    
    */
    private static PreparedStatement so;
    private static Connection connection;
    private static int executeUpdate;
    private static Statement s;
    private static ResultSet rs;
    public serviceImplementationRMI()throws RemoteException
    {
    
    connectdatabase();
    so=null;
    executeUpdate=0;
    } 
    
    /*
    this function is responsible of creating a database connection with database digital _court
    any time this class object is accessed the connection with database will be established 
    with implementation class
    */
    private void connectdatabase()
    {
    
    String DB="jdbc:mysql://localhost/digital_court";//database location located on localhost
            String user="root";//username for the database
            String pass="";//password for the database 
            
            try
            {
                DriverManager.registerDriver(new com.mysql.jdbc.Driver());
                connection=(Connection)DriverManager.getConnection(DB,user,pass);
                if(connection!=null){System.out.println("db connected");}
            }catch(SQLException ex)
            {
                  Logger.getLogger(serviceImplementationRMI .class.getName()).log(Level.SEVERE,null,ex);

            }
    }
    /*
     this function is responsible for registration of users account 
    into the users table and return string message
    */
    public String registerAccount(String personid, String username, String password, int status, int accounttype){
        String success="";// return message declaration
        String csql="INSERT INTO user (username,personid,password,status,accounttype) VALUES(?,?,?,?,?)";
        try {
            
            so = connection.prepareStatement(csql);
        } catch (SQLException ex) {
            Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            so.setString(1,username );//setting a value of username for the 1st colmun
            so.setString(2,personid);//setting a value of person id for the 2nd colmun which is link up with person table
            so.setString(3, password);//setting a value of default password for the 3rd colmun
            so.setInt(4, status);//setting a value of status for the 4th colmun
            so.setInt(5, accounttype);//setting a value of account type for the 5th colmun
            executeUpdate=so.executeUpdate();
            
            
        } catch (SQLException ex) {
            Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
        }
            success="username:"+username+", password:1234";//return value would be the generated username nad defaut password
        return success;
    }

    
    /*
     the register user function is responisble for registring the person detail information on the 
     perosn table which includes name,id ,address,gender,phone,birthdate and link up with user table 
    and return string message  
    */
    @Override
public String registerUser(String personid, String firstname, String lastname, String address, String speciallization, String gender, String accounttype, String birthdate, String phonenumber)throws RemoteException  
{
         String success;
         String sql="INSERT INTO person (personid,fname,lname,address,specialization,gender,accounttype,birthdate,phonenumber) VALUES(?,?,?,?,?,?,?,?,?)";
        try {
                    s=connection.createStatement();
                    so = connection.prepareStatement(sql);
                } catch (SQLException ex) 
                        {
                           Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
                        }
        
        try {     
             so.setString(1, personid);//setting a value of person id for the 1st colmun
            so.setString(2,firstname);//setting a value of firstname for the 2nd colmun
            so.setString(3, lastname);//setting a value of lastname for the 3rd colmun
            so.setString(4, address);//setting a value of Address for the 4th colmun
            so.setString(5, speciallization);//setting a value of specialization for the 5th colmun
            so.setString(6,gender);//setting a value of gender for the 6th colmun
            so.setString(7,accounttype);//setting a value of account type for the 7th colmun
            so.setString(8,birthdate);//setting a value of person id for the 8th colmun
            so.setString(9, phonenumber);//setting a value of person id for the 9th colmun
       executeUpdate=so.executeUpdate();
         } catch (SQLException ex) {
            Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String password="7110eda4d09e062aa5e4a390b0a572ac0d2c0220";//default sha1 value for the password
        int stat=2;//status of the accont
        int acc;//account type 
          Random rnd = new Random();//generate a random number
         int number = rnd.nextInt(999999);
        String username=firstname+String.valueOf(number);//randomly generated username
          switch(accounttype)
         {
         case "Excutive.J": acc=1;break;
         case  "Lawyer": acc=2;break;
         case  "Data Encoder": acc=3;break;  
         case  "Judge": acc=4;break;
         case  "User": acc=5;break;
         default:acc=0;break;   
         }
        success=registerAccount(personid,username,password,stat,acc);//calling the register account method    
        return success;//return the return message of the operation
    }

    /*this function is responsible for login of user into the system which checks the username 
     and password and bring the information of the regarding user in the compact form of arraylist
     */
    @Override
    public ArrayList loginUser(String username, String password)throws RemoteException
    {   
        boolean temp=false;
        String loginsql="SELECT * FROM user WHERE username=? AND password=?";//sql statement for retriving information from user table
        ArrayList arr=new ArrayList();//declare an arraylist for holding the retrived information of user
        int columunindex = 0;
            
         try {
            so = connection.prepareStatement(loginsql);
        } catch (SQLException ex) {
            Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
        }
          try {
                so.setString(1, username);//setting a value of username for the 1st colmun
                so.setString(2,password );//setting a value of password for the 2nd colmun
                rs=so.executeQuery();
                if(rs.next())//if it gets the matching result of the query 
                    {
                        /*
                        fetching the retrived data into arraylist that matched with username
                        and save the password, status ,account type of the user and the user id 
                        */ 
                       columunindex=rs.findColumn("username");
                       arr.add( rs.getString(columunindex));
                       
                       columunindex=rs.findColumn("password");
                       arr.add( rs.getString(columunindex));
                       
                       columunindex=rs.findColumn("status");
                       arr.add( rs.getString(columunindex));
                       
                       columunindex=rs.findColumn("accounttype");
                       arr.add( rs.getString(columunindex));
                       
                       columunindex=rs.findColumn("personid");
                       arr.add( rs.getString(columunindex));
            
                     }
                else//if it doesn't find any value with the related query add no value to the arrayist
                {
                arr.add("no value");
                
                }      
            
        } catch (SQLException ex) {
            Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
        }
     return arr;//return the array list object
        
    }    
    /*
     this fuction is responsible for changing the password of user
     which acepts the usernae and old passowrd of the specific account for the first time
     login of the user have to change the default password of the account 
     */
    @Override
    public boolean updatepassword(String username, String password)throws RemoteException  
    {
        String updatesql="UPDATE user SET `password` = ?,`status` = '1' WHERE username =?";//sql statement for updating password from user table
        boolean status=false;
     executeUpdate=0;
         try {
           // s=connection.createStatement();
            so = connection.prepareStatement(updatesql);//executing the sql query
        } catch (SQLException ex) {
            Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
        }
         try {
           
            
                so.setString(1, password);//setting a value of password for the 1st colmun
                so.setString(2,username);//setting a value of username for the 2nd colmun
                executeUpdate=so.executeUpdate();
          
         }catch (SQLException ex) {
            Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
        }
            
         
         if(executeUpdate>0)//if the there is affected row more than 0 rows(finding the number of affected rows)
         {
             status=true; //setting up the result of the operation
         }
         else//if there is no affectde row with the query
         {
             status=false;//setting up the result of the operation
         }
     
      
      return status;//return the result of the method 
    }
    /* 
    this function is responsible for accessing the person information by accepting the user name 
     of the user it brings the detail information from the person table and pack in the array list 
     in the return
     */
    @Override
    public ArrayList viewPerson(String username)throws RemoteException  
    {
        boolean status=false;
       //sql statement for selecting person information from person table
        String selectusersql="SELECT person.personid, person.fname, person.lname, person.address, person.gender, person.accounttype, person.phonenumber FROM person,user WHERE user.username=? AND user.personid=person.personid;";
       int columunindex = 0;
       ArrayList ulist=new ArrayList();//declare an arraylist for holding the retrived information of a person    
        try {
            so = connection.prepareStatement(selectusersql);//executing the sql query
        } catch (SQLException ex) {
            Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
        }
         try {
           
                so.setString(1,username);//setting a value of username for the 1st colmun
                rs=so.executeQuery();//setting up the result statement 
          
                  if(rs.next())//if it gets the matching result of the query
                    {
                      /*
                        fetching the retrived data into arraylist that matched with username
                        which are the person id, first name, last name, address, account type 
                        of the information from the person table 
                        */ 
                       columunindex=rs.findColumn("personid");
                       ulist.add( rs.getString(columunindex));

                       columunindex=rs.findColumn("fname");
                       ulist.add( rs.getString(columunindex));
                       
                       columunindex=rs.findColumn("lname");
                       ulist.add( rs.getString(columunindex));
                       
                       columunindex=rs.findColumn("address");
                       ulist.add( rs.getString(columunindex));
                       
                       columunindex=rs.findColumn("accounttype");
                       ulist.add( rs.getString(columunindex));
                       
                       columunindex=rs.findColumn("phonenumber");
                       ulist.add( rs.getString(columunindex));
                    }
                  else//if it doesn't find any value with the related query add no value to the arrayist
                    {
                      ulist.add("no value");
                     }
         }catch (SQLException ex) {
            Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
        }
           return ulist;//return the array list object
             
    }
    /* 
    this function is responsible for accessing the case information by accepting the case number
     of the case it brings the detail information from the case table and pack in the array list 
     in the return that is the case number the case detail information, the assigned judge 
     */
    @Override
    public ArrayList viewCase(String casenumber)throws RemoteException  
    {
       boolean status=false;
       //sql statement for selecting case information from case table
       String selectcasesql="SELECT `casenumber`,`casetitle`,`casedescription`,`casejudge` FROM `case` WHERE `casenumber`=? AND `casestatus` = 'active'";
       int columunindex = 0;
       ArrayList vclist=new ArrayList();//declare an arraylist for holding the retrived information of a case     
        try {
           
            so = connection.prepareStatement(selectcasesql);//executing the query
        } catch (SQLException ex) {
            Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
        }
         try {
                so.setString(1,casenumber);//setting a value of case number for the 1st colmun
                rs=so.executeQuery();//setting up the result statement 
          
                  if(rs.next())//if it gets the matching result of the query
                    {
                      /*
                        fetching the retrived data into arraylist that matched with the case number
                        which are the case title, case decription, the assigned judge 
                        of the case information from the case table 
                        */ 
                       columunindex=rs.findColumn("casetitle");
                       vclist.add(rs.getString(columunindex));
                       
                       columunindex=rs.findColumn("casedescription");
                       vclist.add(rs.getString(columunindex));
                       
                       columunindex=rs.findColumn("casejudge");
                       vclist.add(rs.getString(columunindex));
                       
                     }
                else//if it doesn't find any value with the related query add no value to the arrayist
                {
                vclist.add("no value");
                
                }
         }catch (SQLException ex) {
            Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
        }
           
     return vclist;//return the array list object
    }

    /*
    this function is responsible for generating the report with the accepted parameters
    which are case stauts, case Reamrk, case sub catagory and bring the detail information of the 
    related cases from the case table and packed it with array list and return the array list
    */
    @Override
public ArrayList viewReport(String value1, String value2, String value3)throws RemoteException 
{
String q1,q2,q3;
/*
setting the query string for the first criteria parameter
that is the sub category of the case which could be civil affair,
criminal trial, criminal trial and all which can include all
*/
if(value1.equals("All")){
q1="`category` IN('Civil Affairs','Criminal Trial','Labor Dispute')";
}
else if(value1.equals("Civil Affairs")){
q1="`category` IN('Civil Affairs')";
}
else if(value1.equalsIgnoreCase("Criminal Trial"))
{
q1="`category` IN('Criminal Trial')";
}
else{
    q1="`category` IN('Labor Dispute')";
}

/*
setting the query string for the second criteria parameter
that is the case status of the case which could be unfinished,
finished and all which can include all
*/
if(value2.equals("All")){
q2="`caseremark` IN('unfinished','finished')";
}
else if(value2.equals("unfinished")){
q2="`caseremark` IN('unfinished')";
}
else{
    q2="`caseremark` IN('finished')";
}

/*
setting the query string for the third criteria parameter
that is the case degree of the case which could be normal,
special, confidentail and all which can include all
*/
if(value3.equals("All")){
q3="`casedegree` IN('Normal','Special','Confidential')";
}
else if(value3.equals("Normal")){
q3="`casedegree` IN('Normal')";
}
else if(value3.equalsIgnoreCase("Special"))
{
q3="`casedegree` IN('Special')";
}
else{
q3="`casedegree` IN('Confidential')";
}
//sql statement for fetching all case information report from case table
String qo="SELECT `casenumber`, `category`, `caseremark`, `casestartdate`, `casejudge`,`casedegree` FROM `case` WHERE";
String summary=qo+" "+q1+"AND"+q2+"AND"+q3;
        
        ArrayList clist=new ArrayList();//declare an arraylist for holding the retrived information of a case
        int columunindex;
        
       try {
            s = connection.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
        }
       try {
          
                rs=s.executeQuery(summary);//execute the query
                  while(rs.next())//fetch every row with the related query
                    {
                     /*
                        fetching the retrived data into arraylist that matched with the case number
                        which are the case title, case decription, the assigned judge, case degree,
                        case remark and sub category of the case information from the case table 
                        */
                       columunindex=rs.findColumn("casenumber");
                       clist.add( rs.getString(columunindex));

                       columunindex=rs.findColumn("casestartdate");
                       clist.add( rs.getString(columunindex));
                       
                       
                       columunindex=rs.findColumn("casejudge");
                       clist.add( rs.getString(columunindex));
                       
                       columunindex=rs.findColumn("casedegree");
                       clist.add( rs.getString(columunindex));
                       
                       columunindex=rs.findColumn("category");
                       clist.add( rs.getString(columunindex));
                       
                       columunindex=rs.findColumn("caseremark");
                       clist.add( rs.getString(columunindex));
                  }
                
         }catch (SQLException ex) {
            Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    return clist;//return the array list object   
    }

    /*
    this function is responsible for update some of the case information by accepting the 
    case number and tiltle and description and the judge information and change the case information
    which related with case case number in the case table and return string message
    */
    @Override
    public String updatecase(String casenumber, String title, String desc, String judge)throws RemoteException  
    {
        String result;
        //sql statement for setting some case case information from case table 
       String updatecase="UPDATE `case` SET `casetitle` = ?, `casedescription` = ?, `casejudge` = ? WHERE `case`.`casenumber` = ?";
      try {
          so = connection.prepareStatement(updatecase);//executing the query
      }catch (SQLException ex)
      {
          Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
      }
        
     try {
                so.setString(1,title);//setting a value of title for the 1st colmun
                so.setString(2,desc);//setting a value of decription for the 2nd colmun
                so.setString(3,judge);//setting a value of assigned judge for the 3rd colmun
                so.setString(4,casenumber);//setting a value of casenumber for the 4th colmun
                executeUpdate=so.executeUpdate();
          
         }catch (SQLException ex) {
            Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
        }
      //if the there is affected row more than 0 rows(finding the number of affected rows)   
      if(executeUpdate>0)
      {
          result="successful operation";//setting up the result of the operation
      }
      //if there is no affectde row with the query
      else
      {
          result="unsuccessful operation";//setting up the result of the operation
      }
     return result;//return the string message of the result operation
         
    }

    /*
    this function is responsible for register the case in the case table with the accepted
    information(case title ,description, case strating date , catagory of the case, number of plantiff
    and number of defendant of the case)  and fill the case detail table with the number of defenant 
    and number plantiff amount make the case open for another registration opertaion
    finally return result string messgae of the operation 
    */
    @Override
    public String regcase(String casenumber, String casetitle, String casedescription, String cata, String remark, String startingdate, String progressdate, String judge, String degree, int planti, int def)throws RemoteException 
    {
  //
  String queryresult="INSERT INTO `case` (`casenumber`, `casetitle`, `casedescription`, `category`, `caseremark`, `casestartdate`, `caseprogressdate`, `casejudge`, `casedegree`, `plant`, `def`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
  //
  String secondquery="INSERT INTO `casedetail` ( `catagory`, `casenumber`) VALUES ('Plantiff',?)";
  //
  String thirdquery="INSERT INTO `casedetail` ( `catagory`, `casenumber`) VALUES ('Defendant',?)";
  
  String result;     
   try {
                    
                    so = connection.prepareStatement( queryresult);//execute the query
                } catch (SQLException ex) 
                        {
                           Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
                        }
        
       try {
           so.setString(1,casenumber);//setting a value of case number for the 1st colmun
            so.setString(2,casetitle);//setting a value of title for the 2nd colmun
            so.setString(3,casedescription);//setting a value of case decription for the 3rd colmun
            so.setString(4,cata);//setting a value of case category for the 4th colmun
            so.setString(5,remark);//setting a value of remark of the case for the 5th colmun
            so.setString(6,startingdate);//setting a value of starting date for the 6th colmun
            so.setString(7,progressdate);//setting a value of progress date changeable for the 7th colmun
            so.setString(8,judge);//setting a value of assigned judge for the 8th colmun
            so.setString(9,degree);//setting a value of degree of case for the 9th colmun
            so.setInt(10,planti);//setting a value of number of plantiff for the 10th colmun
            so.setInt(11,def);//setting a value of number of defendant for the 11th colmun
            executeUpdate=so.executeUpdate();
          
         }catch (SQLException ex) {
            Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
        }
  //if successfully executing thequery operation     
  if(executeUpdate>0)
  {
      result="successful operation";
  /*
  fill the case detail table with plantiff inforamtion,some of the columns are going to be null
  and still open for detail case registration untill the number
  of plantiff is reached      
  */
 for(int i=0;i<planti;i++)
 {
 
  
   try {
                    
                    so = connection.prepareStatement( secondquery);// executing the second query
                } catch (SQLException ex) 
                        {
                           Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
                        }
     
   try {
           so.setString(1,casenumber);//setting a value of case number for the 1st colmun
           executeUpdate=so.executeUpdate();
          
         }catch (SQLException ex) {
            Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
        }
     
 
 }
 
 /*
  fill the case detail table with defendant inforamtion,some of the columns are going to be null
  and still open for detail case registration untill the number
  of defendant is reached      
  */
 for(int j=0;j<def;j++)
 {
 
   try {                  
                    so = connection.prepareStatement(thirdquery);//executing the third query
                } catch (SQLException ex) 
                        {
                           Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
                        }
     
   try {
           so.setString(1,casenumber);//setting a value of case number for the 1st colmun
           executeUpdate=so.executeUpdate();
          
         }catch (SQLException ex) {
            Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
        }
 }
         
  }
  //if the executing query is not operated successfully
 else
  {
      result="unsuccessful operation";
  }
           
 return result;//return the string result of the operation of the method
               
    }

    /*
    this function is responsible detail registration of the case with the regaridng case number
    to check if there is incomplete unregistered information of the defendant and plantiff information
    on the case detail table and return with string message with the result 
    */
    @Override
    public String checkcase(String casenumber, String cata)throws RemoteException 
    {
        String result=new String();
        //sql statement for selecting some plantiff/defendant information from case detail table 
        String casequery="SELECT * FROM `casedetail` WHERE `casenumber`=? AND `catagory`=? AND `fname` IS NULL";
  
   try {
                    
                    so = connection.prepareStatement(casequery);//execute the query
                } catch (SQLException ex) 
                        {
                           Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
                        }
     

 try {
           so.setString(1,casenumber);//setting a value of case number for the 1st colmun
           so.setString(2,cata);//setting a value of category for the 2nd colmun
        
          rs=so.executeQuery();
          //if the there is affected row more than 0 rows(finding the number of affected rows)
          if(rs.next())
          {
          result="found a record";//assign string message value
          }
          //if the there is no affected rows
          else{
               result="not found a record";//assign string message value
            } 
         
         }catch (SQLException ex) {
            Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
        }
 
    return result;//return string message value of the operation of method     
    }

     /*
    this function is responsible for registration of information (firstname,last name,address, person id 
    issued by government )of the defendant and plantiff perosnal information into case detail  table
    and return string message of the operation
    */
@Override
public String updatecasedetail(String casenumber, String cata, String fname, String lname, String address, String personid, String phone) throws RemoteException 
{
    //sql query for updating the first name last name person id , phone number and address in the case detail table
    String queryresult="UPDATE `casedetail` SET `fname`=?,`lname`=?,`personid`=?,`phonenumber`=?,`address`=? WHERE `casenumber`=? AND `catagory`=? AND `fname` IS NULL LIMIT 1";
    String result;
  
   try {
       so = connection.prepareStatement(queryresult);//execute the query
       }catch (SQLException ex) 
   {
       Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
   }
  try {
            so.setString(1,fname);//setting a value of first number for the 1st colmun
            so.setString(2,lname);//setting a value of last name for the 2nd colmun
            so.setString(3,personid);//setting a value of person id for the 3rd colmun
            so.setString(4,phone);//setting a value of phone number for the 4th colmun
            so.setString(5,address);//setting a value of address for the 5th colmun
            so.setString(6,casenumber);//setting a value of case number for the 6th colmun
            so.setString(7, cata);//setting a value of catageory for the 7th colmun
            executeUpdate=so.executeUpdate();
         }catch (SQLException ex) {
            Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
        }
  //if the there is affected row more than 0 rows(finding the number of affected rows)
  if(executeUpdate>0)
  {
      result="successful operation";//assign string message value
  }
  //if the there is no affected rows
  else
  {
  result="unsuccessful operation";//assign string message value
  }
    return result; //return string message value of the operation of method    
}
    /*
    this function is responsible for checking the case which have completed full registration is avialble for 
    entering the decison of the case in the regarding case number first accepts case number and case catagory 
    and bring out the detail inforamtion(full name of defendant, case category , personal id of defendant )
    from the case detail table and pack into arraylist object
    */
@Override
public ArrayList closecasecheck(String casenumber, String cata)throws RemoteException  
    {
        ArrayList result=new ArrayList();
        int columunindex = 0;
        //sql query for select the first name, last name and person id that match with case number from case detail table
        String closecasecheck="SELECT `fname`,`lname`,`personid` FROM `casedetail` WHERE `casenumber`=? AND `catagory`=? AND `descion` IS NULL AND `fname` IS NOT NULL  ";
        int total=0;
        String temp=new String();
 
  try {
      so = connection.prepareStatement(closecasecheck);//execute the query
  } catch (SQLException ex)
  {
      Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
  }
     

 try {
           so.setString(1,casenumber);//setting a value of case number for the 1st colmun
           so.setString(2,cata);//setting a value of category for the 1st colmun   
          rs=so.executeQuery();
          //
          if(rs.next())
          {
            /*
               fetching the retrived data into arraylist that matched with the case number and category(plantiff/defendant)
               which are the first name, last name and person id of the plantiff/defendant 
              information from the case detail table 
             */
          result.add("found record");
              
          columunindex=rs.findColumn("fname");
          result.add( rs.getString(columunindex));
          
          columunindex=rs.findColumn("lname");
          result.add( rs.getString(columunindex));
          
          columunindex=rs.findColumn("personid");
          result.add( rs.getString(columunindex));
            
          }
          //if ther is no match data
          else
          {
               result.add("not found record");
           } 
         }catch (SQLException ex) {
            Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
     

return result;//return the result array list object
    }
    
    /*
    this function is responsible for registration of the decison on the defendant by accepting the person id
    of the defendant and decision and enter into the case detail table and return string message 
    of the operation
    */
    @Override
    public String updateclosecasedecision(String casenumber, String personid, String decsion)throws RemoteException 
    {
        
        String result;
        //sql query for update case detail table and update the null decision columun 
        String closecaseupdate="UPDATE `casedetail` SET `descion` = ? WHERE    `casenumber`=? AND `personid`=?";
try {
    so = connection.prepareStatement(closecaseupdate);//execute query
} 
catch (SQLException ex) 
{
    Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
}
try {
            so.setString(1,decsion);//setting a value of decsion for the 1st colmun
            so.setString(2,casenumber);//setting a value of case number for the 2nd colmun
            so.setString(3,personid);//setting a value of personid for the 3rd colmun
            executeUpdate=so.executeUpdate();
         }catch (SQLException ex) {
            Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
        }

  //if the there is affected row more than 0 rows(finding the number of affected rows)
  if(executeUpdate>0)
  {
      result="successful operation";//assign string message value
  }
  //if the there is  no affected row
  else
  {
  result="unsuccessful operation";//assign string message value
  }
return result;//return string message value of the operation of method         
}

    /*
    this function is responsible for registration of the file information on the case file table
    by registration file owner, file type, unique file number, descriptio of the file and file type
    and return the result of the method
    */    
public String registerfile(String casenumber,String fileNumber,String descriptionOfFile,String filecata,String fileType,String fileName)throws RemoteException 
{
    //sql statement for inserting a record into case file table
String attachcaseinsert="INSERT INTO `casefile` (`casenumber`, `filetype`, `filebelong`, `filename`, `attachdate`, `filecasedescription`, `filenum`) VALUES (?, ?, ?, ?, ?, ?, ?)";
            
            String dateoffile;
            Date db=new Date();
             SimpleDateFormat sdf=new SimpleDateFormat("YYYY/MM/DD");
             dateoffile=sdf.format(db);
             String result="no record";
  
  
   try {
                    
                    so = connection.prepareStatement(attachcaseinsert);//execute the query
                } catch (SQLException ex) 
                        {
                           Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
                        }
     

 try {
           so.setString(1,casenumber);//setting a value of case number for the 1st colmun
           so.setString(2,fileType);//setting a value of file type for the 2nd colmun
           so.setString(3,filecata);//setting a value of file catagory for the 3rd colmun
           so.setString(4,fileName);//setting a value of file name for the 4th colmun
           so.setString(5,dateoffile);//setting a value of date of regisration for the 5th colmun
           so.setString(6,descriptionOfFile);//setting a value of description about file for the 6th colmun
           so.setString(7,fileNumber);//setting a value of unique file number for the 7th colmun
          executeUpdate=so.executeUpdate();
          
         }catch (SQLException ex) {
            Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
        }
        //if the there is affected row more than 0 rows(finding the number of affected rows)  
         if(executeUpdate>0)
         {
             result="recorded";//assign string message value
         }
         //if the there is  no affected row
         else
         {
             result="not recorded";//assign string message value
         }
return result;//return string message value of the operation of method  
}
/*
this function is responsible for checking the case number is already been registered on 
case table and return whteher there is a data or not 
*/
public String checkattachfile(String casenumber)throws RemoteException
{
    //sql query that selects case number from case file regarding with the given case number
    String attachcasecheck="SELECT `casenumber` FROM `case` WHERE `casenumber`=? AND `casestatus` = `active`";
    String result="no record";//declare a variable to hold the result message
    int columunindex = 0;
    try {
                    
                    so = connection.prepareStatement(attachcasecheck);//execute query
                } catch (SQLException ex) 
                        {
                           Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
                        }
     

 try {
           so.setString(1,casenumber);//setting a value of case number for the 1st colmun
       
          rs=so.executeQuery();
          //if there is matching record data with the given case number
          if(rs.next())
          { 
          columunindex=rs.findColumn("casenumber");
          result= rs.getString(columunindex);//assign string message value
          }   
          else
          {
               result="no record";//assign string message value
            } 
         
         }catch (SQLException ex) {
            Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
        }
return result;//return string message value of the operation of method 
}

 /*
    this function is responsible for droping a case from case table and case detail file 
    checking the case number and delete from the case file and move to drop case table 
    */
    @Override
    public String closecasedecision(String casenumber)throws RemoteException 
    {
        
        String result;
        //sql query for update case status to incative with the given case number  
        String closecaseupdate="UPDATE `case` SET `casestatus` = 'inactive' WHERE `case`.`casenumber` = ? ";
try {
    so = connection.prepareStatement(closecaseupdate);//execute query
} 
catch (SQLException ex) 
{
    Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
}
try {
            so.setString(1,casenumber);//setting a value of case number for the 1st colmun
            executeUpdate=so.executeUpdate();
         }catch (SQLException ex) {
            Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
        }

  //if the there is affected row more than 0 rows(finding the number of affected rows)
  if(executeUpdate>0)
  {
      result="successful operation";//assign string message value
  }
  //if the there is  no affected row
  else
  {
  result="unsuccessful operation";//assign string message value
  }
return result;//return string message value of the operation of method

}
 /*
    this function is responsible for update some of the user information by accepting the 
    first name and last name and address and the phone number information and change the user information
    which related with person id  in the person table and return string message
    */
    @Override
    public String updateUser(String fname, String lname, String address, String phone,String gender,String personid)throws RemoteException  
    {
        String result;
        //sql statement for setting some case case information from case table 
       String updateuser="UPDATE `person` SET `fname` = ?,`lname`=?,`address`=?,`phonenumber`=?,`gender`=? WHERE `person`.`personid` = ?";
      try {
          so = connection.prepareStatement(updateuser);//executing the query
      }catch (SQLException ex)
      {
          Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
      }
        
     try {
                so.setString(1,fname);//setting a value of fname for the 1st colmun
                so.setString(2,lname);//setting a value of lname for the 2nd colmun
                so.setString(3,address);//setting a value of address for the 3rd colmun
                so.setString(4,phone);//setting a value of phone for the 4th colmun
                so.setString(5,gender);//setting a value of gender for the 5th colmun
                so.setString(6,personid);//setting a value of person id for the 6th colmun
                executeUpdate=so.executeUpdate();
          
         }catch (SQLException ex) {
            Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
        }
      //if the there is affected row more than 0 rows(finding the number of affected rows)   
      if(executeUpdate>0)
      {
          result="successful operation";//setting up the result of the operation
      }
      //if there is no affectde row with the query
      else
      {
          result="unsuccessful operation";//setting up the result of the operation
      }
     return result;//return the string message of the result operation
         
    }

public String changestatus(String uname)throws RemoteException
{
String result;
//sql statement for setting some case case information from case table 
       String updateuser="UPDATE `user` SET `status` = '0' WHERE `user`.`username` = ?";
      try {
          so = connection.prepareStatement(updateuser);//executing the query
      }catch (SQLException ex)
      {
          Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
      }
        
     try {
                so.setString(1,uname);//setting a value of user name for the 1st colmun
                executeUpdate=so.executeUpdate();
          
         }catch (SQLException ex) {
            Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
        }
      //if the there is affected row more than 0 rows(finding the number of affected rows)   
      if(executeUpdate>0)
      {
          result="successful operation";//setting up the result of the operation
      }
      //if there is no affectde row with the query
      else
      {
          result="unsuccessful operation";//setting up the result of the operation
      }   
return result;//return the string message of the result operation
}
/*
    this function is responsible for registration the entire system operation
    on log file by file writer class
    */
    @Override
public void logresgister(String ipaddress,String username,String operation, String datacode )throws RemoteException
{
    File file=new File("SystemLog.txt");//create a file object with a name and log file name systemLog
    Date now =new Date();
        try {
            FileWriter write=new FileWriter(file,true);//create file writer object
            PrintWriter printwrite=new PrintWriter(write);//create print write object
            printwrite.print(now);//write a date and time on the first column  
            printwrite.print("\t|");
            printwrite.print(ipaddress);//write a ipaddress on the second column 
            printwrite.print("\t|");
            printwrite.print(username);//write a user name on the third column 
            printwrite.print("\t|");
            printwrite.print(operation);//write a operation on the fourth column 
            printwrite.print("\t|");
            printwrite.print(datacode);//write a data code on the fifth column 
            printwrite.print("\n");
            printwrite.close();
        } catch (IOException ex) {
            Logger.getLogger(serviceImplementationRMI.class.getName()).log(Level.SEVERE, null, ex);
        }
}
public ArrayList load()throws RemoteException
{
ArrayList flist=new ArrayList();//declare an arraylist for reading the file    
File file=new File("SystemLog.txt");//create a file object with a name and log file name systemLog
try{
FileReader fr= new FileReader(file);
BufferedReader br= new BufferedReader(fr);
String line;
while ((line=br.readLine())!=null)
{
flist.add(line);
}
    br.close();
    fr.close();
    

}catch(IOException e)
{
    System.out.println("Exception has handled");
}
        
        
      
        
       
    return flist;//return the array list object   




}
}