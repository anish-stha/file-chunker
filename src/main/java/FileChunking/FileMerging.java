package FileChunking;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

public class FileMerging {
    PreProcessing preProcessing = new PreProcessing();

//    public void mergeFiles(List<File> files, File into)
//            throws IOException {
//        try (FileOutputStream fos = new FileOutputStream(into);
//             BufferedOutputStream mergingStream = new BufferedOutputStream(fos)) {
//            for (File f : files) {
//                Files.copy(f.toPath(), mergingStream);
//            }
//        }
//    }

    public void merge(List<File> files, File into){
        List<byte[]> bytesList = preProcessing.fileListToBytesList(files);
        mergeBytesIntoFile(bytesList, into);
    }

    public void mergeBytesIntoFile(List<byte[]> bytesList, File into){
        List<byte[]> readyToMergeList = preProcessing.preMergeProcess(bytesList);
        try {
            FileOutputStream fos = new FileOutputStream(into);
            for (byte[] buffer: readyToMergeList)
                try {
                    fos.write(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
