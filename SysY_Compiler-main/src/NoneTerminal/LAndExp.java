package NoneTerminal;

import Tables.*;
import WordAnalyse.*;

import java.util.ArrayList;

public class LAndExp {
    public final ArrayList<EqExp> eqExp_List = new ArrayList<>();
    public static String name_LAndExp = "<LAndExp>";

    public static LAndExp analyse(IdentifySymbol identifySymbol){
        EqExp eqExp = EqExp.analyse(identifySymbol);
        LAndExp lAndExp = new LAndExp();
        lAndExp.eqExp_List.add(eqExp);
        Symbol symbol = identifySymbol.get_CurrentSym();
        while (symbol.getRegKey() == RegKey.AND) {
            identifySymbol.addStr(name_LAndExp);
            identifySymbol.getASymbol();
            eqExp = EqExp.analyse(identifySymbol);
            lAndExp.eqExp_List.add(eqExp);
            symbol = identifySymbol.get_CurrentSym();
        }

        identifySymbol.addStr(name_LAndExp);
        return lAndExp;
    }

    public LAndExp() {
    }

    public void genCode() {
        EqExp eqExp = eqExp_List.get(0);
        eqExp.genCode();
        ArrayList<Integer> short_cuts = new ArrayList<>();
        int shortCut = Code.addCode(CodeType.JMF,-1);
        short_cuts.add(shortCut);  // SHORT_CUT
        int size = eqExp_List.size();
        for(int i = 1; i < size; i++){
            eqExp_List.get(i).genCode();
            Code.addCode(CodeType.AND);
            shortCut = Code.addCode(CodeType.JMF,-1);
            short_cuts.add(shortCut);  // SHORT_CUT
        }
        for (int i = 0;i < short_cuts.size();i++) {
            int addr = short_cuts.get(i);
            Code.modify_Y(addr,Code.get_NextFreeRoom());
        }
    }
    //    public void addEqExp(EqExp eqExp){
//        this.eqExp_List.add(eqExp);
//    }
}
