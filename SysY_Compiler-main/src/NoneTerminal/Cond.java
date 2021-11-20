package NoneTerminal;

import WordAnalyse.IdentifySymbol;
import WordAnalyse.Symbol;

public class Cond {
    public static String name = "<Cond>";

    private LOrExp lOrExp;
    public Cond(LOrExp lOrExp){
        this.lOrExp = lOrExp;
    }

    public void genCode() {
        lOrExp.genCode();
    }

    public static Cond analyse(IdentifySymbol identifySymbol){
        Symbol sym;
        boolean judge = true;

        Cond cond = new Cond(LOrExp.analyse(identifySymbol));

        if(judge){
            identifySymbol.addStr(name);
        }
        return cond;
    }
}
