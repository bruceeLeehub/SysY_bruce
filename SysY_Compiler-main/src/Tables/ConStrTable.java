package Tables;

import java.util.ArrayList;

public class ConStrTable {
    private static final ArrayList<String> constStringTab = new ArrayList<>();

    public static String getString(int adr) {
        return constStringTab.get(adr);
    }

    public static int addConString(String constStr){
        constStringTab.add(constStr);
        int size = constStringTab.size();
        return size - 1;
    }
}
