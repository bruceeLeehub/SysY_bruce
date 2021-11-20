package Tables;

import java.util.ArrayList;

public class MemoryStack {
    private static ArrayList<Integer> memoryStack = new ArrayList<>();

    public static void addDataToTop(int data) {
        memoryStack.add(data);
    }

    public static int getDataFromAdr(int adr) {
        return memoryStack.get(adr);
    }

    public static int pop() {
        int value = memoryStack.get(memoryStack.size() - 1);
        memoryStack.remove(memoryStack.size() - 1);
        return value;
    }

    public static void saveDataToAdr(int adr, int value) {
        memoryStack.set(adr, value);
    }

    public static void movePtr(int curPtr) {
        while(memoryStack.size() - 1 != curPtr){
            memoryStack.remove(memoryStack.size() - 1);
        }
    }

    public static int getCurPtr(){
        return memoryStack.size() - 1;
    }
}
