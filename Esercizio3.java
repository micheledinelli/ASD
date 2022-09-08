import java.io.*;
import java.util.*;

/**
 * @author Michele Dinelli
 * 		   Matricola: 0000934209
 * 		   E-mail: michele.dinelli5@studio.unibo.it
 * 
 *         Risoluzione dell'esercizio 3: Dividere il numero.
 */

public class Esercizio3 {
	
	private int num_input; 
	private int[] valori_input;
	
	/**
	 * Il costruttore della classe legge il file in input.
	 * Richiama il metodo divNum per ogni elemento
	 * letto in input e stampa a terminale il risultato.  
	 * 
	 * @param inputf
	 */
	public Esercizio3( String inputf )
	{
		readInput(inputf);
		for(int i = 0; i < valori_input.length; i++) {
			System.out.println( divNum( valori_input[i] ) );
		}
	}
	
	/**
	 * Il metodo legge il file in input e costruisce l'array
	 * dei valori su cui operare per produrre l'output.
	 * 
	 * @param inputf
	 */
	public void readInput( String inputf )
	{
		Scanner scan;
		try {
			scan = new Scanner(new FileReader(inputf));
			num_input = scan.nextInt();			
			valori_input = new int[num_input];
			
			for(int i=0; i < num_input; i++) {
				valori_input[i] = scan.nextInt();
			}
			scan.close();
		}catch(Exception e) {
			System.err.println("Error. Please check the input file. Stack trace below.\n");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	/**
	 * Il metodo divNum separa un numero n > 0
	 * ed ottiene m > 0 numeri, ritorna il prodotto 
	 * massimo ottenibile dagli m numeri (anche ripetuti).
	 * Nota: si opera con long in quanto per input grandi
	 * 		 si verifica integer overflow.
	
	 * @param n
	 */
	public long divNum( int n ) {
		//vettore delle soluzioni
		long[] S = new long[n+1];
		
		//casi base
		S[0] = 0L;
		S[1] = 1L;
		
		//il pattern identificato inizia per n > 6
		//perciò reputo n = 2,3,4 casi pseudo-base e n = 5, 6
		//casi particolari
		for(int i = 2; i < n+1; i++) {
			if(i == 2) {
				S[i] = 1L;
			} else if(i == 3) {
				S[i] = 2L;
			} else if(i == 4) {
				S[i] = 4L;
			//nel caso di i = 5,6 bisogna prestare attenzione a non
			//utilizzare un caso pseudo-base che produca il risulato errato 
			} else if(i == 5 || i == 6) {
				S[i] = (i - 3) * 3L;
			}
			//caso generale
			else if (i > 6) {
				S[i] = S[i-3] * 3L;
			}
		}
		return S[n];
	}
	 	
	/**
	 * Il metodo main istanzia un oggetto della classe
	 * Esercizio3 il quale produrrà l'output. 
	 */
	public static void main( String[] args ) 
	{
		if(args.length != 1) {
			System.out.println("Input file not found.");
			System.exit(0);
		}
		
		Esercizio3 e = new Esercizio3(args[0]);	
	}
}
