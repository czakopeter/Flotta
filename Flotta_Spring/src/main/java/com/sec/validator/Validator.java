package com.sec.validator;

public class Validator {
  
  public static boolean validPassword(String psw) {
    return psw != null && psw.length() >= 6 && psw.length() <= 20;
  }
  
  public static boolean equalsAndValidPassword(String psw, String psw2) {
    return psw != null && psw.length() >= 6 && psw.length() <= 20 && psw.contentEquals(psw2);
  }
}
