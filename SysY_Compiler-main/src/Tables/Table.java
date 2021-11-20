package Tables;

import NoneTerminal.Ident;

import java.util.ArrayList;

public class Table {
    private static ArrayList<ColTable> table = new ArrayList<>();
    private static int preSize = -1;

    public static void createNewLayer() {
        ColTable colTable = new ColTable();
        table.add(colTable);
        preSize = 0;
    }

    public static void comeOutFromLayer(){
        table.remove(table.size() - 1);
    }

    public static int getCurPreSize(){
        return preSize;
    }

    public static void setCurPreSize(int setPreSize){
        preSize = setPreSize;
    }

    public static void addTeToCurTab(String name, Obj obj, Typ typ, int dims, int ref, int lev, int adr){
        boolean isFunc = obj.equals(Obj.OBJ_FUNC);
        int curSize = adr;
        int curPtr = table.size() - 1;
        if(obj.equals(Obj.OBJ_VAR))
            adr = preSize;
        table.get(curPtr).getRightTable(isFunc).put(
                name, new TableEntry(obj, typ, dims, ref, lev, adr)
        );
        if(!obj.equals(Obj.OBJ_CONST))
            preSize += curSize;
    }

    public static void addTeToCurTab(String name, Obj obj, Typ typ, int dims, int ref, int lev, int adr,
                                     boolean isPara){
        boolean isFunc = obj.equals(Obj.OBJ_FUNC);
        int curSize = adr;
        int curPtr = table.size() - 1;
        if(obj.equals(Obj.OBJ_VAR))
            adr = preSize;
        table.get(curPtr).getRightTable(isFunc).put(
                name, new TableEntry(obj, typ, dims, ref, lev, adr, isPara)
        );
        if(!obj.equals(Obj.OBJ_CONST))
            preSize += curSize;
    }

    public static void addFuncTeToCurTab(String name, TableEntry te){
        int curPtr = table.size() - 1;
        table.get(curPtr).getRightTable(true).put(name, te);
    }

    public static int getCurLayer(){
        return table.size() - 1;
    }

    public static TableEntry getIdentTe(boolean isFunc, String name){
        TableEntry te = null;
        int tempPtr = getCurLayer();
        while(te == null && tempPtr >= 0){
            te = table.get(tempPtr).getRightTable(isFunc).get(name);
            tempPtr--;
        }
        return te;
    }

    public static TableEntry getAttrTe(String name){
        return getIdentTe(false, name);
    }

    public static TableEntry getFuncTe(String name) {
        return getIdentTe(true, name);
    }

    public static int getConstArrayValue(Ident ident, ArrayList<Integer> dimValue) {
        TableEntry te = getAttrTe(ident.getIdentName());
        ArrTableEntry arrTableEntry = ArrTable.getArrTable().get(te.getRef());
        int diff = dimValue.get(dimValue.size() - 1);
        for(int i = dimValue.size() - 2; i >=0 ;i--){
            diff += dimValue.get(i) * arrTableEntry.getUpper_Bounds().get(i);
        }
        return arrTableEntry.getConst_Array().get(diff);
    }

    public static int getConstIdentValue(Ident ident) {
        TableEntry te = getAttrTe(ident.getIdentName());
        return te.getAdr();
    }



    public static ArrayList<Integer> getArrayDims(Ident ident) {
        TableEntry te = getAttrTe(ident.getIdentName());
        ArrTableEntry arrTableEntry = ArrTable.getArrTable().get(te.getRef());
        return arrTableEntry.getUpper_Bounds();
    }

    public static int getArrayAdr(Ident ident) {
        TableEntry te = getAttrTe(ident.getIdentName());
        return te.getAdr();
    }

    public static int getAttrLev(Ident ident) {
        TableEntry te = getAttrTe(ident.getIdentName());
        return te.getLev();
    }


}
