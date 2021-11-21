package NoneTerminal;

import WordAnalyse.*;
public class BlockItem {
    public static String name_blockItem = "<---BlockItem--->";

    public static boolean isMyFirst(Symbol sym){
        boolean b1 = Decl.isMyFirst(sym);
        boolean b2 = Stmt.isMyFirst(sym);
        return b1 || b2;
    }

    public static boolean analyse(IdentifySymbol identifySymbol, Block block) {
        Symbol symbol = identifySymbol.get_CurrentSym();
        boolean isDecl = (symbol.getRegKey() == RegKey.CONSTTK ||
                symbol.getRegKey() == RegKey.INTTK);
        if (!isDecl) {
            block.add_Stmt_List(Stmt.analyse(identifySymbol));
        }else{
            block.add_Decl_List(Decl.analyse(identifySymbol));
        }
        return true;
        /*
        if(judge){
            identifySymbol.addStr(name);
        }
        */
    }
}
