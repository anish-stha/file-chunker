package FileChunking;

import com.sun.xml.internal.messaging.saaj.util.Base64;
import org.bouncycastle.jcajce.provider.symmetric.ARC4;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static FileChunking.Globals.myMAC;

public class FileSpliting {

    Indexer indexer = new Indexer();

   public List<File> splitFile(File f) throws IOException {
        int partCounter = 1;//I like to name parts from 001, 002, 003, ...
        //you can change it to 0 if you want 000, 001, ...

        int SIZE_OF_FILE = 2 * 1024 * 1024;// 2MB
        byte[] buffer = new byte[SIZE_OF_FILE];

        List<File> listOfFiles = new ArrayList<>();
        String fileName = f.getName();

        //try-with-resources to ensure closing stream
        try (FileInputStream fis = new FileInputStream(f);
             BufferedInputStream bis = new BufferedInputStream(fis)) {
            long filesize = f.length();
            int bytesAmount = 0;
            while (filesize>0) {
                bytesAmount = bis.read(buffer);
                System.out.println("Read amount: \t\t" +  bytesAmount);
                filesize -= bytesAmount;
                if (bytesAmount < SIZE_OF_FILE){
                    buffer = Arrays.copyOf(buffer, bytesAmount);
                }
                // Add index bytes and mac to split bytes
                byte[] result = indexer.appendIndex(buffer, partCounter);
                System.out.println("After Index: \t\t" + result.length);
                result = myMAC.appendMAC(result);
                System.out.println("After mac: \t\t" + result.length);

                //write each chunk of data into separate file with different number in name
                String filePartName = String.format("%s.%03d", fileName, partCounter++);
                File newFile = new File(f.getParent(), filePartName);
                try (FileOutputStream out = new FileOutputStream(newFile)) {
                    byte[] test = new byte[32];
                    System.arraycopy(result,result.length- 32, test, 0,32);
                    System.out.println("MAC : \t\t" + org.apache.commons.codec.binary.Base64.encodeBase64String(test));
                    out.write(result, 0, result.length);
                }
                listOfFiles.add(newFile);
            }
        }
        return listOfFiles;
    }
}
