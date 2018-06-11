import java.util.Arrays;

/**
 * Die Klasse CubicSpline bietet eine Implementierung der kubischen Splines. Sie
 * dient uns zur effizienten Interpolation von aequidistanten Stuetzpunkten.
 *
 * @author braeckle
 */
public class CubicSpline implements InterpolationMethod {

    /**
     * linke und rechte Intervallgrenze x[0] bzw. x[n]
     */
    double a, b;

    /**
     * Anzahl an Intervallen
     */
    int n;

    /**
     * Intervallbreite
     */
    double h;

    /**
     * Stuetzwerte an den aequidistanten Stuetzstellen
     */
    double[] y;

    /**
     * zu berechnende Ableitunge an den Stuetzstellen
     */
    double yprime[];

    /**
     * {@inheritDoc} Zusaetzlich werden die Ableitungen der stueckweisen
     * Polynome an den Stuetzstellen berechnet. Als Randbedingungen setzten wir
     * die Ableitungen an den Stellen x[0] und x[n] = 0.
     */
    @Override
    public void init(double a, double b, int n, double[] y) {
        this.a = a;
        this.b = b;
        this.n = n;
        h = ((double) b - a) / (n);

        this.y = Arrays.copyOf(y, n + 1);

        /* Randbedingungen setzten */
        yprime = new double[n + 1];
        yprime[0] = 0;
        yprime[n] = 0;

        /* Ableitungen berechnen. Nur noetig, wenn n > 1 */
        if (n > 1) {
            computeDerivatives();
        }
    }

    /**
     * getDerivatives gibt die Ableitungen yprime zurueck
     */
    public double[] getDerivatives() {
        return yprime;
    }

    /**
     * Setzt die Ableitungen an den Raendern x[0] und x[n] neu auf yprime0 bzw.
     * yprimen. Anschliessend werden alle Ableitungen aktualisiert.
     */
    public void setBoundaryConditions(double yprime0, double yprimen) {
        yprime[0] = yprime0;
        yprime[n] = yprimen;
        if (n > 1) {
            computeDerivatives();
        }
    }

    /**
     * Berechnet die Ableitungen der stueckweisen kubischen Polynome an den
     * einzelnen Stuetzstellen. Dazu wird ein lineares System Ax=c mit einer
     * Tridiagonalen Matrix A und der rechten Seite c aufgebaut und geloest.
     * Anschliessend sind die berechneten Ableitungen y1' bis yn-1' in der
     * Membervariable yprime gespeichert.
     * <p>
     * Zum Zeitpunkt des Aufrufs stehen die Randbedingungen in yprime[0] und yprime[n].
     * Speziell bei den "kleinen" Faellen mit Intervallzahlen n = 2
     * oder 3 muss auf die Struktur des Gleichungssystems geachtet werden. Der
     * Fall n = 1 wird hier nicht beachtet, da dann keine weiteren Ableitungen
     * berechnet werden muessen.
     */
    public void computeDerivatives() {
        double[] c = new double[n - 1];
        c[0] = (3 / h) * (y[2] - y[0] - (h / 3) * yprime[0]);
        c[c.length - 1] = (3 / h) * (y[y.length - 1] - y[y.length - 3] - (h / 3) * yprime[n]);

        for (int i = 1; i < n - 2; i++) {
            c[i] = (3 / h) * (y[i + 2] - y[i]);
        }

        //TridiagonalMatrix a = new TridiagonalMatrix(n);

        double[] unten = new double[n-2];
        double [] mitte = new double [n-1];
        double[] oben = new double [n-2];
        for(int i = 0;i<n-2;i++){
            unten[i] = 1.0;
            oben[i] = 1.0;
            mitte[i] = 4.0;
        }
        mitte[n-2] = 4.0;

        TridiagonalMatrix a = new TridiagonalMatrix(unten,mitte,oben);

        double[] tmp = a.solveLinearSystem(c);
        for(int i = 0;i<tmp.length;i++){
            yprime[i+1] = tmp[i];
        }
    }

    /**
     * {@inheritDoc} Liegt z ausserhalb der Stuetzgrenzen, werden die
     * aeussersten Werte y[0] bzw. y[n] zurueckgegeben. Liegt z zwischen den
     * Stuetzstellen x_i und x_i+1, wird z in das Intervall [0,1] transformiert
     * und das entsprechende kubische Hermite-Polynom ausgewertet.
     */
    @Override
    public double evaluate(double z) {
        /* TODO: diese Methode ist zu implementieren */
        if (z >= b)
            return y[n];//IDK return something
        if (z <= a)
            return y[0];

        int i = 0;
        while (a + (i * h) < z) {
            i++;
        }
        i--;

        double t = (z - (a + (i * h))) / h;

        double h0 = 1 - (3 * Math.pow(t, 2)) + (2 * Math.pow(t, 3));
        double h1 = (3 * Math.pow(t, 2)) - (2 * Math.pow(t, 3));
        double h2 = t - 2 * Math.pow(t, 2) + Math.pow(t, 3);
        double h3 = -1 * Math.pow(t, 2) + Math.pow(t, 3);

        return (y[i] * h0) + (y[i + 1] * h1) + (h * yprime[i] * h2) + (h * yprime[i + 1] * h3);
    }
}
