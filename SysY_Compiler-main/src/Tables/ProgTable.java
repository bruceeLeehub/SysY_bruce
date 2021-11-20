package Tables;

import java.util.ArrayList;

public class ProgTable {
    private static final ArrayList<ProgTableEntry> prog_Table = new ArrayList<>();

    public static ProgTableEntry getProg_Entry(int adr){
        return prog_Table.get(adr);
    }

    public static int insertProg_Entry(String name, int paraSize){
        ProgTableEntry progTableEntry = new ProgTableEntry(name,paraSize);
        prog_Table.add(progTableEntry);
        int size = prog_Table.size();
        return size - 1;
    }

}
