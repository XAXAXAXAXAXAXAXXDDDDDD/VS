/*
 * wss, 20190409
 * 
 * Hilfsklasse für die Simulation
 */
package socketslernen.general;

import socketslernen.general.TextumwandlungInterface;

public class HelperSimulation {

    final private static int FAK = 1000;

    public int computeOperationTime(String s) {
        int len = s.length();
        int dur = 0;
        // if (len < 3) return 0;
        // Finde ein Anfangswort maximal der Länge 3, das als Zahl interpretiert werden kann.
        for (int i = 0; i < len && i < 3; i++) {
            Character char1 = s.charAt(0);
            if (!Character.isDigit(char1)) {
                return dur * FAK;
            }
            // char1 ist eine Ziffer
            int digit = Integer.parseInt(char1.toString());
            // schneide erstes Zeichen ab
            s = s.substring(1);
            dur = dur * 10 + digit;
        } // for
        return dur * FAK;
    }

    public String stringWithoutOperationTime(String s) {
        int len = s.length();
        // Finde ein Anfangswort maximal der Länge 3, das als Zahl interpretiert werden kann, und schneidet dies ab.
        for (int i = 0; i < len && i < 3; i++) {
            Character char1 = s.charAt(0);
            if (!Character.isDigit(char1)) {
                return s;
            }
            // char1 ist eine Ziffer
            // schneide erstes Zeichen ab
            s = s.substring(1);
        } // for
        return s;
    }

    public boolean checkShutdown(int fnb, String testvalue) {
        boolean result = false;
        if (fnb == TextumwandlungInterface.SHUTDOWN_FCT_NB.ordinal()) {
            return true;
        }
//    testvalue = testvalue.toLowerCase();
//	  if (testvalue.startsWith(SHUTDOWN_TEXT)) {
// 		  result = true;
// 	  }
        return result;
    }

    public boolean checkEndOfClientConnection(int fnb, String testvalue) {
        boolean result = false;
        if (fnb == TextumwandlungInterface.CONNECTION_CLOSE_FCT_NB.ordinal()) {
            return true;
        }
//    testvalue = testvalue.toLowerCase();
//	  if (testvalue.startsWith(CLIENT_FIN_VALUE)) {
// 		  result = true;
// 	  }
        return result;
    }

} // class
