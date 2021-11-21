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
        char str[] = sym.get_IdentName().toCharArray();
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
        if(!ret) errorPrintList.add(sym.getRow_Idx() + " a");
        return ret;
    }

    public static void checkIfDupDef(boolean isFunc, Symbol sym){
        CompUnit.isNameDupDef = SymTable.currentTableContainName(isFunc, sym.get_IdentName());
        if (CompUnit.isNameDupDef)
            Error.addErrorOutPut(sym.getRow_Idx() + " b");
    }

    public static void checkNameUndefined(boolean isFunc, Symbol sym){
        boolean isDefined = SymTable.NameContainedInAllTable(isFunc, sym.get_IdentName());
        if(isDefined == false)
            Error.addErrorOutPut(sym.getRow_Idx() + " c");
    }

    public static void checkParamNumMatched(Symbol sym, Integer numOfParamsActually){
        int numOfParamsExpected = SymTable.get_ExpectedParamsNum(sym.get_IdentName());
        if(numOfParamsExpected != numOfParamsActually)
            Error.addErrorOutPut(sym.getRow_Idx() + " d");
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
            Error.addErrorOutPut(sym.getRow_Idx() + " e");
    }

    public static void checkAssignValueToConst(Symbol sym){
        TableEntry te = SymTable.get_SymByNameInAllTable(false, sym.get_IdentName());
        if(te != null && te.isConst())
            Error.addErrorOutPut(sym.getRow_Idx() + " h");
    }

    public static void addErrorOutPut(String str){
        errorPrintList.add(str);
    }
}
