package NoneTerminal;

import MyError.Error;
import WordAnalyse.IdentifySymbol;
import WordAnalyse.RegKey;
import WordAnalyse.Symbol;

public class MainFuncDef {
    public static String name = "<MainFuncDef>";
    public static boolean mainIsChecking = false;

    private Block block;

    public MainFuncDef(){
        this.block =  null;
    }

    public void setBlock(Block block){
        this.block = block;
    }

    public void genCode(){
        block.genCode("main", null);
    }

    public static MainFuncDef analyse(IdentifySymbol identifySymbol){
        Symbol sym;
        boolean judge = true;
        MainFuncDef mainFuncDef = new MainFuncDef();

        sym = identifySymbol.getCurSym();
        judge &= sym.getRegKey() == RegKey.INTTK;
        if(judge){
            sym = identifySymbol.getASymbol();
            judge &= sym.getRegKey() == RegKey.MAINTK;
        }
        if(judge){
            sym = identifySymbol.getASymbol();
            judge &= sym.getRegKey() == RegKey.LPARENT;
        }
        if(judge){
            sym = identifySymbol.getASymbol();
            // ERROR -- j: ')' needed
            if (identifySymbol.getCurSym().getRegKey() != RegKey.RPARENT)
                Error.addErrorOutPut(identifySymbol.getPreSym().getRow_Idx() + " j");
            else
                identifySymbol.getASymbol();
        }
        if(judge){
            mainIsChecking = true;
            mainFuncDef.setBlock(Block.analyse(identifySymbol));
            // ERROR -- g: func have return value don't have return stmt in the end
            if (Block.haveRetStmt == false)
                Error.addErrorOutPut(identifySymbol.getCurSym().getRow_Idx() + " g");
        }

        if(judge){
            identifySymbol.addStr(name);
        }
        return mainFuncDef;
    }
}
