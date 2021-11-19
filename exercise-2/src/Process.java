import java.rmi.RemoteException;
import java.util.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map.Entry;
public class Process implements ProcessInter,Runnable {
    ArrayList<Integer> N;
    ArrayList<Character> S;
    LinkedList<Request> Request_buffer;
    boolean has_token;
    Token token_buffer;
    Token my_token;
    int index;
    Process(int process_number,int t_index,boolean t_has_token) {
        N = new ArrayList<Integer>();
        S = new ArrayList<Character>();
        Request_buffer = new LinkedList<Request>();
        has_token = t_has_token;
        index = t_index;
        for (int i = 0; i < process_number; i++)
            N.add(0);
        if (t_has_token) {
            for (int i = 0; i < process_number; i++) {
                if (i != t_index)
                    S.add('O');
                else
                    S.add('H');
            }
        } else {
            for (int i = 0; i < t_index; i++) {
                S.add('R');
            }
            for (int i = t_index; i < process_number; i++) {
                S.add('O');
            }

        }
    }
    Process(int process_number,int t_index,boolean t_has_token,Token t_token) {
        N = new ArrayList<Integer>();
        S = new ArrayList<Character>();
        Request_buffer = new LinkedList<Request>();
        has_token = t_has_token;
        index = t_index;
        my_token = t_token;
        for (int i = 0; i < process_number; i++)
            N.add(0);
        if (t_has_token) {
            for (int i = 0; i < process_number; i++) {
                if (i != t_index)
                    S.add('O');
                else
                    S.add('H');
            }
        } else {
            for (int i = 0; i < t_index; i++) {
                S.add('R');
            }
            for (int i = t_index; i < process_number; i++) {
                S.add('O');
            }

        }
    }
    public void send_request(Request request,int dst_index){
        System.out.printf("Process %d send request to Process %d\n", this.index,dst_index);
        ProcessInter des = this.get_process(Integer.toString(dst_index));
        try {
            des.add_request_buffer(request);
        }
        catch(Exception e){
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public void send_token(Token token,int dst_index){
        System.out.printf("Process %d send token to Process %d\n", this.index,dst_index);
        ProcessInter des = this.get_process(Integer.toString(dst_index));
        try {
            des.get_token(token);
        }
        catch(Exception e){
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
        my_token =null;
        has_token = false;
    }

    public void requesting(){
        System.out.printf("Process %d request for resources\n", this.index);
        S.set(index,'R');
        N.set(index,N.get(index)+1);

        for(int i=0; i<S.size(); i++){
            if(i!=index && S.get(i)=='R'){
                Request t_request = new Request(index,i,N.get(index));
                send_request(t_request,i);
            }
        }
    }

    public void receive_request(Request request){
        int j = request.sender;
        System.out.printf("Process %d receive request from Process %d\n", this.index,j);
        N.set(j, request.request_count);
        switch (S.get(index)){
            case 'E':
            case 'O': S.set(j,'R');
            break;
            case 'R':
                if(S.get(j)!='R'){
                    S.set(j,'R');
                    send_request(new Request(index,j,N.get(index)),j);
                }
                break;
            case 'H':
                S.set(j,'R');
                S.set(index,'O');
                my_token.TS.set(j,'R');
                my_token.TN.set(j, request.request_count);
                    send_token(my_token,j);
                break;

        }
    }

    public void receive_token(){
        System.out.printf("Process %d get the token\n", this.index);
        my_token = token_buffer;
        token_buffer = null;
        has_token = true;
        Random rand = new Random();
        S.set(index,'E');
        try {
            System.out.printf("Process %d start to use CS\n", this.index);
            Thread.sleep((long)(rand.nextInt(5000) + 5000));
            System.out.printf("Process %d end up using CS\n", this.index);
        } catch (Exception var6) {
            System.err.println("Client exception: " + var6.toString());
            var6.printStackTrace();
        }
        S.set(index,'O');
        my_token.TS.set(index,'O');
        for(int i=0; i<S.size(); i++){
            if(N.get(i)>my_token.TN.get(i)){
                my_token.TN.set(i,N.get(i));
                my_token.TS.set(i,S.get(i));
            }
            else{
                N.set(i,my_token.TN.get(i));
                S.set(i,my_token.TS.get(i));
            }
        }
        for(int i=0; i<S.size(); i++){
            if(S.get(i)=='R'){
                send_token(my_token,i);
                return;
            }
        }
        S.set(index,'H');
    }


    public void run() {
        System.out.printf("start to run Process %d\n", this.index);
        Random rand = new Random();

        try {
            Thread.sleep((long)(rand.nextInt(1000) + 1000));
        } catch (Exception var7) {
            System.err.println("Client exception: " + var7.toString());
            var7.printStackTrace();
        }

        while(true) {
            Request temp;
            synchronized(this.Request_buffer) {
                if (this.Request_buffer.size() != 0) {
                    while(this.Request_buffer.size() != 0) {
                        temp = (Request) this.Request_buffer.getLast();
                        this.Request_buffer.removeLast();
                        this.receive_request(temp);
                    }
                }
            }
            if(token_buffer!=null){
                synchronized(token_buffer){
                        receive_token();
                    }
            }

            if(S.get(index)!='H'&&S.get(index)!='E'){
                if(rand.nextInt()%5==0){
                    requesting();
                }
            }

            try {
                Thread.sleep((long)(rand.nextInt(1000) + 1000));
            } catch (Exception var6) {
                System.err.println("Client exception: " + var6.toString());
                var6.printStackTrace();
            }
         }
        }








    ProcessInter get_process( String name,String host_name) {
        try {
            Registry registry = LocateRegistry.getRegistry(host_name);
            ProcessInter stub = (ProcessInter) registry.lookup(name);
            return stub;
        }
        catch(Exception e){
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
        return null;
    }
    ProcessInter get_process(String name) {
        try {
            Registry registry = LocateRegistry.getRegistry(null);
            ProcessInter stub = (ProcessInter) registry.lookup(name);
            return stub;
        }
        catch(Exception e){
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
        return null;
    }
    synchronized  public void add_request_buffer(Request request){
        Request_buffer.add(request);
    }

    public void get_token(Token token){
        token_buffer = token;
    }

    public int get_index(){
        return index;
    }
}
