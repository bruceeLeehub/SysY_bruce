package Tables;

import java.util.ArrayList;

public class MemoryStack {
    private static final ArrayList<Integer> memoryStack = new ArrayList<>();

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
