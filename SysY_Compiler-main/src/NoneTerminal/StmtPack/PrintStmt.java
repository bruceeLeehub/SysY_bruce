package NoneTerminal.StmtPack;

import NoneTerminal.*;
import Tables.*;

import java.util.ArrayList;

public class PrintStmt extends Stmt {
    private int fs_Adr;
    private ArrayList<Exp> exp_List;

    @Override
    public void genCode(){
        for(Exp exp : exp_List) {
            exp.genCode(null);
        }
        Code.addCode(CodeType.WRF, fs_Adr);
    }

    public PrintStmt(int fs_Adr, ArrayList<Exp> exp_List) {
        this.fs_Adr = fs_Adr;
        this.exp_List = exp_List;
    }
}
