package NoneTerminal.StmtPack;

import NoneTerminal.Stmt;
import Tables.Code;
import Tables.CodeType;

import java.util.ArrayList;

public class ContinueStmt extends Stmt {
    public static ArrayList<ArrayList<Integer>> continueStmtList = new ArrayList<>();

    @Override
    public void genCode() {
        if (continueStmtList.size() == 0 || continueStmtList.get(continueStmtList.size() - 1) == null)
            continueStmtList.add(new ArrayList<>());
        continueStmtList.get(continueStmtList.size() - 1).add(Code.addCode(CodeType.BRK,
                WhileStmt.blocksOverBreak.get(WhileStmt.blocksOverBreak.size() - 1), -1));
    }

    public static void modifyAllBreakY(int beginPc) {
        if (continueStmtList.size() != 0) {
            ArrayList<Integer> thisBreakList = continueStmtList.get(continueStmtList.size() - 1);
            if (thisBreakList != null) {
                for (int adr : thisBreakList) {
                    Code.modify_Y(adr, beginPc);
                }
            }
            // continueStmtList.remove(continueStmtList.size() - 1);
        }
    }
}
