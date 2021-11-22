package NoneTerminal;

import Table.SymTable;
import Tables.*;
import WordAnalyse.*;

import java.util.ArrayList;

public class Block {
    private final ArrayList<BlockItemInter> blockItemInterList;
    public static int block_Layers = 0;
    public static boolean hasReturnStmt = false;
    public static String name_block = "<Block>";

    public static Block analyse(IdentifySymbol identifySymbol) {
        Block block = new Block();  // AST TREE NODE
        hasReturnStmt = false;
        block_Layers = block_Layers + 1;
        Symbol sym;
        boolean judge = true;
        // create a new table stack for this block
        if (!FuncDef.funcBlockST) {
            SymTable.create_NewTable();
        }
        else {
            FuncDef.funcBlockST = false;
        }


        sym = identifySymbol.get_CurrentSym();
        boolean b1 = sym.getRegKey() == RegKey.LBRACE;
        judge = b1;
        identifySymbol.getASymbol();
        sym = identifySymbol.get_CurrentSym();
        while (judge && sym.getRegKey() != RegKey.RBRACE) {
            b1 = BlockItem.analyse(identifySymbol, block);
            judge = b1;
            sym = identifySymbol.get_CurrentSym();
        }
        if (judge) {
            b1 = sym.getRegKey() == RegKey.RBRACE;
            judge = b1;
            identifySymbol.getASymbol();
        }

        if (judge) {
            b1 = MainFuncDef.CheckingMain;
            b1 = b1 && identifySymbol.isEndLine();
            if (b1) {
                identifySymbol.add_Times(1);
            }
            identifySymbol.addStr(name_block);
        }
        // popOutdata current table stack of this block
        block_Layers = block_Layers - 1;
        SymTable.popOutterTable();
        return block;
    }

    public void add_Stmt_List(Stmt stmt) {
        blockItemInterList.add(stmt);
    }

    public static boolean isMyFirst(Symbol sym) {
        return sym.getRegKey() == RegKey.LBRACE;
    }


    public void genCode(String name, TableEntry funcTableEntry) {
        int recPreSize = 0;

        boolean isFunc_NotMain = (funcTableEntry != null && !name.equals("main"));
        if (isFunc_NotMain)     // enter from a func def
            Table.add_FuncTentryToCurTable(name, funcTableEntry);
        else {                   // enter from others
            recPreSize = Table.get_CurrentPreSize();
            Table.createANewLayer();
        }

        boolean noName = (name == null);
        if(noName) {
            Code.addCode(CodeType.BKI);
        }

        for(BlockItemInter blockItemInter : blockItemInterList) {
            blockItemInter.genCode();
        }

        Table.getOutFromLayer();
        Table.setCurrentPreSize(recPreSize);

        boolean noName1 = (name == null);
        if(noName1) {
            Code.addCode(CodeType.DBK);
        }

        boolean voidFunc = (funcTableEntry != null);
        voidFunc = voidFunc && funcTableEntry.get_Typ().equals(Typ.VOID_TYP);
        if(voidFunc) {
            Code.addCode(CodeType.RET);
        }
    }

    public void add_Decl_List(Decl decl) {
        blockItemInterList.add(decl);
    }

    public Block() {
        blockItemInterList = new ArrayList<>();
    }
}
