


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Test {
	String ip_address = "192.168.0.102";
    public NodeInter get_Node(int number) {
        try {
            Registry registry = LocateRegistry.getRegistry(ip_address,1099);
            NodeInter stub = (NodeInter) registry.lookup(Integer.toString(number));
            return stub;
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
        return null;
    }
    public void pre_test() {

        try {
            NodeInter n2 = get_Node(2);

            Edge e1 = new Edge(1,2,1);

            n2.add_edge(e1);
            System.out.println("client ready");
            Thread.sleep(10000);

            new Thread(new test_Thread(n2)).start();

            Thread.sleep(10000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void basic_test() {


        try {
            
            NodeInter n3 = get_Node(3);


            Edge e2 = new Edge(2,3,5);
            Edge e3 = new Edge(3,1,3);


            n3.add_edge(e2);n3.add_edge(e3);
            System.out.println("Host ready");
            
            Thread.sleep(10000);


            new Thread(new test_Thread(n3)).start();

            Thread.sleep(10000);
            System.out.println("System end");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void all_test() {




            try {


                NodeInter n5 = get_Node(5);
                NodeInter n6 = get_Node(6);
                NodeInter n7 = get_Node(7);
                NodeInter n8 = get_Node(8);

                Edge e4 = new Edge(4,5,7);
                Edge e5 = new Edge(5,6,2);
                Edge e6 = new Edge(6,7,6);
                Edge e7 = new Edge(7,8,4);
                Edge e8 = new Edge(8,1,8);

                n5.add_edge(e4);n5.add_edge(e5);n6.add_edge(e5); n6.add_edge(e6);
                n7.add_edge(e6);n7.add_edge(e7);n8.add_edge(e7); n8.add_edge(e8);

                System.out.println("client ready");
                Thread.sleep(10000);
                new Thread(new test_Thread(n5)).start();
                new Thread(new test_Thread(n6)).start();
                new Thread(new test_Thread(n7)).start();
                new Thread(new test_Thread(n8)).start();
                
                Thread.sleep(20000);
                System.out.println("System end");

            } catch (Exception e) {
            e.printStackTrace();
        }
    }


    }


