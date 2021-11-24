


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;


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
        test_Thread thread1 = new test_Thread(process1);
        ProcessInter process2 = get_process(all_name.get(1));
        test_Thread thread2 = new test_Thread(process2);
        try {
            process1.reset(2);
            process2.reset(2);
            process1.set_cycle(1);
            process2.set_cycle(1);
            new Thread(thread1).start();
            new Thread(thread2).start();

            Thread.sleep(5000);

            assert(process1.number_finished()  == 1);

            assert(process2.number_finished() == 1);

            System.out.println("basic test passed");
        } catch ( InterruptedException e) {
            e.printStackTrace();
        }
        catch ( RemoteException e) {
            e.printStackTrace();
        }
    }

    public void test_1(ArrayList<String> all_name) {
        ProcessInter process1 = get_process(all_name.get(0));
        test_Thread thread1 = new test_Thread(process1);
        ProcessInter process2 = get_process(all_name.get(1));
        test_Thread thread2 = new test_Thread(process2);
        try {
            process1.reset(2);
            process2.reset(2);

            process1.set_cycle(5);
            process2.set_cycle(5);

            new Thread(thread1).start();
            new Thread(thread2).start();

            Thread.sleep(10000);

            assert(process1.number_finished()  == 5);

            assert(process2.number_finished() == 5);

            System.out.println("test_1 passed");
        } catch ( InterruptedException e) {
            e.printStackTrace();
        }
        catch ( RemoteException e) {
            e.printStackTrace();
        }
    }


    public void test_2(ArrayList<String> all_name) {
        ProcessInter process1 = get_process(all_name.get(0));
        test_Thread thread1 = new test_Thread(process1);
        ProcessInter process2 = get_process(all_name.get(1));
        test_Thread thread2 = new test_Thread(process2);
        ProcessInter process3 = get_process(all_name.get(2));
        test_Thread thread3 = new test_Thread(process3);
        ProcessInter process4 = get_process(all_name.get(3));
        test_Thread thread4 = new test_Thread(process4);

        try {
            process1.reset(4);
            process2.reset(4);
            process3.reset(4);
            process4.reset(4);

            process1.set_cycle(1);
            process2.set_cycle(2);
            process3.set_cycle(3);
            process4.set_cycle(4);

            new Thread(thread1).start();
            new Thread(thread2).start();
            new Thread(thread3).start();
            new Thread(thread4).start();

            Thread.sleep(15000);

            assert(process1.number_finished() == 1);
            assert(process2.number_finished() == 2);
            assert(process3.number_finished() == 3);
            assert(process4.number_finished() == 4);
            System.out.println("test_2 passed");
        } catch ( InterruptedException e) {
            e.printStackTrace();
        }
        catch ( RemoteException e) {
            e.printStackTrace();
        }
    }


    public void test_3(ArrayList<String> all_name) {
        ProcessInter process1 = get_process(all_name.get(0));
        test_Thread thread1 = new test_Thread(process1);
        ProcessInter process2 = get_process(all_name.get(1));
        test_Thread thread2 = new test_Thread(process2);
        ProcessInter process3 = get_process(all_name.get(2));
        test_Thread thread3 = new test_Thread(process3);
        ProcessInter process4 = get_process(all_name.get(3));
        test_Thread thread4 = new test_Thread(process4);

        try {
            process1.reset(4);
            process2.reset(4);
            process3.reset(4);
            process4.reset(4);

            process1.set_cycle(3);
            process2.set_cycle(3);
            process3.set_cycle(3);
            process4.set_cycle(3);


            new Thread(thread2).start();
            new Thread(thread3).start();
            new Thread(thread4).start();
            Thread.sleep(1000);
            new Thread(thread1).start();

            Thread.sleep(15000);

            assert(process1.number_finished() == 3);
            assert(process2.number_finished() == 3);
            assert(process3.number_finished() == 3);
            assert(process4.number_finished() == 3);
            System.out.println("test_3 passed");
        } catch ( InterruptedException e) {
            e.printStackTrace();
        }
        catch ( RemoteException e) {
            e.printStackTrace();
        }
    }


    public void test_4(ArrayList<String> all_name) {
        ProcessInter process1 = get_process(all_name.get(0));
        test_Thread thread1 = new test_Thread(process1);
        ProcessInter process2 = get_process(all_name.get(1));
        test_Thread thread2 = new test_Thread(process2);
        ProcessInter process3 = get_process(all_name.get(2));
        test_Thread thread3 = new test_Thread(process3);
        ProcessInter process4 = get_process(all_name.get(3));
        test_Thread thread4 = new test_Thread(process4);
        ProcessInter process5 = get_process(all_name.get(4));
        test_Thread thread5 = new test_Thread(process5);
        ProcessInter process6 = get_process(all_name.get(5));
        test_Thread thread6 = new test_Thread(process6);
        ProcessInter process7 = get_process(all_name.get(6));
        test_Thread thread7 = new test_Thread(process7);
        ProcessInter process8 = get_process(all_name.get(7));
        test_Thread thread8 = new test_Thread(process8);

        try {
            process1.reset(8);
            process2.reset(8);
            process3.reset(8);
            process4.reset(8);
            process5.reset(8);
            process6.reset(8);
            process7.reset(8);
            process8.reset(8);

            process1.set_cycle(5);
            process2.set_cycle(5);
            process3.set_cycle(5);
            process4.set_cycle(5);
            process5.set_cycle(5);
            process6.set_cycle(5);
            process7.set_cycle(5);
            process8.set_cycle(5);

            new Thread(thread1).start();
            new Thread(thread2).start();
            new Thread(thread3).start();
            new Thread(thread4).start();
            new Thread(thread5).start();
            new Thread(thread6).start();
            new Thread(thread7).start();
            new Thread(thread8).start();

            Thread.sleep(20000);

            assert(process1.number_finished() == 5);
            assert(process2.number_finished() == 5);
            assert(process3.number_finished() == 5);
            assert(process4.number_finished() == 5);
            assert(process5.number_finished() == 5);
            assert(process6.number_finished() == 5);
            assert(process7.number_finished() == 5);
            assert(process8.number_finished() == 5);
            System.out.println("test_4 passed");
        } catch ( InterruptedException e) {
            e.printStackTrace();
        }
        catch ( RemoteException e) {
            e.printStackTrace();
        }
    }

}

