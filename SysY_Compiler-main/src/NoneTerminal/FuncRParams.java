package NoneTerminal;

import MyError.Error;
import ParcelType.*;
import Table.*;
import WordAnalyse.*;

import java.util.ArrayList;

public class FuncRParams {
    public static ArrayList<TableEntry> tbEntryModel = new ArrayList<>();
    public static String name_funcRParams = "<FuncRParams>";
    public static boolean TypeCheck = false;

    public static boolean analyse(IdentifySymbol identifySymbol, My_Int actualParamsNum, UnaryExp unaryExp) {
        Symbol symbol = identifySymbol.get_CurrentSym();
        RegKey regKey = symbol.getRegKey();
        boolean rightRegKey = (regKey == RegKey.NOT || regKey == RegKey.INTCON ||
                regKey == RegKey.IDENFR || regKey == RegKey.LPARENT ||
                regKey == RegKey.PLUS || regKey == RegKey.MINU);
        Symbol funcSym = identifySymbol.getNearest_PreIdent();
        String name = funcSym.get_IdentName();
        TableEntry funcEntry = SymTable.get_SymByNameInAllTable(true, name);
        if (!rightRegKey) {
            return true;
        }else{
            actualParamsNum.my_Int = actualParamsNum.my_Int + 1;
            TypeCheck = true;
        }
        int paramSeq = 1;
        Exp exp = Exp.analyse(identifySymbol);
        unaryExp.add_FuncRParam(exp);
        // e: rparams do not match
        int size = tbEntryModel.size();
        TableEntry actualTE = tbEntryModel.get(size - 1);
        Error.checkRParamsMatched(funcEntry, actualTE, funcSym, paramSeq);
        paramSeq = paramSeq + 1;
        tbEntryModel.remove(size - 1);
        while (identifySymbol.get_CurrentSym().getRegKey() == RegKey.COMMA) {
            actualParamsNum.my_Int = actualParamsNum.my_Int + 1;
            TypeCheck = true;
            identifySymbol.getASymbol();
            Exp exp1 = Exp.analyse(identifySymbol);
            unaryExp.add_FuncRParam(exp1);
            // e: rparams do not match
            size = tbEntryModel.size();
            actualTE = tbEntryModel.get(size - 1);
            Error.checkRParamsMatched(funcEntry, actualTE, funcSym, paramSeq);
            paramSeq = paramSeq + 1;
            tbEntryModel.remove(size - 1);
        }

        identifySymbol.addStr(name_funcRParams);
        return true;
    }
}
