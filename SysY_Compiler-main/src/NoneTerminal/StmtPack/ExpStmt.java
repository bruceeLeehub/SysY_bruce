package NoneTerminal.StmtPack;

import NoneTerminal.*;

public class ExpStmt extends Stmt {
    public Exp exp;

    @Override
    public void genCode() {
        if (exp != null) {
            exp.genCode(null);
        }
    }

    public ExpStmt(Exp exp) {
        this.exp = exp;
    }
}
