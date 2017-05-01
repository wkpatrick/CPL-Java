import java.util.HashMap;
import java.util.List;

/**
 * William Patrick
 * CS 4308 - Concepts of Programming Languages
 * Course Project - Parser
 * 4/30/2017
 */
public class luaInterpreter {
    List<Statement> program;
    HashMap variables = new HashMap(); //In order to not have to do the work of searching a list or array, we just use a hashmap to store variables that are in play.

    /**
     * The program will take in a List of statements from the parser, and execute it line by line.
     */
    public void execute()
    {

    }

}
