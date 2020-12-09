package findAndReplace;

import java.util.*;

public class KMP { 
	
	public static ArrayList<Integer> KMPSearch(String pat, String txt) { 
		
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		
		int M = pat.length(); 
		int N = txt.length(); 

		int lps[] = new int[M]; 
		int j = 0; // index for pat[] 
 
		computeLPSArray(pat, M, lps); 

		int i = 0; // index for txt[] 
		while (i < N) { 
			if (pat.charAt(j) == txt.charAt(i)) { 
				j++; 
				i++; 
			} 
			if (j == M) { 
				indexes.add(i-j); 
				j = lps[j - 1]; 
			} 

			// mismatch after j matches 
			else if (i < N && pat.charAt(j) != txt.charAt(i)) { 
				
				if (j != 0) 
					j = lps[j - 1]; 
				else
					i = i + 1; 
			} 
		} 
		return indexes;
	} 

	public static void computeLPSArray(String pat, int M, int lps[]) 
	{ 
		// length of the previous longest prefix suffix 
		int len = 0; 
		int i = 1; 
		lps[0] = 0;  

		 
		while (i < M) { 
			if (pat.charAt(i) == pat.charAt(len)) { 
				len++; 
				lps[i] = len; 
				i++; 
			} 
			else // (pat[i] != pat[len]) 
			{ 
				if (len != 0) { 
					len = lps[len - 1]; 
				} 
				else // if len == 0 
				{ 
					lps[i] = len; 
					i++; 
				} 
			} 
		} 
	} 
}