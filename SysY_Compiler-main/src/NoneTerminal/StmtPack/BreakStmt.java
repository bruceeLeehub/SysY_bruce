package NoneTerminal.StmtPack;

import NoneTerminal.Stmt;
import Tables.Code;
import Tables.CodeType;

import java.util.ArrayList;

public class BreakStmt extends Stmt {
    public static ArrayList<ArrayList<Integer>> breakStmtList = new ArrayList<>();

    @Override
    public void genCode() {
        if (breakStmtList.size() == 0 || breakStmtList.get(breakStmtList.size() - 1) == null)
            breakStmtList.add(new ArrayList<>());
        breakStmtList.get(breakStmtList.size() - 1).add(Code.addCode(CodeType.BRK,
                WhileStmt.blocksOverBreak.get(WhileStmt.blocksOverBreak.size() - 1), -1));
    }

    public static void modifyAllBreakY(int endPC) {
        if (breakStmtList.size() != 0) {
            ArrayList<Integer> thisBreakList = breakStmtList.get(breakStmtList.size() - 1);
            if (thisBreakList != null) {
                for (int adr : thisBreakList) {
                    Code.modify_Y(adr, endPC);
                }
            }
            // breakStmtList.remove(breakStmtList.size() - 1);
        }
    }
}
