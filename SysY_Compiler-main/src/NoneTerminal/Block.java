package NoneTerminal;

import Table.SymTable;
import Tables.*;
import WordAnalyse.IdentifySymbol;
import WordAnalyse.RegKey;
import WordAnalyse.Symbol;

import java.util.ArrayList;

public class Block {
    public static String name = "<Block>";
    public static boolean haveRetStmt = false;
    public static int blockLayer = 0;

    private ArrayList<BlockItemInter> blockItemInters;

    public Block() {
        this.blockItemInters = new ArrayList<>();
    }

    public void addDeclList(Decl decl) {
        this.blockItemInters.add(decl);
    }

    public void addStmtList(Stmt stmt) {
        this.blockItemInters.add(stmt);
    }

    public void genCode(String name, TableEntry funcTe) {
        int recPreSize = 0;
        if (funcTe != null /* is func */ &&
                !name.equals("main") /* is not main */)     // enter from a func def
            Table.addFuncTeToCurTab(name, funcTe);
        else {                   // enter from others
            recPreSize = Table.getCurPreSize();
            Table.createNewLayer();
        }

        if(name == null)
            Code.addCode(CodeType.BKI);

        for(BlockItemInter blockItemInter : blockItemInters)
            blockItemInter.genCode();

        Table.comeOutFromLayer();
        Table.setCurPreSize(recPreSize);

        if(name == null)
            Code.addCode(CodeType.DBK);

        if(funcTe != null && funcTe.getTyp().equals(Typ.TYP_VOID))
            Code.addCode(CodeType.RET);
    }

    public static Block analyse(IdentifySymbol identifySymbol) {
        blockLayer++;
        Symbol sym;
        boolean judge = true;
        haveRetStmt = false;
        Block block = new Block();  // ast Tree node
        // create a new table stack for this block
        if (!FuncDef.funcBlockST)
            SymTable.createNewTable();
        else
            FuncDef.funcBlockST = false;


        sym = identifySymbol.getCurSym();
        judge &= sym.getRegKey() == RegKey.LBRACE;
        identifySymbol.getASymbol();
        sym = identifySymbol.getCurSym();
        while (judge && sym.getRegKey() != RegKey.RBRACE) {
            judge &= BlockItem.analyse(identifySymbol, block);
            sym = identifySymbol.getCurSym();
        }
        if (judge) {
            judge &= sym.getRegKey() == RegKey.RBRACE;
            identifySymbol.getASymbol();
        }

        if (judge) {
            if (MainFuncDef.mainIsChecking && identifySymbol.isEndLine()) identifySymbol.addTimes(1);
            identifySymbol.addStr(name);
        }
        // popOutdata current table stack of this block
        SymTable.removeOuterTable();
        blockLayer--;
        return block;
    }

    public static boolean isMyFirst(Symbol sym) {
        if (sym.getRegKey() == RegKey.LBRACE) {
            return true;
        }
        return false;
    }
}
