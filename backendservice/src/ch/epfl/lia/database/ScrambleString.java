package ch.epfl.lia.database;

public class ScrambleString {
	public boolean isScramble(String s1, String s2) {
        // Note: The Solution object is instantiated only once and is reused by each test case.
        int l1=s1.length();
        int l2=s2.length();
        
        char[] array1=s1.toCharArray();
        char[] array2=s2.toCharArray();
        
        if(l1!=l2) return false;
        
        boolean[][][] result=new boolean[l1-1][l1][l1];
        
        for(int i=0;i<l1;i++){
        	for(int j=0;j<l2;j++){
        		result[0][i][j]=(array1[i]==array2[j]);
        	}
        }
        
        for(int i=1;i<l1;i++){
        	for(int j=0;j<l1;j++){
        		for(int k=0;k<l1;k++){
        			if(i+j>=l1||i+k>=l1) continue;
        			boolean temp=false;
        			for(int m=0;m<i;m++){
        				if(!temp)
        					temp=result[m][j][k]&&result[i-1-m][j+m+1][k+m+1];
        				if(!temp)
        					temp=result[m][j][k+m+1]&&result[i-m-1][j+m+1][k];
        				if(temp) break;
        			}
        			result[i][j][k]=temp;
        			
        			
        		}
        	}
        }
        
        return result[l1-1][0][0];
	}
}
