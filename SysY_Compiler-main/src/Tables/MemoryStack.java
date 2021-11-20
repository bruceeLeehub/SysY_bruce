package Tables;

import java.util.ArrayList;

public class MemoryStack {
    private static final ArrayList<Integer> memoryStack = new ArrayList<>();

    public static int popOutdata() {
        int value = memoryStack.get(memoryStack.size() - 1);
        memoryStack.remove(memoryStack.size() - 1);
        return value;
    }

    public static int get_CurPtr(){
        int size = memoryStack.size();
        return size - 1;
    }

    public static void add_DataToTop(int data) {
        memoryStack.add(data);
    }

    public static int getDataFromAdr(int adr) {
        return memoryStack.get(adr);
    }


    public static void saveDataToAdr(int adr, int value) {
        memoryStack.set(adr, value);
    }

    public static void movePtr(int curPtr) {
        while(memoryStack.size() - 1 != curPtr){
            memoryStack.remove(memoryStack.size() - 1);
        }
    }

}
