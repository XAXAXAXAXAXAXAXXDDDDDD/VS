package threadslernen.aufgabe4;

/**
 *
 * @author kai bechmann
 */
public class MsgBuffer {

    class FinishedException extends Exception {

        public FinishedException() {
        }

        public FinishedException(String text) {
            super(text);
        }
    }

    private Message[] messages; // Puffer
    private int size; // Größe des Puffers
    private int next_free = 0, next_full = 0; // Ringpufferverwaltung
    private int count = 0; // Anzahl Zeichen im Puffer
    private boolean finished;

    public MsgBuffer(int s) {
        size = s;
        messages = new Message[size];
        finished = false; // neuer Puffer startet Arbeit
    }

    public synchronized void stopwork() {
        finished = true;
        notifyAll(); 
    }

    public synchronized void put(Message m) throws FinishedException {
        while (count == size) {
            try {
                wait();
            } catch (InterruptedException e) {
                /* nothing */ }
        }
        if (finished == true) {
            throw new FinishedException("Ende");
        }
        this.messages[next_free] = m;
        // Ringpuffer
        next_free = (next_free + 1) % size;
        count++;
        notifyAll();
    }

    public synchronized Message get() throws FinishedException {
        while (count == 0) {    
            if (finished == true) {
                throw new FinishedException("Ende");
            }
            try {
                wait();
            } catch (InterruptedException e) {
                /* nothing */ }
        }
        Message result = this.messages[next_full];
        // Ringpuffer
        next_full = (next_full + 1) % size;
        count--;
        notifyAll();
        return result;
    }
}
