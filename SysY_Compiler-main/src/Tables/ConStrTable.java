package Tables;

import java.util.ArrayList;

public class ConStrTable {
    private static ArrayList<String> constStrTab = new ArrayList<>();

    public static int addConStr(String constStr){
        constStrTab.add(constStr);
        return constStrTab.size() - 1;
    }

    public static String getStr(int adr) {
        return constStrTab.get(adr);
    }
}
