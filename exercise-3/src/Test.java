


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

        try{
            LocateRegistry.createRegistry(1099);
        } catch(RemoteException e){
            e.printStackTrace();
        }

        ArrayList<Node> all_process = new ArrayList<Node>();
        try {
            for(int i=1; i<=2; i++) {
                Node obj = new Node(i);
                all_process.add(obj);
                // Bind the remote object's stub in the registry
                Registry registry = LocateRegistry.getRegistry(ip_address,1099);
                registry.rebind(Integer.toString(i), (NodeInter)obj);
            }
            NodeInter n1 = get_Node(1);
            //NodeInter n2 = get_Node(2);
            //NodeInter n3 = get_Node(3);

            Edge e1 = new Edge(1,2,1);
            //Edge e2 = new Edge(2,3,5);
            //Edge e3 = new Edge(3,1,3);

            n1.add_edge(e1);
            System.out.println("host ready");
            Thread.sleep(10000);


            new Thread(new test_Thread(n1)).start();

            Thread.sleep(10000);

            System.out.println("System End");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void basic_test() {

        try{
            LocateRegistry.createRegistry(1099);
        } catch(RemoteException e){
            e.printStackTrace();
        }

        ArrayList<Node> all_process = new ArrayList<Node>();
        try {
            for(int i=1; i<=3; i++) {
                Node obj = new Node(i);
                all_process.add(obj);
                // Bind the remote object's stub in the registry
                Registry registry = LocateRegistry.getRegistry();
                registry.rebind(Integer.toString(i), obj);
            }
            NodeInter n1 = get_Node(1);
            NodeInter n2 = get_Node(2);


            Edge e1 = new Edge(1,2,1);
            Edge e2 = new Edge(2,3,5);
            Edge e3 = new Edge(3,1,3);

            n1.add_edge(e1);n1.add_edge(e3);n2.add_edge(e1); n2.add_edge(e2);
            System.out.println("host ready");
            Thread.sleep(10000);

            new Thread(new test_Thread(n1)).start();
            new Thread(new test_Thread(n2)).start();


            Thread.sleep(10000);
            System.out.println("System end");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void all_test() {

            try{
                LocateRegistry.createRegistry(1099);
            } catch(RemoteException e){
                e.printStackTrace();
            }

            ArrayList<Node> all_process = new ArrayList<Node>();
            try {
                for(int i=1; i<=8; i++) {
                    Node obj = new Node(i);
                    all_process.add(obj);
                    // Bind the remote object's stub in the registry
                    Registry registry = LocateRegistry.getRegistry();
                    registry.rebind(Integer.toString(i), obj);
                }
                NodeInter n1 = get_Node(1);
                NodeInter n2 = get_Node(2);
                NodeInter n3 = get_Node(3);
                NodeInter n4 = get_Node(4);

                Edge e1 = new Edge(1,2,1);
                Edge e2 = new Edge(2,3,5);
                Edge e3 = new Edge(3,4,3);
                Edge e4 = new Edge(4,5,7);
                Edge e8 = new Edge(8,1,8);

                n1.add_edge(e1);n1.add_edge(e8);n2.add_edge(e1); n2.add_edge(e2);
                n3.add_edge(e2);n3.add_edge(e3);n4.add_edge(e3); n4.add_edge(e4);

                System.out.println("host ready");
                Thread.sleep(10000);

                new Thread(new test_Thread(n1)).start();
                new Thread(new test_Thread(n2)).start();
                new Thread(new test_Thread(n3)).start();
                new Thread(new test_Thread(n4)).start();

                Thread.sleep(20000);
                System.out.println("System end");

            } catch (Exception e) {
            e.printStackTrace();
        }
    }


    }


