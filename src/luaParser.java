import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * William Patrick
 * CS 4308 - Concepts of Programming Languages
 * Course Project - Parser
 * 4/10/2017
 */
public class luaParser {
    //The States array is for keeping track of opening and closing of the 3 different flow statements, any time one is opened, its spot in the array is incremented
    //and the parser decrements and makes sure it isnt negative, and if it is it exits.
    public int[] states = new int[3]; // 0 is if statements, 1 is repeat statements 2 is Do/While statements
    public List<Statement> program;

    public luaParser() {
    }

    /**
     * Takes scanned program, and turns into into arrays to be parsed by the parseLine function,
     * @param program
     */
    public void parse(List<Statement> program) {

        printline("Each line's tokens start at 1, and each token is delimted with a | ");

        //Each statement is a line, we need turn each line into an array of tokens and recursively parse it
        Statement[] statementArray = new Statement[program.size()];
        for (int i = 0; i < program.size(); i++) {
            statementArray[i] = program.get(i);
        }

        for (Statement line : statementArray) {
            Token[] sentence = new Token[line.line.size()];
            for (int i = 0; i < line.line.size(); i++) {
                sentence[i] = line.line.get(i);
            }
            System.out.println(line.toLexmeString());
            System.out.println(line.toString());
            parseLine(sentence);
        }
    }

    /**
     * Parses the line of tokens provided, and prints out the statements within. It also checks there are enough opening and closing statements.
     *
     * @param line the input line of tokens, an array because its easier to look ahead and behind compared to a list.
     */
    public void parseLine(Token[] line) {
        for (int i = 0; i < line.length; i++) {
            switch (line[i].getType()) {
                case PRINT_STATE_BEGIN:
                    printline("\tPrint Statement at token number: " + i + 1);
                    Statement inputLine = new Statement(State.PRINT_STATE);
                    List<Token> tokenLine;
                    break;
                case ADD_OP:
                case SUB_OP:
                case DIV_OP:
                case MULT_OP:
                    printline("\tArithmetic statement at token number: " + i);
                    printline("\tStatement: " + getLexemesOfRange(line, i - 1, getPosOfNextLiteral(line, i)));
                    break;
                case LT_OP:
                case LE_OP:
                case GE_OP:
                case GT_OP:
                case EQ_OP:
                case NE_OP:
                    printline("\tComparison statement at token number: " + i);
                    printline("\tStatement: " + getLexemesOfRange(line, i - 1, getPosOfNextLiteral(line, i)));
                    break;
                case IF_STATE_BEGIN:
                    printline("\tIf statement at token number: " + i + 1);
                    states[0]++;
                    break;
                case THEN_STATE:
                    printline("\tThen statement at token number: " + i + 1);
                    break;
                case ELSE_STATE:
                    printline("\tElse statement at token number: " + i + 1);
                    //Throws all sorts of exceptions if there isnt something there, so it halts the interpreter if there is an umatched if/else statement
                    states[0]--;
                    if (states[0] == -1) {
                        printline("Missing an if statement somewhere");
                        System.exit(1);
                    }
                    break;
                case WHILE_STATE:
                    printline("\tWhile statement at token number: " + i + 1);
                    break;
                case DO_STATE:
                    printline("\tDo statement at token number: " + i + 1);
                    break;
                case REPEAT_STATE:
                    printline("\tRepeat statement at token number: " + i + 1);
                    states[1]++;
                    break;
                case UNTIL_STATE:
                    printline("\tUntil statement at token number: " + i + 1);
                    states[1]--;
                    if (states[1] == -1) {
                        printline("Missing an Remians/Until statement somewhere");
                        System.exit(1);
                    }
                    break;
                case ASSIGN_OP:
                    printline("\tAssign statement at token number: " + i);
                    printline("\tStatement: " + getLexemesOfRange(line, i - 1, getPosOfNextLiteral(line, i)));
                    break;
                case END_KEYWORD:
                    printline("\tEnd Statement at token number: " + i + 1);
            }
        }

        //Make sure all statements are closed
        if(states[0] > 1)
        {
            printline("You are missing the end of an if statement!");
            System.exit(1);
        }
        if(states[1] > 1)
        {
            printline("You are missing the end of a repeat statement!");
            System.exit(1);
        }
        if(states[2] > 1)
        {
            printline("You are missing the end of a while statement!");
            System.exit(1);
        }
    }

    /**
     *
     * @param line  the line we are currently working on
     * @param start where the statement starts, we work to find the next literal after that positon.
     * @return the integer representing the next string or int literal. Returns -1 if it cannot find one.
     */
    public int getPosOfNextLiteral(Token[] line, int start) {
        for (int i = start + 1; i < line.length; i++) {
            if (line[i].getType().equals(TokenType.INT_LIT) || line[i].getType().equals(TokenType.STRING_LIT)) {
                //In order to get a full statement, we check to see if there are any additional operations after the first literal.
                if ((i + 1) < line.length) {
                    if (line[i + 1].getType().equals(TokenType.ADD_OP) || line[i + 1].getType().equals(TokenType.SUB_OP) || line[i + 1].getType().equals(TokenType.MULT_OP) || line[i + 1].getType().equals(TokenType.DIV_OP) || line[i + 1].getType().equals(TokenType.ASSIGN_OP)) {
                        return getPosOfNextLiteral(line, i);
                    } else {
                        return i;
                    }
                } else {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * This makes life much easier
     *
     * @param line  The line we are working on
     * @param start the index of where we want to start
     * @param end   the index of the last lexeme we wish to print
     * @return the string representing the lexemes of the range within the provided line
     */
    public String getLexemesOfRange(Token[] line, int start, int end) {
        String retStr = "";
        for (int i = start; i <= end; i++) {
            retStr = retStr.concat(line[i].getLexeme().toString());
        }

        return retStr;
    }

    public boolean containsToken(Token[] line, TokenType search) {
        for (int i = 0; i < line.length; i++) {
            if (line[i].getType().equals(search)) {
                return true;
            }
        }

        return false;
    }

    public Token[] toRPN(Token[] line) {
        Token[] retArr = new Token[line.length];
        Deque<Token> retVal = new ArrayDeque<>();
        Deque<Token> spare = new ArrayDeque<>();
        for (int i = 0; i < line.length; i++) {
            if (line[i].getType().equals(TokenType.INT_LIT) || line[i].getType().equals(TokenType.STRING_LIT)) {
                retVal.push(line[i]);
            } else if (line[i].getType().equals(TokenType.ADD_OP) || line[i].getType().equals(TokenType.SUB_OP)) {
                spare.push(line[i]);
            } else if (line[i].getType().equals(TokenType.MULT_OP) || line[i].getType().equals(TokenType.DIV_OP)) {
                spare.push(line[i]);
            }
        }

        for (int i = 0; i < spare.size(); i++) {
            retVal.push(spare.pop());
        }

        for (int i = 0; i < retVal.size(); i++) {
            retArr[i] = retVal.pop();
        }

        return retArr;
    }

    public void printline(String input) {
        System.out.println(input);
    }
}
