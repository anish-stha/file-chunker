package Exceptions;

import org.bouncycastle.crypto.CryptoException;

public class MessageAuthenticationException extends CryptoException {
    public MessageAuthenticationException()
    {
        super("Message Integrity check failed");
    }
}
