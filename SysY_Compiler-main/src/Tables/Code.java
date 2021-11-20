package Tables;

import java.util.ArrayList;

public class Code {
    private static ArrayList<CodeType> codeTypeList = new ArrayList<>();
    private static ArrayList<Integer> xList = new ArrayList<>();
    private static ArrayList<Integer> yList = new ArrayList<>();

    public static int addCode(CodeType codeType, int x, int y) {
        codeTypeList.add(codeType);
        xList.add(x);
        yList.add(y);
        return codeTypeList.size() - 1;
    }

    public static int addCode(CodeType codeType, int y) {
        codeTypeList.add(codeType);
        xList.add(-777);
        yList.add(y);
        return codeTypeList.size() - 1;
    }

    public static int addCode(CodeType codeType) {
        codeTypeList.add(codeType);
        xList.add(-777);
        yList.add(-777);
        return codeTypeList.size() - 1;
    }

    public static int getNextFreeRoom() {
        return codeTypeList.size();
    }

    public static void modifyY(int codePtr, int modify) {
        yList.set(codePtr, modify);
    }

    public static void modifyX(int codePtr, int modify){
        xList.set(codePtr, modify);
    }

    public static int codeSize() {
        return codeTypeList.size();
    }

    public static CodeType getCodeType(int pc) {
        return codeTypeList.get(pc);
    }

    public static int getX(int pc) {
        return xList.get(pc);
    }

    public static int getY(int pc) {
        return yList.get(pc);
    }

    public static String seeCode() {
        String s = "idx  CodeType   X   Y\n";
        String idx;
        String x;
        String y;
        for (int i = 0; i < codeSize(); i++) {
            idx = String.format("%3d  ", i);
            if (xList.get(i) == -777)
                x = "    ";
            else
                x = String.format("%4d", xList.get(i));
            if (yList.get(i) == -777)
                y = "    ";
            else
                y = String.format("%4d", yList.get(i));
            String t = idx + codeTypeList.get(i) + "     " +  x + y + "\n";
            s += t;
        }
        return s;
    }
}
