import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
public class Message implements Serializable {
    String sender;
    String receiver;
    ArrayList<Integer> timestamp;
    HashMap<Integer, List<Integer>> message_state;
    int index;
    Message(String t_sender,String t_receiver,int t_index){
        sender = t_sender;
        receiver = t_receiver;
        index = t_index;
    }
    void set_timestamp(ArrayList<Integer> clock){
        timestamp = new  ArrayList<Integer>();
        for(int i=0; i<clock.size();i++){
            timestamp.add(clock.get(i));
        }
    }
    void set_message_state(HashMap<Integer, List<Integer>> input_state) {
        message_state = new HashMap<Integer, List<Integer>>();
        Iterator<Entry<Integer,List<Integer>>> t_iter = input_state.entrySet().iterator();
        while(t_iter.hasNext()){
            Entry<Integer,List<Integer>> temp = t_iter.next();
            int id = temp.getKey();
            List<Integer> content = temp.getValue();
            List<Integer> new_content = new ArrayList<Integer>();
            for(int i=0; i<content.size(); i++){
                new_content.add(content.get(i));
            }
            message_state.put(id,new_content);
        }
    }

}
