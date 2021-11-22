package NoneTerminal.StmtPack;

import NoneTerminal.*;
import Tables.*;

public class AssignStmt extends Stmt {
    public Exp exp_rhs;
    public LVal lVal_lhs;

    public AssignStmt(LVal lVal_lhs, Exp exp_rhs) {
        this.lVal_lhs = lVal_lhs;
        this.exp_rhs = exp_rhs;
    }

    @Override
    public void genCode(){
        boolean isLeftValue = true;
        lVal_lhs.genCode(null, isLeftValue);   // need assignment
        exp_rhs.genCode(null);
        Code.addCode(CodeType.STO);
    }
}
