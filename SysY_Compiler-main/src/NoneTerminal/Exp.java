package NoneTerminal;

import ParcelType.MyInt;
import WordAnalyse.IdentifySymbol;
import WordAnalyse.Symbol;

public class Exp {
    public static String name = "<Exp>";

    private AddExp addExp;
    public Exp(AddExp addExp){
        this.addExp = addExp;
    }

    public void genCode(MyInt value ){
        addExp.genCode(value);
    }

    public static Exp analyse(IdentifySymbol identifySymbol) {
        boolean judge = true;
        Exp exp = new Exp(AddExp.analyse(identifySymbol));

        if(judge){
            identifySymbol.addStr(name);
        }
        return exp;
    }

    public static boolean isMyFirst(Symbol sym) {
        return AddExp.isMyFirst(sym);
    }
}
