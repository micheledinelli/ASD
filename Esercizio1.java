import java.io.*;
import java.util.*;

/**
 * @author Michele Dinelli
 * 		   Matricola: 0000934209
 * 		   E-mail: michele.dinelli5@studio.unibo.it
 * 
 *         Risoluzione dell'Esercizio 1: Palindroma più lunga.
 */ 

public class Esercizio1 {
	
	private int num_input; //numero degli array in input
	private List<int[]> arrays_input; //lista degli array

	/**
	 * Il costruttore legge l'input e richiama per ogni array letto 
	 * il metodo palindromaPiuLunga, stampando a terminale l'output.
	 * 
	 * @param inputf file in input
	 */
	public Esercizio1( String inputf ) {
		readInput(inputf);
		
		for(int[] a : arrays_input) {
			System.out.println( palindromaPiuLunga(a) );
		}
	}

	/**
	 * Il metodo readInput legge l'input popolando la lista degli
	 * array.
	 * 
	 * @param inputf file da leggere in input
	 */
	public void readInput( String inputf )
	{
		Locale.setDefault(Locale.US);
		Scanner scan;
		arrays_input = new LinkedList<int[]>();

		try {
		    scan = new Scanner(new FileReader(inputf));
			String intero = scan.nextLine();
			num_input = Integer.parseInt(intero);
				
			for(int i = 0; i < num_input; i++) {
				String line =  scan.nextLine();
				String[] str = line.split(" ");
				int[] array = new int[str.length];
				
				for(int j = 0; j < str.length; j++) {
					array[j] = Integer.parseInt(str[j]);
				}
				arrays_input.add( i, array );
			}
			scan.close();
		}catch(Exception e) {
			System.err.println("Error. Please check the input file. Stack trace below.\n");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	/**
	 * Il metodo palindromaPiuLunga cotruisce una matrice S di dimensione n x n,
	 * dove n è la dimensione dell'array. 
	 * Viene utilizzata solo la parte triangolare superiore della matrice perchè
	 * le combinazioni degli indici della matrice triangolare superiore sono le
	 * combinazioni di sottovettori possibili per l'array in input.
	 * 
	 * Se l'elemento in posizione S[i,j] è uguale ad 1 significa che il sottovettore
	 * da i a j è palindromo, se uguale a 0 il sottovettore non è palindromo.
	 * Viene posto 1 in tutti gli elementi sulla diagonale principale, in seguito si valuta 
	 * la diagonale che parte da [0, 1] e termina in [n-1, n], ovvero la diagonale
	 * che considera i sottovettori composti da elementi adiacenti nell'array. Viene
	 * posto 1 o 0 a seconda dell'uguaglianza o meno degli elementi considerati.
	 * 
	 * La restante parte della matrice viene riempita scorrendo diagonalamente,
	 * valutando se gli elementi agli estremi del sottovettore con indici i e j sono 
	 * uguali e se la porzione di array compresa tra essi è palindroma (ovvero se è presente
	 * un 1 in posizione S[ i+1, j-1] ).
	 * Il risultato è ottenuto conservando una variabile massimo che si aggiorna
	 * come il numero di elementi nel sottovettore [i...j] se le condizioni sopra sono verificate.
	 * 
	 * @param array 
	 */
	public int palindromaPiuLunga( int[] array ) {
		
		//matrice delle soluzioni 
		int n = array.length;
		int[][] S = new int[n][n];
		
		//viene posto 1 sulla diagonale 
		for(int i = 0; i < n; i++) {
			S[i][i] = 1;
		}
		
		//viene posto 0 se gli elementi adiacenti nell'array 
		//sono diversi ed 1 se sono uguali
		int max = 1;
		for(int i = 0, j = 1; i < n && j < n; i++, j++) {
			if(array[i] == array[j]) {
				S[i][j] = 1;
				//eventuale aggiornamento del massimo 
				if( max < j - i + 1 ) {
					max = j - i + 1;
				}
 			} else {
 				S[i][j] = 0;
 			}
		}
		
		//si scorre la matrice diagonalmente, ad ogni iterazione
		//del ciclo for più esterno aumenta di 1 la porzione di
		//array che si considera ossia la differenza tra gli indici i e j
		for(int diff_indici = 2; diff_indici < n; diff_indici ++) {
			for(int i = 0; i < n - diff_indici; i++) {
				int j = i + diff_indici;

				//si controlla se gli estremi sono palindromi e se
				//il sottovettore compreso tra essi è palindromo
				if(array[i] == array[j] && S[i+1][j-1] == 1) {
					S[i][j] = 1;	
					//eventuale aggirnamento del massimo come differenza tra
					//l'ultimo indice ed il primo + 1
					if(max < j - i + 1) {
						max = j - i + 1;
					}
				} else {
					S[i][j] = 0;
				}
			}
		}
		return max;
	}

	/**
	 * Il metodo main istanzia un oggetto della classe
	 * Esercizio1 il quale produce l'output.
	 * @param args
	 */
	public static void main( String[] args ) 
	{
		if(args.length != 1) {
			System.out.println("Input file not found.");
			System.exit(0);
		}
		
		Esercizio1 e = new Esercizio1(args[0]);
	}
}







