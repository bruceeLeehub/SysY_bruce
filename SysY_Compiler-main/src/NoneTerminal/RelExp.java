package NoneTerminal;

import Tables.*;
import WordAnalyse.*;
import java.util.ArrayList;

public class RelExp {
    public ArrayList<AddExp> addExp_List = new ArrayList<>();
    public ArrayList<RegKey> op_List = new ArrayList<>();
    public static String name_RelExp = "<RelExp>";

    public static RelExp analyse(IdentifySymbol identifySymbol) {
        RelExp relExp = new RelExp();
        AddExp addExp = AddExp.analyse(identifySymbol);
        relExp.addExp_List.add(addExp);

        Symbol symbol = identifySymbol.get_CurrentSym();
        while (symbol.getRegKey() == RegKey.LSS ||
                symbol.getRegKey() == RegKey.LEQ ||
                symbol.getRegKey() == RegKey.GRE ||
                symbol.getRegKey() == RegKey.GEQ) {
            identifySymbol.addStr(name_RelExp);

            RegKey regKey = symbol.getRegKey();
            relExp.op_List.add(regKey);

            identifySymbol.getASymbol();

            AddExp addExp1 = AddExp.analyse(identifySymbol);
            relExp.addExp_List.add(addExp1);

            symbol = identifySymbol.get_CurrentSym();
        }
        identifySymbol.addStr(name_RelExp);
        return relExp;
    }

    public void genCode() {
        AddExp addExp = addExp_List.get(0);
        addExp.genCode(null);

        for (int i = 1; i < addExp_List.size(); i++) {
            addExp = addExp_List.get(i);
            addExp.genCode(null);

            RegKey regKey = op_List.get(i - 1);
            boolean isLSS = (regKey.equals(RegKey.LSS));
            boolean isLEQ = (regKey.equals(RegKey.LEQ));
            boolean isGRE = (regKey.equals(RegKey.GRE));
            if (isLSS) {
                Code.addCode(CodeType.LSS);
            }
            else if (isLEQ) {
                Code.addCode(CodeType.LEQ);
            }
            else if (isGRE) {
                Code.addCode(CodeType.GRT);
            }
            else {
                Code.addCode(CodeType.GEQ);
            }
        }
    }

    public RelExp() {
    }
}
