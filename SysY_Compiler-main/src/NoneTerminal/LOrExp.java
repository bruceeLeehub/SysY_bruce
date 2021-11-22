package NoneTerminal;

import Tables.*;
import WordAnalyse.*;

import java.util.ArrayList;

public class LOrExp {
    public ArrayList<LAndExp> lAndExpList = new ArrayList<>();
    public static String name_LOrExp = "<LOrExp>";

//    public void addLAndExp(LAndExp lAndExp){
//        this.lAndExpList.add(lAndExp);
//    }

    public static LOrExp analyse(IdentifySymbol identifySymbol){
        LOrExp lOrExp = new LOrExp();
        LAndExp lAndExp = LAndExp.analyse(identifySymbol);
        lOrExp.lAndExpList.add(lAndExp);
        Symbol sym = identifySymbol.get_CurrentSym();
        while (sym.getRegKey() == RegKey.OR) {
            identifySymbol.addStr(name_LOrExp);
            identifySymbol.getASymbol();
            lAndExp = LAndExp.analyse(identifySymbol);
            lOrExp.lAndExpList.add(lAndExp);
            sym = identifySymbol.get_CurrentSym();
        }
        identifySymbol.addStr(name_LOrExp);
        return lOrExp;
    }

    public LOrExp() {
    }

    public void genCode() {
        LAndExp lAndExp = lAndExpList.get(0);
        lAndExp.genCode();
        ArrayList<Integer> short_cuts = new ArrayList<>();
        int shortCut = Code.addCode(CodeType.JMT,-1);
        short_cuts.add(shortCut);  // short-cut
        int size = lAndExpList.size();
        for(int i = 1; i < size; i++){
            lAndExpList.get(i).genCode();
            Code.addCode(CodeType.ORR);
            shortCut = Code.addCode(CodeType.JMT,-1);
            short_cuts.add(shortCut);  // short-cut
        }
        for (int i = 0;i < short_cuts.size();i++) {
            int addr = short_cuts.get(i);
            Code.modify_Y(addr,Code.get_NextFreeRoom());
        }
    }

}
