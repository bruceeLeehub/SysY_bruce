package NoneTerminal;

import Table.*;
import WordAnalyse.*;

import java.util.ArrayList;

public class FuncFParams {
    public static String name_funcFParams = "<FuncFParams>";

    public static boolean analyse(IdentifySymbol identSymbol, ArrayList<TableEntry> paramList, FuncDef funcDef){
        FuncFParam funcFParam = FuncFParam.analyse(identSymbol,paramList);
        funcDef.add_FuncFParam(funcFParam);
        while(identSymbol.get_CurrentSym().getRegKey() == RegKey.COMMA){
            identSymbol.getASymbol();
            FuncFParam funcFParam1 = FuncFParam.analyse(identSymbol,paramList);
            funcDef.add_FuncFParam(funcFParam1);
        }
        identSymbol.addStr(name_funcFParams);
        return true;
    }
}
