

import java.rmi.Remote;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.rmi.RMISecurityManager;

public class mainframe {
    public static void main(String args[]) {
        String ip_address = "192.168.0.102";
        System.setProperty("java.rmi.server.hostname",ip_address);
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

            Test tmp_test = new Test();
            //tmp_test.pre_test();
            //tmp_test.basic_test();
            tmp_test.all_test();

    }
}