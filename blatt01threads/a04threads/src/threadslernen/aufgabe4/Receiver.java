package threadslernen.aufgabe4;

import java.util.Date;

/**
 *
 * @author kai bechmann
 */
public class Receiver implements Runnable {

    private final MsgBuffer buffer;
    private final String name;
    
    public Receiver(MsgBuffer buff, String name) {
        this.buffer = buff;
        this.name = name;
    }

    @Override
    public void run() {
        Date date = new Date();

        while (true) {          
            try {
                Message msg = this.buffer.get();
                System.out.println(name + ": Nachricht mit Inhalt=" + msg.getValue() + " entnommen.");
                long t = date.getTime();
                long d = t % 1000;

                if (d > 0) {
                    Thread.sleep(d);
                }

            } catch (MsgBuffer.FinishedException ex) {
                System.out.println("Beende Thread " + name + " weil Buffer geschlossen.");
                return;
            } catch (InterruptedException ex) {
                System.out.println("Beende Thread " + name + " waehrend sleep.");
                return;
            }
        }
    }

}
