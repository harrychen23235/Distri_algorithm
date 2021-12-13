import java.io.Serializable;

public class Edge implements Serializable {
    int id1;
    int id2;
    int weight;
    public Edge(int t_id1, int t_id2, int t_weight){
        id1 = t_id1;
        id2 = t_id2;
        weight = t_weight;
    }
    public int hashCode(){
        return 10000*id1+1000*id2+weight;
    }
    public boolean equals(Object obj) {
        if (obj.getClass() == this.getClass()) {

            boolean condition_1 = (this.id1 == ((Edge) obj).id1 && this.id2 == ((Edge) obj).id2)
                                ||(this.id1 == ((Edge) obj).id2 && this.id2 == ((Edge) obj).id1);
            boolean condition_2 = this.weight == ((Edge) obj).weight;

            return condition_2 && condition_1;

        }
        else{
            return false;
        }
    }
}
