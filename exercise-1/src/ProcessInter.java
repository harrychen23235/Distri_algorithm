import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public interface ProcessInter extends Remote{

    public void send(Message message) throws RemoteException;

    public void receive(Message message) throws RemoteException;

    public void deliver(Message message) throws RemoteException;

    public void add_receive_buffer(Message message) throws RemoteException;

    public int get_index() throws RemoteException;

}
