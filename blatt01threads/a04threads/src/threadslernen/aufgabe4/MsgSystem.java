package threadslernen.aufgabe4;

import java.util.Scanner;

/**
 *
 * @author kai bechmann
 */
public class MsgSystem {

    public static void main(String[] args) {
        System.out.println("Diese Loesung wurde erstellt von Kai Bechmann.");

        if (args.length == 0) {
            System.err.println("Puffergroesse als Argument erwartet!");
            return;
        }

        int bufferSize = Integer.parseInt(args[0]);
        MsgBuffer buffer = new MsgBuffer(bufferSize);

        // single thread
        int numberOfSenderThreads = 1;
        int numberOfReceiverThreads = 1;

        // multiple threads
        //int numberOfSenderThreads = 2;
        //int numberOfReceiverThreads = 2;        
        Thread[] senderList = new Thread[numberOfSenderThreads];
        for (int i = 1; i <= numberOfSenderThreads; i++) {
            Thread sender = new Thread(new Sender(buffer, "Sender " + i));
            senderList[i - 1] = sender;
            sender.start();
        }

        Thread[] receiverList = new Thread[numberOfReceiverThreads];
        for (int i = 1; i <= numberOfReceiverThreads; i++) {
            Thread receiver = new Thread(new Receiver(buffer, "Receiver " + i));
            receiverList[i - 1] = receiver;
            receiver.start();
        }

        try (Scanner in = new Scanner(System.in)) {
            System.out.println("Um Programm zu beenden bitte Eingabe taetigen: ");
            in.next();

            buffer.stopwork();

            for (Thread t : senderList) {
                t.interrupt();
            }
            for (Thread t : senderList) {
                t.join();
            }

            for (Thread t : receiverList) {
                t.interrupt();
            }
            for (Thread t : receiverList) {
                t.join();
            }
        } catch (InterruptedException ex) {
            System.err.println(ex.toString());
        }
    }

}
