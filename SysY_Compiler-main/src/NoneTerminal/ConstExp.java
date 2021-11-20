package NoneTerminal;

import ParcelType.MyInt;
import WordAnalyse.IdentifySymbol;
import WordAnalyse.Symbol;

public class ConstExp {
    public static String name = "<ConstExp>";

    private AddExp addExp;

    public ConstExp(AddExp addExp) {
        this.addExp = addExp;
    }

    public void genCode(MyInt value){
        addExp.genCode(value);
    }

    public static ConstExp analyse(IdentifySymbol identifySymbol) {
        boolean judge = true;
        ConstExp constExp = new ConstExp(AddExp.analyse(identifySymbol));

        if (judge) {
            identifySymbol.addStr(name);
        }
        return constExp;
    }

    public static boolean isMyFirst(Symbol sym) {
        return AddExp.isMyFirst(sym);
    }
}
