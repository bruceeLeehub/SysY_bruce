package NoneTerminal;

import WordAnalyse.*;
import MyError.Error;

import java.util.ArrayList;

public class ConstDecl extends Decl{
    private final ArrayList<ConstDef> constDefList;
    public static String name_constDecl = "<ConstDecl>";
    private BType bType;

    public ConstDecl(){
        this.constDefList = new ArrayList<>();
        this.bType = null;
    }

    public void setBType(BType bType){
        this.bType = bType;
    }

    public void addConstDef(ConstDef constDef){
        this.constDefList.add(constDef);
    }

    @Override
    public void genCode(){
        for(ConstDef constDef : constDefList){
            constDef.genCode();
        }
    }

    public static ConstDecl analyse(IdentifySymbol identifySymbol) {
        Symbol sym;
        boolean judge;
        ConstDecl constDecl = new ConstDecl();

        sym = identifySymbol.get_CurrentSym();
        judge = sym.getRegKey() == RegKey.CONSTTK;
        if (judge) {
            identifySymbol.getASymbol();
            constDecl.setBType(BType.analyse(identifySymbol));
        }
        if (judge) {
            constDecl.addConstDef(ConstDef.analyse(identifySymbol));
        }
        while (judge && identifySymbol.get_CurrentSym().getRegKey() == RegKey.COMMA) {
            identifySymbol.getASymbol();
            constDecl.addConstDef(ConstDef.analyse(identifySymbol));
        }
        if (judge && identifySymbol.get_CurrentSym().getRegKey() == RegKey.SEMICN) identifySymbol.getASymbol();
        else Error.addErrorOutPut(identifySymbol.get_PreSym().getRow_Idx() + " i"); // ERROR -- i: ';' needed


        if (judge) {
            identifySymbol.addStr(name_constDecl);
        }
        return constDecl;
    }
}
