package WordAnalyse;

public enum RegKey {
    IDENFR, INTCON, STRCON, MAINTK, CONSTTK, INTTK, BREAKTK,
    CONTINUETK, IFTK, ELSETK, NOT, AND, OR, WHILETK, GETINTTK, PRINTFTK,
    LEQ, GRE, GEQ, EQL, NEQ,
    LBRACK, RBRACK, LBRACE, RBRACE,
    RETURNTK, PLUS, MINU, VOIDTK, MULT, DIV, MOD, LSS,
    ASSIGN, SEMICN, COMMA, LPARENT, RPARENT;

    public static RegKey getRegKey(String str){
        return switch (str) {
            case "main" -> MAINTK;
            case "const" -> CONSTTK;
            case "int" -> INTTK;
            case "break" -> BREAKTK;
            case "continue" -> CONTINUETK;
            case "if" -> IFTK;
            case "else" -> ELSETK;
            case "while" -> WHILETK;
            case "getint" -> GETINTTK;
            case "printf" -> PRINTFTK;
            case "return" -> RETURNTK;
            case "void" -> VOIDTK;
            case "!" -> NOT;
            case "&&" -> AND;
            case "||" -> OR;
            case "+" -> PLUS;
            case "-" -> MINU;
            case "*" -> MULT;
            case "/" -> DIV;
            case "%" -> MOD;
            case "<" -> LSS;
            case "<=" -> LEQ;
            case ">" -> GRE;
            case ">=" -> GEQ;
            case "==" -> EQL;
            case "!=" -> NEQ;
            case "=" -> ASSIGN;
            case ";" -> SEMICN;
            case "," -> COMMA;
            case "(" -> LPARENT;
            case ")" -> RPARENT;
            case "[" -> LBRACK;
            case "]" -> RBRACK;
            case "{" -> LBRACE;
            case "}" -> RBRACE;
            default -> IDENFR;
        };
    }
}
