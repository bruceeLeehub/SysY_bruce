package NoneTerminal;

import ParcelType.My_Int;
import Table.*;
import Tables.Code;
import Tables.CodeType;
import WordAnalyse.IdentifySymbol;
import WordAnalyse.RegKey;

public class Number {
    public static String name = "<Number>";

    private int number;

    public Number(int number) {
        this.number = number;
    }

    public void genCode(My_Int value) {
        if (value != null)
            value.my_Int = number;
        else
            Code.addCode(CodeType.LDC, number);
    }

    public static Number analyse(IdentifySymbol identifySymbol) {
        boolean judge = true;
        Number number = new Number(Integer.valueOf(identifySymbol.get_CurrentSym().get_IdentName()));
        judge &= identifySymbol.get_CurrentSym().getRegKey() == RegKey.INTCON;

        if (judge) {
            identifySymbol.getASymbol();
            identifySymbol.addStr(name);
        }
        // checking RParams type
        if (FuncRParams.TypeCheck) {
            FuncRParams.tbEntryModel.add(new TableEntry(true, DataType.INT_DATA, 0));
            if (LVal.in_Dims == 0)
                FuncRParams.TypeCheck = false;
        }
        return number;
    }
}
