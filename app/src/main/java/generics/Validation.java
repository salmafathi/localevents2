package generics;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

public class Validation {
	public boolean isValidMail(String userEmail) 
    {
        boolean check;
        Pattern p;
        Matcher m;

        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        p = Pattern.compile(EMAIL_STRING);

        m = p.matcher(userEmail);
        check = m.matches();
        return check;
    }
    public boolean isValidUserName(String userName){
    	boolean check;
    	check=userName.length()<50;
    	return check;	
		
    }
    public boolean isValidPassword(String userPassword) {
    	boolean check;
    	check=userPassword.length()>8;
    	return check;
	}
    public boolean isEmptyUserName(String userName){
    	return TextUtils.isEmpty(userName);
    }
    public boolean isEmptyPassword(String userPassword){
    	return TextUtils.isEmpty(userPassword);
    }
    public boolean isEmptyMail(String userEmail){
    	return TextUtils.isEmpty(userEmail);
    }
    public StringBuffer passwordMD5Hash(String userPassword)
    {
    	StringBuffer MD5Hash = null;
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(userPassword.getBytes());
            byte messageDigest[] = digest.digest();
            MD5Hash= new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
            {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                MD5Hash.append(h);
            }
            } 
            catch (NoSuchAlgorithmException e) 
            {
            	e.printStackTrace();
            }
        return MD5Hash;
    }
    
}
