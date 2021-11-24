public class Delay_receive implements  Runnable{
    Message message;
    Process process;
    Delay_receive(Message t_message, Process t_process){
        message = t_message;
        process = t_process;
    }
    public void run(){
        System.out.printf("PostPone process%d receive of message%d for %d millis\n",process.index,message.index,message.delay);
        try{
            Thread.sleep(message.delay);
            message.set_delay(0);
            process.receive(message);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }

        }
    }

