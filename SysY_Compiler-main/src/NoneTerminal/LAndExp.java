package NoneTerminal;

import Tables.Code;
import Tables.CodeType;
import WordAnalyse.IdentifySymbol;
import WordAnalyse.RegKey;
import WordAnalyse.Symbol;

import java.util.ArrayList;

public class LAndExp {
    public static String name = "<LAndExp>";

    private ArrayList<EqExp> eqExpList;

    public LAndExp() {
        this.eqExpList = new ArrayList<>();
    }

    public void addEqExp(EqExp eqExp){
        this.eqExpList.add(eqExp);
    }

    public void genCode() {
        ArrayList<Integer> short_cuts = new ArrayList<>();
        eqExpList.get(0).genCode();
        short_cuts.add(Code.addCode(CodeType.JMF, -1));  // short-cut
        for(int i = 1; i<eqExpList.size(); i++){
            eqExpList.get(i).genCode();
            Code.addCode(CodeType.AND);
            short_cuts.add(Code.addCode(CodeType.JMF, -1));  // short-cut
        }
        for(int adr : short_cuts)
            Code.modifyY(adr, Code.getNextFreeRoom());
    }

    public static LAndExp analyse(IdentifySymbol identifySymbol){
        Symbol sym;
        boolean judge = true;
        LAndExp lAndExp = new LAndExp();

        lAndExp.addEqExp(EqExp.analyse(identifySymbol));
        sym = identifySymbol.getCurSym();
        while (judge && sym.getRegKey() == RegKey.AND) {
            if (judge) identifySymbol.addStr(name);
            identifySymbol.getASymbol();
            lAndExp.addEqExp(EqExp.analyse(identifySymbol));
            sym = identifySymbol.getCurSym();
        }

        if (judge) identifySymbol.addStr(name);
        return lAndExp;
    }
}
