package NoneTerminal;

import WordAnalyse.IdentifySymbol;
import WordAnalyse.RegKey;
import WordAnalyse.Symbol;

public class BlockItem {
    public static String name = "<BlockItem>";

    public static boolean analyse(IdentifySymbol identifySymbol, Block block) {
        Symbol sym;
        boolean judge = true;

        sym = identifySymbol.getCurSym();
        if (sym.getRegKey() == RegKey.CONSTTK ||
                sym.getRegKey() == RegKey.INTTK) {
            block.addDeclList(Decl.analyse(identifySymbol));
        }else{
            block.addStmtList(Stmt.analyse(identifySymbol));
        }

        /*
        if(judge){
            identifySymbol.addStr(name);
        }
        */
        return judge;
    }

    public static boolean isMyFirst(Symbol sym){
        if(Decl.isMyFirst(sym) || Stmt.isMyFirst(sym)){
            return true;
        }
        return false;
    }
}
