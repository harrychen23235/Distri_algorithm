


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Test {

    public ProcessInter get_process(String name) {
        try {
            Registry registry = LocateRegistry.getRegistry(null);
            ProcessInter stub = (ProcessInter) registry.lookup(name);
            return stub;
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
        return null;
    }

    public void basic_test(ArrayList<String> all_name) {
        ProcessInter process1 = get_process(all_name.get(0));
        ProcessInter process2 = get_process(all_name.get(1));
        try {
            process1.reset();
            process2.reset();
            Message message1 = new Message(all_name.get(0), all_name.get(1), 1);
            process1.add_send_buffer(message1);
            process1.send();
            process2.receive();

            Message message2 = new Message(all_name.get(0), all_name.get(1), 2);
            process1.add_send_buffer(message2);
            process1.send();
            process2.receive();
            // Sleep at least the sum of all delays to be sure all messages have arrived.
            Thread.sleep(20);

            LinkedList<Message> messages = process2.get_received_messages();
            assert (2 == messages.size());
            assert (message1.index == messages.get(0).index);
            assert (message2.index == messages.get(1).index);
            System.out.println("basic test passed");
        } catch ( InterruptedException e) {
            e.printStackTrace();
        }
        catch ( RemoteException e) {
            e.printStackTrace();
        }
    }

        public void test_1 (ArrayList < String > all_name) {
            ProcessInter process1 = get_process(all_name.get(0));
            ProcessInter process2 = get_process(all_name.get(1));
            ProcessInter process3 = get_process(all_name.get(2));

            try {
                process1.reset();
                process2.reset();
                process3.reset();
                Message message1 = new Message(all_name.get(0), all_name.get(1), 1);
                message1.set_delay(1000);
                process1.add_send_buffer(message1);
                process1.send();
                process2.receive();

                Message message2 = new Message(all_name.get(0), all_name.get(2), 2);
                process1.add_send_buffer(message2);
                process1.send();
                process3.receive();

                Message message3 = new Message(all_name.get(2), all_name.get(1), 3);
                process3.add_send_buffer(message3);
                process3.send();
                process2.receive();

                // Sleep at least the sum of all delays to be sure all messages have arrived.
                Thread.sleep(2500);

                LinkedList<Message> messages_3 = process3.get_received_messages();
                LinkedList<Message> messages_2 = process2.get_received_messages();

                assert (1 == messages_3.size());
                assert (2 == messages_2.size());
                assert (message1.index == messages_2.get(0).index);
                assert (message3.index == messages_2.get(1).index);
                System.out.println("test1 passed");
            } catch ( InterruptedException e) {
                e.printStackTrace();
            }
            catch ( RemoteException e) {
                e.printStackTrace();
            }

        }

    public void test_2 (ArrayList < String > all_name) {
        ProcessInter process1 = get_process(all_name.get(0));
        ProcessInter process2 = get_process(all_name.get(1));
        ProcessInter process3 = get_process(all_name.get(2));

        try {
            process1.reset();
            process2.reset();
            process3.reset();
            Message message1 = new Message(all_name.get(0), all_name.get(1), 1);
            message1.set_delay(500);
            process1.add_send_buffer(message1);
            process1.send();
            process2.receive();

            Message message2 = new Message(all_name.get(0), all_name.get(1), 2);
            message2.set_delay(0);
            process1.add_send_buffer(message2);
            process1.send();
            process2.receive();

            Message message3 = new Message(all_name.get(1), all_name.get(2), 3);
            message2.set_delay(100);
            process2.add_send_buffer(message3);
            process2.send();
            process3.receive();



            // Sleep at least the sum of all delays to be sure all messages have arrived.
            Thread.sleep(1000);

            LinkedList<Message> messages_2 = process2.get_received_messages();
            LinkedList<Message> messages_3 = process3.get_received_messages();

            assert (2 == messages_2.size());
            assert (1 == messages_3.size());

            assert (message1.index == messages_2.get(0).index);
            assert (message2.index == messages_2.get(1).index);
            assert (message3.index == messages_3.get(0).index);
            System.out.println("test_2 passed");
        } catch ( InterruptedException e) {
            e.printStackTrace();
        }
        catch ( RemoteException e) {
            e.printStackTrace();
        }
    }

    public void test_3 (ArrayList < String > all_name) {
        ProcessInter process1 = get_process(all_name.get(0));
        ProcessInter process2 = get_process(all_name.get(1));
        ProcessInter process3 = get_process(all_name.get(2));
        ProcessInter process4 = get_process(all_name.get(3));
        try {
            process1.reset();
            process2.reset();
            process3.reset();
            Message message1 = new Message(all_name.get(0), all_name.get(2), 1);
            message1.set_delay(100);
            process1.add_send_buffer(message1);
            process1.send();
            process3.receive();

            Message message2 = new Message(all_name.get(0), all_name.get(1), 2);
            message2.set_delay(0);
            process1.add_send_buffer(message2);
            process1.send();
            process2.receive();

            Message message3 = new Message(all_name.get(0), all_name.get(3), 3);
            message3.set_delay(200);
            process1.add_send_buffer(message3);
            process1.send();
            process4.receive();

            Message message4 = new Message(all_name.get(1), all_name.get(2), 4);
            message4.set_delay(10);
            process2.add_send_buffer(message4);
            process2.send();
            process3.receive();

            Message message5 = new Message(all_name.get(1), all_name.get(3), 5);
            message5.set_delay(20);
            process2.add_send_buffer(message5);
            process2.send();
            process4.receive();

            // Sleep at least the sum of all delays to be sure all messages have arrived.
            Thread.sleep(1000);

            LinkedList<Message> messages_2 = process2.get_received_messages();
            LinkedList<Message> messages_3 = process3.get_received_messages();
            LinkedList<Message> messages_4 = process4.get_received_messages();

            assert (1 == messages_2.size());
            assert (2 == messages_3.size());
            assert (2 == messages_4.size());

            assert (message2.index == messages_2.get(0).index);
            assert (message1.index == messages_3.get(0).index);
            assert (message4.index == messages_3.get(1).index);
            assert (message5.index == messages_4.get(0).index);
            assert (message3.index == messages_4.get(1).index);
            System.out.println("test_3 passed");
        } catch ( InterruptedException e) {
            e.printStackTrace();
        }
        catch ( RemoteException e) {
            e.printStackTrace();
        }

    }
    }

