package NoneTerminal.StmtPack;

import NoneTerminal.*;
import Tables.*;

public class IfStmt extends Stmt {
    private Cond cond;
    private Stmt ifStmt;
    private Stmt elseStmt;

    public IfStmt(Cond cond, Stmt ifStmt, Stmt elseStmt) {
        this.cond = cond;
        this.ifStmt = ifStmt;
        this.elseStmt = elseStmt;
    }

    @Override
    public void genCode() {
        int ifJumpInsAdr = -1;
        int jmpAdr_2 = -1;
        cond.genCode(); // result is store in the stack[top]
        ifJumpInsAdr = Code.addCode(CodeType.JMC, -1); // TODO: modify adr later
        ifStmt.genCode();
        jmpAdr_2 = Code.addCode(CodeType.JMP, -1);
        Code.modify_Y(ifJumpInsAdr, Code.get_NextFreeRoom());
        if(elseStmt != null){
            elseStmt.genCode();
        }
        Code.modify_Y(jmpAdr_2, Code.get_NextFreeRoom());
    }
}
