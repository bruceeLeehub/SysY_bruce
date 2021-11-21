package NoneTerminal;

import ParcelType.*;
import Tables.*;
import WordAnalyse.*;

import java.util.ArrayList;

public class AddExp {
    private final ArrayList<MulExp> mulExpList;
    private final ArrayList<RegKey> operatorList;
    public static String name_addExp = "<AddExp>";

    public static boolean isMyFirst(Symbol sym) {
        return MulExp.isMyFirst(sym);
    }

    public static AddExp analyse(IdentifySymbol identSymbol) {
        AddExp addExp = new AddExp();   // ast Tree node
        //boolean judge = true;
        MulExp mulExp = MulExp.analyse(identSymbol);
        addExp.add_MulExp(mulExp);

        while ((identSymbol.get_CurrentSym().getRegKey() == RegKey.PLUS ||
                identSymbol.get_CurrentSym().getRegKey() == RegKey.MINU)) {
            identSymbol.addStr(name_addExp);
            Symbol symbol = identSymbol.get_CurrentSym();
            RegKey regKey = symbol.getRegKey();

            addExp.add_Operator(regKey);
            identSymbol.getASymbol();
            MulExp mulExp1 = MulExp.analyse(identSymbol);
            addExp.add_MulExp(mulExp1);
        }
//        if (judge)
//            identSymbol.addStr(name_addExp);
        identSymbol.addStr(name_addExp);
        return addExp;
    }

    public void add_MulExp(MulExp mul_Exp) {
        mulExpList.add(mul_Exp);
    }

    public void genCode(My_Int value_int) {
        if (value_int == null) {
            // get it when running program,not a const
            //mulExpList.get(0).genCode(null);
            MulExp mulExp = mulExpList.get(0);
            mulExp.genCode(null);
            for (int i = 1; i < mulExpList.size(); i++) {
                MulExp mulExp1 = mulExpList.get(i);
                mulExp1.genCode(null);
                //mulExpList.get(i).genCode(null);
                RegKey regKey = operatorList.get(i - 1);
                boolean isPlus = (regKey.equals(RegKey.PLUS));
                if (!isPlus) {
                    Code.addCode(CodeType.SUB);
                }
                else {
                    Code.addCode(CodeType.ADD);
                }
            }
        } else {          // calculate it right now,is const
            My_Int value_int_1 = new My_Int();
            //mulExpList.get(0).genCode(value_int);
            MulExp mulExp = mulExpList.get(0);
            mulExp.genCode(value_int);
            for (int i = 1; i < mulExpList.size(); i++) {
                MulExp mulExp1 = mulExpList.get(i);
                mulExp1.genCode(value_int_1);
                //mulExpList.get(i).genCode(value_int_1);
                RegKey regKey = operatorList.get(i - 1);
                boolean isPlus = regKey.equals(RegKey.PLUS);
                if (!isPlus) {
                    value_int.my_Int = value_int.my_Int - value_int_1.my_Int;
                } else {
                    value_int.my_Int = value_int.my_Int + value_int_1.my_Int;
                }
            }
        }
    }

    public void add_Operator(RegKey regKey) {
        operatorList.add(regKey);
    }

    public AddExp() {
        this.operatorList = new ArrayList<>();
        this.mulExpList = new ArrayList<>();
    }
}
