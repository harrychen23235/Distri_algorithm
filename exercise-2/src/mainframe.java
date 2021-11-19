

import java.rmi.Remote;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class mainframe {
    public static void main(String args[]) {
        int process_number = 3;
        if(args.length == 1){
            process_number = Integer.parseInt(args[0]);
        }
        System.out.printf("Process number in total=%d\n",process_number);
        try {
            ArrayList<Process> all_process = new ArrayList<Process>();
            for(int i=0; i<process_number; i++) {
                Process obj;
                if(i==0)
                    obj = new Process(process_number,i,true,new Token(process_number));
                else
                    obj = new Process(process_number,i,false);
                all_process.add(obj);
                ProcessInter stub = (ProcessInter) UnicastRemoteObject.exportObject(obj, 0);
                // Bind the remote object's stub in the registry
                Registry registry = LocateRegistry.getRegistry();
                registry.rebind(Integer.toString(i), stub);
            }
            for(int i=0; i<process_number; i++) {
                new Thread((Process) all_process.get(i)).start();
            }
            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}