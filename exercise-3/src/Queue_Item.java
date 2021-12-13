import java.io.Serializable;

public class Queue_Item implements Serializable {
    Message message;
    Edge r_edge;
    public Queue_Item(Message t_message, Edge t_r_edge){
        message = t_message;
        r_edge = t_r_edge;
    }
}
