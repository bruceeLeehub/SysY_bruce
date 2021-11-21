package NoneTerminal;

import WordAnalyse.*;

public class Decl implements BlockItemInter{
    public static String name_decl = "<---Decl--->";

    public void genCode() {
    }
    public static Decl analyse(IdentifySymbol identSymbol){
        Decl decline;
        Symbol symbol = identSymbol.get_CurrentSym();
        RegKey regKey = symbol.getRegKey();
        boolean isInt = (regKey == RegKey.INTTK);
        if(!isInt){
            ConstDecl constDecl = ConstDecl.analyse(identSymbol);
            decline = constDecl;
        }else{
            VarDecl varDecl = VarDecl.analyse(identSymbol);
            decline = varDecl;
        }
        Block.hasReturnStmt = false;
        return decline;
    }
    public static boolean isMyFirst(Symbol symbol) {
        RegKey regKey = symbol.getRegKey();
        boolean isConst = (regKey == RegKey.CONSTTK);
        boolean isInt = (regKey == RegKey.INTTK);
        return isConst || isInt;
    }
}
