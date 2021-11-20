package NoneTerminal;

import ParcelType.MyInt;
import Tables.Code;
import Tables.CodeType;
import WordAnalyse.IdentifySymbol;
import WordAnalyse.RegKey;
import WordAnalyse.Symbol;

import java.util.ArrayList;

public class AddExp {
    public static String name = "<AddExp>";

    private ArrayList<MulExp> mulExpList;
    private ArrayList<RegKey> opList;

    public AddExp() {
        this.mulExpList = new ArrayList<>();
        this.opList = new ArrayList<>();
    }

    public void addMulExp(MulExp mulExp) {
        this.mulExpList.add(mulExp);
    }

    public void addOp(RegKey regKey) {
        this.opList.add(regKey);
    }

    public void genCode(MyInt value  ) {
        if (value != null) {      // is const, you need to calculate it right now
            MyInt value_1 = new MyInt();
            mulExpList.get(0).genCode(value);
            for (int i = 1; i < mulExpList.size(); i++) {
                mulExpList.get(i).genCode(value_1);
                if (opList.get(i - 1).equals(RegKey.PLUS)) {
                    value.myInt += value_1.myInt;
                } else {
                    value.myInt -= value_1.myInt;
                }
            }
        } else {          // not a const you need to get it when running program
            mulExpList.get(0).genCode(null);
            for (int i = 1; i < mulExpList.size(); i++) {
                mulExpList.get(i).genCode(null);
                if (opList.get(i - 1).equals(RegKey.PLUS))
                    Code.addCode(CodeType.ADD);
                else Code.addCode(CodeType.SUB);
            }
        }
    }

    public static AddExp analyse(IdentifySymbol identifySymbol) {
        boolean judge = true;
        AddExp addExp = new AddExp();   // ast Tree node
        addExp.addMulExp(MulExp.analyse(identifySymbol));

        while (judge && (identifySymbol.getCurSym().getRegKey() == RegKey.PLUS ||
                identifySymbol.getCurSym().getRegKey() == RegKey.MINU)) {
            if (judge)
                identifySymbol.addStr(name);
            addExp.addOp(identifySymbol.getCurSym().getRegKey());
            identifySymbol.getASymbol();
            addExp.addMulExp(MulExp.analyse(identifySymbol));
        }

        if (judge)
            identifySymbol.addStr(name);

        return addExp;
    }

    public static boolean isMyFirst(Symbol sym) {
        return MulExp.isMyFirst(sym);
    }
}
