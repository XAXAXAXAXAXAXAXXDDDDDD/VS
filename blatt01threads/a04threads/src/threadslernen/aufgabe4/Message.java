package threadslernen.aufgabe4;

/**
 *
 * @author kai bechmann
 */
public class Message {

    private static int counter;

    public static synchronized void IncCounter() {
        counter++;
    }

    public static synchronized int readCounter() {
        return counter;
    }

    private int id;
    private String sender;
    private int value;

    public Message(int id, String sender, int value) {
        this.id = id;
        this.sender = sender;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
