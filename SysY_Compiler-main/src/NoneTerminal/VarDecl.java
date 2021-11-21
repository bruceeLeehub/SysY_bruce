package NoneTerminal;

import MyError.Error;
import WordAnalyse.IdentifySymbol;
import WordAnalyse.RegKey;
import WordAnalyse.Symbol;

import java.util.ArrayList;

public class VarDecl extends Decl {
    public static String name = "<VarDecl>";

    private BType bType;
    private ArrayList<VarDef> varDefList;

    public VarDecl() {
        this.bType = null;
        this.varDefList = new ArrayList<>();
    }

    public void setBType(BType bType){
        this.bType = bType;
    }

    public void addVarDef(VarDef varDef){
        this.varDefList.add(varDef);
    }

    @Override
    public void genCode(){
        for(VarDef varDef : varDefList){
            varDef.genCode();
        }
    }

    public static VarDecl analyse(IdentifySymbol identifySymbol) {
        Symbol sym;
        boolean judge = true;
        VarDecl varDecl = new VarDecl();


        sym = identifySymbol.get_CurrentSym();
        varDecl.setBType(BType.analyse(identifySymbol));

        if (judge) {
            varDecl.addVarDef(VarDef.analyse(identifySymbol));
        }
        while (judge && identifySymbol.get_CurrentSym().getRegKey() == RegKey.COMMA) {
            identifySymbol.getASymbol();
            varDecl.addVarDef(VarDef.analyse(identifySymbol));
        }
        if (judge && identifySymbol.get_CurrentSym().getRegKey() == RegKey.SEMICN) identifySymbol.getASymbol();
        else Error.addErrorOutPut(identifySymbol.get_PreSym().getRow_Idx() + " i"); // ERROR -- i: ';' needed


        if (judge) {
            identifySymbol.addStr(name);
        }
        return varDecl;
    }
}
