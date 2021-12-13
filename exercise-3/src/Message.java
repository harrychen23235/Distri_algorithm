import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

enum Message_type{
    CONNECT,INITIATE,TEST,ACCEPT,REJECT,REPORT,CHANGE_ROOT
}
public class Message implements Serializable {
    int sender;
    int receiver;
    Message_type message_type;
    int L;
    int F;
    ALL_SN S;
    Edge r_edge;
    public Message(int t_sender, int t_receiever, Message_type t_message_type, int t_L, int t_F, ALL_SN t_S){
        sender = t_sender;
        receiver = t_receiever;
        message_type = t_message_type;
        L = t_L;
        F = t_F;
        S = t_S;
    }
    public void set_r_edge(Edge edge){
        r_edge = edge;
    }

}
