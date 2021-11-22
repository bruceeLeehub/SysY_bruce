package NoneTerminal.StmtPack;

import NoneTerminal.Exp;
import NoneTerminal.Stmt;
import Tables.Code;
import Tables.CodeType;

public class ReturnExp extends Stmt {
    private Exp exp;

    public ReturnExp(Exp exp) {
        this.exp = exp;
    }

    @Override
    public void genCode() {
        if (exp != null) {
            exp.genCode(null);
            Code.addCode(CodeType.VRE, BlockStmt.block_Layers);
        } else {
            Code.addCode(CodeType.RET, BlockStmt.block_Layers);
        }
    }
}
