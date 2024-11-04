package threadslernen.aufgabe1;

/**
 *
 * @author kai bechmann
 */
public class HelloWorld {

    public static void main(String[] args) {
        System.out.println("Diese Loesung wurde erstellt von Kai Bechmann.");
        
        System.out.println("Hello World!");
        for (int i = 0; i < args.length; i++) {
            System.out.println("Konsolenargument " + i + ": " + args[i]);
        }
    }

}
