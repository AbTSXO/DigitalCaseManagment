/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitalcasemanagmentrmi;

import java.rmi.registry.Registry;

/**
 *
 * @author Abenezer Ashenafi
 */
public class digitalCaseManagmentServer {

public static void main(String[] args) {
try
{
Registry r=java.rmi.registry.LocateRegistry.createRegistry(1099);/*creating registry*/
r.rebind("digitalcasemanagment",new serviceImplementationRMI());/*Bindning the implementation class that is serviceImplementationRMI 
to digitalcasemanagment*/
    System.out.println("Server@X269 connected...");

}catch(Exception e)
{
    System.out.println("Server@X289 not connected");
}


}
}

    

