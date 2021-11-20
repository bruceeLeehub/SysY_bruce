package Tables;

import java.util.ArrayList;

public class ProgTable {
    private static ArrayList<ProgTableEntry> progTable = new ArrayList<>();

    public static int insPE(String name, int paraSize){
        progTable.add(new ProgTableEntry(name, paraSize));
        return progTable.size() - 1;
    }

    public static ProgTableEntry getPE(int adr){
        return progTable.get(adr);
    }
}
