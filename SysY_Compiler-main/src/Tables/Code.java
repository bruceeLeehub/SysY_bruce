package Tables;

import java.util.ArrayList;

public class Code {
    private static final ArrayList<CodeType> codeTypeList = new ArrayList<>();
    private static final ArrayList<Integer> xList = new ArrayList<>();
    private static final ArrayList<Integer> yList = new ArrayList<>();

    public static String seeCode() {
        StringBuilder s = new StringBuilder("index------CodeType------X------Y\n");
        for (int i = 0; i < codeSize(); i++) {
            s.append(String.format("%3d   ",i));
            s.append(codeTypeList.get(i));
            s.append("     ");
            if (xList.get(i) == -19373460)
                s.append("       ");
            else
                s.append(String.format("%4d", xList.get(i)));
            if (yList.get(i) == -19373460)
                s.append("       ");
            else
                s.append(String.format("%4d", yList.get(i)));
            s.append('\n');
        }
        return s.toString();
    }

    public static int getX(int pc) {
        return xList.get(pc);
    }

    public static int getY(int pc) {
        return yList.get(pc);
    }

    public static int getNextFreeRoom() {
        return codeTypeList.size();
    }

    public static void modifyY(int codePtr, int modify) {
        yList.set(codePtr, modify);
    }


    public static int addCode(CodeType codeType, int x, int y) {
        xList.add(x);
        yList.add(y);
        codeTypeList.add(codeType);
        int size = codeTypeList.size();
        return size - 1;
    }

    public static int addCode(CodeType codeType, int y) {
        xList.add(-19373460);
        yList.add(y);
        codeTypeList.add(codeType);
        int size = codeTypeList.size();
        return size - 1;
    }

    public static int addCode(CodeType codeType) {
        xList.add(-19373460);
        yList.add(-19373460);
        codeTypeList.add(codeType);
        int size = codeTypeList.size();
        return size - 1;
    }

    public static int codeSize() {
        return codeTypeList.size();
    }

    public static CodeType getCodeType(int pc) {
        return codeTypeList.get(pc);
    }
}
