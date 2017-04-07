/**
 * William Patrick
 * CS 4308 - Concepts of Programming Languages
 * Course Project - Parser
 * 3/27/2017
 */

public class Main {
    public static void main(String[] args) {

        Statement testSt = new Statement();
        testSt.state = State.PRINT_STATE;

        luaScanner test = new luaScanner();
        test.processFile("test.lua"); //Can change to the file name of whatever test file you want to use.
    }
}
