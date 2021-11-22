package WordAnalyse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SourceCode {
    private final ArrayList<Character> sourceCode;
    private int ptr;
    private int rowIdx;


    public char getNext_Char() {
        char ch = sourceCode.get(ptr);
        if (ch == '\n') {
            rowIdx = rowIdx + 1;
        }
        ptr += 1;
        return ch;
    }


    public void back_Ward() {
        this.ptr = this.ptr - 1;
    }

    public int get_RowIdx() {
        return this.rowIdx;
    }

    public String readToString(String testfileName) {
        File testFile = new File(testfileName);
        long filelength = testFile.length();
        byte[] file_content = new byte[(int) filelength];
        try {
            FileInputStream input = new FileInputStream(testFile);
            input.read(file_content);
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(file_content, StandardCharsets.UTF_8);
    }

    public void jumpOverMulLineComment() {
        char ch = getNext_Char();
        boolean meetSecStar = false;
        while (!isEnd()) {
            if (meetSecStar && ch == '/') {
                break;
            } else {
                meetSecStar = false;
            }

            if (ch == '*') {
                meetSecStar = true;
            }
            ch = getNext_Char();
        }
    }
    public SourceCode(String testfilename) {
        this.ptr = 0;
        this.rowIdx = 1;
        String code = readToString(testfilename);
//        System.out.print(code);
        this.sourceCode = new ArrayList<>();
        for(int i = 0; i < code.length(); i++){
            this.sourceCode.add(code.charAt(i));
        }
    }
    public boolean isEnd() {
        long distance = sourceCode.size() - ptr;
        return (distance == 0);
    }
    public void jumpOverLineComments() {
        char ch = getNext_Char();
        while (ch != '\n' && !isEnd()) {
            ch = getNext_Char();
        }
    }
}
