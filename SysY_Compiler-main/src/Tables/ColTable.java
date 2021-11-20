package Tables;

import java.util.HashMap;

public class ColTable{
    public HashMap<String, TableEntry> funcTable;
    public HashMap<String, TableEntry> attrTable;
    public ColTable(){
        this.funcTable = new HashMap<>();
        this.attrTable = new HashMap<>();
    }

    public HashMap<String, TableEntry> getRightTable(boolean isFunc){
        if(isFunc)
            return funcTable;
        return attrTable;
    }
}