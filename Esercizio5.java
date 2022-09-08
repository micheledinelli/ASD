import java.io.*;
import java.util.*;

/**
 * @author Michele Dinelli
 * 		   Matricola: 0000934209
 * 		   email: michele.dinelli5@studio.unibo.it
 *
 * Risoluzione dell'esercizio 5: Resto non erogabile
 */

public class Esercizio5 {
	
	private int n; //dimensione dell'array di monete
	private int[] monete; //array delle monete
	private int S; //somma delle monete presenti nell'array 
	
	/**
	 * Il costruttore della classe legge il file in input e richiama il 
	 * metodo minRestoNonErog che calcola il minimo resto non erogabile date 
	 * le monete in input, infine stampa il risultato.
	 *
	 * @param inputf
	 */
	public Esercizio5( String inputf ) 
	{
		readInput( inputf );
		int minimoNonErogabile = minRestoNonErog( monete, S );
		System.out.println( minimoNonErogabile );
	}
	
	/**
	 * Il metodo legge il file in input e riempe l'array
	 * di monete con i valori letti dal file, inoltre calcola la
	 * somma S delle monete presenti nell'array. 
	 * 
	 * @param inputf
	 */
	public void readInput( String inputf )
	{
		Scanner scan;
		try {
			scan = new Scanner(new FileReader(inputf));
			n = scan.nextInt();			
			monete = new int[n];
			
			for(int i=0; i < n; i++) {
				monete[i] = scan.nextInt();
				S += monete[i];
			}
			scan.close();
		}catch(Exception e) {
			System.err.println("Error. Please check the input file. Stack trace below.\n");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	/**
	 * Il metodo minRestoNonErog utilizza programmazione dinamica
	 * per risolvere il problema  del minimo resto non erogabile.
	 * Utilizza una matrice di dimensione n x S+1, dove n è il numero di monete disponibili
	 * mentre S + 1 è la somma delle monete dipsonibili + 1, ossia il limite superiore al minimo
	 * resto non erogabile. 
	 * Esistono due casi basi: (1) il resto da erogare è zero
	 * 						   (2) abbiamo solo una moneta a disposizione
	 * Il caso generale utilizza i casi base per riempire in definitva la matrice.
	 * In generale:
	 * Se M[i][j] = 1 il resto j è erogabile, non erogabile altrimenti.
	 * M[i][j] = 1 se: M[i-1][j] = 1 oppure monete[i] = j oppure se M[i-1][j - monete[i]] = 1
	 * M[i][j] = 0 se M[i-1][j] = 0 e monete[i] > j oppure se M[i-1][j - monete[i]] = 0
	 * 
	 * Il risultato si trova nell'ultima riga e corrisponde 
	 * al primo indice j tale per cui valga M[n-1][j] = 0 
	 * oppure il risultato è uguale ad S + 1 se il minimo non viene mai aggiornato
	 * 
	 * @param monete array contenente i valori delle monete 
	 * @param S somma dei valori contenuti nell'array
	 * @return minNonErog minimo resto non erogabile
	 */
	public int minRestoNonErog( int[] monete, int S )
	{
		int minNonErog = S + 1;
		//matrice delle soluzioni con n righe come il numero di monete
		//ed S + 1 colonne
		int[][] M = new int[n][S + 1];
		
		//caso base (1): j = 0
		//se dobbiamo erogare resto = 0
		for(int i = 0; i < n; i++) {
			M[i][0] = 0;
		}
		//caso base (2): i = 0 
		//se abbiamo a disposizione una sola moneta
		for(int j = 0; j < M[0].length; j++) {
			if(monete[0] == j) {
				M[0][j] = 1;
			} else {
				M[0][j] = 0;
			}
		}
		//caso generale
		for(int i = 1; i < n; i++) {
			for(int j = 1; j < M[0].length; j++) {
				if(monete[i] == j) {
					M[i][j] = 1;
				} else if(monete[i] != j && M[i-1][j] == 1) {
					M[i][j] = M[i-1][j];
				} else if(M[i-1][j] == 0) {
					if(monete[i] > j) {
						M[i][j] = 0;
					} else if(M[i-1][j - monete[i]] == 1) {
						M[i][j] = 1;
					} else {
						M[i][j] = 0;
					}
				}				
			}
		}
		//si scorre l'ultima riga per identificare il primo indice 
		//j per cui valga M[n-1][j] = 0
		for(int j = 1; j < M[0].length; j++) {
			if(M[n-1][j] == 0) {
				minNonErog = j;
				break;
			}
		}
		
		return minNonErog;
	}
	
	/**
	 * Il metodo main istanzia un oggetto della classe 
	 * Esercizio5 che produce il risultato.
	 * 
	 * @param args file input
	 */
	public static void main( String[] args ) 
	{
		if(args.length != 1) {
			System.out.println("Input file not found.");
			System.exit(0);
		}
		
		Esercizio5 e = new Esercizio5(args[0]);
	}
}
