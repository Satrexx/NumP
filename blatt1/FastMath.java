import java.util.ArrayList;

/**
 * @author Christoph Riesinger (riesinge@in.tum.de)
 * @author Jürgen Bräckle (braeckle@in.tum.de)
 * @author Sebastian Rettenberger (rettenbs@in.tum.de)
 * @version 1.2
 * <p>
 * This class contains methods for rapidly calculating basic
 * mathematical operations.
 * @since Oktober 22, 2014
 */
public class FastMath {


/*
    public static void main(String[] args) {
        int nums = 256;

        Gleitpunktzahl.setSizeMantisse(8);
        Gleitpunktzahl.setSizeExponent(4);

        double[] res = new double[nums];

        double[] realRes = new double[1000];

        Gleitpunktzahl g;


        for (int i = 500; i < 1500; i++) {
            setMagic(i);

            for (int t = 1; t < nums; t++) {
                g = new Gleitpunktzahl(t);//Math.pow(t, 2));
                res[t-1] = absInvSqrtErr(g);
            }
            int min = 0;
            double avg = 0;

            for (int i1 = 0; i1 < nums; i1++) {
                    avg = avg + res[i1];

            }
            avg = avg / nums;
            realRes[i-500] = avg;
            System.out.println(""+(i) + " - " + avg);
        }
        double end = Double.MAX_VALUE;
        int endi = 0;
        for(int e = 0; e<1000;e++){
            if(end> realRes[e]){
                end = realRes[e];
                endi = e;
            }
        }
        System.out.println("Beste Magic Number lautet: " + (endi + 500));

    }
    */

    /**
     * The "magic" constant which is used in the fast inverse square root
     * algorithm.
     * <p>
     * The given initial value is just a test value for 8 mantissa bits and 4
     * exponent bits, and has to be optimized by the students.
     * <p>
     * In literature, several of those constants for floats or doubles can be
     * found. There's no optimal constant for all cases.
     */
    private static int MAGIC_NUMBER = 1332;

    //UM AUF DIE MAGIC NUMBER ZU KOMMEN----------------------!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!----------------------------------------------------------------------------------
    //
    //Um auf die Magic Number zu kommen haben wir mittels Brute Force verfahren für die Magic Number alle Zahlen zwischen 500 und 1500 eingesetzt und
    //für jede Magic Number den Durchschnittlichen Fehler für Zahlen 1-256 berechnet.
    //Letztlich haben wir einfach die Magic Number gewählt für die der Durchschnittliche Fehler am niedrigsten war.
    //
    //UM AUF DIE MAGIC NUMBER ZU KOMMEN----------------------!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!----------------------------------------------------------------------------------

    /**
     * belegt die MAGIC_NUMBER mit dem Wert magic
     */
    public static void setMagic(int magic) {
        FastMath.MAGIC_NUMBER = magic;
    }

    /**
     * This method contains the code for the fast inverse square root algorithm
     * which can e.g. be found in "Fast Inverse Square Root" from Lomont, Chris
     * (February, 2003).
     * <p>
     * It approximately calculates the value 1 / sqrt(x).
     * <p>
     * No Newton steps to improve the result has to be implemented in this
     * exercise.
     *
     * @param x Input value of which the inverse square root should be
     *          computed.
     * @return Approximation for 1 / sqrt(x).
     */
    public static Gleitpunktzahl invSqrt(Gleitpunktzahl x) {

        /* TODO: hier den "fast inverse square root" Algorithmus implementieren */

        int zahl = gleitpunktzahlToIEEE(x);
        zahl = zahl >> 1;
        zahl = MAGIC_NUMBER - zahl;
        Gleitpunktzahl newZahl = iEEEToGleitpunktzahl(zahl);

        return newZahl;
    }


    /**
     * Calculates the absolute error between the result of the fast inverse
     * square root algorithm and the "exact" IEEE-conform result.
     *
     * @param x Position where the absolute error should be determined.
     * @return Absolute error between invSqrt(x) and 1 / Math.sqrt(x).
     */
    public static double absInvSqrtErr(Gleitpunktzahl x) {
        double exact = 1 / Math.sqrt(x.toDouble());
        double approx = invSqrt(x).toDouble();
        double absErr = Math.abs(exact - approx);

        return absErr;
    }

    /**
     * Calculates the relative error between the result of the fast inverse
     * square root algorithm and the "exact" IEEE-conform result.
     *
     * @param x Position where the relative error should be determined.
     * @return Relative error between invSqrt(x) and 1 / Math.sqrt(x).
     */
    public static double relInvSqrtErr(Gleitpunktzahl x) {
        double absErr = absInvSqrtErr(x);
        double relErr = Math.abs(absErr * Math.sqrt(x.toDouble()));

        return relErr;
    }

    /**
     * Uebersetzt die Gleitpunktzahl in eine Bitfolge (int) aehnlich dem IEEE
     * Standard, d.h. in die Form [Vorzeichen, Exponent, Mantisse], wobei die
     * führende 1 der Mantisse nicht gespeichert wird. Dieser Wechsel ist noetig
     * für ein Funktionieren des Fast Inverse Sqrt Algorithmus
     */
    public static int gleitpunktzahlToIEEE(Gleitpunktzahl x) {
        int sizeExponent = Gleitpunktzahl.getSizeExponent();
        int sizeMantisse = Gleitpunktzahl.getSizeMantisse();

        int result;

        /* mantisse ohne fuehrende 1 einfuegen */
        int mask = (int) Math.pow(2, sizeMantisse - 1) - 1;
        result = (x.mantisse & mask);

        /* exponent vorne anhaengen */
        result |= (x.exponent << sizeMantisse - 1);

        /* vorzeichen setzen */
        if (x.vorzeichen)
            result |= (1 << sizeExponent + sizeMantisse - 1);

        return result;
    }

    /**
     * Liefert aus einer Bitfolge (int) in IEEE Darstellung, d.h. [Vorzeichen,
     * Exponent, Mantisse] mit Mantisse ohne führende Null, die entsprechende
     * Gleitpunktdarstellung
     */
    public static Gleitpunktzahl iEEEToGleitpunktzahl(int b) {
        Gleitpunktzahl g = new Gleitpunktzahl();
        int sizeExponent = Gleitpunktzahl.getSizeExponent();
        int sizeMantisse = Gleitpunktzahl.getSizeMantisse();

        /* fuehrende 1 fuer mantisse eintragen */
        g.mantisse = 1;
        g.mantisse <<= sizeMantisse - 1;
        /* mantisse ohne fuehrende 1 einfuegen */
        int mask = (int) Math.pow(2, sizeMantisse - 1) - 1;
        g.mantisse |= (b & mask);

        /* exponent eintrage */
        b >>= sizeMantisse - 1;
        mask = (int) Math.pow(2, sizeExponent) - 1;
        g.exponent = (b & mask);

        /* vorzeichen setzen */
        b >>= sizeExponent;
        g.vorzeichen = (b & 1) > 0;

        return g;
    }

}
