package NoneTerminal;

import Tables.Code;
import Tables.CodeType;
import WordAnalyse.IdentifySymbol;
import WordAnalyse.RegKey;
import WordAnalyse.Symbol;

import java.util.ArrayList;

public class EqExp {
    public static String name = "<EqExp>";

    private ArrayList<RelExp> relExpList;
    private ArrayList<RegKey> opList;

    public EqExp() {
        this.relExpList = new ArrayList<>();
        this.opList = new ArrayList<>();
    }

    public void addRelExp(RelExp relExp){
        this.relExpList.add(relExp);
    }

    public void addOpList(RegKey regKey){
        this.opList.add(regKey);
    }

    public void genCode() {
        relExpList.get(0).genCode();
        for(int i = 1; i < relExpList.size(); i++){
            relExpList.get(i).genCode();
            if(opList.get(i - 1).equals(RegKey.EQL))
                Code.addCode(CodeType.EQL);
            else
                Code.addCode(CodeType.NEQ);
        }
    }

    public static EqExp analyse(IdentifySymbol identifySymbol){
        Symbol sym;
        boolean judge = true;
        EqExp eqExp = new EqExp();

        eqExp.addRelExp(RelExp.analyse(identifySymbol));
        sym = identifySymbol.getCurSym();
        while (judge && (sym.getRegKey() == RegKey.EQL || sym.getRegKey() == RegKey.NEQ)) {
            if (judge) identifySymbol.addStr(name);
            eqExp.addOpList(sym.getRegKey());
            identifySymbol.getASymbol();
            eqExp.addRelExp(RelExp.analyse(identifySymbol));
            sym = identifySymbol.getCurSym();
        }

        if (judge) identifySymbol.addStr(name);
        return eqExp;
    }
}
