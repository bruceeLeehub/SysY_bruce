package NoneTerminal;

import WordAnalyse.IdentifySymbol;
import WordAnalyse.RegKey;
import WordAnalyse.Symbol;

public class Decl implements BlockItemInter{
    public static String name = "<Decl>";

    public static Decl analyse(IdentifySymbol identifySymbol){
        Symbol sym;
        boolean judge = true;
        Decl decl;

        Block.haveRetStmt = false;
        sym = identifySymbol.getCurSym();
        if(sym.getRegKey() == RegKey.INTTK){
            decl = VarDecl.analyse(identifySymbol);
        }else{
            decl = ConstDecl.analyse(identifySymbol);
        }
        return decl;
    }

    public static boolean isMyFirst(Symbol sym) {
        if(sym.getRegKey() == RegKey.CONSTTK || sym.getRegKey() == RegKey.INTTK){
            return true;
        }
        return false;
    }

    public void genCode() {
    }
}
