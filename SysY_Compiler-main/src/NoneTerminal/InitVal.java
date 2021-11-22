package NoneTerminal;

import WordAnalyse.*;

import java.util.ArrayList;

public class InitVal {
    public final ArrayList<Exp> expList = new ArrayList<>();
    public static String name_initVal = "<InitVal>";

    public static boolean isMyFirst(Symbol sym){
        RegKey regKey = sym.getRegKey();
        boolean isLBRACE = regKey == RegKey.LBRACE;
        boolean exp_isMyFirst = Exp.isMyFirst(sym);
        return isLBRACE || exp_isMyFirst;
    }

    public static InitVal analyse(IdentifySymbol identifySymbol, InitVal initVal) {
        if(initVal == null) {
            initVal = new InitVal();
        }
        Symbol symbol = identifySymbol.get_CurrentSym();
        RegKey regKey = symbol.getRegKey();
        boolean isLBRACE = (regKey == RegKey.LBRACE);
        boolean judge = true;
        if (isLBRACE) {
            identifySymbol.getASymbol();
            Symbol curSymbol = identifySymbol.get_CurrentSym();
            regKey = curSymbol.getRegKey();
            boolean isRBRACE = regKey == RegKey.RBRACE;
            if (!isRBRACE) {
                InitVal.analyse(identifySymbol, initVal);
                while (identifySymbol.get_CurrentSym().getRegKey() == RegKey.COMMA) {
                    identifySymbol.getASymbol();
                    InitVal.analyse(identifySymbol, initVal);
                }
            }
            curSymbol = identifySymbol.get_CurrentSym();
            regKey = curSymbol.getRegKey();
            isRBRACE = (regKey == RegKey.RBRACE);
            if(!isRBRACE){
                judge = false;
            }else{
                identifySymbol.getASymbol();
                judge = true;
            }
        }else{
            Exp exp = Exp.analyse(identifySymbol);
            initVal.expList.add(exp);
        }

        if(judge){
            identifySymbol.addStr(name_initVal);
        }
        return initVal;
    }

    public InitVal(){
    }

    public void genCode(){
        for(Exp exp : this.expList){
            exp.genCode(null);
        }
    }
}
