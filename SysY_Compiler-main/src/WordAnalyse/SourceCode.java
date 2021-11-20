package WordAnalyse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class SourceCode {
    private final ArrayList<Character> sourceCode;
    private int ptr;
    private int rowIdx;


    public char getNextChar() {
        char c = sourceCode.get(ptr++);
        if (c == '\n') {
            rowIdx++;
        }
        return c;
    }

    public boolean isEnd() {
        return ptr == sourceCode.size();
    }

    public void backWard() {
        ptr--;
    }

    public int getRowIdx() {
        return rowIdx;
    }

    public void skipLineCom() {
        char c = getNextChar();
        while (c != '\n' && !isEnd()) {
            c = getNextChar();
        }
    }


    public void skipMulLineCom() {
        char c = getNextChar();
        boolean flag = false;
        while (!isEnd()) {
            if (flag && c == '/') {
                break;
            } else {
                flag = false;
            }

            if (c == '*') {
                flag = true;
            }
            c = getNextChar();
        }
    }

    public String readToString(String fileName) {
        String encoding = "UTF-8";
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }

    public SourceCode(String path) {
        this.ptr = 0;
        this.rowIdx = 1;
        String code = readToString(path);
        this.sourceCode = new ArrayList<>();
        for(int i = 0; i < code.length(); i++){
            this.sourceCode.add(code.charAt(i));
        }
    }
}
