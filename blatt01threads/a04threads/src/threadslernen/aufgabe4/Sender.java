package threadslernen.aufgabe4;

import java.util.Random;

/**
 *
 * @author kai bechmann
 */
public class Sender implements Runnable {

    private final MsgBuffer buffer;
    private final String name;

    public Sender(MsgBuffer buff, String name) {
        this.buffer = buff;
        this.name = name;
    }

    @Override
    public void run() {
        Random rnd = new Random();

        while (true) {
            if (Thread.interrupted()) {
                System.out.println("Beende Thread " + name + " nach Ausfuehrung des try-Blocks.");
                return;
            }

            try {
                int i = rnd.nextInt(30);
                if (i > 0) {
                    Thread.sleep(i * i);
                }

                Message.IncCounter();
                Message msg = new Message(Message.readCounter(), name, i);
                System.out.println(name + ": versuche Nachricht mit Inhalt=" + msg.getValue() + " zu platzieren.");

                this.buffer.put(msg);
                System.out.println(name + ": Nachricht mit Inhalt=" + msg.getValue() + " ist platziert worden.");
            } catch (InterruptedException ex) {
                System.out.println("Beende Thread " + name + " waehrend sleep.");
                return;
            } catch (MsgBuffer.FinishedException ex) {
                System.out.println("Beende Thread " + name + " weil Buffer geschlossen.");
                return;
            }
        }
    }
}
