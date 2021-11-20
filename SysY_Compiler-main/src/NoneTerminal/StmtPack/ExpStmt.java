package NoneTerminal.StmtPack;

import NoneTerminal.Exp;
import NoneTerminal.Stmt;
import Tables.Code;
import Tables.CodeType;

public class ExpStmt extends Stmt {
    private Exp exp;

    public ExpStmt(Exp exp) {
        this.exp = exp;
    }

    @Override
    public void genCode() {
        if (exp != null)
            exp.genCode(null);
    }
}
