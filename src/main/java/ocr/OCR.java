package ocr;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.javatuples.Pair;

public class OCR {
    public static void main(String[] args) throws IOException {
        parseAccount();
        calculateChekSum();
    }
    
    public static void parseAccount() throws FileNotFoundException, IOException {
        Pair<char[][], Integer> zero = new Pair<>(new char[][] {
                            {' ','_',' '},
                            {'|',' ','|'},
                            {'|','_','|'}
                        } ,0);
        Pair<char[][], Integer> one = new Pair<>(new char[][] {
                            {' ',' ',' '},
                            {' ',' ','|'},
                            {' ',' ','|'}
                        } ,1);
        Pair<char[][], Integer> two = new Pair<>(new char[][] {
                            {' ','_',' '},
                            {' ','_','|'},
                            {'|','_',' '}
                        } ,2);
        Pair<char[][], Integer> three = new Pair<>(new char[][] {
                            {' ','_',' '},
                            {' ','_','|'},
                            {' ','_','|'}
                        } ,3);
        Pair<char[][], Integer> four = new Pair<>(new char[][] {
                            {' ',' ',' '},
                            {'|','_','|'},
                            {' ',' ','|'}
                        } ,4);
        Pair<char[][], Integer> five = new Pair<>(new char[][] {
                            {' ','_',' '},
                            {'|','_',' '},
                            {' ','_','|'}
                        } ,5);
        Pair<char[][], Integer> six = new Pair<>(new char[][] {
                            {' ','_',' '},
                            {'|','_',' '},
                            {'|','_','|'}
                        } ,6);
        Pair<char[][], Integer> seven = new Pair<>(new char[][] {
                            {' ','_',' '},
                            {' ',' ','|'},
                            {' ',' ','|'}
                        } ,7);
        Pair<char[][], Integer> eight = new Pair<>(new char[][] {
                            {' ','_',' '},
                            {'|','_','|'},
                            {'|','_','|'}
                        } ,8);
        Pair<char[][], Integer> nine = new Pair<>(new char[][] {
                            {' ','_',' '},
                            {'|','_','|'},
                            {' ','_','|'}
                        } ,9);
        List<Pair> numbers = Stream.of(zero, one, two, three, four, five, six, seven, eight, nine).collect(Collectors.toList());   
        File file = new File("./src/main/resources/bank_ocr_dojo_us1"); 
        BufferedReader br = new BufferedReader(new FileReader(file)); 

        String st;
        List<String> accounts = new ArrayList<>();
        List<char[][]> charLine = new ArrayList<>();
        int row = 1;
        int currentNumber = 1;
        while ((st = br.readLine()) != null) {
            if (row == 0) {
                row = 1;
            } else {    
                int column = 1;
                char[][] number = new char[3][3];
                for (int i = 0; i < st.length(); i++) {
                    char c = st.charAt(i);
                    number[row-1][column-1] = c;
                    if (row == 1) { 
                        if (column == 3) {
                            charLine.add(number);
                        } 
                    } else {
                            char[][] element = charLine.get(currentNumber-1);
                            element[row-1][column-1] = c; 
                            charLine.set(currentNumber-1, element);
                    }
                    ++column;
                    if (column == 4) {
                        number = new char[3][3];
                        column = 1;
                        ++currentNumber;
                    }
                } 
                currentNumber = 1;
                ++row;
                if (row == 4) {
                    String account = "";
                    for(char[][] current: charLine) {
                        for (Pair parsedNumber : numbers) {
                            if (Arrays.deepEquals((char[][])parsedNumber.getValue0(), current)) {
                                account += Integer.toString((Integer)parsedNumber.getValue1());
                            }
                        }
                    }
                    accounts.add(account);
                    charLine = new ArrayList<>();
                    row = 0;
                }
            }
        }
        br.close();
        
        File save = new File("./src/main/resources/output.txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(save));
        if (!save.exists()) {
            save.createNewFile();
        } else {
            save.delete();
            save.createNewFile();
        }
        
        boolean first = true;
        for (String currentAccount : accounts) {
            if (first) {
                first = false;
            } else {
                bw.newLine();
            }
            bw.write(currentAccount);
            bw.newLine();
        }
        bw.close();
    }
    
    public static void calculateChekSum() throws FileNotFoundException, IOException {
        File file = new File("./src/main/resources/output.txt"); 
        BufferedReader br = new BufferedReader(new FileReader(file)); 
        String st;
        
        while ((st = br.readLine()) != null) {
            if ("".equals(st)) {
                continue;
            }
            int cheksum = 0;
            for (int i=0; i < st.length(); i++) {
                int c = Character.getNumericValue(st.charAt(i));
                cheksum += c*(st.length()-i);
            }
            cheksum = cheksum % 11;
            System.out.println(st + " cheksum: " + cheksum  + " " + ((cheksum == 0)? "valid" : "invalid"));
        }
    }
}
