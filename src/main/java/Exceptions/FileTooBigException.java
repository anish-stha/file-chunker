package Exceptions;

public class FileTooBigException extends Exception {
    public FileTooBigException() { super(); }
    public FileTooBigException(String message) { super(message); }
    public FileTooBigException(String message, Throwable cause) { super(message, cause); }
    public FileTooBigException(Throwable cause) { super(cause); }
}