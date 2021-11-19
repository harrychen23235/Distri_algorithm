import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
public class Request implements Serializable {
    int sender;
    int receiver;
    int request_count;
    Request(int t_sender,int t_receiver,int t_request_count){
        sender = t_sender;
        receiver = t_receiver;
        request_count = t_request_count;
    }
}
