package Tables;

import java.util.ArrayList;

public class ConStrTable {
    private int Ky_0 = 0;
    private int Ky_1 = 0;
    private int Ky_2 = 0;
    private int Ky_3 = 0;
    private int Ky_4 = 0;
    private static final ArrayList<String> constStringTab = new ArrayList<>();

    public int getKy_0() {
        return Ky_0;
    }

    public void setKy_0(int ky_0) {
        Ky_0 = ky_0;
    }

    public int getKy_4() {
        return Ky_4;
    }

    public void setKy_4(int ky_4) {
        Ky_4 = ky_4;
    }

    public int getKy_3() {
        return Ky_3;
    }

    public void setKy_3(int ky_3) {
        Ky_3 = ky_3;
    }

    public int getKy_2() {
        return Ky_2;
    }

    public void setKy_2(int ky_2) {
        Ky_2 = ky_2;
    }

    public int getKy_1() {
        return Ky_1;
    }

    public void setKy_1(int ky_1) {
        Ky_1 = ky_1;
    }

    public static String getString(int adr) {
        return constStringTab.get(adr);
    }

    public static int addConString(String constStr){
        constStringTab.add(constStr);
        int size = constStringTab.size();
        return size - 1;
    }
}
