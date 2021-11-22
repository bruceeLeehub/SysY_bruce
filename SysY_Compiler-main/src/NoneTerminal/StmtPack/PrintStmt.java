package NoneTerminal.StmtPack;

import NoneTerminal.*;
import Tables.*;

import java.util.ArrayList;

public class PrintStmt extends Stmt {
    private int fsAdr;
    private ArrayList<Exp> expList;

    @Override
    public void genCode(){
        for(Exp exp : expList) {
            exp.genCode(null);
        }
        Code.addCode(CodeType.WRF, fsAdr);
    }

    public PrintStmt(int fsAdr, ArrayList<Exp> expList) {
        this.fsAdr = fsAdr;
        this.expList = expList;
    }
}
