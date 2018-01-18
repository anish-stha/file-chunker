package FileChunking;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        String testPath = "src//main//resources//TestFiles//test.mp3";
        File fileToBeChunked = new File ( testPath );
        String totestPath = "src//main//resources//TestFiles//testAfterChunking.mp3";
        File fileToBetested = new File( totestPath );
        List<File> listOfFiles = new ArrayList<>();
        FileSpliting fileSpliting = new FileSpliting();
        listOfFiles = fileSpliting.splitFile(fileToBeChunked);
        FileMerging fileMerging = new FileMerging();
        fileMerging.mergeFiles(listOfFiles, fileToBetested);
    }

}
