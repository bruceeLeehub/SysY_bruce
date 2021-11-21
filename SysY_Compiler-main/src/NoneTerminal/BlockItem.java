package NoneTerminal;

import WordAnalyse.*;
public class BlockItem {
    public static String name_blockItem = "<BlockItem>";

    public static boolean isMyFirst(Symbol sym){
        boolean b1 = Decl.isMyFirst(sym);
        boolean b2 = Stmt.isMyFirst(sym);
        return b1 || b2;
    }

    public static boolean analyse(IdentifySymbol identifySymbol, Block block) {
        Symbol sym;
        boolean judge = true;

        sym = identifySymbol.getCurSym();
        if (sym.getRegKey() == RegKey.CONSTTK ||
                sym.getRegKey() == RegKey.INTTK) {
            block.add_Decl_List(Decl.analyse(identifySymbol));
        }else{
            block.add_Stmt_List(Stmt.analyse(identifySymbol));
        }

        /*
        if(judge){
            identifySymbol.addStr(name);
        }
        */
        return judge;
    }
}
