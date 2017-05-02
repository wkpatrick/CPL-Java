import java.util.List;

/**
 * William Patrick
 * CS 4308 - Concepts of Programming Languages
 * Course Project - Parser
 * 4/10/2017
 */
enum State {
    IF_STATE, ASSIGN_STATE, WHILE_STATE, PRINT_STATE, REPEAT_STATE, EMPTY, UNCHECKED
}

public class Statement {
    public State state;
    /**
     * A statement can either be a list of tokens, (ex print(a)) or a list of tokens and then a line (print(a+b))
     */
    public List<Token> line;
    public Statement inside;


    public Statement() {
        this.state = State.EMPTY;
    }

    public Statement(State state) {
        this.state = state;
    }

    public Statement(State state, List<Token> line) {
        this.state = state;
        this.line = line;
    }

    public String toString() {
        String toReturn = "";
        for (Token tok : this.line) {
            toReturn = toReturn.concat(tok.toString()) + " | ";
        }
        return toReturn;
    }

    /**
     *
     * @return A string containg the lexemes of a line, seperated by |
     */
    public String toLexmeString()
    {
        String toReturn = "";
        for (Token tok : this.line) {
            toReturn = toReturn.concat(tok.getLexeme()) + " | ";
        }
        return toReturn;
    }

}
