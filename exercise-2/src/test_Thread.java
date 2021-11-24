import java.rmi.RemoteException;
public class test_Thread implements Runnable {
    ProcessInter process;

    test_Thread(ProcessInter t_process) {
        process = t_process;
    }

    public void run() {
        try {
            process.wrapper();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
