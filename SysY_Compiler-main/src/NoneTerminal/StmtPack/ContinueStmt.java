package NoneTerminal.StmtPack;

import NoneTerminal.*;
import Tables.*;

import java.util.ArrayList;

public class ContinueStmt extends Stmt {
    public static ArrayList<ArrayList<Integer>> continueStmt_List = new ArrayList<>();

    public static void modifyAllBreakY(int beginPc) {
        if (continueStmt_List.size() != 0) {
            ArrayList<Integer> thisBreakList = continueStmt_List.get(continueStmt_List.size() - 1);
            if (thisBreakList != null) {
                for (int adr : thisBreakList) {
                    Code.modify_Y(adr, beginPc);
                }
            }
            // continueStmtList.remove(continueStmtList.size() - 1);
        }
    }

    @Override
    public void genCode() {
        if (continueStmt_List.size() == 0 || continueStmt_List.get(continueStmt_List.size() - 1) == null)
            continueStmt_List.add(new ArrayList<>());
        continueStmt_List.get(continueStmt_List.size() - 1).add(Code.addCode(CodeType.BRK,
                WhileStmt.blocksOverBreak.get(WhileStmt.blocksOverBreak.size() - 1), -1));
    }
}
