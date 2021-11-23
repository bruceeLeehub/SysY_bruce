package NoneTerminal.StmtPack;

import NoneTerminal.*;
import Tables.*;

import java.util.ArrayList;

public class BreakStmt extends Stmt {
    public static ArrayList<ArrayList<Integer>> breakStmt_List = new ArrayList<>();

    public static void modifyAllBreakY(int endPC) {
        int size = breakStmt_List.size();
        if (size != 0) {
            int back = breakStmt_List.size() - 1;
            ArrayList<Integer> thisBreakList = breakStmt_List.get(back);
            if (thisBreakList != null) {
                for (int addr : thisBreakList) {
                    Code.modify_Y(addr, endPC);
                }
            }
            // breakStmtList.remove(breakStmtList.size() - 1);
        }
    }

    @Override
    public void genCode() {
        int size = breakStmt_List.size();
        if (size == 0 || breakStmt_List.get(size - 1) == null) {
            ArrayList<Integer> arrayList = new ArrayList<>();
            breakStmt_List.add(arrayList);
        }
        int back = breakStmt_List.size() - 1;
        int x = WhileStmt.blocksOverBreak.get(WhileStmt.blocksOverBreak.size() - 1);
        int y = -1;
        int code = Code.addCode(CodeType.BRK,x,y);
        breakStmt_List.get(back).add(code);
    }

}
