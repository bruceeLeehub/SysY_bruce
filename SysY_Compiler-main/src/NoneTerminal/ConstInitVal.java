package NoneTerminal;

import ParcelType.My_Int;
import WordAnalyse.IdentifySymbol;
import WordAnalyse.RegKey;
import WordAnalyse.Symbol;

import java.util.ArrayList;

public class ConstInitVal {
    public static String name = "<ConstInitVal>";

    private ArrayList<ConstExp> constExpList;

    public ConstInitVal() {
        this.constExpList = new ArrayList<>();
    }

    public void addConstExp(ConstExp constExp) {
        this.constExpList.add(constExp);
    }

    public void genCode(ArrayList<Integer> constIniValue){
        if(constIniValue != null) {
            My_Int value = new My_Int();
            for (ConstExp constExp : constExpList) {
                constExp.genCode(value);
                constIniValue.add(value.my_Int);
            }
        }
    }

    public static ConstInitVal analyse(IdentifySymbol identifySymbol, ConstInitVal constInitVal) {
        Symbol sym;
        boolean judge = true;
        if(constInitVal == null)
            constInitVal = new ConstInitVal();

        sym = identifySymbol.getCurSym();
        if (sym.getRegKey() == RegKey.LBRACE) {
            identifySymbol.getASymbol();
            if (identifySymbol.getCurSym().getRegKey() != RegKey.RBRACE) {
                ConstInitVal.analyse(identifySymbol, constInitVal);
                while (judge && identifySymbol.getCurSym().getRegKey() == RegKey.COMMA) {
                    identifySymbol.getASymbol();
                    ConstInitVal.analyse(identifySymbol, constInitVal);
                }
            }
            if (judge && identifySymbol.getCurSym().getRegKey() == RegKey.RBRACE) {
                judge = true;
                identifySymbol.getASymbol();
            } else {
                judge = false;
            }
        } else {
            constInitVal.addConstExp(ConstExp.analyse(identifySymbol));
        }

        if (judge) {
            identifySymbol.addStr(name);
        }
        return constInitVal;
    }

    public static boolean isMyFirst(Symbol sym) {
        if (sym.getRegKey() == RegKey.LBRACE || ConstExp.isMyFirst(sym)) {
            return true;
        }
        return false;
    }
}
