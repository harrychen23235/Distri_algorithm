

import java.rmi.Remote;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class mainframe {
    public static void main(String args[]) {
        int process_number = 8;
        String mode = "basic";
        if(args.length == 1){
            process_number = Integer.parseInt(args[0]);
        }
        System.out.printf("Process number in total=%d\n",process_number);
        try{
            LocateRegistry.createRegistry(1099);
        } catch(RemoteException e){
            e.printStackTrace();
        }
        ArrayList<String> all_name = new ArrayList<String>();
        for(int i=0; i<process_number; i++){
            all_name.add(Integer.toString(i));
        }
        ArrayList<Process> all_process = new ArrayList<Process>();

        try {
            for(int i=0; i<process_number; i++) {
                Process obj;
                if(i==0)
                    obj = new Process(process_number,i,true,new Token(process_number),mode);
                else
                    obj = new Process(process_number,i,false,mode);
                all_process.add(obj);
                ProcessInter stub = (ProcessInter) UnicastRemoteObject.exportObject(obj, 0);
                // Bind the remote object's stub in the registry
                Registry registry = LocateRegistry.getRegistry();
                registry.rebind(Integer.toString(i), stub);
            }
            if(mode.equals("auto")) {
                for (int i = 0; i < process_number; i++) {
                    new Thread((Process) all_process.get(i)).start();
                }
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
            tmp_test.test_4(all_name);
        }
    }
}