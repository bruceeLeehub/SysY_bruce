package NoneTerminal.StmtPack;

import NoneTerminal.*;
import Tables.*;

public class ReadStmt extends Stmt {
    private LVal lVal;
    private int read_Int;

    @Override
    public void genCode() {
        lVal.genCode(null, true);   // to be written
        Code.addCode(CodeType.RDI);
    }

    public ReadStmt(LVal lVal) {
        this.lVal = lVal;
        this.read_Int = 0;
    }
}
