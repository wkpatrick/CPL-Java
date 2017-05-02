import java.util.*;

/**
 * William Patrick
 * CS 4308 - Concepts of Programming Languages
 * Course Project - Parser
 * 4/30/2017
 */
public class luaInterpreter {
    ArrayList<Token> program = new ArrayList<>();
    HashMap variables = new HashMap(); //In order to not have to do the work of searching a list or array, we just use a hashmap to store variables that are in play.

    /**
     * The program will take in a List of statements from the parser, turn it into an array of tokens, and execute it
     */

    public void execute(List<Statement> inputProgram) {

        //First we turn the list of statements into an array of tokens, we do this to do recursion easier.
        //We begin by taking all of the different lines and put them into a single List, then call toArray()
        for (int i = 0; i < inputProgram.size(); i++) {
            program.addAll(inputProgram.get(i).line);
        }

        Token[] programArray = program.toArray(new Token[0]);

        //Now we begin the main execute loop
        execute(programArray);

    }

    /**
     * Executes the program, recursive to allow us to execute smaller batches of the program, we move i around in the first for loop similar to a program counter
     * @param programArray
     */
    public void execute(Token[] programArray) {
        int endIndex = 0;
        Token[] slice;


        for (int i = 0; i < programArray.length; i++) {
            switch (programArray[i].getType()) {
                case STRING_LIT: //It should be an assignment, so we check to see if its an assign op and a number after

                    slice = Arrays.copyOfRange(programArray, i, programArray.length);
                    if (programArray[i + 1].getType().equals(TokenType.ASSIGN_OP) && programArray[i + 2].getType().equals(TokenType.INT_LIT) && !(isArithToken(programArray[i + 3]))) {
                        variables.put(programArray[i].getLexeme(), Integer.parseInt(programArray[i + 2].getLexeme())); //Sets the variable in the hashtable equal to whatever the int is
                        i += 2; //We do this because we already executed the contents of everything else
                    }
                    //The case of a = a + 1
                    else if(programArray[i + 1].getType().equals(TokenType.ASSIGN_OP) && (isArithToken(programArray[i + 3])))
                    {
                        int stop = indexOfNexStatement(programArray, i);
                        if(stop == -1)
                        {
                            slice = Arrays.copyOfRange(programArray, i + 2, programArray.length);
                            variables.put(programArray[i].getLexeme(), executeArithStatement(slice));
                        }
                        i = programArray.length;

                    }
                    //In order to see if its arithmetic statemant or comparison
                    else if (isArithToken(programArray[i + 1])) //In order to see if its arithmetic statemant or comparison
                    {
                        slice = Arrays.copyOfRange(programArray, i , programArray.length);
                        int result = executeArithStatement(slice);
                    }
                    else if (isCompToken(programArray[i + 1])) {

                    }
                    else if(isArithStatement(slice))
                    {
                        execute(slice);
                    }
                    break;
                case INT_LIT:
                    break;
                case PRINT_STATE_BEGIN: //The scanner does not recognize print(x  , but recognizes print (x
                    /**
                     * We start at i + 1 because of the open parentheses that follows the print statement
                     * We need to get the next close parenthes so we can then execute whatever is in the print statement in the case of it beign an arith operation
                     */
                    int end = getIndexOfNextToken(programArray, TokenType.CL_PAREN, i + 1);
                    if (end - i == 3) {
                        //This is the case that its print(a), so we just print the value of a
                        if (programArray[end - 1].getType().equals(TokenType.INT_LIT)) {
                            System.out.println(Integer.parseInt(programArray[end - 1].getLexeme()));
                        } else {
                            System.out.println(variables.get(programArray[end - 1].getLexeme()));
                        }

                        i = end;

                    } else {
                        /**
                         * In case its something like print(a + b)
                         */
                        slice = Arrays.copyOfRange(programArray, i + 2, end);
                        if (isArithToken(programArray[i + 3])) {
                            System.out.println(executeArithStatement(slice));
                        } else {
                            execute(slice);
                        }

                        i = end; //This way we dont parse the same thing we just did
                    }
                    break;
                case IF_STATE_BEGIN:
                    int thenIndex = getIndexOfNextToken(programArray, TokenType.THEN_STATE, i);
                    int elseIndex = getIndexOfNextToken(programArray, TokenType.ELSE_STATE, thenIndex);
                    endIndex = getIndexOfNextToken(programArray, TokenType.END_KEYWORD, elseIndex);
                    slice = Arrays.copyOfRange(programArray, i + 1, thenIndex);
                    //If the statement is true
                    if (executeCompStatement(slice)) {
                        //We move the program counter to the right section
                        slice = Arrays.copyOfRange(programArray, thenIndex + 1, elseIndex);
                        execute(slice);
                    } else {
                        slice = Arrays.copyOfRange(programArray, elseIndex + 1, endIndex);
                        execute(slice);
                    }
                    i = endIndex;
                    break;
                case WHILE_STATE:
                    int doIndex = getIndexOfNextToken(programArray, TokenType.DO_STATE, i);
                    endIndex = getIndexOfNextToken(programArray, TokenType.END_KEYWORD, i);
                    slice = Arrays.copyOfRange(programArray, i + 1, doIndex);
                    Token[] doSlice = Arrays.copyOfRange(programArray, doIndex + 1, endIndex);
                    while(executeCompStatement(slice))
                    {
                        execute(doSlice);
                    }

                    i = endIndex;
                    break;
                case REPEAT_STATE:
                    int untilIndex = getIndexOfNextToken(programArray, TokenType.UNTIL_STATE, i);
                    endIndex = getIndexOfNextToken(programArray, TokenType.END_KEYWORD, i);
                    Token[] repeatSLice = Arrays.copyOfRange(programArray, i + 1, untilIndex);
                    Token[] untilSlice = Arrays.copyOfRange(programArray, untilIndex + 1, endIndex);
                    do {
                        execute(repeatSLice);
                    }
                    while(!executeCompStatement(untilSlice));
                    i = endIndex;
                    break;
            }
        }
    }

    /**
     * Gets the index of the next Token of search
     * @param line the array of Tokens
     * @param search The TokenType we wish to get the index of
     * @param start Where we want to begin our search
     * @return The index of the token that matches the TokenType
     */
    public int getIndexOfNextToken(Token[] line, TokenType search, int start) {
        for (int i = start + 1; i < line.length; i++) {
            if (line[i].getType() == search) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @param token
     * @return Returns true if the token is an Arithmetic Token
     */
    public boolean isArithToken(Token token) {
        if (token.getType().equals(TokenType.ADD_OP)) {
            return true;
        } else if (token.getType().equals(TokenType.SUB_OP)) {
            return true;
        } else if (token.getType().equals(TokenType.MULT_OP)) {
            return true;
        } else if (token.getType().equals(TokenType.DIV_OP)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param token
     * @return Returns true if the token is a Comparison token
     */
    public boolean isCompToken(Token token) {
        if (token.getType().equals(TokenType.LE_OP)) {
            return true;
        } else if (token.getType().equals(TokenType.LT_OP)) {
            return true;
        } else if (token.getType().equals(TokenType.GE_OP)) {
            return true;
        } else if (token.getType().equals(TokenType.GT_OP)) {
            return true;
        } else if (token.getType().equals(TokenType.EQ_OP)) {
            return true;
        } else if (token.getType().equals(TokenType.NE_OP)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Executes and returns the result of an arithmetic statement
     * @param program
     * @return the integer result of the arithmatic statement
     */
    public int executeArithStatement(Token[] program) {
        int result = 0;
        if (program.length == 1 && program[0].getType().equals(TokenType.STRING_LIT)) //If its something like a = b, this just fetches the value of b and returns it
        {
            return (int) variables.get(program[0].getLexeme());
        } else {
            // We want to just do each arith op, from left to right
            /**
             * Something like this
             * A + B + C
             * A = A + B
             * A + C
             * A = A + C
             * ret A
             *
             * To accomplish this we use a stack, isnt that nice
             */
            Deque<Integer> scratchSpace = new ArrayDeque<Integer>();
            Deque<Token> operands = new ArrayDeque<Token>();
            for (int i = 0; i < program.length; i++) {
                if (program[i].getType().equals(TokenType.STRING_LIT) || program[i].getType().equals(TokenType.INT_LIT)) {
                    if (program[i].getType().equals(TokenType.STRING_LIT)) {
                        scratchSpace.push((int) variables.get(program[i].getLexeme()));
                    } else if (program[i].getType().equals(TokenType.INT_LIT)) {
                        scratchSpace.push(Integer.parseInt(program[i].getLexeme()));
                    }
                } else if (isArithToken(program[i])) {
                    operands.push(program[i]);
                }
            }

            result = scratchSpace.peek();
            for (int i = 0; i < scratchSpace.size(); i++) {
                int temp1 = scratchSpace.pop();
                int temp2 = scratchSpace.pop();
                switch (operands.pop().getType()) {
                    case ADD_OP:
                        result = temp1 + temp2;
                        break;
                    case SUB_OP:
                        result = temp1 - temp2;
                        break;
                    case MULT_OP:
                        result = temp1 * temp2;
                        break;
                    case DIV_OP:
                        result = temp1 / temp2;
                        break;
                }

                scratchSpace.push(result);
                if (scratchSpace.size() == 1) {
                    return result;
                }
            }
        }
        return result;
    }

    /**
     * Executes a comparitive statement by taking the program and splitting it between the comparitive expression. It then performs arithmatic on the left and right if needed, and then comaprews
     * @param program
     * @return the boolean result of the comparitive statment
     */
    public boolean executeCompStatement(Token[] program) {
        int index = 0;
        //We need to find the comparative operator to get determine where we split the program;
        for (int i = 0; i < program.length; i++) {
            if (isCompToken(program[i])) {
                index = i;
            }
        }

        int value = 0;
        //We get the two sides of the comparison, so that we can operate on them and get their values should they be arith or comp, and then compare
        Token[] preComp = Arrays.copyOfRange(program, 0, index);
        Token[] postComp = Arrays.copyOfRange(program, index + 1, program.length);

        //Calculate or get the left side of the equation
        if (preComp.length == 1 && (preComp[0].getType().equals(TokenType.STRING_LIT) || preComp[0].getType().equals(TokenType.INT_LIT))) {
            //If the side is just a variable or integer
            if (preComp[0].getType().equals(TokenType.INT_LIT)) {
                value = Integer.parseInt(preComp[0].getLexeme());
            } else if (preComp[0].getType().equals(TokenType.STRING_LIT)) {
                value = (int) variables.get(preComp[0].getLexeme());
            }
        } else if (isArithStatement(preComp)) {
            value = executeArithStatement(preComp);
        }

        int comValue = 0; //The value we are going to compare against

        if (postComp.length == 1 && (postComp[0].getType().equals(TokenType.STRING_LIT) || postComp[0].getType().equals(TokenType.INT_LIT))) {
            //If the side is just a variable or integer
            if (postComp[0].getType().equals(TokenType.INT_LIT)) {
                comValue = Integer.parseInt(postComp[0].getLexeme());
            } else if (postComp[0].getType().equals(TokenType.STRING_LIT)) {
                comValue = (int) variables.get(postComp[0].getLexeme());
            }
        } else if (isArithStatement(postComp)) {
            comValue = executeArithStatement(postComp);
        }

        switch (program[index].getType()) {
            case LE_OP:
                if (value <= comValue) {
                    return true;
                } else {
                    return false;
                }
            case LT_OP:
                if (value < comValue) {
                    return true;
                } else {
                    return false;
                }
            case GE_OP:
                if (value >= comValue) {
                    return true;
                } else {
                    return false;
                }
            case GT_OP:
                if (value > comValue) {
                    return true;
                } else {
                    return false;
                }
            case EQ_OP:
                if (value == comValue) {
                    return true;
                } else {
                    return false;
                }
            case NE_OP:
                if (value != comValue) {
                    return true;
                } else {
                    return false;
                }
        }
        return false;
    }

    /**
     * To save us the time of having to manually check if something is an arithmetic statement
     *
     * @param program
     * @return true if the statement is a literal, false if it is not
     */
    public boolean isArithStatement(Token[] program) {
        for (int i = 0; i < program.length; i++) {
            if (isArithToken(program[i]) || program[i].getType().equals(TokenType.STRING_LIT) || program[i].getType().equals(TokenType.INT_LIT)) {

            } else {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param token The token to check
     * @return True if the token is a literal, false if it is not
     */
    public boolean isLiteral(Token token)
    {
        if(token.getType().equals(TokenType.INT_LIT) || token.getType().equals(TokenType.STRING_LIT))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Determines when the next statement is, by checking for the next group of 2 literals
     * @param program the array of tokens that is the program
     * @param start the index we wish to start searching after.
     * @return the index of the first literal in the pair of two.
     */
    public int indexOfNexStatement(Token[] program, int start)
    {
        for(int i = start; i < program.length - 1; i++)
        {
            if(isLiteral(program[i]) && isLiteral(program[i + 1]))
            {
                return i;
            }
        }

        return -1;
    }

}
