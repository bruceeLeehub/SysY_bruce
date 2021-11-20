package Table;


import java.util.HashMap;

public class AggTable {
    private HashMap<String, TableEntry> attrTable;
    private HashMap<String, TableEntry> funcTable;
    public AggTable(){
        attrTable = new HashMap<>();
        funcTable = new HashMap<>();
    }

    public HashMap<String, TableEntry> getAttrTable(){
        return this.attrTable;
    }

    public HashMap<String, TableEntry> getFuncTable(){
        return this.funcTable;
    }

    public HashMap<String, TableEntry> getRightTable(boolean isFunc){
        if(isFunc)
            return this.funcTable;
        else
            return this.attrTable;
    }
}
