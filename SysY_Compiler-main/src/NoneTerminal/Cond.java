package NoneTerminal;

import WordAnalyse.*;

public class Cond {
    private final LOrExp lOrExp;
    public static String name_cond = "<Cond>";

    public void genCode() {
        this.lOrExp.genCode();
    }

    public static Cond analyse(IdentifySymbol identSymbol){
        LOrExp lOrExp1 = LOrExp.analyse(identSymbol);
//        Cond cond = new Cond(lOrExp1);
        identSymbol.addStr(name_cond);
        return new Cond(lOrExp1);
    }
    public Cond(LOrExp lOrExp){
        this.lOrExp = lOrExp;
    }
}
