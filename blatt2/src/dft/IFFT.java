package dft;

import java.util.Arrays;

/**
 * Schnelle inverse Fourier-Transformation
 *
 * @author Sebastian Rettenberger
 */
public class IFFT {
	/**
	 * Schnelle inverse Fourier-Transformation (IFFT).
	 *
	 * Die Funktion nimmt an, dass die Laenge des Arrays c immer eine
	 * Zweierpotenz ist. Es gilt also: c.length == 2^m fuer ein beliebiges m.
	 */
	public static Complex[] ifft(Complex[] c) {
		return ifft1(c,c.length);
	}

	public static Complex[] ifft1(Complex[] c , int n){
		Complex[] v = new Complex[n];
		if(n==1)
			v[0] = c[0];
		else{
			int m = n/2;

			Complex[] z1 = new Complex[m];
			Complex[] z2 = new Complex[m];
			for(int i = 0;i<c.length;i++){
				if((i%2)==0)
					z1[i/2] = c[i];
				else
					z2[i/2] = c[i];
			}

			//z2[z2.length-1] = c[n-1];
			z1 = ifft1(z1,n/2);
			z2 = ifft1(z2,n/2);

			double omega = (2.0*Math.PI /(double)n);
			Complex imgomg = Complex.fromPolar(1.0,omega);

			for(int j = 0;j<(m);j++){
				//System.out.println(j + " --- "+ m);
				v[j] = z1[j].add(imgomg.power(j).mul(z2[j]));
				v[m+j] = z1[j].sub(imgomg.power(j).mul(z2[j]));
			}

		}

		return v;
	}
}
