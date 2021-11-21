package NoneTerminal;

import Tables.*;
import WordAnalyse.*;

import java.util.ArrayList;

public class EqExp {
    public static String name_eqExp = "<EqExp>";

    private final ArrayList<RelExp> relExp_List = new ArrayList<>();
    private final ArrayList<RegKey> operator_List = new ArrayList<>();

    public void addOpList(RegKey regKey){
        operator_List.add(regKey);
    }

    public void genCode() {
        relExp_List.get(0).genCode();
        for(int i = 1; i < relExp_List.size(); i++){
            relExp_List.get(i).genCode();
            if(operator_List.get(i - 1).equals(RegKey.EQL))
                Code.addCode(CodeType.EQL);
            else
                Code.addCode(CodeType.NEQ);
        }
    }

    public static EqExp analyse(IdentifySymbol identSymbol){
        EqExp eqExp = new EqExp();
        RelExp relExp = RelExp.analyse(identSymbol);
        eqExp.addRelExp(relExp);
        Symbol symbol = identSymbol.get_CurrentSym();
        while (symbol.getRegKey() == RegKey.EQL || symbol.getRegKey() == RegKey.NEQ) {
            identSymbol.addStr(name_eqExp);

            RegKey regKey = symbol.getRegKey();
            eqExp.addOpList(regKey);

            identSymbol.getASymbol();

            RelExp relExp1 = RelExp.analyse(identSymbol);
            eqExp.addRelExp(relExp1);

            symbol = identSymbol.get_CurrentSym();
        }
        identSymbol.addStr(name_eqExp);
        return eqExp;
    }

    public void addRelExp(RelExp relExp){
        relExp_List.add(relExp);
    }

    public EqExp() {
    }
}
