package threadslernen.aufgabe2;

import java.util.Date;

public class SumComputation {

    final static int SIZE = 100000;
    private int sum = 0;
    private final int[] sa1 = new int[SIZE];
    private final int[] sa2 = new int[SIZE];

    public static void main(String[] args) {
        System.out.println("Diese Loesung wurde erstellt von Kai Bechmann.");
        
        SumComputation prog = new SumComputation();
        prog.execute(args);
    }

    
    synchronized void addToSum(int i) {
        // kritischer Abschnitt
        // hier wird auf gemeinsame Ressource aus beiden WorkerThreads zugegriffen
        // hier ist Synchronisation notwendig
        sum = sum + i;
    }
    
    
    /*
    // hier fehlt Synchronisation 
    void addToSum(int i) {
        // kritischer Abschnitt
        // hier wird auf gemeinsame Ressource aus beiden WorkerThreads zugegriffen
        sum = sum + i;
    }
    */

    private void execute(String[] args) {
        System.out.println("Starte Programm SumComputation.");
        long timeStart = new Date().getTime();
        
        // Initialisiere bei sa1 alle Elemente mit 1, bei sa2 mit 2
        for (int i = 0; i < SIZE; i++) {
            sa1[i] = 1;
            sa2[i] = 2;
        }

        Thread worker1 = new WorkerThread(1, sa1); // Erzeuge Worker-Thread 1
        Thread worker2 = new WorkerThread(2, sa2); // Erzeuge worker-Thread 2
        worker1.start();// Starte Worker-Thread 1
        worker2.start();// Starte Worker-thread 2
             
        try {
            // Warte auf das Ende von Worker-Thread 1
            worker1.join();
            // Warte auf das Ende von Worker-Thread 2
            worker2.join();
        } catch (InterruptedException e) {
        }
        long timeEnd = new Date().getTime();
        
        System.out.println("Programm: sum = " + sum);
        System.out.println("Beende Programm SumComputation. Ausfuehrungsdauer: " + (timeEnd - timeStart) + "ms");
    } 

// Private Klasse Worker für die Worker-Threads
    private class WorkerThread extends Thread {

        private final int id;
        private final int[] arr;

        public WorkerThread(int id, int[] arr) {
            super();
            this.id = id;
            this.arr = arr;
        }

        @Override
        public void run() {
            System.out.println("Starte worker " + id + " der Klasse "
                    + getClass().getName());

            // Thread soll 1 Sekunde schlafen
            try {
                WorkerThread.sleep(1000, 0);
            } catch (InterruptedException e) {
            }
            // Thread addiert die Zahlen seines Arrays einzeln zur Gesamtsumme mit
            // Hilfe von addToSum()
            for (int i = 0; i < arr.length; i++) {
                addToSum(arr[i]);
            }

            // vor dem Ende des Worker-Threads die Ergebnisausgabe
            System.out.println("Worker " + id + ": sum = " + sum);
            System.out.println("Beende worker " + id + " der Klasse "
                    + getClass().getName());
        }
    }
}
