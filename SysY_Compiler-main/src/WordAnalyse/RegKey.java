package WordAnalyse;

public enum RegKey {
    IDENFR, INTCON, STRCON, MAINTK, CONSTTK, INTTK, BREAKTK,
    CONTINUETK, IFTK, ELSETK, NOT, AND, OR, WHILETK, GETINTTK, PRINTFTK,
    LEQ, GRE, GEQ, EQL, NEQ,
    LBRACK, RBRACK, LBRACE, RBRACE,
    RETURNTK, PLUS, MINU, VOIDTK, MULT, DIV, MOD, LSS,
    ASSIGN, SEMICN, COMMA, LPARENT, RPARENT;

    public static RegKey getRegKey(String str){
        RegKey r = null;
        if (str.equals("main")){
            r = MAINTK;
        }else if(str.equals("const")){
            r = CONSTTK;
        }else if(str.equals("int")){
            r = INTTK;
        }else if(str.equals("break")){
            r = BREAKTK;
        }else if(str.equals("continue")){
            r = CONTINUETK;
        }else if(str.equals("if")){
            r = IFTK;
        }else if(str.equals("else")){
            r = ELSETK;
        }else if(str.equals("while")){
            r = WHILETK;
        }else if(str.equals("getint")){
            r = GETINTTK;
        }else if(str.equals("printf")){
            r = PRINTFTK;
        }else if(str.equals("return")){
            r = RETURNTK;
        }else if(str.equals("void")){
            r = VOIDTK;
        }else if(str.equals("!")){
            r = NOT;
        }else if(str.equals("&&")){
            r = AND;
        }else if(str.equals("||")){
            r = OR;
        }else if(str.equals("+")){
            r = PLUS;
        }else if(str.equals("-")){
            r = MINU;
        }else if(str.equals("*")){
            r = MULT;
        }else if(str.equals("/")){
            r = DIV;
        }else if(str.equals("%")){
            r = MOD;
        }else if(str.equals("<")){
            r = LSS;
        }else if(str.equals("<=")){
            r = LEQ;
        }else if(str.equals(">")){
            r = GRE;
        }else if(str.equals(">=")){
            r = GEQ;
        }else if(str.equals("==")){
            r = EQL;
        }else if(str.equals("!=")){
            r = NEQ;
        }else if(str.equals("=")){
            r = ASSIGN;
        }else if(str.equals(";")){
            r = SEMICN;
        }else if(str.equals(",")){
            r = COMMA;
        }else if(str.equals("(")){
            r = LPARENT;
        }else if(str.equals(")")){
            r = RPARENT;
        }else if(str.equals("[")){
            r = LBRACK;
        }else if(str.equals("]")){
            r = RBRACK;
        }else if(str.equals("{")){
            r = LBRACE;
        }else if(str.equals("}")){
            r = RBRACE;
        }else{
            r = IDENFR;
        }
        return r;
    }
}
