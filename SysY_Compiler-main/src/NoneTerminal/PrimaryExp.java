package NoneTerminal;

import MyError.Error;
import ParcelType.MyInt;
import WordAnalyse.IdentifySymbol;
import WordAnalyse.RegKey;
import WordAnalyse.Symbol;

public class PrimaryExp {
    public static String name = "<PrimaryExp>";

    // choose one from three
    private Exp exp;
    private LVal lVal;
    private Number number;

    private PrimaryExp() {
        this.exp = null;
        this.lVal = null;
        this.number = null;
    }

    public void setExp(Exp exp) {
        this.exp = exp;
    }

    public void setlVal(LVal lVal) {
        this.lVal = lVal;
    }

    public void setNumber(Number number) {
        this.number = number;
    }

    public static boolean isMyFirst(Symbol sym) {
        if (sym.getRegKey() == RegKey.LPARENT ||
                sym.getRegKey() == RegKey.INTCON) {
            return true;
        }
        return false;
    }

    public void genCode(MyInt value) {
        if (exp != null)
            exp.genCode(value);
        else if (lVal != null)
            lVal.genCode(value, false);
        else
            number.genCode(value);
    }

    public static PrimaryExp analyse(IdentifySymbol identifySymbol) {
        Symbol sym;
        boolean judge = true;
        PrimaryExp primaryExp = new PrimaryExp();

        if (identifySymbol.getCurSym().getRegKey() == RegKey.LPARENT) {   // '(' Exp ')'
            identifySymbol.getASymbol();
            primaryExp.setExp(Exp.analyse(identifySymbol));
            if (judge) {
                if (identifySymbol.getCurSym().getRegKey() != RegKey.RPARENT)
                    Error.addErrorOutPut(identifySymbol.getPreSym().getRowIdx() + " j");
                else
                    identifySymbol.getASymbol();
            }
        } else {
            if (identifySymbol.getCurSym().getRegKey() == RegKey.IDENFR) {  // LVal --> Ident { '[' Exp ']' }
                primaryExp.setlVal(LVal.analyse(identifySymbol));
            } else {  // Number
                primaryExp.setNumber(Number.analyse(identifySymbol));
            }
        }
        if (judge)
            identifySymbol.addStr(name);
        return primaryExp;
    }
}
