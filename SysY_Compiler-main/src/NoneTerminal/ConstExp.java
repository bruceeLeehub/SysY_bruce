package NoneTerminal;

import ParcelType.*;
import WordAnalyse.*;

public class ConstExp {
    private final AddExp addExp;
    public static String name_constExp = "<ConstExp>";

    public static boolean isMyFirst(Symbol symbol) {
        boolean isMyFirst;
        isMyFirst = AddExp.isMyFirst(symbol);
        return isMyFirst;
    }

    public void genCode(My_Int intValue){
        addExp.genCode(intValue);
    }

    public ConstExp(AddExp addExp) {
        this.addExp = addExp;
    }

    public static ConstExp analyse(IdentifySymbol identSymbol) {
        AddExp addExp1 = AddExp.analyse(identSymbol);
        ConstExp constExp = new ConstExp(addExp1);
        identSymbol.addStr(name_constExp);
        return constExp;
    }

}
