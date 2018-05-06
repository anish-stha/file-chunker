package FileChunking;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileMergeAndSplit {

    @Test
    public void test() throws IOException {

        String testPath = "src//main//resources//TestFiles//test.mp3";
        File fileToBeChunked = new File ( testPath );
        String totestPath = "src//main//resources//TestFiles//testAfterChunking.mp3";
        File fileToBetested = new File( totestPath );
        List<File> listOfFiles = new ArrayList<>();
        FileSpliting fileSpliting = new FileSpliting();
        listOfFiles = fileSpliting.splitFile(fileToBeChunked);
        FileMerging fileMerging = new FileMerging();
        fileMerging.merge(listOfFiles, fileToBetested);

        Path f1Path = Paths.get("src//main//resources//TestFiles//test.mp3");
        byte[] f1 = Files.readAllBytes(f1Path);
        Path f2Path = Paths.get("src//main//resources//TestFiles//testAfterChunking.mp3");
        byte[] f2 = Files.readAllBytes(f2Path);
        assertArrayEquals(f1,f2);
    }

}
