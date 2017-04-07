import java.util.List;

/**
 * William Patrick
 * CS 4308 - Concepts of Programming Languages
 * Course Project - Parser
 * 3/27/2017
 */
enum State
{
    IF_STATE, ASSIGN_STATE, WHILE_STATE, PRINT_STATE, REPEAT_STATE, EMPTY
}
public class Statement
{
    State state;
    List<Token> line;
    public Statement()
    {
        this.state = State.EMPTY;
    }

}
