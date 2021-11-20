package Tables;

import java.util.TreeMap;

public class ColTable{
    public TreeMap<String, TableEntry> funcTable;
    public TreeMap<String, TableEntry> attrTable;

    public TreeMap<String, TableEntry> getTheRightTable(boolean isFunc){
        if(!isFunc) {
            return attrTable;
        }
        else {
            return funcTable;
        }
    }
    public ColTable(){
        this.funcTable = new TreeMap<>();
        this.attrTable = new TreeMap<>();
    }
}