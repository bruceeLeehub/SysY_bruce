package Tables;

import java.util.ArrayList;

public class ArrTable {
    private static ArrayList<ArrTableEntry> ArrTable;

    public static int createAnEntry(ArrayList<Integer> upperBounds){
        ArrTableEntry arrTableEntry = new ArrTableEntry(upperBounds);
        ArrTable.add(arrTableEntry);
        int size = ArrTable.size();
        return size - 1;
    }

    public static ArrayList<ArrTableEntry> getArrTable(){
        return ArrTable;
    }

    public static void createArrTable(){
        ArrTable = new ArrayList<>();
    }

    public static int createAnEntry(ArrayList<Integer> upperBounds, ArrayList<Integer> constArray){
        ArrTable.add(new ArrTableEntry(upperBounds, constArray));
        return ArrTable.size() - 1;
    }

}
