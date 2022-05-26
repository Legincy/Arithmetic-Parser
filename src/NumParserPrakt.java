import java.io.FileReader;

public class NumParserPrakt {
    final static String SOURCE_FILE = "testdatei.txt";
    static final char[][] VALID_CHARACTERS_SETS = {{'1','2','3','4','5','6','7','8','9','0'},
    {'+', '*', '/', '-'}, {'(', ')'}};
    static final char EOF = (char) 255;
    static int BUFFER_SIZE = 256;
    static int CHAR_POINTER = 0;
    static int MAX_POINTER = 0;
    static char CHAR_SOURCE[];

    public static void main(String[] args) {
        CHAR_SOURCE = new char[BUFFER_SIZE];

        if(readFile()){
            if(expression(0) && isEndOfLine()){
                System.out.println("Korrekter Ausdruck.");
                return;
            }
            System.out.println("Fehler im Ausdruck");
        }
    }

    private static boolean expression(int step){
        displayMessage("expression ->", step);
        if(checkNextChar(step)){
            return true;
        }
        return false;
    }

    private static boolean rightExpression(int step){
        return true;
    }

    private static boolean term(int step){
        return true;
    }

    private static boolean operator(int step){
        return true;
    }

    private static boolean num(int step){
        return true;
    }

    private static boolean digit(int step){

        displayMessage(String.format("digit -> %s", CHAR_SOURCE[CHAR_POINTER]), step);
        return true;
    }

    private static boolean match(char[] set, int step){
        for(char item : set){
            if(item == CHAR_SOURCE[CHAR_POINTER]){
                displayMessage(String.format("match: %s", CHAR_SOURCE[CHAR_POINTER]), step);
                CHAR_POINTER++;
                return true;
            }
        }
        return false;
    }

    private static boolean checkNextChar(int step){
        for(int i=0; i<VALID_CHARACTERS_SETS.length; i++){
            for(char item : VALID_CHARACTERS_SETS[i]){
                if(item == CHAR_SOURCE[CHAR_POINTER+1]){
                    switch(i){
                        case 0:
                            return term(step +1) && rightExpression(step+1);
                        case 1:
                            return term(step +1) && rightExpression(step+1);
                        case 2:
                            System.out.println("operation");
                    }

                }
            }
        }
        return false;
    }

    private static void displayMessage(String msg, int step){
        String whitespace = "";
        for(int i=0; i<step; i++){
            whitespace += "    ";
        }

        System.out.println(whitespace + msg);
    }

    private static boolean isEndOfLine(){
        if(CHAR_POINTER == MAX_POINTER){
            displayMessage("", 0);
            return true;
        }
        displayMessage("", 0);
        return false;
    }

    private static boolean readFile(){
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
            System.out.println("Fehler beim lesen der Datei: " + SOURCE_FILE + "\n" + e);
            return false;
        }
        return true;
    }
}
