import java.io.*;
import java.util.*;

/**
 * @author Michele Dinelli
 * 		   Matricola: 0000934209
 * 		   email: michele.dinelli5@studio.unibo.it
 * 
 *         Risoluzione dell'eserczio 4: Binary search tree
 */

public class Esercizio4 {
	
	private BST tree; //Albero generato dalla lettura dell'input
	private NodeBST radice; //Radice dell'albero
	
	/**
	 * Il costruttore della classe legge l'input. 
	 * Stampa l'output richiesto a terminale ovvero 
	 * il minimo del BST se e solo se l'albero processato
	 * è un BST, altrimenti produce la stampa "NON BST".
	 *
	 * @param inputf
	 */
	public Esercizio4( String inputf )  
	{
		readInput(inputf);
	
		if( isBST(radice, Integer.MIN_VALUE, Integer.MAX_VALUE) )  {
			int minBST = minBST(tree);
			System.out.println(minBST);
		} else {
			System.out.println("NON BST");
		}
	}
	
	/**
	 * Nodo di un BST.
	 * Sono specificati solo
	 * i metodi essenziali per produrre l'output.
	 */
	private class NodeBST{
		
		private Integer value; //valore
		private NodeBST left;  //figlio sinistro
		private NodeBST right; //figlio destro
		private NodeBST parent;//genitore
		
		public NodeBST( Integer value )
		{
			this.value = value;
		}
		 
		 public void setParent( NodeBST node)
		 {
			 this.parent = node;
		 }
		 
		 public void setLeft( NodeBST node )
		 {
			 this.left = node;
		 }
		 
		 public void setRight( NodeBST node )
		 {
			 this.right = node;
		 }
		 
		 public void addRightSibling( NodeBST node )
		 {
			 this.parent.setRight(node);
		 }
	}
	
	/**
	 * BST radicato nel nodo radice passato al costruttore.
	 */
	private class BST{
		
		private NodeBST radice; 
		
		public BST( NodeBST node )
		{
			this.radice = node;
		}
	}
	
	/**
	 * Il metodo readInput è abbastanza laborioso.
	 * In mancanza, per ora, di un'opportuna implementazione ricorsiva
	 * readInput legge e conserva i nodi presenti nel file compresi quelli 
	 * nulli, questo per permettere, grazie al vettore guida ovvero il vettore 
	 * delle 'istruzioni' contenente i caratteri "("  ","  ")" , di operare 
	 * correttamente e creare le relazioni tra i nodi.
	 * 
	 * @param inputf
	 */
	public void readInput( String inputf )
	{
		Locale.setDefault(Locale.US);
		Scanner scan;
		try {
			scan = new Scanner(new FileReader(inputf));
			String line = scan.nextLine();
			String[] str = line.split(" ");	
		
			LinkedList<NodeBST> nodi = new LinkedList<NodeBST>();
			NodeBST nodo;
			String[] guida = new String[str.length];
			int k = 0;
			//si popola la stringa guida e la lista che conterrà 
			//i nodi dell'albero
			for(int i = 0; i < str.length; i++) {
				String token = str[i];
				if(!token.equals(")") && !token.equals("(") && !token.equals(",")) {
					if( token.equals("-")) {
						nodo = new NodeBST(null);
						nodi.add(nodo);
					} else {
						nodo = new NodeBST(Integer.parseInt(token));
						nodi.add(nodo);
					}
				} else {
					guida[k] = token;
					k++;
				}
			}
		
			//Vengono create le relazioni trai nodi
			NodeBST currentNode = null;
			NodeBST newNode;
			for(int i = 0; i < guida.length - nodi.size(); i++) {
				if(guida[i] != null){
					String token = guida[i];

					if(token.equals("(") && i == 0) {
						radice = nodi.pollFirst();
						if(radice == null) {
							System.out.println("Albero vuoto.");
							System.exit(0);
						}
						tree = new BST(radice);
						radice.setParent(radice);
						currentNode = radice;
					} else if(token.equals("(") && radice.left == null) {
						newNode = nodi.pollFirst();
						radice.setLeft(newNode);
						newNode.setParent(radice);
						currentNode = newNode;
					} else if(token.equals("(")) {						
						newNode = nodi.pollFirst();						
						currentNode.setLeft(newNode);
						newNode.setParent(currentNode);
						currentNode = newNode;
					} else if(token.equals(",")) {
						newNode = nodi.pollFirst();	
						currentNode.addRightSibling(newNode);
						newNode.parent = currentNode.parent;
						currentNode = newNode;	
					} else if(token.equals(")")) {
						currentNode = currentNode.parent;
					}
				}
			}
			scan.close();
		}catch(Exception e) {
			System.err.println("Error. Please check the input file. Stack trace below.\n");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * minBST applica una classica ricerca del
	 * minimo valore in un BST percorrendo il cammino
	 * sinistro fino all'estrema foglia sinistra.
	 * I controlli sul valore del figlio sinistro sono dovuti
	 * al fatto di aver memorizzaro i nodi '-' come effettivi 
	 * nodi con valore null.
	
	 * @param tree
	 * @return minimo del BST o radice del BST se essa non presenta figlio sinistro
	 */
	public int minBST( BST tree ) {
		
		NodeBST u = tree.radice;
		int min = radice.value;
		
		while(u.left != null) {
			if(u.left.value != null) {
				u = u.left;
				min = u.value;
			} else { 
				return min; 
			}
		}
		return min;
	}
	
	/**
	 * Il metodo isBST partendo dalla radice  
	 * controlla ricorsivamente le proprietà del BST.
	 * I sono controlli sui valori dei nodi sono sempre 
	 * dovuti al metodo di memorizzazione.
	 *
	 * @param radice
	 * @param min 
	 * @param max
	 * @return true se BST false altrimenti
	 */
	public boolean isBST( NodeBST radice, int min, int max )
	{
		NodeBST u = radice;
		
		if(u == null) { 
			return true;
		}
	
		if(u.value != null) {
			if(u.value < min || u.value > max) {
				return false;
			}
		}
	
		if(u.value != null) {
			return isBST(u.left, min, u.value - 1) && isBST(u.right, u.value + 1, max);
		} else { return true; }
	} 
	
	/**
	 * Il metodo main istanzia un oggetto della classe 
	 * Esercizio4 che produce l'output.
	 * 		
	 * @param args
	 */
	public static void main( String[] args )   
	{
		if(args.length != 1) {
			System.out.println("Input file not found.");
			System.exit(0);
		}
		
		Esercizio4 e = new Esercizio4(args[0]);
	}
}
