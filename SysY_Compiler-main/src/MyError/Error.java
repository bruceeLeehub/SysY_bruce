package MyError;

import NoneTerminal.*;
import ParcelType.My_Int;
import Table.SymTable;
import Table.TableEntry;
import WordAnalyse.Symbol;

import java.util.ArrayList;

public class Error {
    public static ArrayList<String> errorPrintList = new ArrayList<>();
    public Error() {
    }

    public static boolean checkFormatStr(Symbol sym, My_Int numOfExpExpected) {
        char[] str = sym.get_IdentName().toCharArray();
        boolean ret = true;
        numOfExpExpected.my_Int = 0;
        for (int i = 0; i < str.length && ret; i++) {
            if (str[i] == '%') {
                i++;
                if(i < str.length) {
                    ret = str[i] == 'd';
                    if(ret) {
                        numOfExpExpected.my_Int++;
                    }
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
        if(!ret) {
            errorPrintList.add(sym.getRow_Idx() + " a");
        }
        return ret;
    }

    public static void checkIfDupDef(boolean isFunc, Symbol sym){
        CompUnit.isNameDuplicateDef = SymTable.currentTableContainName(isFunc, sym.get_IdentName());
        if (CompUnit.isNameDuplicateDef) {
            Error.add_ErrorOutPut(sym.getRow_Idx() + " b");
        }
    }

    public static void checkNameUndefined(boolean isFunc, Symbol sym){
        boolean isDefined = SymTable.NameContainedInAllTable(isFunc, sym.get_IdentName());
        if(!isDefined) {
            Error.add_ErrorOutPut(sym.getRow_Idx() + " c");
        }
    }

    public static void checkParamNumMatched(Symbol sym, Integer numOfParamsActually){
        int numOfParamsExpected = SymTable.get_ExpectedParamsNum(sym.get_IdentName());
        if(numOfParamsExpected != numOfParamsActually) {
            Error.add_ErrorOutPut(sym.getRow_Idx() + " d");
        }
    }

    public static void checkRParamsMatched(TableEntry funcTE, TableEntry actuallyTE, Symbol sym, int paramSeq){
        boolean isMatched;
        if(paramSeq > funcTE.get_ParamSize()){
            return;
        }
        TableEntry expectedTE = funcTE.get_ParamsList().get(paramSeq - 1);
        isMatched = expectedTE.get_DType() == actuallyTE.get_DType();
        isMatched &= expectedTE.get_Dims() == actuallyTE.get_Dims();
        if(!isMatched) {
            Error.add_ErrorOutPut(sym.getRow_Idx() + " e");
        }
    }

    public static void checkAssignValueToConst(Symbol sym){
        TableEntry te = SymTable.get_SymByNameInAllTable(false, sym.get_IdentName());
        if(te != null && te.isConst()) {
            Error.add_ErrorOutPut(sym.getRow_Idx() + " h");
        }
    }

    public static void add_ErrorOutPut(String str){
        errorPrintList.add(str);
    }
}
