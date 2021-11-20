package NoneTerminal;

import Tables.Code;
import Tables.CodeType;
import WordAnalyse.IdentifySymbol;
import WordAnalyse.RegKey;
import WordAnalyse.Symbol;

import java.util.ArrayList;

public class LOrExp {
    public static String name = "<LOrExp>";

    private ArrayList<LAndExp> lAndExpList;

    public LOrExp() {
        this.lAndExpList = new ArrayList<>();
    }

    public void addLAndExp(LAndExp lAndExp){
        this.lAndExpList.add(lAndExp);
    }

    public void genCode() {
        ArrayList<Integer> short_cuts = new ArrayList<>();
        lAndExpList.get(0).genCode();
        short_cuts.add(Code.addCode(CodeType.JMT, -1));  // short-cut
        for(int i = 1; i < lAndExpList.size(); i++){
            lAndExpList.get(i).genCode();
            Code.addCode(CodeType.ORR);
            short_cuts.add(Code.addCode(CodeType.JMT, -1));  // short-cut
        }
        for(int adr : short_cuts)
            Code.modifyY(adr, Code.getNextFreeRoom());
    }

    public static LOrExp analyse(IdentifySymbol identifySymbol){
        Symbol sym;
        boolean judge = true;
        LOrExp lOrExp = new LOrExp();

        lOrExp.addLAndExp(LAndExp.analyse(identifySymbol));
        sym = identifySymbol.getCurSym();
        while (judge && sym.getRegKey() == RegKey.OR) {
            if (judge) identifySymbol.addStr(name);
            identifySymbol.getASymbol();
            lOrExp.addLAndExp(LAndExp.analyse(identifySymbol));
            sym = identifySymbol.getCurSym();
        }

        if (judge) identifySymbol.addStr(name);
        return lOrExp;
    }

}
