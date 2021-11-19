import java.io.Serializable;
import java.util.ArrayList;

public class Token implements Serializable {
ArrayList<Integer> TN;
ArrayList<Character> TS;
    Token(int process_number){
        TN = new ArrayList<Integer>();
        TS = new ArrayList<Character>();
        for(int i=0; i<process_number; i++){
            TN.add(0);
            TS.add('O');
        }
    }
}
