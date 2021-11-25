import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public interface ProcessInter extends Remote{

    public void send_request(Request request,int dst_index) throws RemoteException;

    public void send_token(Token token,int dst_index) throws RemoteException;

    public void requesting() throws RemoteException;


    public void receive_request(Request request) throws RemoteException;

    public void add_request_buffer(Request request) throws RemoteException;

    public void get_token(Token token) throws RemoteException;

    public void wrapper() throws RemoteException;

    public int get_index() throws RemoteException;

    public void reset(int process_number) throws RemoteException;

    public int number_finished() throws RemoteException;

    public void set_cycle(int t_cycle) throws RemoteException;

}
