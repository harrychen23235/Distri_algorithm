

import java.rmi.Remote;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.rmi.RMISecurityManager;

public class mainframe {
    public static void main(String args[]) {
        int process_number = 4;
        String mode = "basic";

        if(args.length == 1){
            process_number = Integer.parseInt(args[0]);
        }
        if(args.length == 2){
            mode = args[1];
        }

        System.out.printf("Process number in total=%d\n",process_number);
        try{
            LocateRegistry.createRegistry(1099);
        } catch(RemoteException e){
            e.printStackTrace();
        }
        ArrayList<String> all_name = new ArrayList<String>();
        for(int i=0; i<process_number; i++){
            all_name.add("Process"+i);
        }
        ArrayList<Process> all_process = new ArrayList<Process>();
        try {
            for(int i=0; i<process_number; i++) {
                Process obj = new Process(process_number,i,all_name.get(i),all_name,mode);
                all_process.add(obj);
                // Bind the remote object's stub in the registry
                Registry registry = LocateRegistry.getRegistry();
                registry.rebind(all_name.get(i), obj);
            }
            //Create and install a security manager

            for(int i=0; i<process_number; i++) {
                new Thread((Process) all_process.get(i)).start();
            }
            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
        if(!mode.equals("auto")){
            Test tmp_test = new Test();
            tmp_test.basic_test(all_name);
            tmp_test.test_1(all_name);
            tmp_test.test_2(all_name);
            tmp_test.test_3(all_name);
        }
    }
}