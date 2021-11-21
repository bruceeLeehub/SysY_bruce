package NoneTerminal;

import WordAnalyse.IdentifySymbol;
import WordAnalyse.RegKey;
import WordAnalyse.Symbol;

public class UnaryOp {
    public static String name = "<UnaryOp>";

    private RegKey op;

    public UnaryOp(RegKey op) {
        this.op = op;
    }

    public RegKey getOp(){
        return this.op;
    }

    public static UnaryOp analyse(IdentifySymbol identifySymbol) {
        boolean judge = true;
        Symbol sym = identifySymbol.get_CurrentSym();
        if (sym.getRegKey() != RegKey.PLUS &&
                sym.getRegKey() != RegKey.MINU &&
                sym.getRegKey() != RegKey.NOT) {
            return null;
        }
        UnaryOp unaryOp = new UnaryOp(sym.getRegKey());
        identifySymbol.getASymbol();

        if (judge) {
            identifySymbol.addStr(name);
        }
        return unaryOp;
    }

    public static boolean isMyFirst(Symbol sym) {
        if(sym.getRegKey() == RegKey.PLUS ||
        sym.getRegKey() == RegKey.MINU ||
        sym.getRegKey() == RegKey.NOT){
            return true;
        }
        return false;
    }
}
