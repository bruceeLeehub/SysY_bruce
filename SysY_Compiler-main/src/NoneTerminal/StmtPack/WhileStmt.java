package NoneTerminal.StmtPack;

import NoneTerminal.Cond;
import NoneTerminal.Stmt;
import Tables.Code;
import Tables.CodeType;

import java.util.ArrayList;

public class WhileStmt extends Stmt {
    private Cond cond;
    private Stmt whileStmt;
    public static ArrayList<Integer> blocksOverBreak = new ArrayList<>();

    @Override
    public void genCode(){
        int JMCAdr;
        int whileBeginAdr = -1;
        int whileEndAdr = -1;
        whileBeginAdr = Code.get_NextFreeRoom();
        cond.genCode();
        JMCAdr = Code.addCode(CodeType.JMC, -1);


        blocksOverBreak.add(0);
        BreakStmt.breakStmt_List.add(new ArrayList<>());
        ContinueStmt.continueStmt_List.add(new ArrayList<>());
        whileStmt.genCode();
        blocksOverBreak.remove(blocksOverBreak.size() - 1);

        Code.addCode(CodeType.JMP, whileBeginAdr);
        whileEndAdr = Code.get_NextFreeRoom();

        Code.modify_Y(JMCAdr, whileEndAdr);

        BreakStmt.modifyAllBreakY(whileEndAdr);
        ContinueStmt.modifyAllBreakY(whileBeginAdr);

        BreakStmt.breakStmt_List.remove(BreakStmt.breakStmt_List.size() - 1);
        ContinueStmt.continueStmt_List.remove(ContinueStmt.continueStmt_List.size() - 1);
    }

    public WhileStmt(Cond cond, Stmt whileStmt) {
        this.cond = cond;
        this.whileStmt = whileStmt;
    }
}
