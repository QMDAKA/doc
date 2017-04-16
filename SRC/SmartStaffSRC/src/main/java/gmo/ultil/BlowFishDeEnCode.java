package gmo.ultil;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by quangminh on 22/09/2016.
 */
public class BlowFishDeEnCode {


    public static void encrypt(String username, String password) throws Exception {
        byte[] keyData = ("K69712").getBytes();
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyData, "Blowfish");
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] hasil = cipher.doFinal(password.getBytes());
        System.out.println(new BASE64Encoder().encode(hasil));
    }

    public static String decrypt(String string) {
        byte[] keyData = ("K69712").getBytes();
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyData, "Blowfish");
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("Blowfish");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        byte[] hasil = new byte[0];
        try {
            hasil = cipher.doFinal(new BASE64Decoder().decodeBuffer(string));
        } catch (IllegalBlockSizeException e) {
            //e.printStackTrace();
            System.out.println("Can't Decode that code\n");
            return null;
        } catch (BadPaddingException e) {
            e.printStackTrace();
            System.out.println("2");

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("3");
        }

        String bash = new String(hasil);
        return bash;

        //System.out.println(new String(hasil));
    }
}
