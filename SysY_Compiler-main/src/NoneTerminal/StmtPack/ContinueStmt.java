package NoneTerminal.StmtPack;

import NoneTerminal.*;
import Tables.*;

import java.util.ArrayList;

public class ContinueStmt extends Stmt {
    public static ArrayList<ArrayList<Integer>> continueStmt_List = new ArrayList<>();

    public static void modifyAllBreakY(int beginPc) {
        int size = continueStmt_List.size();
        if (size != 0) {
            int back =continueStmt_List.size() - 1;
            ArrayList<Integer> thisBreakList = continueStmt_List.get(back);
            if (thisBreakList != null) {
                for (int addr : thisBreakList) {
                    Code.modify_Y(addr, beginPc);
                }
            }
            // continueStmtList.remove(continueStmtList.size() - 1);
        }
    }

    @Override
    public void genCode() {
        int back = continueStmt_List.size() - 1;
        int size = continueStmt_List.size();
        boolean b1 = (size == 0 || continueStmt_List.get(back) == null);
        if (b1) {
            ArrayList<Integer> arrayList = new ArrayList<>();
            continueStmt_List.add(arrayList);
        }
        back = continueStmt_List.size() - 1;
        int x =  WhileStmt.blocksOverBreak.get(WhileStmt.blocksOverBreak.size() - 1);
        int y = -1;
        int code = Code.addCode(CodeType.BRK,x,y);
        continueStmt_List.get(back).add(code);
    }
}
