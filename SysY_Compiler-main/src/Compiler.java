import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

import MyError.Error;
import NoneTerminal.CompUnit;
import WordAnalyse.IdentifySymbol;
import WordAnalyse.SourceCode;
import WordAnalyse.Symbol;
import Tables.Code;


public class Compiler {
//    public static void addValToDisStack(int lev, int value) {
//        if (Interpret.disStack.get(lev) == null) {
//            Interpret.disStack.put(lev, new ArrayList<>());
//        }
//        Interpret.disStack.get(lev).add(value);
//    }
    public static void main(String[] args) throws IOException {
        String sourceFile = "testfile.txt";
        String resultFile = "pcoderesult.txt";
        SourceCode myCode = new SourceCode(sourceFile);
        CompUnit compUnit_astRoot;


        ArrayList<Symbol> symbolsRecordList = new ArrayList<>();
        ArrayList<String> outputList = new ArrayList<>();

        IdentifySymbol identifySymbol = new IdentifySymbol(symbolsRecordList, myCode, outputList);
        identifySymbol.getASymbol();

        compUnit_astRoot = CompUnit.analyse(identifySymbol);
        compUnit_astRoot.genCode();
        //System.out.println(Code.seeCode());//将代码输出到屏幕上
        Interpret.runInterpreter();


        FileWriter fw = new FileWriter(resultFile);
        Error.errorPrintList.sort(Comparator.comparingInt(o -> Integer.parseInt(o.split(" ")[0])));

        /* print error
        for (String s : Error.errorPrintList) {
            fw.write(s + "\n");
            fw.flush();
        }
        */
        for (String line_result : Interpret.pcodeOutput) {
            fw.write(line_result);
            fw.flush();
        }
    }
}
