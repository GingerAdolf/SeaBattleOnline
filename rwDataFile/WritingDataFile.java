package rwDataFile;

import java.io.FileWriter;
import java.io.IOException;

public class WritingDataFile {
    private static String pathToTheFile = "/home/vladimir/Desktop/SeaBattleOnlineFinal/src/gameResources/resourse.txt";
    public void writingDataFile(String playerName){
        ReadingDataFile readingDataFileq = new ReadingDataFile();
        String localHost = readingDataFileq.localHost();
        String port  = Integer.toString(readingDataFileq.port());
        String text = port + " " + localHost + " " + playerName + "\n";
        try(FileWriter writer = new FileWriter(pathToTheFile, false))
        {
            writer.write(text);
            writer.flush();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
