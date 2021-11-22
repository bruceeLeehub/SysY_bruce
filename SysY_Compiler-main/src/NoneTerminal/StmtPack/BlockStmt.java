package NoneTerminal.StmtPack;

import NoneTerminal.*;
import Tables.TableEntry;

public class BlockStmt extends Stmt {
    public static int block_Layers = 0;
    public Block block;

    @Override
    public void genCode() {
        int blockOverBreak_Size = WhileStmt.blocksOverBreak.size();
        for(int i = 0; i < blockOverBreak_Size; i++){
            int element = WhileStmt.blocksOverBreak.get(i) + 1;
            WhileStmt.blocksOverBreak.set(i, element);
        }
        block_Layers = block_Layers + 1;
        TableEntry tableEntry = null;
        String name = null;
        block.genCode(name, tableEntry);
        block_Layers = block_Layers - 1;
        for(int i = 0; i < blockOverBreak_Size; i++){
            int element = WhileStmt.blocksOverBreak.get(i) - 1;
            WhileStmt.blocksOverBreak.set(i,element);
        }
    }

    public BlockStmt(Block block){
        this.block = block;
    }
}
