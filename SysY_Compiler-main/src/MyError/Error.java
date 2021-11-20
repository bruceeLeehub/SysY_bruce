package MyError;

import NoneTerminal.CompUnit;
import ParcelType.MyInt;
import Table.SymTable;
import Table.TableEntry;
import WordAnalyse.Symbol;

import java.util.ArrayList;

public class Error {
    public static ArrayList<String> errorPrintList = new ArrayList<>();
    public Error() {
    }

    public static boolean checkFormatStr(Symbol sym, MyInt numOfExpExpected) {
        char str[] = sym.getIdentName().toCharArray();
        boolean ret = true;
        numOfExpExpected.myInt = 0;
        for (int i = 0; i < str.length && ret; i++) {
            if (str[i] == '%') {
                i++;
                if(i < str.length) {
                    ret = str[i] == 'd';
                    if(ret)
                        numOfExpExpected.myInt++;
                }else {
                    ret = false;
                    break;
                }
            }else if(str[i] == '\\'){
                i++;
                ret = str[i] == 'n';
            }else if(str[i] == '\"' || str[i] == '!' || str[i] == ' ' || str[i] >= '(' && str[i] <= '~'){
                continue;
            }else{
                ret = false;
            }
        }
        if(!ret) errorPrintList.add(sym.getRowIdx() + " a");
        return ret;
    }

    public static void checkIfDupDef(boolean isFunc, Symbol sym){
        CompUnit.isNameDupDef = SymTable.curTabContainsName(isFunc, sym.getIdentName());
        if (CompUnit.isNameDupDef)
            Error.addErrorOutPut(sym.getRowIdx() + " b");
    }

    public static void checkNameUndefined(boolean isFunc, Symbol sym){
        boolean isDefined = SymTable.allTabContainsName(isFunc, sym.getIdentName());
        if(isDefined == false)
            Error.addErrorOutPut(sym.getRowIdx() + " c");
    }

    public static void checkParamNumMatched(Symbol sym, Integer numOfParamsActually){
        int numOfParamsExpected = SymTable.getParamsExpected(sym.getIdentName());
        if(numOfParamsExpected != numOfParamsActually)
            Error.addErrorOutPut(sym.getRowIdx() + " d");
    }

    public static void checkRParamsMatched(TableEntry funcTE, TableEntry actuallyTE, Symbol sym, int paramSeq){
        boolean isMatched = true;
        if(paramSeq > funcTE.getParamNums()){
            return;
        }
        TableEntry expectedTE = funcTE.getParamList().get(paramSeq - 1);
        isMatched &= expectedTE.getDType() == actuallyTE.getDType();
        isMatched &= expectedTE.getDims() == actuallyTE.getDims();
        if(isMatched == false)
            Error.addErrorOutPut(sym.getRowIdx() + " e");
    }

    public static void checkAssignValueToConst(Symbol sym){
        TableEntry te = SymTable.getSymByNameFromAllTab(false, sym.getIdentName());
        if(te != null && te.isConst())
            Error.addErrorOutPut(sym.getRowIdx() + " h");
    }

    public static void addErrorOutPut(String str){
        errorPrintList.add(str);
    }
}
