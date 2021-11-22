package NoneTerminal;

import ParcelType.*;
import Tables.*;
import WordAnalyse.*;

import java.util.ArrayList;

public class MulExp {
    public ArrayList<UnaryExp> unaryExp_List = new ArrayList<>();
    public ArrayList<RegKey> op_List = new ArrayList<>();
    public static String name_MulExp = "<MulExp>";

    public static boolean isMyFirst(Symbol symbol) {
        return UnaryExp.isMyFirst(symbol);
    }

    public static MulExp analyse(IdentifySymbol identifySymbol) {

        UnaryExp unaryExp = UnaryExp.analyse(identifySymbol);
        MulExp mulExp = new MulExp();
        mulExp.unaryExp_List.add(unaryExp);

        while (identifySymbol.get_CurrentSym().getRegKey() == RegKey.MULT ||
                identifySymbol.get_CurrentSym().getRegKey() == RegKey.DIV ||
                identifySymbol.get_CurrentSym().getRegKey() == RegKey.MOD) {

            identifySymbol.addStr(name_MulExp);

            Symbol curSymbol = identifySymbol.get_CurrentSym();
            RegKey regKey = curSymbol.getRegKey();
            mulExp.op_List.add(regKey);

            identifySymbol.getASymbol();

            unaryExp = UnaryExp.analyse(identifySymbol);
            mulExp.unaryExp_List.add(unaryExp);
        }

        identifySymbol.addStr(name_MulExp);
        return mulExp;
    }

    public void genCode(My_Int value) {
        if (value == null) {
            //get it when running program ,not a const
            UnaryExp unaryExp = unaryExp_List.get(0);
            unaryExp.genCode(null);

            for (int i = 1; i < unaryExp_List.size(); i++) {
                unaryExp = unaryExp_List.get(i);
                unaryExp.genCode(null);

                RegKey op = op_List.get(i - 1);
                boolean isMult = (op.equals(RegKey.MULT));
                boolean isDiv = (op.equals(RegKey.DIV));
                boolean isMod = (op.equals(RegKey.MOD));
                if (isMult) {
                    Code.addCode(CodeType.MUL);
                }
                else if (isDiv) {
                    Code.addCode(CodeType.DIV);
                }
                else if (isMod){
                    Code.addCode(CodeType.MOD);
                }
            }
        }else {
            //calculate it now ,is const
            UnaryExp unaryExp = unaryExp_List.get(0);
            unaryExp.genCode(value);
            My_Int value_1 = new My_Int();
            for (int i = 1; i < unaryExp_List.size(); i++) {
                unaryExp = unaryExp_List.get(i);
                unaryExp.genCode(value_1);

                RegKey op = op_List.get(i - 1);
                boolean isMult = (op.equals(RegKey.MULT));
                boolean isDiv = (op.equals(RegKey.DIV));
                boolean isMod = (op.equals(RegKey.MOD));
                if (isMult) {
                    value.my_Int = value.my_Int * value_1.my_Int;
                }
                else if (isDiv) {
                    value.my_Int = value.my_Int / value_1.my_Int;
                }
                else if (isMod){
                    value.my_Int = value.my_Int % value_1.my_Int;
                }
            }
        }
    }

    public MulExp() {
    }
}
