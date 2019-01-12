package network;

import errorMessages.ErrorMessages;
import rwDataFile.ReadingDataFile;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class Client {
    private static Socket serverSocket;
    public Client(){
        ReadingDataFile readingDataFile = new ReadingDataFile();
        int serverPort = readingDataFile.port();
        String serverHost = readingDataFile.localHost();
        try {
            serverSocket = new Socket(serverHost, serverPort);
        } catch (IOException e) {
            ErrorMessages errorMessages = new ErrorMessages();
            errorMessages.showWindowError("Exception network, plese restart game!");
        }
    }
    private static void writeMessage(String message) {
        OutputStream out = null;
        try {
            out = serverSocket.getOutputStream();
        } catch (IOException e) {
            System.out.println("Failed to get output stream.");
        }
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Objects.requireNonNull(out)));
            writer.write(message + "\n");
            writer.flush();
        } catch (IOException e) {
            System.out.println("Error write message.");
            ErrorMessages errorMessages = new ErrorMessages();
            errorMessages.showWindowError("Error write message.");
        }
    }
    public static void commandExit(){ writeMessage("@quit"); }
    public static void commandNewName(String playerName){ writeMessage("@newName " + playerName);}
    public static void commandSetGameList (){writeMessage("@setFreeGameList");}
    public static void commandNewGame (){writeMessage( "@newGame" );}
    public static void commandOverGame (){writeMessage( "@overGame" );}
    public static void commandStartGame (String nameOpponent){writeMessage( "@startGame " + nameOpponent );}
    public static void commandReturnToPage() { writeMessage("@returnToPage");}
    public static void commandMyName() {writeMessage("@myName");}
    public static void commandShot(String name, int x, int y) { writeMessage("@shot " + name + " " + Integer.toString(x) + " " + Integer.toString(y)); }
    public static void commandYes(String name){writeMessage("@yes " + name);}
    public static void commandNo(String name){writeMessage("@no " + name);}
    public static void commandLoser(String name) { writeMessage("@loser " + name);}
    public static String readMessage() throws IOException {
        InputStream in = null;
        try { in = serverSocket.getInputStream(); }
        catch (IOException e) { System.out.println("Failed to get input stream."); }
        BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(in)));
        String ln;
        ln = reader.readLine();
        System.out.println(ln);
        return ln;
    }



}
