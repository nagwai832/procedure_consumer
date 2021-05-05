import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

class MessageQueue {

    Queue<String> queue = new LinkedList<String>();
    private int maxSize = 10;

    synchronized void removeElement() throws InterruptedException {
        while (true) {
            while (queue.isEmpty()) {
                wait(); // when the queue is empty, the consumer must stop the
                // consumption.
            }
            queue.remove();
            System.out.print("After Consumption Queue::::::[");
            Iterator<String> it = queue.iterator();
            while (it.hasNext())
                System.out.print(it.next() + ",");
                System.out.print("]");
                System.out.println();
                notifyAll();
        }
    }

    synchronized void addElement(String message) throws InterruptedException {
        while (queue.size() == maxSize) {
            wait(); // when the queue is full, the producer must stop the
            // production.
        }
        queue.add(message);
        System.out.print("After Insert Queue::::::[");
        Iterator<String> it = queue.iterator();
        while (it.hasNext())
            System.out.print(it.next() + ",");
            System.out.print("]");
            System.out.println();
            notify();
    }
}

class Producer implements Runnable {
    MessageQueue messageQueue;
    String emailMessageContent;

    public Producer(String message, MessageQueue messageQueue) {
        emailMessageContent = message;
        this.messageQueue = messageQueue;
    }

    public void run() {
        try {
            messageQueue.addElement(emailMessageContent);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }
}

class Consumer implements Runnable {

    MessageQueue messageQueue;

    public Consumer(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    public void run() {
        try {
            messageQueue.removeElement();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
public class procedure_consumer {
    public static void main(String[] args) throws InterruptedException {
        MessageQueue messageQueue = new MessageQueue();
        new Thread(new Consumer(messageQueue)).start();
        for (int i = 0; i < 10; ++i) {
            new Thread(new Producer("" + i, messageQueue)).start();
        }
    }
}
