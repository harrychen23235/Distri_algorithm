import javax.management.remote.JMXServerErrorException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map.Entry;

enum ALL_SN {
    FOUND, FIND, SLEEPING
}
enum Edge_S{
    IN, NOT_IN, UNKNOWN
}

public class Node extends UnicastRemoteObject implements NodeInter {
    public int id;
    public ArrayList<Edge> all_edge;
    ALL_SN SN;
    int LN;
    int FN;
    HashMap<Edge,Edge_S> SE = new HashMap<Edge,Edge_S>();
    int find_count;
    Queue<Queue_Item> queue;
    Edge in_branch = null;
    Edge best_edge = null;
    Edge test_edge = null;
    int best_weight = Integer.MAX_VALUE;
    Queue<Queue_Item> main_queue;
    boolean solved = false;
    String ip_address = "192.168.0.102";

    Random random_number_gen;
    Node(int t_id) throws RemoteException {
        id = t_id;
        all_edge = new ArrayList<Edge>();
        SE = new HashMap<Edge,Edge_S>();
        queue = new LinkedList<Queue_Item>();
        main_queue = new LinkedList<Queue_Item>();
        SN = ALL_SN.SLEEPING;
        FN = -1;
        solved = false;
        random_number_gen = new Random();
    }
    public void add_edge(Edge edge){
        all_edge.add(edge);
    }
    public void wakeup(){
        try {
            Thread.sleep(random_number_gen.nextInt(100));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        System.out.printf("Node%d starts to wakup\n",id);
        int min_weight = Integer.MAX_VALUE;
        Edge j = null;
        for(int i=0; i<all_edge.size(); i++){
            SE.put(all_edge.get(i),Edge_S.UNKNOWN);
            if(all_edge.get(i).weight<min_weight){
                min_weight = all_edge.get(i).weight;
                j = all_edge.get(i);
            }
        }
        SE.put(j,Edge_S.IN);
        LN = 0;
        SN = ALL_SN.FOUND;
        find_count = 0;
        send_message(Message_type.CONNECT, 0, FN, SN, j);
    }
    public void receive_connect(Message message,Edge j) throws RemoteException{
        System.out.printf("Node%d receive connect from Node%d\n",id,edgeJudge(j));
        if(SN == ALL_SN.SLEEPING){
            wakeup();
        }
        if(message.L < LN) {
            SE.put(j, Edge_S.IN);
            System.out.printf("Node%d and Node%d are now connected,different level\n",id,edgeJudge(j));
            System.out.printf("Node%d send initiate to Node%d\n",id,edgeJudge(j));
            send_message(Message_type.INITIATE, LN, FN, SN, j);
            if(SN == ALL_SN.FIND){
                find_count +=1;
            }
        }
        else{
            //System.out.printf("Edge %d to %d's SE in Node %d is ",j.id1,j.id2,id);
            //System.out.println(SE.get(j));
            if(SE.get(j)==Edge_S.UNKNOWN){
                message.set_r_edge(j);
                queue.add(new Queue_Item(message,j));
            }
            else{
                System.out.printf("Node%d and Node%d are now connected, same level\n",id,edgeJudge(j));

                System.out.printf("Node%d send initiate to Node%d\n",id,edgeJudge(j));
                send_message(Message_type.INITIATE, LN+1, j.weight,ALL_SN.FIND, j);
            }
        }

    }
    public void receive_initiate(Message message, Edge j) throws RemoteException{
        System.out.printf("Node%d receive initiate from Node%d\n",id,edgeJudge(j));
        LN = message.L;
        FN = message.F;
        SN = message.S;
        in_branch = j;
        best_edge = null;
        best_weight = Integer.MAX_VALUE;
        for(int i=0; i<all_edge.size(); i++){
            if(!all_edge.get(i).equals(j)&&SE.get(all_edge.get(i)) == Edge_S.IN){
                System.out.printf("Node%d send initiate to Node%d\n",id,edgeJudge(j));
                send_message(Message_type.INITIATE, LN, FN,SN, all_edge.get(i));
                if(SN == ALL_SN.FIND){
                    find_count += 1;
                }
            }
        }
        if(SN == ALL_SN.FIND){
            test();
        }
    }
    public void test() throws  RemoteException{
        try {
            Thread.sleep(random_number_gen.nextInt(100));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        System.out.printf("Node%d start to test\n",id);
        Edge t_edge = null;
        int t_weight = Integer.MAX_VALUE;
        for(int i=0; i<all_edge.size();i++){
            if(SE.get(all_edge.get(i)) == Edge_S.UNKNOWN){
                if(all_edge.get(i).weight < t_weight){
                    t_edge = all_edge.get(i);
                    t_weight  = all_edge.get(i).weight;
                }
            }
        }
        if(t_edge!=null){
            System.out.printf("Node%d send test to Node%d\n",id,edgeJudge(t_edge));
            send_message(Message_type.TEST, LN, FN,SN, t_edge);
        }
        else{
            report();
        }
    }

    public void receive_test(Message message, Edge j) throws  RemoteException{
        System.out.printf("Node%d receive test from Node%d\n",id,edgeJudge(j));
        if(SN == ALL_SN.SLEEPING){
            wakeup();
        }
        if(message.L>LN){
            message.set_r_edge(j);
            queue.add(new Queue_Item(message,j));
        }
        else{
            if(message.F !=FN){
                System.out.printf("Node%d send accept to Node%d\n",id,edgeJudge(j));
                send_message(Message_type.ACCEPT, LN, FN,SN, j);
            }
            else{
                if(SE.get(j)==Edge_S.UNKNOWN){
                    SE.put(j,Edge_S.NOT_IN);
                }
                if(test_edge == null || !test_edge.equals(j)){
                    System.out.printf("Node%d send reject to Node%d\n",id,edgeJudge(j));
                    send_message(Message_type.REJECT, LN, FN,SN, j);
                }
                else{
                    test();
                }
            }
        }
    }

    public void receive_accept(Message message, Edge j) throws RemoteException{
        System.out.printf("Node%d receive accept from Node%d\n",id,edgeJudge(j));
        test_edge = null;
        if(j.weight<best_weight){
            best_edge = j;
            best_weight = j.weight;
        }
        report();
    }
    public void receive_reject(Message message, Edge j) throws  RemoteException{
        System.out.printf("Node%d receive reject from Node%d\n",id,edgeJudge(j));
        if(SE.get(j) == Edge_S.UNKNOWN){
            SE.put(j,Edge_S.NOT_IN);
        }
        test();
    }
    public void report() throws  RemoteException{
        try {
            Thread.sleep(random_number_gen.nextInt(100));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        System.out.printf("Node%d start to report\n",id);
        if(find_count == 0 && test_edge == null){
            SN = ALL_SN.FOUND;
            System.out.printf("Node%d send report to Node%d\n",id,edgeJudge(in_branch));
            send_message(Message_type.REPORT, LN, best_weight,SN, in_branch);
        }
    }

    public void receieve_report(Message message, Edge j) throws  RemoteException{
        System.out.printf("Node%d receive report from Node%d\n",id,edgeJudge(j));
        if(!j.equals(in_branch)){
            find_count -=1;
            if(message.F < best_weight){
                best_weight = message.F;
                best_edge = j;
            }
            report();
        }
        else{
            if(SN == ALL_SN.FIND){
                message.set_r_edge(j);
                queue.add(new Queue_Item(message,j));
            }
            else{
                if(message.F>best_weight){
                    change_root();
                }
                else{
                    if(best_weight == Integer.MAX_VALUE && message.F == Integer.MAX_VALUE){
                        solved = true;
                    }
                }
            }
        }
    }
    public void change_root() throws RemoteException{
        try {
            Thread.sleep(random_number_gen.nextInt(100));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        System.out.printf("Node%d start to change_root\n",id);
        if(SE.get(best_edge) == Edge_S.IN){
            in_branch = best_edge;
            System.out.printf("Node%d send change_root to Node%d\n",id,edgeJudge(best_edge));
            send_message(Message_type.CHANGE_ROOT, LN, FN,SN, best_edge);
        }
        else{
            System.out.printf("Node%d send connect to Node%d\n",id,edgeJudge(best_edge));
            send_message(Message_type.CONNECT, LN, FN,SN, best_edge);
            SE.put(best_edge,Edge_S.IN);
        }
    }

    public void receive_change_root(Message message, Edge j) throws RemoteException{
        System.out.printf("Node%d receive change_root from Node%d\n",id,edgeJudge(j));
        change_root();
    }

    public synchronized void add_main_queue(Queue_Item item) throws RemoteException{
        synchronized (main_queue) {
            main_queue.add(item);
        }
    }

    public void send_message(Message_type message_type,int LN, int FN,ALL_SN SN, Edge send_edge){
        try {
            Thread.sleep(random_number_gen.nextInt(100));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        int r_id = -1;
        if(this.id == send_edge.id1){
            r_id = send_edge.id2;
        }
        else
            r_id = send_edge.id1;
        Message temp_message = new Message(this.id,r_id, message_type,LN, FN, SN);
        Queue_Item tqm = new Queue_Item(temp_message,send_edge);
        try {
            get_Node(r_id, ip_address).add_main_queue(tqm);
        }
        catch(Exception e){
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }



    NodeInter get_Node( int name,String host_name) {
        try {
            Registry registry = LocateRegistry.getRegistry(null);
            String[] temp = registry.list();
            NodeInter stub = (NodeInter) registry.lookup(Integer.toString(name));
            return stub;
        }
        catch(Exception e){
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
        return null;
    }


    public void execute_message(Queue_Item item_now) throws RemoteException{
        try {
            Thread.sleep(random_number_gen.nextInt(100));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        switch (item_now.message.message_type){
            case CONNECT:
                receive_connect(item_now.message,item_now.r_edge);
                break;

            case INITIATE:
                receive_initiate(item_now.message, item_now.r_edge);
                break;
            case TEST:
                receive_test(item_now.message,item_now.r_edge);
                break;

            case ACCEPT:
                receive_accept(item_now.message, item_now.r_edge);
                break;
            case REJECT:
                receive_reject(item_now.message, item_now.r_edge);
                break;
            case REPORT:
                receieve_report(item_now.message, item_now.r_edge);
                break;
            case CHANGE_ROOT:
                receive_change_root(item_now.message, item_now.r_edge);
                break;
            default:
                break;
        }
    }

    public void check_queue() throws RemoteException{
        synchronized (queue) {
            int num_in_all = queue.size();
            while(num_in_all!=0) {
                if(solved == true){
                    return;
                }
                Queue_Item t_it = queue.poll();
                if(t_it != null)
                execute_message(t_it);
                num_in_all--;
            }
        }
    }

    public int edgeJudge(Edge e1) throws  RemoteException{
        int r_id = -1;
        if(this.id == e1.id1){
            r_id = e1.id2;
        }
        else
            r_id = e1.id1;
        return r_id;
    }
    public void execute(){
        wakeup();
        while(true) {
            try {
                if(solved == true){
                    break;
                }
                Queue_Item item_now;
                synchronized (main_queue) {
                    item_now = main_queue.poll();
                }
                if (item_now == null) {

                } else {
                    execute_message(item_now);
                }
                check_queue();
                Thread.sleep(100);
            }
            catch(Exception e){
                System.err.println("Client exception: " + e.toString());
                e.printStackTrace();
            }
        }
    }
}
