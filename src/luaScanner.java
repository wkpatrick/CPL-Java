import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * William Patrick
 * CS 4308 - Concepts of Programming Languages
 * Course Project - Parser
 * 4/10/2017
 */
public class luaScanner {

    public luaScanner() {
    }

    /**
     * processFile reads the file, and determines the tokens line by line.
     *
     * @param fileName the file name of the lua test program.
     * @return a list of statements (which are a list of tokens) that are of the UNCHECKED type, which means a later function
     * will determine what type of statement they are.
     */
    public List<Statement> processFile(String fileName) {
        List<String> stringList = new ArrayList<>();
        List<Statement> program = new ArrayList<>();

        //Read the file into a string
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
            List<Token> tokenList = new ArrayList<>();
            String[] slices = stringList.get(i).split(" ");
            for (int j = 0; j < slices.length; j++) {
                /**
                 * In case the test lua program does not have a space between parentheses and the content inside of them.\
                 * It first prints that there is an open parentheses, and then prints the type of the character(s) after it.
                 * In part two, this will be moved into the getType method to
                 */

                //if its an open paren and something else without a space ex (a
                if (slices[j].startsWith("(") && slices[j].length() > 1) {
                    tokenList.add(new Token(TokenType.OP_PAREN, "("));
                    tokenList.add(getType(slices[j].substring(1)));

                } else if (slices[j].endsWith(")") && slices[j].length() > 1) {
                    if (getType(slices[j]).getType().equals(TokenType.FUNC_ID)) {
                        tokenList.add(getType(slices[j]));

                    } else if (slices[j].endsWith("()")) {
                    }

                    //In case its a closed paren and something without a space between the two eg b)
                    else {
                        if (slices[j].length() == 2) {
                            tokenList.add(getType(String.valueOf(slices[j].charAt(0))));
                            tokenList.add(new Token(TokenType.CL_PAREN, ")"));
                        }
                        //In case its something like var)
                        else {
                            tokenList.add(getType(slices[j].substring(0, slices[j].length() - 2)));
                            tokenList.add(new Token(TokenType.CL_PAREN, ")"));
                        }
                    }
                } else {
                    tokenList.add(getType(slices[j]));
                }
            }

            Statement insState = new Statement();
            insState.line = tokenList;
            insState.state = State.UNCHECKED;
            program.add(insState);
        }

        return program;
    }

    /**
     * getType works by checking if the input string matches a regex pattern, or is the same as a string literal, and returns the relevant string.
     *
     * @param input the lexeme whose token is return
     * @return a string representing the token of the input lexeme
     */
    public Token getType(String input) {
        if (input.matches("[A-Za-z]\\(\\)")) {
            return new Token(TokenType.FUNC_ID, input);
        } else if (input.equals("function")) {
            return new Token(TokenType.FUNC_KEYWORD, input);
        } else if (input.equals("end")) {
            return new Token(TokenType.END_KEYWORD, input);
        } else if (input.equals("=")) {
            return new Token(TokenType.ASSIGN_OP, input);
        } else if (input.equals("<=")) {
            return new Token(TokenType.LE_OP, input);
        } else if (input.equals("<")) {
            return new Token(TokenType.LT_OP, input);
        } else if (input.equals(">=")) {
            return new Token(TokenType.GE_OP, input);
        } else if (input.equals(">")) {
            return new Token(TokenType.GT_OP, input);
        } else if (input.equals("==")) {
            return new Token(TokenType.EQ_OP, input);
        } else if (input.equals("~=")) {
            return new Token(TokenType.NE_OP, input);
        } else if (input.equals("+")) {
            return new Token(TokenType.ADD_OP, input);
        } else if (input.equals("-")) {
            return new Token(TokenType.SUB_OP, input);
        } else if (input.equals("*")) {
            return new Token(TokenType.MULT_OP, input);
        } else if (input.equals("/")) {
            return new Token(TokenType.DIV_OP, input);
        } else if (input.matches("\\d+")) {
            return new Token(TokenType.INT_LIT, input);
        } else if (input.matches("[A-Za-z]")) {
            return new Token(TokenType.STRING_LIT, input);
        } else if (input.equals("(")) {
            return new Token(TokenType.OP_PAREN, input);
        } else if (input.equals(")")) {
            return new Token(TokenType.CL_PAREN, input);
        } else if (input.equals("print")) {
            return new Token(TokenType.PRINT_STATE_BEGIN, input);
        } else if (input.equals("if")) {
            return new Token(TokenType.IF_STATE_BEGIN, input);
        } else if (input.equals("then")) {
            return new Token(TokenType.THEN_STATE, input);
        } else if (input.equals("else")) {
            return new Token(TokenType.ELSE_STATE, input);
        } else if (input.equals("while")) {
            return new Token(TokenType.WHILE_STATE, input);
        } else if (input.equals("do")) {
            return new Token(TokenType.DO_STATE, input);
        } else if (input.equals("repeat")) {
            return new Token(TokenType.REPEAT_STATE, input);
        } else if (input.equals("until")) {
            return new Token(TokenType.UNTIL_STATE, input);
        }
        return new Token(TokenType.UNKNOWN, input);
    }
}
