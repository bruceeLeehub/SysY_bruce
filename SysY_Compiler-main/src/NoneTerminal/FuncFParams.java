package NoneTerminal;

import Table.TableEntry;
import WordAnalyse.IdentifySymbol;
import WordAnalyse.RegKey;
import WordAnalyse.Symbol;

import java.util.ArrayList;

public class FuncFParams {
    public static String name = "<FuncFParams>";

    public static boolean analyse(IdentifySymbol identifySymbol, ArrayList<TableEntry> paramList, FuncDef funcDef){
        Symbol sym;
        boolean judge = true;

        funcDef.addFuncFParam(FuncFParam.analyse(identifySymbol, paramList));
        while(judge && identifySymbol.get_CurrentSym().getRegKey() == RegKey.COMMA){
            identifySymbol.getASymbol();
            funcDef.addFuncFParam(FuncFParam.analyse(identifySymbol, paramList));
        }

        if(judge){
            identifySymbol.addStr(name);
        }
        return judge;
    }
}
