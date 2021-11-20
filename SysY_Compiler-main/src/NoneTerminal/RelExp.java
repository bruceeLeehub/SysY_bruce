package NoneTerminal;

import Tables.Code;
import Tables.CodeType;
import WordAnalyse.IdentifySymbol;
import WordAnalyse.RegKey;
import WordAnalyse.Symbol;

import java.util.ArrayList;

public class RelExp {
    public static String name = "<RelExp>";

    private ArrayList<AddExp> addExpList;
    private ArrayList<RegKey> opList;

    public RelExp() {
        this.addExpList = new ArrayList<>();
        this.opList = new ArrayList<>();
    }

    public void addAddExp(AddExp addExp) {
        this.addExpList.add(addExp);
    }

    public void addOp(RegKey regKey) {
        this.opList.add(regKey);
    }

    public void genCode() {
        addExpList.get(0).genCode(null);
        for (int i = 1; i < addExpList.size(); i++) {
            addExpList.get(i).genCode(null);
            if (opList.get(i - 1).equals(RegKey.LSS))
                Code.addCode(CodeType.LSS);
            else if (opList.get(i - 1).equals(RegKey.LEQ))
                Code.addCode(CodeType.LEQ);
            else if (opList.get(i - 1).equals(RegKey.GRE))
                Code.addCode(CodeType.GRT);
            else
                Code.addCode(CodeType.GEQ);
        }
    }

    public static RelExp analyse(IdentifySymbol identifySymbol) {
        Symbol sym;
        boolean judge = true;
        RelExp relExp = new RelExp();

        relExp.addAddExp(AddExp.analyse(identifySymbol));
        sym = identifySymbol.getCurSym();
        while (judge && (sym.getRegKey() == RegKey.LSS ||
                sym.getRegKey() == RegKey.LEQ ||
                sym.getRegKey() == RegKey.GRE ||
                sym.getRegKey() == RegKey.GEQ)) {
            if (judge) identifySymbol.addStr(name);
            relExp.addOp(sym.getRegKey());
            identifySymbol.getASymbol();
            relExp.addAddExp(AddExp.analyse(identifySymbol));
            sym = identifySymbol.getCurSym();
        }

        if (judge) identifySymbol.addStr(name);

        return relExp;
    }
}
