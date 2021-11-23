package NoneTerminal;

import MyError.Error;
import WordAnalyse.*;

import java.util.ArrayList;

public class VarDecl extends Decl {
    public ArrayList<VarDef> varDef_List = new ArrayList<>();
    public static String name_varDecl = "<VarDecl>";
    public BType bType = null;

    public static VarDecl analyse(IdentifySymbol identifySymbol) {
        Symbol sym = identifySymbol.get_CurrentSym();
        VarDecl varDecl = new VarDecl();

        varDecl.bType = (BType.analyse(identifySymbol));
        VarDef varDef = VarDef.analyse(identifySymbol);
        varDecl.varDef_List.add(varDef);
        while (identifySymbol.get_CurrentSym().getRegKey() == RegKey.COMMA) {
            identifySymbol.getASymbol();
            VarDef varDef1 = VarDef.analyse(identifySymbol);
            varDecl.varDef_List.add(varDef1);
        }
        Symbol curSymbol = identifySymbol.get_CurrentSym();
        RegKey regKey = curSymbol.getRegKey();
        boolean isSEMICN = regKey == RegKey.SEMICN;
        if (isSEMICN) {
            identifySymbol.getASymbol();
        }
        else {
            Symbol preSymbol = identifySymbol.get_PreSym();
            int rowidx = preSymbol.getRow_Idx();
            Error.add_ErrorOutPut(rowidx + " i");
        } //i: ';' needed

        identifySymbol.addStr(name_varDecl);
        return varDecl;
    }

    public VarDecl() {
    }

    @Override
    public void genCode(){
        for(VarDef varDef : this.varDef_List){
            varDef.genCode();
        }
    }
}
