package rwDataFile;

import errorMessages.ErrorMessages;

import java.io.File;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class ReadingDataFile {
    private String localHost;
    private String playerName;
    private int port;
    public ReadingDataFile() {
        try{
            String pathToTheFile = "/home/vladimir/Desktop/SeaBattleOnlineFinal/src/gameResources/resourse.txt";
            Scanner sc = new Scanner(new File(pathToTheFile));
            while(sc.hasNext()){
                String data = sc.nextLine();
                String [] datas = data.split(" ");
                this.port = Integer.parseInt(datas[0]);
                this.localHost = datas[1];
                this.playerName = datas[2];
            }
        } catch (FileNotFoundException e) {
            ErrorMessages errorMessages = new ErrorMessages();
            errorMessages.showWindowError("Error read resourse.");

        }
    }
    public int port(){
        return this.port;
    }
    public String localHost(){
        return this.localHost;
    }
    public String name(){
        return this.playerName;
    }

}
