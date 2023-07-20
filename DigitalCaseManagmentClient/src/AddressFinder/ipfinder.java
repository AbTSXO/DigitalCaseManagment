/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AddressFinder;

/**
 *
 * @author AbeXo
 */
public class ipfinder {
private String ipaddr;
private String username;

public ipfinder(String ipaddress,String username)
{
this.ipaddr=ipaddress;
this.username=username;
}


public void setip(String addr)
{
this.ipaddr=addr;
}
public String getip()
{
return this.ipaddr;
}
public void setusername(String user)
{
this.username=user;
}
public String getusername()
{
return this.username;
}


}
