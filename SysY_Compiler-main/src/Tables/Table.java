package Tables;

import NoneTerminal.Ident;

import java.util.ArrayList;
import java.util.TreeMap;

public class Table {
    private static final ArrayList<ColTable> table = new ArrayList<>();
    private static int pre_size = -1;

    public static TableEntry getIdentTabEntry(boolean isFunc, String name){
        int tempPtr = getCurLayer();
        TableEntry tableEntry = null;
        while(tableEntry == null && tempPtr >= 0) {
            TreeMap<String,TableEntry> theTable = table.get(tempPtr).getTheRightTable(isFunc);
            tableEntry = theTable.get(name);
            tempPtr = tempPtr - 1;
        }
        return tableEntry;
    }

    public static int get_ConstArrayValue(Ident ident, ArrayList<Integer> dim_Value) {
        String name = ident.getId_Name();
        TableEntry tableEntry = getAttrTableEntry(name);
        ArrayList<ArrTableEntry> arrayTable = ArrTable.getArrTable();
        int ref = tableEntry.get_Ref();
        ArrTableEntry arrTableEntry = arrayTable.get(ref);
        int back = dim_Value.size() - 1;
        int diff = dim_Value.get(back);
        for(int i = dim_Value.size() - 2; i >=0 ;i--){
            int dimValue_i = dim_Value.get(i);
            int upperBounds = arrTableEntry.getUpper_Bounds().get(i);
            diff  = diff + dimValue_i * upperBounds;
        }
        ArrayList<Integer> constArray = arrTableEntry.getConst_Array();
        int value = constArray.get(diff);
        return value;
    }

    public static int get_AttrLev(Ident ident) {
        String name = ident.getId_Name();
        TableEntry tableEntry = getAttrTableEntry(name);
        int level = tableEntry.get_Level();
        return level;
    }

    public static ArrayList<Integer> get_ArrayDims(Ident ident) {
        String name = ident.getId_Name();
        TableEntry tableEntry = getAttrTableEntry(name);
        int ref = tableEntry.get_Ref();
        ArrayList<ArrTableEntry> arrTable = ArrTable.getArrTable();
        ArrTableEntry arrTableEntry = arrTable.get(ref);
        ArrayList<Integer> arrayDims = arrTableEntry.getUpper_Bounds();
        return arrayDims;
    }

    public static void getOutFromLayer(){
        int size = table.size();
        table.remove(size - 1);
    }

    public static void setCurrentPreSize(int PreSize){
        pre_size = PreSize;
    }

    public static int getCurLayer(){
        int size = table.size();
        return size - 1;
    }


    public static void addTeToCurrentTable(String name, Obj obj, Typ type, int dims, int ref, int level, int addr,
                                           boolean isPara){
        int curPtr = table.size() - 1;
        int curSize = addr;
        boolean isVar = obj.equals(Obj.VAR_OBJ);
        boolean isConst = obj.equals(Obj.CONST_OBJ);
        if(isVar) {
            addr = pre_size;
        }
        TableEntry tableEntry = new TableEntry(obj, type, dims, ref, level, addr, isPara);
        table.get(curPtr).getTheRightTable(obj.equals(Obj.FUNC_OBJ)).put(name, tableEntry);
        if(!isConst) {
            pre_size = pre_size + curSize;
        }
    }

    public static void addTeToCurrentTable(String name, Obj obj, Typ type, int dims, int ref, int level, int addr){
        int curPtr = table.size() - 1;
        int curSize = addr;
        boolean isVar = obj.equals(Obj.VAR_OBJ);
        boolean isConst = obj.equals(Obj.CONST_OBJ);
        if(isVar) {
            addr = pre_size;
        }
        TableEntry tableEntry = new TableEntry(obj, type, dims, ref, level, addr);
        table.get(curPtr).getTheRightTable(obj.equals(Obj.FUNC_OBJ)).put(name, tableEntry);
        if(!isConst) {
            pre_size = pre_size + curSize;
        }
    }


    public static TableEntry getAttrTableEntry(String name){
        TableEntry tableEntry = getIdentTabEntry(false, name);
        return tableEntry;
    }

    public static TableEntry getFuncTableEntry(String name) {
        TableEntry tableEntry = getIdentTabEntry(true,name);
        return tableEntry;
    }

    public static int get_ConstIdentValue(Ident ident) {
        String name = ident.getId_Name();
        TableEntry tableEntry = getAttrTableEntry(name);
        int identValue = tableEntry.get_Addr();
        return identValue;
    }

    public static int get_ArrayAdr(Ident ident) {
        String name = ident.getId_Name();
        TableEntry tableEntry = getAttrTableEntry(name);
        int adr = tableEntry.get_Addr();
        return adr;
    }

    public static void createANewLayer() {
        pre_size = 0;
        table.add(new ColTable());
    }

    public static int get_CurrentPreSize(){
        return pre_size;
    }

    public static void add_FuncTentryToCurTable(String name, TableEntry tableEntry){
        table.get(table.size() - 1).getTheRightTable(true).put(name, tableEntry);
    }
}
