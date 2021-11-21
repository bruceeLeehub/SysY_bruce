package Table;

import java.util.ArrayList;
import java.util.TreeMap;

public class SymTable {
    private static int table_Ptr = -1;
    private static final ArrayList<AggTable> table_Stack = new ArrayList<>();

    public static void popOutterTable() {
        table_Stack.remove(table_Ptr);
        table_Ptr = table_Ptr - 1;
    }

    public static TableEntry get_SymByNameInAllTable(boolean isFunction, String idName) {
        int tablePtr = table_Ptr;
        TableEntry tableEntry = null;
        for(; tableEntry == null && tablePtr >= 0; tablePtr--){
            AggTable aggTable = table_Stack.get(tablePtr);
            TreeMap<String,TableEntry> right_table = aggTable.get_RightTable(isFunction);
            tableEntry = right_table.get(idName);
            ///tableEntry = table_Stack.get(tablePtr).get_RightTable(isFunction).get(idName);
        }
        return tableEntry;
    }


    public static boolean NameContainedInAllTable(boolean isFunc, String identName) {
        int tablePtr = table_Ptr;
        boolean haveName = false;
        for(; tablePtr >= 0; tablePtr--){
            AggTable aggTable = table_Stack.get(tablePtr);
            TreeMap<String,TableEntry> right_table = aggTable.get_RightTable(isFunc);
            haveName = right_table.containsKey(identName);
            if (haveName) {
                return true;
            }
            //haveName |= table_Stack.get(tablePtr).get_RightTable(isFunc).containsKey(identName);
        }
        return haveName;
    }

    public static void create_NewTable() {
        table_Ptr = table_Ptr + 1;
        table_Stack.add(new AggTable());
    }

    public static TableEntry createTableEntryModel(TableEntry tempEntry, int dims) {
        boolean isConst = tempEntry.isConst();
        DataType dataType = tempEntry.get_DType();
        int Dims = tempEntry.get_Dims();
        ArrayList<TableEntry> paramList = tempEntry.get_ParamsList();
        TableEntry tableEntry = new TableEntry(isConst, dataType, Dims, paramList);
//        if(tempEntry.get_Dims() == dims){
//            tableEntry.set_Dims(0);
//        }else if(tempEntry.get_Dims() == 2 && dims == 1){
//            tableEntry.set_Dims(1);
//        }
        if(tempEntry.get_Dims() == 2 && dims == 1){
            tableEntry.set_Dims(1);
        }else if(tempEntry.get_Dims() == dims){
            tableEntry.set_Dims(0);
        }
        return tableEntry;
    }

    public static void add_Param(ArrayList<TableEntry> paramList, boolean isConst, DataType dataType, int dims) {
        TableEntry tableEntry = new TableEntry(isConst, dataType, dims, paramList);
        paramList.add(tableEntry);
    }

    public static boolean currentTableContainName(boolean isFunction, String name) {
        AggTable aggTable = table_Stack.get(table_Ptr);
        TreeMap<String,TableEntry> right_table= aggTable.get_RightTable(isFunction);
        boolean hasName = right_table.containsKey(name);
        return hasName;
        //return table_Stack.get(table_Ptr).get_RightTable(isFunction).containsKey(name);
    }

    public static void insertTabEntryIntoCurTab(boolean isFunction, String name, boolean isConst, DataType dataType, int dims) {
        AggTable aggTable = table_Stack.get(table_Ptr);
        TreeMap<String,TableEntry> right_table = aggTable.get_RightTable(isFunction);
        Table.TableEntry tableEntry = new TableEntry(isConst, dataType, dims);
        right_table.put(name,tableEntry);
        //table_Stack.get(table_Ptr).get_RightTable(isFunction).put(name, new TableEntry(isConst, dataType, dims));
    }

    public static void insertTabEntryIntoPreTab(boolean isFunction, String name, boolean isConst, DataType dType, int dims, ArrayList<TableEntry> paramList) {
        AggTable aggTable = table_Stack.get(table_Ptr - 1);
        TreeMap<String,TableEntry> right_table = aggTable.get_RightTable(isFunction);
        TableEntry tableEntry = new TableEntry(isConst, dType, dims, paramList);
        right_table.put(name,tableEntry);
        //table_Stack.get(table_Ptr - 1).get_RightTable(isFunction).put(name, new TableEntry(isConst, dType, dims, paramList));
    }

    public static int get_ExpectedParamsNum(String functionName){
        int stack_ptr = table_Ptr;
        int paramNum = 0;
        for(; stack_ptr >= 0; stack_ptr--){
            AggTable aggTable = table_Stack.get(stack_ptr);
            TreeMap<String,TableEntry> funcTable = aggTable.get_FuncTable();
            boolean hasFuncName = (funcTable.containsKey(functionName));
            if(hasFuncName){
                TableEntry tableEntry = funcTable.get(functionName);
                paramNum = tableEntry.get_ParamSize();
                return paramNum;
                //paramNum = table_Stack.get(stack_ptr).get_FuncTable().get(functionName).get_ParamSize();
                //break;
            }
        }
        return paramNum;
    }
}
