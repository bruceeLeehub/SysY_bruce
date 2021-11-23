package NoneTerminal.StmtPack;

import NoneTerminal.*;
import Tables.*;

public class ReturnExp extends Stmt {
    public Exp exp;

    @Override
    public void genCode() {
        if (exp == null) {
            Code.addCode(CodeType.RET, BlockStmt.block_Layers);
        } else {
            exp.genCode(null);
            Code.addCode(CodeType.VRE, BlockStmt.block_Layers);
        }
    }

    public ReturnExp(Exp exp) {
        this.exp = exp;
    }
}
