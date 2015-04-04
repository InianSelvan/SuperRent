/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.superrent.modules;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
/**
 *
 * @author Iniyan
 */
public class Encrypt {
    
    private byte[] input;
    private byte[] KyeBytes = "12121212".getBytes();
    private byte[] ivbytes = "input123".getBytes();
    private SecretKeySpec key = new SecretKeySpec(KyeBytes, "DES");
    private IvParameterSpec ivspec = new IvParameterSpec(ivbytes);
    private Cipher  cipher;
    byte[] cipherText;
    int ctLength;
    
    public String encryptPwd(JTextField text){
        try{
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            input = text.getText().getBytes();
            SecretKeySpec key = new SecretKeySpec(KyeBytes, "DES");
            IvParameterSpec ivspec = new IvParameterSpec(ivbytes);
            cipher = Cipher.getInstance("DES/CTR/NoPadding","BC");
            cipher.init(Cipher.ENCRYPT_MODE,key,ivspec);
            cipherText = new byte[cipher.getOutputSize(input.length)];
            ctLength = cipher.update(input,0, input.length,cipherText,0);
            ctLength += cipher.doFinal(cipherText, ctLength);
            System.out.println(new String(cipherText));
        }catch(NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | ShortBufferException | IllegalBlockSizeException | BadPaddingException e){
            JOptionPane.showMessageDialog(null, e);
        }
        return new String(cipherText);
    }
    
    public void decryptPwd(){
       try{
           cipher.init(Cipher.DECRYPT_MODE, key, ivspec);
           byte[] plainText = new byte[cipher.getOutputSize(ctLength)];
           int ptLength = cipher.update(cipherText, 0, ctLength, plainText, 0);
           ptLength += cipher.doFinal(plainText, ptLength);
           System.out.println(new String(plainText));           
       }catch(InvalidKeyException | InvalidAlgorithmParameterException | ShortBufferException | IllegalBlockSizeException | BadPaddingException e){
            JOptionPane.showMessageDialog(null, e);
       }
    }  
}
