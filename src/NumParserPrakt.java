import java.io.FileReader;
import java.util.concurrent.TimeUnit;

public class NumParserPrakt {
    final static String SOURCE_FILE = "testdatei.txt";
    static final char EOF = (char) 255;
    static final char[][] VALID_CHARACTERS_SETS = {
            {'1','2','3','4','5','6','7','8','9','0'},
            {'*', '/'},
            {'-', '+'},
            {'(', ')'},
            {EOF}};
    static int BUFFER_SIZE = 256;
    static int CHAR_POINTER = 0;
    static int MAX_POINTER = 0;
    static int OPEN_OPERATIONS = 0;
    static char CHAR_SOURCE[];

    public static void main(String[] args) {
        CHAR_SOURCE = new char[BUFFER_SIZE];

        if(readFile()){
            if(expression(0) && isEndOfLine() && !isEmpty() && checkSettings()){
                System.out.println("\n[INFO]: Valid syntax.");
                return;
            }
            System.out.println(String.format("\n[INFO]: Error in syntax at position %s with char: '%s'", CHAR_POINTER, CHAR_SOURCE[CHAR_POINTER-1]));
        }
    }

    private static boolean checkSettings(){
        if(OPEN_OPERATIONS > 0){
            displayMessage("Closing bracket expected.", 0);
            return false;
        }else if(OPEN_OPERATIONS < 0){
            displayMessage("Too many opened brackets found.", 0);
            return false;
        }
        return true;
    }

    private static boolean expression(int step){
        displayMessage("expression", step);
        return term(step) && rightExpression(step);
    }

    private static boolean rightExpression(int step){
        displayMessage("rightExpression", step+1);

        if(match(VALID_CHARACTERS_SETS[2], step+1)) {
            return term(step+1) && rightExpression(step+1);
        }

        displayMessage("ε", step+2);
        return true;
    }

    private static boolean rightTerm(int step){
        int nextChar = checkNextChar();
        displayMessage("rightTerm", step+1);

        if(match(VALID_CHARACTERS_SETS[1], step+1)){ //1:= {'*', '/'}
            return operator(step+1) && rightTerm(step+1);
        }
        displayMessage("ε", step+2);
        return true;
    }

    private static boolean term(int step){
        displayMessage("term", step+1);
        return operator(step+1) && rightTerm(step+1);
    }

    private static boolean operator(int step){
        char[] openSet = {'('};
        char[] closeSet = {')'};
        displayMessage("operator", step+1);

        if(match(VALID_CHARACTERS_SETS[0], -1)){ // {'1','2','3','4','5','6','7','8','9','0'}
            return num(step+1);
        }
        else if (match(VALID_CHARACTERS_SETS[3], -1)){ // {'(', ')'}
            if(CHAR_SOURCE[CHAR_POINTER] == VALID_CHARACTERS_SETS[3][0]){ // if char == '('
                if(match(openSet, step +1)) OPEN_OPERATIONS++;
                expression(step+2);
                if (match(closeSet, step +2)) OPEN_OPERATIONS--;
                return true;
            }
        }
        return false;
    }

    private static boolean num(int step){
        displayMessage("num", step+1);

        if(checkNextChar() == 0){ //0:= {'1','2','3','4','5','6','7','8','9','0'}
            return  digit(step+1) && num(step+1);
        }else{
            return digit(step+1);
        }
    }

    private static boolean digit(int step){
        displayMessage("digit", step+1);

        match(VALID_CHARACTERS_SETS[0], step);
        return true;
    }

    private static boolean match(char[] set, int step){
        for(char item : set){
            if(item == CHAR_SOURCE[CHAR_POINTER]){
                if(step != -1) {
                    displayMessage(String.format("match: %s [%s]", CHAR_SOURCE[CHAR_POINTER], CHAR_POINTER), step+1);
                    CHAR_POINTER++;
                }
                return true;
            }
        }
        return false;
    }

    private static int checkNextChar(){
        for(int i=0; i<VALID_CHARACTERS_SETS.length; i++){
            for(char item : VALID_CHARACTERS_SETS[i]){
                if(item == CHAR_SOURCE[CHAR_POINTER+1]){
                    return i;
                }
            }
        }
        return -1;
    }

    private static void displayMessage(String msg, int step){
        String whitespace = "";
        for(int i=0; i<step; i++){
            if(i == step-1){
                if(msg.contains("match")) whitespace += "[*] ";
                else whitespace += "<---";
            }else{
                whitespace += "|   ";
            }
        }

        System.out.println(whitespace + msg);
    }

    private static boolean isEmpty(){
        if(CHAR_SOURCE.length == 0) {
            displayMessage("Cannot parse an empty file.", 0);
            return true;
        }
        return false;
    }

    private static boolean isEndOfLine(){
        if(CHAR_POINTER == MAX_POINTER){
            return true;
        }
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
