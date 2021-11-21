package NoneTerminal;

import WordAnalyse.IdentifySymbol;
import WordAnalyse.RegKey;
import WordAnalyse.Symbol;

import java.util.ArrayList;

public class InitVal {
    public static String name = "<InitVal>";

    private ArrayList<Exp> expList;

    public InitVal(){
        this.expList = new ArrayList<>();
    }

    public void addExp(Exp exp){
        this.expList.add(exp);
    }

    public void genCode(){
        for(Exp exp : expList){
            exp.genCode(null);
        }
    }

    public static InitVal analyse(IdentifySymbol identifySymbol, InitVal initVal) {
        Symbol sym;
        boolean judge = true;
        if(initVal == null)
            initVal = new InitVal();

        sym = identifySymbol.get_CurrentSym();
        if (sym.getRegKey() == RegKey.LBRACE) {
            identifySymbol.getASymbol();
            if (identifySymbol.get_CurrentSym().getRegKey() != RegKey.RBRACE) {
                InitVal.analyse(identifySymbol, initVal);
                while (judge && identifySymbol.get_CurrentSym().getRegKey() == RegKey.COMMA) {
                    identifySymbol.getASymbol();
                    InitVal.analyse(identifySymbol, initVal);
                }
            }
            if(judge && identifySymbol.get_CurrentSym().getRegKey() == RegKey.RBRACE){
                judge = true;
                identifySymbol.getASymbol();
            }else{
                judge = false;
            }
        }else{
            initVal.addExp(Exp.analyse(identifySymbol));
        }

        if(judge){
            identifySymbol.addStr(name);
        }
        return initVal;
    }

    public static boolean isMyFirst(Symbol sym){
        return sym.getRegKey() == RegKey.LBRACE || Exp.isMyFirst(sym);
    }
}
