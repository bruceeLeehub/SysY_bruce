package NoneTerminal;

import ParcelType.*;
import WordAnalyse.*;

import java.util.ArrayList;

public class ConstInitVal {
    public static String name_constInitVal = "<ConstInitVal>";

    private final ArrayList<ConstExp> constExpList = new ArrayList<>();

    public static boolean isMyFirst(Symbol symbol) {
        RegKey regKey = symbol.getRegKey();
        boolean isLbrace = (regKey == RegKey.LBRACE);
        boolean ConstExp_isMyFirst = ConstExp.isMyFirst(symbol);
        return isLbrace || ConstExp_isMyFirst;
    }

    public ConstInitVal() {
    }

    public static ConstInitVal analyse(IdentifySymbol identSymbol, ConstInitVal constInitVal) {
        if(constInitVal == null) {
            constInitVal = new ConstInitVal();
        }
        boolean judge = true;
        Symbol sym = identSymbol.get_CurrentSym();
        RegKey regKey = sym.getRegKey();
        boolean isLBRACE = (regKey == RegKey.LBRACE);
        if (isLBRACE) {
            identSymbol.getASymbol();
            Symbol curSymbol = identSymbol.get_CurrentSym();
            RegKey curRegKey = curSymbol.getRegKey();
            boolean isRBRACE = (curRegKey == RegKey.RBRACE);
            if (!isRBRACE) {
                ConstInitVal.analyse(identSymbol, constInitVal);
                while (identSymbol.get_CurrentSym().getRegKey() == RegKey.COMMA) {
                    identSymbol.getASymbol();
                    ConstInitVal.analyse(identSymbol, constInitVal);
                }
            }
            curSymbol = identSymbol.get_CurrentSym();
            curRegKey = curSymbol.getRegKey();
            isRBRACE = (curRegKey == RegKey.RBRACE);
            if (isRBRACE) {
                identSymbol.getASymbol();
            }
            judge = isRBRACE;
        } else {
            ConstExp constExp = ConstExp.analyse(identSymbol);
            constInitVal.addConstExp(constExp);
        }
        if (judge) {
            identSymbol.addStr(name_constInitVal);
        }
        return constInitVal;
    }

    public void addConstExp(ConstExp constExp) {
        constExpList.add(constExp);
    }

    public void genCode(ArrayList<Integer> constIniValue_List){
        My_Int intValue = new My_Int();
        for (ConstExp constExp : this.constExpList) {
            if(constIniValue_List != null) {
                constExp.genCode(intValue);
                int value = intValue.my_Int;
                constIniValue_List.add(value);
            }
        }
    }
}
