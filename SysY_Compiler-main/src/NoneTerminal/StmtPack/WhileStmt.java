package NoneTerminal.StmtPack;

import NoneTerminal.*;
import Tables.*;

import java.util.ArrayList;

public class WhileStmt extends Stmt {
    public static ArrayList<Integer> blocksOverBreak = new ArrayList<>();
    public Cond condition;
    public Stmt while_Stmt;

    public WhileStmt(Cond condition, Stmt while_Stmt) {
        this.condition = condition;
        this.while_Stmt = while_Stmt;
    }

    @Override
    public void genCode(){
        int while_Begin_Addr = Code.get_NextFreeRoom();
        condition.genCode();
        int JMCAdr = Code.addCode(CodeType.JMC, -1);


        blocksOverBreak.add(0);
        ArrayList<Integer> arrayList = new ArrayList<>();
        BreakStmt.breakStmt_List.add(arrayList);
        ArrayList<Integer> arrayList1 = new ArrayList<>();
        ContinueStmt.continueStmt_List.add(arrayList1);
        while_Stmt.genCode();
        blocksOverBreak.remove(blocksOverBreak.size() - 1);

        Code.addCode(CodeType.JMP, while_Begin_Addr);
        int while_End_Addr = Code.get_NextFreeRoom();

        Code.modify_Y(JMCAdr, while_End_Addr);

        BreakStmt.modifyAllBreakY(while_End_Addr);
        ContinueStmt.modifyAllBreakY(while_Begin_Addr);
        int back = BreakStmt.breakStmt_List.size() - 1;
        BreakStmt.breakStmt_List.remove(back);
        back = ContinueStmt.continueStmt_List.size() - 1;
        ContinueStmt.continueStmt_List.remove(back);
    }
}
