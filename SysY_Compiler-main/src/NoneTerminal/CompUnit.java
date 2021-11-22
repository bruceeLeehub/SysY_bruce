package NoneTerminal;

import Tables.*;
import Table.SymTable;
import WordAnalyse.*;

import java.util.ArrayList;

public class CompUnit {
    private final ArrayList<Decl> decl_List;
    public static boolean isNameDuplicateDef = false;
    private final ArrayList<FuncDef> funcDef_List;
    private MainFuncDef main_FuncDef;

    public static String name_compUnit = "<CompUnit>";

    public void setMain_FuncDef(MainFuncDef main_FuncDef){
        this.main_FuncDef = main_FuncDef;
    }

    public boolean isDecl(IdentifySymbol identSymbol) {
        boolean b1 = identSymbol.get_CurrentSym().getRegKey() == RegKey.CONSTTK;
        boolean b2 = identSymbol.get_CurrentSym().getRegKey() == RegKey.INTTK;
        return b1 || b2;
    }


    public static CompUnit analyse(IdentifySymbol identSymbol) {
        boolean isDecl;
        boolean isFuncDef;
        CompUnit compUnit = new CompUnit();
        SymTable.create_NewTable();

        Symbol sym = identSymbol.get_CurrentSym();
        while (identSymbol.get_CurrentSym().getRegKey() == RegKey.CONSTTK
                || identSymbol.get_CurrentSym().getRegKey() == RegKey.INTTK) { // Decl
            Symbol symbol = identSymbol.get_CurrentSym();
            RegKey regKey = symbol.getRegKey();
            boolean isInt = (regKey == RegKey.INTTK);
            if (isInt) {
                sym = identSymbol.getASymbol();
                regKey = sym.getRegKey();
                boolean isIdent = (regKey == RegKey.IDENFR);
                if (!isIdent) {
                    identSymbol.spitSym(1);
                    break;
                } else {
                    sym = identSymbol.getASymbol();
                    regKey = sym.getRegKey();
                    boolean isLparent = (regKey == RegKey.LPARENT);
                    identSymbol.spitSym(2);
                    if (isLparent) {
                        break;
                    }
                }
            }
            Decl decl = Decl.analyse(identSymbol);
            compUnit.addDecl(decl);
        }

        while (identSymbol.get_CurrentSym().getRegKey() == RegKey.INTTK ||
                identSymbol.get_CurrentSym().getRegKey() == RegKey.VOIDTK) {
            Symbol symbol = identSymbol.get_CurrentSym();
            RegKey regKey = symbol.getRegKey();
            boolean isInt = (regKey == RegKey.INTTK);
            if (isInt) {
                sym = identSymbol.getASymbol();
                regKey = sym.getRegKey();
                boolean isIdent= (regKey == RegKey.IDENFR);
                identSymbol.spitSym(1);
                if (!isIdent) {
                    break;
                }
            }
            FuncDef funcDef = FuncDef.analyse(identSymbol);
            compUnit.addFuncDef(funcDef);
        }

        MainFuncDef mainFuncDef = MainFuncDef.analyse(identSymbol);
        compUnit.setMain_FuncDef(mainFuncDef);

        identSymbol.addStr(name_compUnit);

        return compUnit;
    }

    public void addDecl(Decl decl){
        decl_List.add(decl);
    }

    public void addFuncDef(FuncDef funcDef){
        funcDef_List.add(funcDef);
    }

    public void genCode(){
        ArrTable.createArrTable();
        Code.addCode(CodeType.INI);
        Table.createANewLayer();
        for(Decl decline : decl_List) {
            decline.genCode();
        }
        int main_JumpInsAdr = Code.addCode(CodeType.JMP, -1);
        // TODO: modify adr later, you need to jump to main
        for(FuncDef func_Def : funcDef_List) {
            func_Def.genCode();
        }
        Code.modify_Y(main_JumpInsAdr, Code.addCode(CodeType.MAI));
        main_FuncDef.genCode();
    }

    public CompUnit(){
        decl_List = new ArrayList<>();
        funcDef_List = new ArrayList<>();
        main_FuncDef = null;
    }
}
