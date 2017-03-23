import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * William Patrick
 * 
 */
public class luaScanner {

    public luaScanner() {
    }

    /**
     *
     * processFile prints the token of each lexeme in the test lua file supplied by the program.
     * The method accomplishes this by reading the input file line by line, splicing the line into strings with a space as the delimiter, and passes it to getType().
     * @param fileName the file name of the lua test program.
     */
    public void processFile(String fileName) {
        List<String> stringList = new ArrayList<String>();

        try {
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            while ((line = reader.readLine()) != null) {
                stringList.add(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < stringList.size(); i++) {
            String[] slices = stringList.get(i).split(" ");
            for (int j = 0; j < slices.length; j++) {
                /**
                 * In case the test lua program does not have a space between parentheses and the content inside of them.\
                 * It first prints that there is an open parentheses, and then prints the type of the character(s) after it.
                 * In part two, this will be moved into the getType method to
                 */
                if (slices[j].startsWith("(") && slices[j].length() > 1) {
                    System.out.println(slices[j].substring(0, 1) + " : Open Parentheses");
                    System.out.println(slices[j].substring(1) + " : " + getType(slices[j].substring(1)));
                } else if (slices[j].endsWith(")")) {
                    if (getType(slices[j]).equals("Function ID")) {

                    } else {
                        System.out.println(slices[j].substring(0, 1) + " : " + getType(slices[j].substring(0, 1)));
                        System.out.println(slices[j].substring(slices[j].length() - 1) + ": Close Parentheses");
                    }

                } else {
                    System.out.println(slices[j] + " : " + getType(slices[j]));
                }

            }
        }
    }

    /**
     * getType works by checking if the input string matches a regex pattern, or is the same as a string literal, and returns the relevant string.
     *
     * @param input the lexeme whose token is return
     * @return a string representing the token of the input lexeme
     */
    public String getType(String input) {
        if (input.matches("[A-Za-z]\\(\\)")) {
            return "Function ID";
        } else if (input.equals("function")) {
            return "Function Keyword";
        } else if (input.equals("end")) {
            return "End Keyword";
        } else if (input.equals("=")) {
            return "Assignment Operator";
        } else if (input.equals("<=")) {
            return "LE Operator";
        } else if (input.equals("<")) {
            return "LT Operator";
        } else if (input.equals(">=")) {
            return "GE Operator";
        } else if (input.equals(">")) {
            return "GT Operator";
        } else if (input.equals("==")) {
            return "EQ Operator";
        } else if (input.equals("~=")) {
            return "NE Operator";
        } else if (input.equals("+")) {
            return "Add Operator";
        } else if (input.equals("-")) {
            return "Sub Operator";
        } else if (input.equals("*")) {
            return "Mult Operator";
        } else if (input.equals("/")) {
            return "Div Operator";
        } else if (input.matches("\\d+")) {
            return "Int Literal";
        } else if (input.matches("[A-Za-z]")) {
            return "String literal";
        } else if (input.equals("(")) {
            return "Open Parentheses";
        } else if (input.equals(")")) {
            return "Close Parentheses";
        } else if (input.equals("print")) {
            return "Print Statement";
        } else if (input.equals("if")) {
            return "If Statement";
        } else if (input.equals("then")) {
            return "Then Statement";
        } else if (input.equals("else")) {
            return "Else Statement";
        } else if (input.equals("while")) {
            return "While Statement";
        } else if (input.equals("do")) {
            return "Do Statement";
        } else if (input.equals("repeat")) {
            return "Repeat Statement";
        } else if (input.equals("until")) {
            return "Until Statement";
        }
        return "Unknown Token";
    }
}
