import java.io.*;
import java.util.*;

/**
 * @author Michele Dinelli
 * 		   Matricola: 0000934209
 * 		   E-mail: michele.dinelli5@studio.unibo.it
 * 
 *  	   Risoluzione dell'Esercizio 2: Trasporto di materiali.
 */

public class Esercizio2 {
	
	private int n; //numero di nodi
	private LinkedList<Edge> edges; //lista degli archi
	private double[] d; //array delle distanze dalla sorgente
	private int[] p; //nodi di provenienza
	private LinkedList<Integer> stores; //lista dei nodi di partenza (magazzini)
	private int source; //sorgente
	private int destination; //nodo di destinazione (Ospedale)
	private double max_load; //carico massimo
	private double[] final_d; //distanze finali
	private int[] final_p; //provenienze finali

	/**
	 * Arco orientato di un grafo
	 */
	private class Edge {
		
		final int src;
		final double weight;
		final int dst;
		
		public Edge( int src, int dst, double w )
		{
			this.src = src;
			this.dst = dst;
			this.weight = w;
		}
	}

	/**
	 * Il costruttore della classe legge il file in input. 
	 * Per tutti i nodi magazzino viene richiamato il metodo 
	 * maxLoad.
	 * Viene stampato, se esiste, il percorso da un nodo
	 * sorgente (magazzino) alla destinazione (ospedale), 
	 * che massimizza il carico. Se il carico finale è negativo
	 * si stampa -1.
	 * 
	 * @param inputf
	 */
	public Esercizio2( String inputf )
	{
		readInput(inputf);
		max_load = -1;
		for(Integer i : stores) {
			maxLoad(i);
		}
		
		if(max_load < 0 ) {
			System.out.println("-1");
			System.exit(1);
		} 
		
		printPath(source, destination);
	}
	
	/**
	 * Lettura del file. Gli archi vengono memorizzati in una lista
	 * e si tiene traccia dei possibili nodi di partenza (magazzini) e del nodo
	 * di destinazione (ospedale). Se il primo nodo non è un magazzino si stampa -1
	 * 
	 * @param inputf
	 */
	public void readInput( String inputf )
	{
        Locale.setDefault(Locale.US);
		Scanner scan;
		try {
			scan = new Scanner(new FileReader(inputf));
			stores = new LinkedList<Integer>();
			edges = new LinkedList<Edge>();
			n = scan.nextInt();
			destination = -1;
			for(int i = 0; i < n; i++){
				final int src = i;
				final String type = scan.next();
				final double w = scan.nextDouble();
				final int numAdiacenti = scan.nextInt();
				
				//si controlla la natura dei nodi ed alla prima
				//iterazione se il primo nodo è un magazzino
				if(i == 0 && !type.equalsIgnoreCase("M")) {
					System.out.println("-1");
					System.exit(1);
				}else if(type.equalsIgnoreCase("M")) {
					stores.add(src);
				} else if(type.equalsIgnoreCase("O")) {
					destination = src;
				}

				for(int j = 0; j < numAdiacenti; j++) {
					final int dst = scan.nextInt();
					edges.add( new Edge( src, dst, w ));
				}								
			}	
			//si controlla se esistono dei magazzini da cui partire
			//ed un ospedale in cui arrivare in caso negativo viene prodotta la stampa -1
			if(stores.isEmpty() || destination == -1) {
				System.out.println("-1");
				System.exit(1);
			}
			scan.close();
		}catch(Exception e) {
			System.err.println("Error. Please check the input file. Stack trace below.\n");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	/**
	 * Il metodo maxLoad fa uso dell'algoritmo di Bellman-Ford
	 * e calcola anzichè il cammino di peso minimo, quello massimo, da un nodo
	 * sorgente (magazzino) ad un nodo destinazione (ospedale).
	 * Preferito in questo caso a Dijkstra per via della possibile presenza 
	 * di pesi negativi nel grafo.
	 * Quando si ottiene il carico massimo viene salvata una copia dell'array delle
	 * distanze, dei nodi di provenienza e l'indice della sorgente che porta ad ottenere
	 * il carico massimo.
	 * Viene verificata la raggiungibilità della destinazione (Ospedale)
	 * e l'assenza di cicli postivi. Viene prodotta la stampa "-1" se non si verfica 
	 * una delle due precedenti o se il primo nodo non è un magazzino.
	 * 
	 * @param s sorgente
	 * @return massimo carico accumulabile dalla sorgente alla destinazione
	 */
	public void maxLoad( int s )
	{
		Iterator<Edge> iterator; 
		d = new double[n];
		p = new int[n];
		
		Arrays.fill( d, Double.NEGATIVE_INFINITY );
		Arrays.fill( p, -1 );
		
		d[s] = 0.0;
		for(int i = 0; i < n; i++) {
			iterator = edges.iterator();
			while(iterator.hasNext()) {
				final Edge e = iterator.next();
				final int src = e.src;
				final int dst = e.dst;
				final double w = e.weight;
				//rilassamento
				if( d[src] + w > d[dst]) {
					d[dst] = d[src] + w;
					p[dst] = src;
				}
			}
		}
		//verifica se la destinazione è raggiungibile da s
		if(d[n-1] == Double.NEGATIVE_INFINITY) {
	    	System.out.println( "-1" );
	    	System.exit(0);
	    } 
		
		//ulteriore verifica per cicli positivi
		iterator = edges.iterator();
	    while (iterator.hasNext()) {
	    	final Edge e = iterator.next();
	    	final int src = e.src;
	    	final int dst = e.dst;
	    	final double w = e.weight;
	    	
	    	if (d[src] + w > d[dst]) {
	    		System.out.println("-1");
	    		System.exit(0);
	        }
    	}		
	    //aggiornamento del percorso e della sorgente 
	    //se il carico è maggiore del precedente massimo
	    if(d[n-1] > max_load) {
	    	max_load = d[n-1];
			source = s;
			final_d = d;
			final_p = p;
	    }
	}
	
	/**
	 * printPath stampa ricorsivamente il percorso dal nodo
	 * sorgente al nodo destinazione.
	 * 
	 * @param src sorgente
	 * @param dst destinazione
	 */
	public void printPath( int src, int dst )
	{
		if (final_d[dst] < 0) {
			System.out.printf("No path from %d to %d\n", src, dst);
		} else if (src == dst) {
			System.out.println(src);
		} else {
			printPath(src, final_p[dst]);
			System.out.println(dst);
		}
	}
	
	/**
	 * Il metodo main istanzia un oggetto della classe
	 * Esercizio5 il quale produce l'output.
	 *
	 * @param args
	 */
	public static void main( String[] args ) 
	{
		if(args.length != 1) {
			System.out.println("Input file not found.");
			System.exit(0);
		}
		
		Esercizio2 e = new Esercizio2(args[0]);
		
	}
}
