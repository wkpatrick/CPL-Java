/**
 * William Patrick
 * CS 4308 - Concepts of Programming Languages
 * Course Project - Parser
 * 4/10/2017
 */
enum TokenType
{
    FUNC_ID, FUNC_KEYWORD, END_KEYWORD, ASSIGN_OP, LE_OP, LT_OP, GE_OP, GT_OP, EQ_OP, NE_OP, ADD_OP, SUB_OP, MULT_OP,
    DIV_OP, INT_LIT, STRING_LIT, OP_PAREN, CL_PAREN, PRINT_STATE_BEGIN, IF_STATE_BEGIN, THEN_STATE, ELSE_STATE, WHILE_STATE,
    DO_STATE, REPEAT_STATE, UNTIL_STATE, UNKNOWN
}
public class Token {
    private TokenType type;
    private String lexeme;

    public Token(TokenType type, String lexeme) {
        this.type = type;
        this.lexeme = lexeme;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public String toString() {
        return "Lexeme: " + this.lexeme + " Token: " + this.type.toString();
    }
}
