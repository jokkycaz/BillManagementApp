package com.example.billmanagement;

public class inputValidation {
    
	// TODO All these would be better using Regular Expressions.  We can do that later.
	
	public static Boolean isName(String s) {
        s = s.replaceAll("\\s", "");
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isLetter(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    public static Boolean isCurrency(String s) {
        for (int i = 0; i < s.length(); i++){
            if (!Character.isDigit(s.charAt(i)) && s.charAt(i)!='.' && s.charAt(i)!='$') {
                System.out.println(s.charAt(i));
                return false;
            }
        }
        return true;
    }
    
    
    public static Boolean isDate(String s) {
        if (!Character.isDigit(s.charAt(0)) && !Character.isDigit(s.charAt(1)) && s.charAt(2)!='-' &&
                !Character.isDigit(s.charAt(3)) && !Character.isDigit(s.charAt(4)) && s.charAt(5)!='-' &&
                !Character.isDigit(s.charAt(6)) && !Character.isDigit(s.charAt(7)) && 
                !Character.isDigit(s.charAt(8)) && !Character.isDigit(s.charAt(9)) &&
                !Character.isDigit(s.charAt(10)) && !Character.isDigit(s.charAt(11))) {
            return false;
        }
        return true;
    }

}
