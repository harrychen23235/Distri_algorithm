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
    boolean has_token_at_first;

    Token token_buffer;
    Token my_token;

    int index;
    String mode;

    int action_finished=0;

    int cycle = 1;
    Process(int process_number,int t_index,boolean t_has_token,String t_mode) {
        N = new ArrayList<Integer>();
        S = new ArrayList<Character>();
        Request_buffer = new LinkedList<Request>();
        has_token = t_has_token;
        index = t_index;
        mode = t_mode;
        has_token_at_first = t_has_token;
        action_finished = 0;
        if(t_mode.equals("auto")){
            cycle = Integer.MAX_VALUE;
        }
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
    Process(int process_number,int t_index,boolean t_has_token,Token t_token,String t_mode) {
        N = new ArrayList<Integer>();
        S = new ArrayList<Character>();
        Request_buffer = new LinkedList<Request>();
        has_token = t_has_token;
        index = t_index;
        my_token = t_token;
        mode = t_mode;
        has_token_at_first = t_has_token;
        action_finished = 0;
        if(t_mode.equals("auto")){
            cycle = Integer.MAX_VALUE;
        }
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

    public void reset(int process_number){
        N = new ArrayList<Integer>();
        S = new ArrayList<Character>();
        Request_buffer = new LinkedList<Request>();
        has_token = has_token_at_first;
        action_finished = 0;
        cycle = 1;
        for (int i = 0; i < process_number; i++)
            N.add(0);
        if (has_token) {
            my_token = new Token(process_number);
            for (int i = 0; i < process_number; i++) {
                if (i != index)
                    S.add('O');
                else
                    S.add('H');
            }
        } else {
            my_token = null;
            for (int i = 0; i < index; i++) {
                S.add('R');
            }
            for (int i = index; i < process_number; i++) {
                S.add('O');
            }

        }
    }
    public void send_request(Request request,int dst_index){
        System.out.printf("Process %d send request to Process %d\n", this.index,dst_index);
        ProcessInter des = this.get_process(Integer.toString(dst_index));
        try {
            des.receive_request(request);
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
        requesting_action();

    }
    public void requesting_action(){
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

    synchronized public void receive_request(Request request){
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
                synchronized (my_token) {
                    S.set(j, 'R');
                    S.set(index, 'O');
                    my_token.TS.set(j, 'R');
                    my_token.TN.set(j, request.request_count);
                    send_token(my_token, j);
                    break;
                }

        }
    }

    public void execute(){
        try {
            Random rand = new Random();
            System.out.printf("Process %d start to use CS\n", this.index);
            Thread.sleep((long)(rand.nextInt(200)));
            System.out.printf("Process %d end up using CS\n", this.index);
            action_finished++;
        } catch (Exception var6) {
            System.err.println("Client exception: " + var6.toString());
            var6.printStackTrace();
        }
    }
    public void receive_token(){
        boolean leave = false;
        while (!leave) {

            if (action_finished < cycle) {
                System.out.printf("Process %d keep waiting for token\n",index);
                if (token_buffer != null) {
                    synchronized (token_buffer) {
                        leave = true;
                        receive_token_action();
                    }
                } else if (my_token != null) {
                    synchronized (my_token) {
                        leave = true;
                        receive_token_action();
                    }
                }
                try {
                    Thread.sleep(100);
                } catch (Exception var6) {
                    System.err.println("Client exception: " + var6.toString());
                    var6.printStackTrace();
                }
            }
        }
    }
    public void receive_token_action() {
        System.out.printf("Process %d get the token\n", this.index);
        if (my_token == null) {
            my_token = token_buffer;
            token_buffer = null;
        }
        synchronized (my_token) {
            has_token = true;
            S.set(index, 'E');

            execute();
            S.set(index, 'O');
            my_token.TS.set(index, 'O');
            for (int i = 0; i < S.size(); i++) {
                if (N.get(i) > my_token.TN.get(i)) {
                    my_token.TN.set(i, N.get(i));
                    my_token.TS.set(i, S.get(i));
                } else {
                    N.set(i, my_token.TN.get(i));
                    S.set(i, my_token.TS.get(i));
                }
            }
            for (int i = 0; i < S.size(); i++) {
                if (S.get(i) == 'R') {
                    send_token(my_token, i);
                    return;
                }
            }
            S.set(index, 'H');
        }
    }

    public void wrapper() {
        Random rand = new Random();
        while (action_finished<cycle) {
            //System.out.printf("action_finished of Process %d = %d\n",index,action_finished);
            if (my_token == null&&action_finished<cycle) {
                requesting();
            }
            receive_token();
            try {
                Thread.sleep(rand.nextInt(200));
            }
            catch (Exception var6) {
                System.err.println("Client exception: " + var6.toString());
                var6.printStackTrace();
            }
        }
    }

    public void run() {
        System.out.printf("start to run Process %d\n", this.index);
        if (mode.equals("auto")) {
            System.out.printf("run Process %d in auto mode\n", this.index);
            Random rand = new Random();

            try {
                Thread.sleep((long) (rand.nextInt(1000) + 1000));
            } catch (Exception var7) {
                System.err.println("Client exception: " + var7.toString());
                var7.printStackTrace();
            }

            while (true) {
                if (rand.nextInt() % 5 == 0&& (S.get(index) != 'H' && S.get(index) != 'E') ) {
                    requesting();
                    receive_token();
                }




                try {
                    Thread.sleep((long) (rand.nextInt(1000) + 1000));
                } catch (Exception var6) {
                    System.err.println("Client exception: " + var6.toString());
                    var6.printStackTrace();
                }
            }
        }
        else{

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

    public int number_finished(){
        return action_finished;
    }

    public void set_cycle(int t_cycle){
        cycle = t_cycle;
    }
}