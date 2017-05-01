import java.util.List;

/**
 * William Patrick
 * CS 4308 - Concepts of Programming Languages
 * Course Project - Parser
 * 4/10/2017
 */

public class Main {
    public static void main(String[] args) {

        List<Statement> program;
        luaScanner test = new luaScanner();
        luaParser test2 = new luaParser();
        luaInterpreter test3 = new luaInterpreter();

        program = test.processFile("test.lua"); //Can change to the file name of whatever test file you want to use.
        test2.parse(program);
    }
}
