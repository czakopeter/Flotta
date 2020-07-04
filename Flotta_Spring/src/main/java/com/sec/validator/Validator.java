package com.sec.validator;

public class Validator {
  
  public static boolean validPassword(String psw) {
    return psw != null && psw.length() >= 6 && psw.length() <= 20;
  }
  
  public static boolean equalsAndValidPassword(String psw, String psw2) {
    return psw != null && psw.length() >= 6 && psw.length() <= 20 && psw.contentEquals(psw2);
  }
  
  public static boolean validHunPhoneNumber(String number) {
    return number.matches("20|30|70|50[0-9](7)");
  }
  
  //Luhn algorithm
  //length min 15
  public static boolean isValidImieWithLuhnAlg(String imei) {
    int i = 1;
    int count = 0;
    int checkDigit = Character.getNumericValue(imei.charAt(imei.length() - 1));
    while(i < imei.length()) {
      char ch = imei.charAt(i - 1);
      if(!Character.isDigit(ch)) {
        return false;
      }
      if(i % 2 == 1) {
        count += Character.getNumericValue(ch);
      } else {
        int n = Character.getNumericValue(ch);
        count += n / 10;
        count += n % 10;
      }
    }
    return 10 - checkDigit == count % 10;
  }
}
