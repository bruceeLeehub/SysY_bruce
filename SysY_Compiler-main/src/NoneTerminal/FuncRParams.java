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

    public static boolean analyse(IdentifySymbol identifySymbol, My_Int numOfParamsActually, UnaryExp unaryExp) {
        Symbol sym = identifySymbol.get_CurrentSym();
        int paramSeq = 1;
        Symbol funcSym = identifySymbol.getNearest_PreIdent();
        TableEntry funcEntry = SymTable.get_SymByNameInAllTable(true, funcSym.get_IdentName());
        if (sym.getRegKey() == RegKey.IDENFR || sym.getRegKey() == RegKey.LPARENT ||
                sym.getRegKey() == RegKey.PLUS || sym.getRegKey() == RegKey.MINU ||
                sym.getRegKey() == RegKey.NOT || sym.getRegKey() == RegKey.INTCON) {
            numOfParamsActually.my_Int++;
            TypeCheck = true;
        }else{
            return true;
        }
        unaryExp.addFuncRParam(Exp.analyse(identifySymbol));
        // ERROR -- e: RParams not match
        Error.checkRParamsMatched(funcEntry, tbEntryModel.get(tbEntryModel.size() - 1), funcSym, paramSeq++);
        tbEntryModel.remove(tbEntryModel.size() - 1);
        while (identifySymbol.get_CurrentSym().getRegKey() == RegKey.COMMA) {
            numOfParamsActually.my_Int++;
            TypeCheck = true;
            identifySymbol.getASymbol();
            unaryExp.addFuncRParam(Exp.analyse(identifySymbol));
            // ERROR -- e: RParams not match
            Error.checkRParamsMatched(funcEntry, tbEntryModel.get(tbEntryModel.size() - 1), funcSym, paramSeq++);
            tbEntryModel.remove(tbEntryModel.size() - 1);
        }

        identifySymbol.addStr(name_funcRParams);
        return true;
    }
}
