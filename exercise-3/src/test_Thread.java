import java.rmi.RemoteException;
public class test_Thread implements Runnable {
    NodeInter node;

    test_Thread(NodeInter t_node) {
        node = t_node;
    }

    public void run() {
        try {
            node.execute();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
