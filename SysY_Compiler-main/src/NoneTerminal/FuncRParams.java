package NoneTerminal;

import MyError.Error;
import ParcelType.My_Int;
import Table.SymTable;
import Table.TableEntry;
import WordAnalyse.IdentifySymbol;
import WordAnalyse.RegKey;
import WordAnalyse.Symbol;

import java.util.ArrayList;

public class FuncRParams {
    public static String name = "<FuncRParams>";
    public static boolean checkingType = false;
    public static ArrayList<TableEntry> tbEntryModel = new ArrayList<>();

    public static boolean analyse(IdentifySymbol identifySymbol, My_Int numOfParamsActually, UnaryExp unaryExp) {
        Symbol sym = identifySymbol.get_CurrentSym();
        boolean judge = true;
        int paramSeq = 1;
        Symbol funcSym = identifySymbol.getNearest_PreIdent();
        TableEntry funcEntry = SymTable.get_SymByNameInAllTable(true, funcSym.get_IdentName());
        if (sym.getRegKey() == RegKey.IDENFR || sym.getRegKey() == RegKey.LPARENT ||
                sym.getRegKey() == RegKey.PLUS || sym.getRegKey() == RegKey.MINU ||
                sym.getRegKey() == RegKey.NOT || sym.getRegKey() == RegKey.INTCON) {
            numOfParamsActually.my_Int++;
            checkingType = true;
        }else{
            return true;
        }
        unaryExp.addFuncRParam(Exp.analyse(identifySymbol));
        // ERROR -- e: RParams not match
        Error.checkRParamsMatched(funcEntry, tbEntryModel.get(tbEntryModel.size() - 1), funcSym, paramSeq++);
        tbEntryModel.remove(tbEntryModel.size() - 1);
        while (judge && identifySymbol.get_CurrentSym().getRegKey() == RegKey.COMMA) {
            numOfParamsActually.my_Int++;
            checkingType = true;
            identifySymbol.getASymbol();
            unaryExp.addFuncRParam(Exp.analyse(identifySymbol));
            // ERROR -- e: RParams not match
            Error.checkRParamsMatched(funcEntry, tbEntryModel.get(tbEntryModel.size() - 1), funcSym, paramSeq++);
            tbEntryModel.remove(tbEntryModel.size() - 1);
        }

        if (judge) {
            identifySymbol.addStr(name);
        }


        return judge;
    }
}
