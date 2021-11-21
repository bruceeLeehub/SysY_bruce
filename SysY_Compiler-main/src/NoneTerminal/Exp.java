package NoneTerminal;

import ParcelType.*;
import WordAnalyse.*;

public class Exp {
    public static String name_exp = "<Exp>";

    private final AddExp addExp;

    public static boolean isMyFirst(Symbol symbol) {
        boolean addExp_isMyFirst;
        addExp_isMyFirst = AddExp.isMyFirst(symbol);
        return addExp_isMyFirst;
    }

    public Exp(AddExp addExp){
        this.addExp = addExp;
    }

    public static Exp analyse(IdentifySymbol identSymbol) {
        AddExp addExp1 = AddExp.analyse(identSymbol);
        Exp exp = new Exp(addExp1);
        identSymbol.addStr(name_exp);
        return exp;
    }

    public void genCode(My_Int intValue){
        addExp.genCode(intValue);
    }
}
