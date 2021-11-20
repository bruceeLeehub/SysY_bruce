package NoneTerminal;

import ParcelType.MyInt;
import Tables.Code;
import Tables.CodeType;
import WordAnalyse.IdentifySymbol;
import WordAnalyse.RegKey;
import WordAnalyse.Symbol;

import java.util.ArrayList;

public class MulExp {
    public static String name = "<MulExp>";

    private ArrayList<UnaryExp> unaryExpList;
    private ArrayList<RegKey> opList;

    public MulExp() {
        this.unaryExpList = new ArrayList<>();
        this.opList = new ArrayList<>();
    }

    public void addUnaryExp(UnaryExp unaryExp) {
        this.unaryExpList.add(unaryExp);
    }

    public void addOp(RegKey regKey) {
        this.opList.add(regKey);
    }

    public void genCode(MyInt value) {
        if (value != null) {      // is const, you need to calculate it right now
            MyInt value_1 = new MyInt();
            unaryExpList.get(0).genCode(value);
            for (int i = 1; i < unaryExpList.size(); i++) {
                unaryExpList.get(i).genCode(value_1);
                if (opList.get(i - 1).equals(RegKey.MULT))
                    value.myInt *= value_1.myInt;
                else if (opList.get(i - 1).equals(RegKey.DIV))
                    value.myInt /= value_1.myInt;
                else
                    value.myInt %= value_1.myInt;
            }
        } else {          // not a const you need to get it when running program
            unaryExpList.get(0).genCode(null);
            for (int i = 1; i < unaryExpList.size(); i++) {
                unaryExpList.get(i).genCode(null);
                if (opList.get(i - 1).equals(RegKey.MULT))
                    Code.addCode(CodeType.MUL);
                else if (opList.get(i - 1).equals(RegKey.DIV))
                    Code.addCode(CodeType.DIV);
                else
                    Code.addCode(CodeType.MOD);
            }
        }
    }

    public static MulExp analyse(IdentifySymbol identifySymbol) {
        boolean judge = true;
        MulExp mulExp = new MulExp();
        mulExp.addUnaryExp(UnaryExp.analyse(identifySymbol));

        while (judge && (identifySymbol.getCurSym().getRegKey() == RegKey.MULT ||
                identifySymbol.getCurSym().getRegKey() == RegKey.DIV ||
                identifySymbol.getCurSym().getRegKey() == RegKey.MOD)) {
            if (judge) identifySymbol.addStr(name);
            mulExp.addOp(identifySymbol.getCurSym().getRegKey());
            identifySymbol.getASymbol();
            mulExp.addUnaryExp(UnaryExp.analyse(identifySymbol));
        }


        if (judge) identifySymbol.addStr(name);

        return mulExp;
    }

    public static boolean isMyFirst(Symbol sym) {
        return UnaryExp.isMyFirst(sym);
    }
}
