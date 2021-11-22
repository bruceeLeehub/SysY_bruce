package NoneTerminal.StmtPack;

import NoneTerminal.*;
import Tables.*;

public class IfStmt extends Stmt {
    public Stmt ifStatement;
    public Stmt elseStatement;
    public Cond condition;

    @Override
    public void genCode() {
        condition.genCode(); // result ----> stack[top]
        int ifJumpInsAdr = Code.addCode(CodeType.JMC, -1); //  modify adr later
        ifStatement.genCode();
        int jmpAdr_2 = Code.addCode(CodeType.JMP, -1);
        Code.modify_Y(ifJumpInsAdr, Code.get_NextFreeRoom());
        if(elseStatement != null){
            elseStatement.genCode();
        }
        Code.modify_Y(jmpAdr_2, Code.get_NextFreeRoom());
    }

    public IfStmt(Cond condition, Stmt ifStatement, Stmt elseStatement) {
        this.ifStatement = ifStatement;
        this.elseStatement = elseStatement;
        this.condition = condition;
    }
}
