package NoneTerminal;

import Tables.ArrTable;
import Table.SymTable;
import Tables.Code;
import Tables.CodeType;
import Tables.Table;
import WordAnalyse.IdentifySymbol;
import WordAnalyse.RegKey;
import WordAnalyse.Symbol;

import java.util.ArrayList;

public class CompUnit {
    public static String name = "<CompUnit>";
    public static boolean isNameDupDef = false;

    private ArrayList<Decl> declList;
    private ArrayList<FuncDef> funcDefList;
    private MainFuncDef mainFuncDef;

    public CompUnit(){
        this.declList = new ArrayList<>();
        this.funcDefList = new ArrayList<>();
        this.mainFuncDef = null;
    }

    public void setMainFuncDef(MainFuncDef mainFuncDef){
        this.mainFuncDef = mainFuncDef;
    }

    public void addDecl(Decl decl){
        this.declList.add(decl);
    }

    public void addFuncDef(FuncDef funcDef){
        this.funcDefList.add(funcDef);
    }

    public void genCode(){
        int mainJumpInsAdr = -1;
        Table.createANewLayer();
        ArrTable.createArrTable();
        Code.addCode(CodeType.INI);
        for(Decl decl : declList)
            decl.genCode();
        mainJumpInsAdr = Code.addCode(CodeType.JMP, -1); // TODO: modify adr later, you need to jump to main
        for(FuncDef funcDef : funcDefList)
            funcDef.genCode();
        Code.modify_Y(mainJumpInsAdr, Code.addCode(CodeType.MAI));
        mainFuncDef.genCode();
    }

    public static CompUnit analyse(IdentifySymbol identifySymbol) {
        Symbol sym;
        boolean judge = true;
        CompUnit compUnit = new CompUnit();
        SymTable.createNewTable();

        sym = identifySymbol.getCurSym();
        while (judge && (identifySymbol.getCurSym().getRegKey() == RegKey.CONSTTK ||
                identifySymbol.getCurSym().getRegKey() == RegKey.INTTK)) { // Decl
            if (identifySymbol.getCurSym().getRegKey() == RegKey.INTTK) {
                sym = identifySymbol.getASymbol();
                if (sym.getRegKey() == RegKey.IDENFR) {
                    sym = identifySymbol.getASymbol();
                    if (sym.getRegKey() == RegKey.LPARENT) {
                        identifySymbol.spitSym(2);
                        break;
                    } else {
                        identifySymbol.spitSym(2);
                    }
                } else {
                    identifySymbol.spitSym(1);
                    break;
                }
            }
            compUnit.addDecl(Decl.analyse(identifySymbol));
        }

        while (judge && (identifySymbol.getCurSym().getRegKey() == RegKey.VOIDTK ||
                identifySymbol.getCurSym().getRegKey() == RegKey.INTTK)) {
            if (identifySymbol.getCurSym().getRegKey() == RegKey.INTTK) {
                sym = identifySymbol.getASymbol();
                if (sym.getRegKey() == RegKey.IDENFR) {
                    identifySymbol.spitSym(1);
                } else {
                    identifySymbol.spitSym(1);
                    break;
                }
            }
            compUnit.addFuncDef(FuncDef.analyse(identifySymbol));
        }

        if (judge) {
            compUnit.setMainFuncDef(MainFuncDef.analyse(identifySymbol));
        }

        if (judge) {
            identifySymbol.addStr(name);
        }

        return compUnit;
    }
}
