package FileChunking;

import Exceptions.MessageAuthenticationException;
import sun.misc.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

import static FileChunking.Globals.myMAC;

public class PreProcessing {

    int MAX_FILE_SIZE = 100*1024*1024; //100MB
    public class FileTooBigException extends Exception {
        public FileTooBigException() { super(); }
        public FileTooBigException(String message) { super(message); }
        public FileTooBigException(String message, Throwable cause) { super(message, cause); }
        public FileTooBigException(Throwable cause) { super(cause); }
    }

    Indexer indexer = new Indexer();

    public List<byte[]> preMergeProcess(List<byte[]> bytesList){
        TreeMap bytesTree = new TreeMap();
        bytesTree = preMergeProcessBytes(bytesList);
        List<byte[]> processedByteList = new ArrayList<>(bytesTree.values());
        return processedByteList;
    }

    private void preMergeProcessing(List<File> fileList){
        preMergeProcessBytes(fileListToBytesList(fileList));
    }

    private TreeMap preMergeProcessBytes(List<byte[]> byteList){
        TreeMap bytesTree = new TreeMap();
        for (byte[] buffer : byteList) {
            try {
                buffer = myMAC.verifyMAC(buffer);
                int index = indexer.getIndex(buffer);
                System.out.println(index);
                byte[] data = indexer.getData(buffer);
                bytesTree.put(index, data);
            } catch (MessageAuthenticationException e) {
                e.printStackTrace();
            }
        }
        Set keys = bytesTree.keySet();
        for (Iterator i = keys.iterator(); i.hasNext();) {

            Integer key = (Integer) i.next();
            System.out.println(key);
        }
        return bytesTree;
    }

    // return a list of byte[] from list of files
    public List<byte[]> fileListToBytesList(List<File> fileList) {
        List<byte[]> bytesListBuffer = new ArrayList<byte[]>();
        for (File f : fileList) {

            byte[] buffer = new byte[0];
            try {
                buffer = fileToBytes(f);
                bytesListBuffer.add(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytesListBuffer;
    }


    // Convert a file into byte[]
    public byte[] fileToBytes(File file) throws IOException {
        byte[] buffer = new byte[(int) file.length()];
        InputStream ios = null;
        try {
            ios = new FileInputStream(file);
            if (ios.read(buffer) == -1) {
                throw new IOException(
                        "EOF reached while trying to read the whole file");
            }
        } finally {
            try {
                if (ios != null)
                    ios.close();
            } catch (IOException e) {
            }
        }
        return buffer;
    }
}
