package NoneTerminal;

import MyError.Error;
import ParcelType.*;
import WordAnalyse.*;

public class PrimaryExp {
    public LVal lVal = null;
    public Number number = null;
    public Exp exp = null;
    //three conditions
    public static String name_primaryExp = "<PrimaryExp>";

    public static boolean isMyFirst(Symbol symbol) {
        RegKey regKey = symbol.getRegKey();
        boolean isLPARENT = regKey == RegKey.LPARENT;
        boolean isINTCON = regKey == RegKey.INTCON;
        return isLPARENT || isINTCON;
    }

    public static PrimaryExp analyse(IdentifySymbol identifySymbol) {
        Symbol curSymbol = identifySymbol.get_CurrentSym();
        RegKey regKey = curSymbol.getRegKey();
        boolean isLPARENT = (regKey == RegKey.LPARENT);

        PrimaryExp primaryExp = new PrimaryExp();
        if (isLPARENT) {   // '(' Exp ')'
            identifySymbol.getASymbol();
            Exp exp = Exp.analyse(identifySymbol);
            primaryExp.exp = exp;

            curSymbol = identifySymbol.get_CurrentSym();
            regKey = curSymbol.getRegKey();
            boolean isRPARENT = regKey == RegKey.RPARENT;
            if (isRPARENT) {
                identifySymbol.getASymbol();
            }
            else {
                Symbol preSymbol = identifySymbol.get_PreSym();
                int rowidx = preSymbol.getRow_Idx();
                Error.add_ErrorOutPut(rowidx + " j");
            }
        } else {
            curSymbol = identifySymbol.get_CurrentSym();
            regKey = curSymbol.getRegKey();
            boolean isIDENFR = regKey == RegKey.IDENFR;
            if (!isIDENFR) {
                // number
                primaryExp.number = (Number.analyse(identifySymbol));
            } else {  //Ident { '[' Exp ']' }
                primaryExp.lVal = (LVal.analyse(identifySymbol));
            }
        }
        identifySymbol.addStr(name_primaryExp);
        return primaryExp;
    }

    public void genCode(My_Int value) {
        if (lVal != null) {
            lVal.genCode(value, false);
        }
        else if (exp != null) {
            exp.genCode(value);
        }
        else {
            number.genCode(value);
        }
    }

    private PrimaryExp() {
    }

}
