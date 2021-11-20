package NoneTerminal.StmtPack;

import NoneTerminal.Cond;
import NoneTerminal.Stmt;
import Tables.Code;
import Tables.CodeType;

import java.util.ArrayList;
import java.util.HashMap;

public class WhileStmt extends Stmt {
    private Cond cond;
    private Stmt whileStmt;
    public static ArrayList<Integer> blocksOverBreak = new ArrayList<>();

    @Override
    public void genCode(){
        int JMCAdr;
        int whileBeginAdr = -1;
        int whileEndAdr = -1;
        whileBeginAdr = Code.getNextFreeRoom();
        cond.genCode();
        JMCAdr = Code.addCode(CodeType.JMC, -1);


        blocksOverBreak.add(0);
        BreakStmt.breakStmtList.add(new ArrayList<>());
        ContinueStmt.continueStmtList.add(new ArrayList<>());
        whileStmt.genCode();
        blocksOverBreak.remove(blocksOverBreak.size() - 1);

        Code.addCode(CodeType.JMP, whileBeginAdr);
        whileEndAdr = Code.getNextFreeRoom();

        Code.modifyY(JMCAdr, whileEndAdr);

        BreakStmt.modifyAllBreakY(whileEndAdr);
        ContinueStmt.modifyAllBreakY(whileBeginAdr);

        BreakStmt.breakStmtList.remove(BreakStmt.breakStmtList.size() - 1);
        ContinueStmt.continueStmtList.remove(ContinueStmt.continueStmtList.size() - 1);
    }

    public WhileStmt(Cond cond, Stmt whileStmt) {
        this.cond = cond;
        this.whileStmt = whileStmt;
    }
}
