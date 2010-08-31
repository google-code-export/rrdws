package org.vietspider.html.parser;

import java.io.IOException;
import java.io.PrintStream; 

/*************************************************************************
 *  Compilation:  javac Diff
 *  Execution:    java Diff filename1 filename2
 *  Dependencies: In.java
 *  
 *  Reads in two files and compute their diff.
 *  A bare bones version.
 * 
 *  % java Diff input1.txt input2.txt
 * 
 *
 *  Limitations
 *  -----------
 *   - Could hash the lines to avoid potentially more expensive 
 *     string comparisons.
 *
 *************************************************************************/

public class Diff {
	
	public Diff(){
		this(System.out);
	}

    public Diff(PrintStream out) {
		this.out = out; 
	}
    
    In a;
    In b; 
    
    void diff() throws IOException{

        
        String[] x = a.readAll().split("\\n");
        String[] y = b.readAll().split("\\n");
        
        this.diff(x, y);
    }

    public String[] diff(String[] x, String[] y) {
    	
    	StringBuilder  a2b= new StringBuilder(); 
    	StringBuilder  b2a= new StringBuilder(); 
    	StringBuilder  same= new StringBuilder(); 
    	synchronized (this) {
			
	        // number of lines of each file
	        int M = x.length;
	        int N = y.length;
	
	        // opt[i][j] = length of LCS of x[i..M] and y[j..N]
	        int[][] opt = new int[M+1][N+1];
	
	        // compute length of LCS and all subproblems via dynamic programming
	        for (int i = M-1; i >= 0; i--) {
	            for (int j = N-1; j >= 0; j--) {
	                if (x[i].equals(y[j]))
	                    opt[i][j] = opt[i+1][j+1] + 1;
	                else 
	                    opt[i][j] = Math.max(opt[i+1][j], opt[i][j+1]);
	            }
	        }
	
	        // recover LCS itself and print out non-matching lines to standard output
	        int i = 0, j = 0;
	        while(i < M && j < N) {
	            if (x[i].equals(y[j])) {
	            	println(same, "  " + x[i] );
	                i++;
	                j++;
	            }
	            else if (opt[i+1][j] >= opt[i][j+1]) println(a2b,"< " + x[i++]);
	            else                                 println(b2a,"> " + y[j++]);
	        } 
	        // dump out one remainder of one string if the other is exhausted
	        while(i < M || j < N) {
	            if      (i == M) println(a2b,"> " + y[j++]);
	            else if (j == N) println(b2a,"< " + x[i++]);
	        }    
	        return (a2b.toString() +b2a.toString()).toString().split("\n");
    	}

    }

	public static void main(String[] args) throws IOException {

        // read in lines of each file
    	Diff diffTmp = new Diff();
    	diffTmp.setA(new In(args[0]) );
    	diffTmp.setB(new In(args[1]) );
    	diffTmp.diff();
    }

	private void setB(In in) {
		this.a = in;
	}

	private void setA(In in) {
		this.b = in;
	}

	private PrintStream out;

	/**
	 * @author vipup
	 * @param x
	 * @param i
	 */
	private void println(StringBuilder sb, String  x ) {
		sb.append(x);
		sb.append("\n");
		this.out.println ( x );
	}
	


	public PrintStream getOut() { 
			return out;
	}

	public void setOut(PrintStream out) {
		this.out = out;
	}

}