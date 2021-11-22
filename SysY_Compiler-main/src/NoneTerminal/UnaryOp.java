package NoneTerminal;

import WordAnalyse.*;

public class UnaryOp {
    public RegKey unaryOp;
    public static String name_unaryop = "<UnaryOp>";


    public static boolean isMyFirst(Symbol sym) {
        RegKey regKey = sym.getRegKey();
        boolean isPLUS = regKey == RegKey.PLUS;
        boolean isMINU = regKey == RegKey.MINU;
        boolean isNOT = regKey == RegKey.NOT;
        return isPLUS || isMINU || isNOT;
    }

    public static UnaryOp analyse(IdentifySymbol identifySymbol) {
        Symbol sym = identifySymbol.get_CurrentSym();
        RegKey regKey = sym.getRegKey();
        boolean isPLUS = regKey == RegKey.PLUS;
        boolean isMINU = regKey == RegKey.MINU;
        boolean isNOT = regKey == RegKey.NOT;
        if (!isPLUS && !isMINU && !isNOT) {
            return null;
        }
        UnaryOp unaryOp = new UnaryOp(sym.getRegKey());
        identifySymbol.getASymbol();

        identifySymbol.addStr(name_unaryop);
        return unaryOp;
    }

    public RegKey getUnaryOp(){
        return unaryOp;
    }

    public UnaryOp(RegKey unaryOp) {
        this.unaryOp = unaryOp;
    }
}
