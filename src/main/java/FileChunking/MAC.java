package FileChunking;

import Exceptions.MessageAuthenticationException;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.crypto.CryptoException;
import sun.misc.resources.Messages_sv;

import javax.crypto.Mac;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MAC {

    private static MAC instance;

    SecretKeySpec signKey;
    Mac mac;
    String algoMAC="HmacSha256";

    static {
        instance = new MAC("password");
    }

    /** Static 'instance' method */
    public static MAC getInstance() {
        return instance;
    }

    private MAC(String password){
        //Initialize the MAC with Key
        try {
            init(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    public void init(String password) throws NoSuchAlgorithmException, InvalidKeyException{
        //Generate Secret key from user key
        signKey = new SecretKeySpec(password.getBytes(), algoMAC);
        //Get mac instance
        mac = Mac.getInstance(algoMAC);
        //Init mac
        mac.init(signKey);

    }


    //generate MAC
    public byte[] giveMeMAC(byte[] message){
        return ( mac.doFinal(message));

    }

    public byte[] appendMAC(byte[] data) {
        byte[] result = new byte[data.length + mac.getMacLength()];
        byte[] appendingMAC =mac.doFinal(data);
        System.arraycopy(data,0, result, 0, data.length);
        System.arraycopy(appendingMAC, 0 ,result,data.length, mac.getMacLength());
        return result;
    }

    public byte[] verifyMAC(byte[] data) throws MessageAuthenticationException {

        if (data.length > mac.getMacLength()) {

            byte[] messageData = new byte[data.length - mac.getMacLength() ];
            System.arraycopy(data, 0,messageData, 0,data.length - mac.getMacLength());

            byte[] appendedMac = new byte[mac.getMacLength()];
            System.arraycopy(data,data.length- mac.getMacLength(), appendedMac, 0, mac.getMacLength());

            byte[] testMac = this.mac.doFinal(messageData);

            if (!Arrays.equals(testMac, appendedMac)) {
                // Ignore the message
                throw new MessageAuthenticationException();
            }
            return messageData;
        }
        else
            throw new MessageAuthenticationException();

    }

    public boolean verifyMac( byte[] data, byte[] mac) {
        return Arrays.equals(giveMeMAC(data), mac);
    }
    public boolean verify(byte[] macByte){
        return Arrays.equals(this.mac.doFinal(), macByte);
    }
    public MAC addData(byte[] data){
        mac.update(data);
        return this;
    }
}