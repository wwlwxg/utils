package com.fatty;

import static org.hamcrest.CoreMatchers.containsString;

public class Test1 {
	public static void main(String[] args) {
		String str = "Coderbyte";
		StringBuffer sb = new StringBuffer();
	    for(int i = 0; i < str.length(); i++ ){
	        char c = str.charAt(i);
	        if(c == 90) {
	            c = 65;
	        } else if(c >= 65 && c < 90) {
	            c = (char) (c + 1);
	        } else if(c == 122) {
	            c = 97;
	        } else if(c >= 97 && c < 122) {
	            c = (char) (c + 1);
	        }
	        if(c == 'a' 
	        	|| c == 'e'
	        	|| c == 'i'
	        	|| c == 'o'
	        	|| c == 'u') {
	        	c += 32;
	        }
	        sb.append(c);
	    }
	    System.out.println(sb);
	}
}
