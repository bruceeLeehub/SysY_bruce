package NoneTerminal.StmtPack;

import NoneTerminal.LVal;
import NoneTerminal.Stmt;
import Tables.Code;
import Tables.CodeType;

public class ReadStmt extends Stmt {
    private LVal lVal;
    private int readInt;

    public ReadStmt(LVal lVal) {
        this.lVal = lVal;
        this.readInt = 0;
    }

    @Override
    public void genCode() {
        lVal.genCode(null, true);   // need to be written, so left value
        Code.addCode(CodeType.RDI);
    }
}
