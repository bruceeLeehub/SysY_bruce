package NoneTerminal;

import MyError.Error;
import WordAnalyse.*;

public class MainFuncDef {
    public static String name_mainFuncDef = "<MainFuncDef>";
    public Block block = null;
    public static boolean CheckingMain = false;

    public static MainFuncDef analyse(IdentifySymbol identifySymbol){
        MainFuncDef mainFuncDef = new MainFuncDef();

        Symbol symbol = identifySymbol.get_CurrentSym();
        RegKey regKey = symbol.getRegKey();
        boolean checkToken = regKey == RegKey.INTTK;
        if(checkToken){
            symbol = identifySymbol.getASymbol();
            regKey = symbol.getRegKey();
            checkToken = regKey == RegKey.MAINTK;
        }
        if(checkToken){
            symbol = identifySymbol.getASymbol();
            regKey = symbol.getRegKey();
            checkToken = regKey == RegKey.LPARENT;
        }
        if(checkToken){
            symbol = identifySymbol.getASymbol();
            //j: ')' needed
            symbol = identifySymbol.get_CurrentSym();
            regKey = symbol.getRegKey();
            if (regKey != RegKey.RPARENT) {
                Symbol preSymbol = identifySymbol.get_PreSym();
                int rowIdx = preSymbol.getRow_Idx();
                Error.add_ErrorOutPut(rowIdx + " j");
            }
            else {
                identifySymbol.getASymbol();
            }
        }
        if(checkToken){
            CheckingMain = true;
            mainFuncDef.block = (Block.analyse(identifySymbol));
            // g: no return stmt in the end
            if (!Block.hasReturnStmt) {
                symbol = identifySymbol.get_CurrentSym();
                int rowidx = symbol.getRow_Idx();
                Error.add_ErrorOutPut(rowidx + " g");
            }


            identifySymbol.addStr(name_mainFuncDef);
        }
        return mainFuncDef;
    }
    public void genCode(){
        String name = "main";
        block.genCode(name, null);
    }
    public MainFuncDef(){
    }
}
