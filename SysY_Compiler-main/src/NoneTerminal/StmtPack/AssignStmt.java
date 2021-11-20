package NoneTerminal.StmtPack;

import NoneTerminal.Exp;
import NoneTerminal.LVal;
import NoneTerminal.Stmt;
import Tables.Code;
import Tables.CodeType;

public class AssignStmt extends Stmt {
    private LVal lVal;
    private Exp exp;

    @Override
    public void genCode(){
        lVal.genCode(null, true);   // need to be assigned (left value)
        exp.genCode(null);
        Code.addCode(CodeType.STO);
    }

    public AssignStmt(LVal lVal, Exp exp) {
        this.lVal = lVal;
        this.exp = exp;
    }
}
