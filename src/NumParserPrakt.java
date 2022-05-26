import java.io.FileReader;

public class NumParserPrakt {
    final static String SOURCE_FILE = "testdatei.txt";
    static final char EOF = (char) 255;
    static int BUFFER_SIZE = 256;
    static int CHAR_POINTER = 0;
    static int MAX_POINTER = 0;
    static char CHAR_SOURCE[];

    public static void main(String[] args) {
        if(readCharFromFile())

    }

    private static boolean readCharFromFile(){
        try {
            FileReader reader = new FileReader(SOURCE_FILE);
            for(int i=0; i<BUFFER_SIZE; i++){
                int char_pointer = reader.read();
                if(char_pointer != -1){
                    CHAR_SOURCE[i] = (char) char_pointer;
                }else{
                    MAX_POINTER = i;
                    CHAR_SOURCE[i] = EOF;
                    break;
                }
            }
        }catch(Exception e){
            System.out.println("Fehler beim lesen der Datei: " + SOURCE_FILE);
            return false;
        }
        return true;
    }
}
