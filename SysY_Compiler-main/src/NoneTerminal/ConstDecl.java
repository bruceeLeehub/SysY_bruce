package NoneTerminal;

import WordAnalyse.*;
import MyError.Error;

import java.util.ArrayList;

public class ConstDecl extends Decl{
    private final ArrayList<ConstDef> constDefList;
    public static String name_constDecl = "<ConstDecl>";
    private BType bType;

    public void setBType(BType bType){
        this.bType = bType;
    }

    public static ConstDecl analyse(IdentifySymbol identSymbol) {
        ConstDecl constDecl = new ConstDecl();
        Symbol symbol = identSymbol.get_CurrentSym();
        boolean isConst;
        isConst = symbol.getRegKey() == RegKey.CONSTTK;
        if (isConst) {
            identSymbol.getASymbol();
            BType bType1 = BType.analyse(identSymbol);
            constDecl.setBType(bType1);

            ConstDef constDef = ConstDef.analyse(identSymbol);
            constDecl.add_ConstDef(constDef);
        }
        while (isConst && identSymbol.get_CurrentSym().getRegKey() == RegKey.COMMA) {
            identSymbol.getASymbol();
            ConstDef constDef = ConstDef.analyse(identSymbol);
            constDecl.add_ConstDef(constDef);
        }
        boolean isSemicn = isConst && identSymbol.get_CurrentSym().getRegKey() == RegKey.SEMICN;
        if (isSemicn) {
            identSymbol.getASymbol();
        } else {
            Error.add_ErrorOutPut(identSymbol.get_PreSym().getRow_Idx() + " i"); // ERROR -- i: ';' needed
        }
        if (isConst) {
            identSymbol.addStr(name_constDecl);
        }
        return constDecl;
    }

    public void add_ConstDef(ConstDef constDef){
        constDefList.add(constDef);
    }

    public ConstDecl(){
        this.constDefList = new ArrayList<>();
        this.bType = null;
    }

    @Override
    public void genCode(){
        for(ConstDef constDef : constDefList)
            constDef.genCode();
    }
}
