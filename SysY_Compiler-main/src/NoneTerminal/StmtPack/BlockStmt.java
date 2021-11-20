package NoneTerminal.StmtPack;

import NoneTerminal.Block;
import NoneTerminal.Stmt;

public class BlockStmt extends Stmt {
    private Block block;
    public static int blockLayers = 0;

    public BlockStmt(Block block){
        this.block = block;
    }

    @Override
    public void genCode() {
        int blockOverBreadSize = WhileStmt.blocksOverBreak.size();
        for(int i = 0; i < blockOverBreadSize; i++){
            WhileStmt.blocksOverBreak.set(i, WhileStmt.blocksOverBreak.get(i) + 1);
        }
        blockLayers++;
        block.genCode(null, null);
        blockLayers--;
        for(int i = 0; i < blockOverBreadSize; i++){
            WhileStmt.blocksOverBreak.set(i, WhileStmt.blocksOverBreak.get(i) - 1);
        }
    }
}
