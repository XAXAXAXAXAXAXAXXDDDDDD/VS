package threadslernen.aufgabe3;

import java.util.Date;
import java.util.Random;

/**
 *
 * @author kai bechmann
 */
public class ControlThreads {

    public static void main(String[] args) {
        System.out.println("Diese Loesung wurde erstellt von Kai Bechmann.");
        
        if (args.length < 2) {
            System.err.println("Nicht genug Aufrufparameter! Es werden 2 erwartet.");
            return;
        }
        int cycleTimeT1;
        int cycleTimeT2;
        try {
            cycleTimeT1 = Integer.parseInt(args[0]);
            cycleTimeT2 = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println(e.toString());
            return;
        }

        new ControlThreads().execute(cycleTimeT1, cycleTimeT2);
    }

    private void execute(int cycleTimeThread1, int cycleTimeThread2) {
        long startTime = System.currentTimeMillis();

        Thread workerThread1 = new WorkerThread("Thread 1", cycleTimeThread1);

        WorkerRunnable runnable = new WorkerRunnable("Thread 2", cycleTimeThread2);
        Thread workerThread2 = new Thread(runnable);

        workerThread1.start();
        workerThread2.start();
        try {
            Thread.sleep(3000);
            workerThread1.interrupt();
            Thread.sleep(1000);
            workerThread2.interrupt();

            workerThread1.join();
            workerThread2.join();
        } catch (Exception e) {
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Programm beendet! Ausfuehrungsdauer war: " + (endTime - startTime) + "ms");
    }

    private boolean isPrimeNumber(int num) {
        if (num < 2) {
            return false;
        }

        for (int i = 2; i < num; i++) {
            if (num % i == 0) {
                return false;
            }
        }

        return true;
    }

    public class WorkerThread extends Thread {

        private final String threadName;
        private final int waitTime;

        public WorkerThread(String name, int waitT) {
            threadName = name;
            waitTime = waitT;
        }

        @Override
        public void run() {
            System.out.println(this.threadName + " wurde gestartet.");

            Random rand;
            int infiniteLoopCounter = 0;
            while (true) {
                infiniteLoopCounter++;
                rand = new Random(new Date().getTime());
                boolean primeNumberFound;
                int possiblePrimeNum;
                do {
                    possiblePrimeNum = rand.nextInt();
                    primeNumberFound = isPrimeNumber(possiblePrimeNum);
                } while (primeNumberFound == false);

                System.out.println("Primzahl gefunden in " + this.threadName + ": " + possiblePrimeNum);
                System.out.println("Schleifenzaehler " + infiniteLoopCounter + " in " + this.threadName);

                if (this.isInterrupted()) {
                    System.out.println("Thread " + threadName + " wurde waehrend Schleifendurchlauf beendet.");
                    break;
                }

                try {
                    this.sleep(this.waitTime);
                } catch (InterruptedException e) {
                    System.out.println("Thread " + threadName + " wurde waehrend Sleep beendet.");
                    break;
                }
            }
            System.out.println("Ende von " + this.threadName);
        }
    }

    public class WorkerRunnable implements Runnable {

        private final String threadName;
        private final int waitTime;

        public WorkerRunnable(String threadName, int waitTime) {
            this.threadName = threadName;
            this.waitTime = waitTime;
        }

        @Override
        public void run() {
            System.out.println(this.threadName + " wurde gestartet.");

            Random rand;
            int infiniteLoopCounter = 0;
            while (true) {
                rand = new Random(new Date().getTime());
                boolean primeNumberFound;
                int possiblePrimeNum;
                infiniteLoopCounter++;
                do {
                    possiblePrimeNum = rand.nextInt();
                    primeNumberFound = isPrimeNumber(possiblePrimeNum);
                } while (primeNumberFound == false);

                System.out.println("Found Prime Number in " + this.threadName + ": " + possiblePrimeNum);
                System.out.println("Loop Count " + infiniteLoopCounter + " in " + this.threadName);

                if (Thread.interrupted()) {
                    System.out.println("Thread " + threadName + " wurde waehrend lauffaehig beendet.");
                    break;
                }

                try {
                    Thread.sleep(this.waitTime);
                } catch (InterruptedException e) {
                    System.out.println(this.threadName + " wurde waehrend sleep beendet.");
                    break;
                }
            }

            System.out.println("Ende von " + this.threadName);
        }

    }
}
