package NoneTerminal;

import ParcelType.*;
import Table.*;
import Tables.Code;
import Tables.CodeType;
import WordAnalyse.*;

public class Number {
    public int number;
    public static String name_number = "<Number>";

    public static Number analyse(IdentifySymbol identifySymbol) {
        Symbol symbol = identifySymbol.get_CurrentSym();
        String int_string = symbol.get_IdentName();
        int intValue  = Integer.parseInt(int_string);
        Number number = new Number(intValue);

        symbol = identifySymbol.get_CurrentSym();
        RegKey regKey = symbol.getRegKey();


        if (regKey == RegKey.INTCON) {
            identifySymbol.getASymbol();
            identifySymbol.addStr(name_number);
        }
        // check rparams type
        if (FuncRParams.TypeCheck) {
            TableEntry tableEntry = new TableEntry(true,DataType.INT_DATA,0);
            FuncRParams.tbEntryModel.add(tableEntry);
            if (LVal.in_Dims == 0) {
                FuncRParams.TypeCheck = false;
            }
        }
        return number;
    }

    public void genCode(My_Int value) {
        if (value == null) {
            Code.addCode(CodeType.LDC, this.number);
        }
        else {
            value.my_Int = this.number;
        }
    }

    public Number(int number) {
        this.number = number;
    }
}
