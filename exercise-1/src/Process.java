import java.rmi.RemoteException;
import java.util.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map.Entry;
public class Process implements ProcessInter,Runnable {
    LinkedList<Message> send_buffer;
    LinkedList<Message> receive_buffer;
    LinkedList<Message> received_messages;
    HashMap<Integer, List<Integer>> status_store;
    LinkedList<Message> deliever_buffer;

    ArrayList<Integer> clock;
    int index;

    String process_name;
    ArrayList<String> name_list;
    Process(int process_number,int t_index, String t_process_name, ArrayList<String>t_name_list) {
        send_buffer = new LinkedList<Message>();
        receive_buffer = new LinkedList<Message>();
        received_messages = new LinkedList<Message>();
        deliever_buffer = new LinkedList<Message>();
        status_store = new HashMap<Integer, List<Integer>>();
        clock = new ArrayList<Integer>();
        for (int i = 0; i < process_number; i++)
            clock.add(0);
        index = t_index;
        process_name = t_process_name;
        name_list = t_name_list;
    }
    void increase_clock(){
        clock.set(index,clock.get(index)+1);
    }
    void increase_clock(ArrayList<Integer> t_clock){
        t_clock.set(index,t_clock.get(index)+1);
    }

    public void send(Message message){
        increase_clock();
        message.set_timestamp(clock);
        message.set_message_state(status_store);
        try {

            ProcessInter des = get_process(message.receiver);
            des.add_receive_buffer(message);

                System.out.printf("Send message %d from process%d to process%d\n",message.index,this.index,des.get_index());

            status_store.put(des.get_index(),copy_clock());
        }
        catch(Exception e){
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }

    }

    public void receive(Message message){
        System.out.printf("Process %d get message %d received\n",index,message.index);
        if(clock_compare(message.timestamp)){
            deliver(message);
            for(int i=0; i<deliever_buffer.size(); i++){
                Message temp = deliever_buffer.get(i);
                if(clock_compare(temp.timestamp)) {
                    deliver(temp);
                    deliever_buffer.remove(i);
                    i--;
                }
            }
        }
        else{
            deliever_buffer.add(message);
        }
    }

    public void deliver(Message message){

        received_messages.add(message);
        System.out.printf("Process %d get message %d delivered\n",index,message.index);
        Iterator<Entry<Integer,List<Integer>>> temp_it = message.message_state.entrySet().iterator();
        while(temp_it.hasNext()){
            Entry<Integer,List<Integer>> t_en = temp_it.next();
            int key = t_en.getKey();
            List<Integer> value = t_en.getValue();
            if(status_store.containsKey(key)){
                status_store.put(key,timestamp_compare(value,status_store.get(key)));
            }
            else{
                status_store.put(key,value);
            }
        }
    }

    public void run() {
        System.out.printf("start to run %s\n",process_name);
        Random rand = new Random();
        try {
            Thread.sleep(rand.nextInt(1000)+1000);
        }
        catch(Exception e){
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
        while(true){
            synchronized (receive_buffer){
                if(receive_buffer.size()!=0){
                    while(receive_buffer.size()!=0){
                        Message temp = receive_buffer.getLast();
                        receive_buffer.removeLast();
                        receive(temp);
                    }
                }
            }
            synchronized (send_buffer){
                random_send(rand.nextInt(5));
                if(send_buffer.size()!=0) {
                    while (send_buffer.size() != 0) {
                        Message temp = send_buffer.getLast();
                        send_buffer.removeLast();
                        send(temp);
                    }
                }
            }
            try {
                Thread.sleep(rand.nextInt(1000)+1000);
            }
            catch(Exception e){
                System.err.println("Client exception: " + e.toString());
                e.printStackTrace();
            }
        }

    }

    void random_send(int number){
        Random rand =new Random();
        for(int i=0; i<number; i++){
            Message temp = new Message(process_name,name_list.get(rand.nextInt(name_list.size())), rand.nextInt(1000));
            send_buffer.add(temp);
        }
    }
    ArrayList<Integer> copy_clock(){
        ArrayList<Integer> out_clock = new ArrayList<Integer>();
        for(int i=0; i<clock.size(); i++){
            out_clock.add(clock.get(i));
        }
        return out_clock;
    }
    boolean clock_compare(ArrayList<Integer>timestamp){
        ArrayList<Integer> cp_clock = copy_clock();
        increase_clock(cp_clock);
        for(int i=0; i<cp_clock.size(); i++){
            if(timestamp.get(i)>cp_clock.get(i)){
                return false;
            }
        }
        return true;
    }

    List<Integer> timestamp_compare(List<Integer>timestamp1,List<Integer>timestamp2){
        ArrayList<Integer> new_timestamp = new ArrayList<Integer>();
        for(int i=0; i<timestamp1.size(); i++){
            new_timestamp.add(Integer.max(timestamp1.get(i),timestamp2.get(i)));
        }
        return new_timestamp;
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
    synchronized  public void add_receive_buffer(Message message){
        receive_buffer.add(message);
    }
    public int get_index(){
        return index;
    }
}
