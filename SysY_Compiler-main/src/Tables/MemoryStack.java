package Tables;

import java.util.ArrayList;

public class MemoryStack {
    private int Ky_0 = 0;
    private int Ky_1 = 0;
    private int Ky_2 = 0;
    private int Ky_3 = 0;
    private int Ky_4 = 0;

    private static final ArrayList<Integer> memoryStack = new ArrayList<>();


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
    public static void saveDataToAdr(int adr, int value) {
        memoryStack.set(adr, value);
    }

    public static int popOutdata() {
        int back = memoryStack.size() - 1;
        int dataValue = memoryStack.get(back);
        memoryStack.remove(back);
        return dataValue;
    }

    public static int get_CurPtr(){
        int size = memoryStack.size();
        return size - 1;
    }

    public static void add_DataToTop(int data) {
        memoryStack.add(data);
    }

    public static int get_DataFromAdr(int adr) {
        return memoryStack.get(adr);
    }

    public static void move_Ptr(int curPtr) {
        while(memoryStack.size() != curPtr + 1){
            int back = memoryStack.size() - 1;
            memoryStack.remove(back);
        }
    }

}
