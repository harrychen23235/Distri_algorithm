import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public interface NodeInter extends Remote{

    void add_main_queue(Queue_Item item) throws RemoteException;
    public void add_edge(Edge edge) throws RemoteException;
    public void execute() throws RemoteException;
}
