package NoneTerminal;

import MyError.Error;
import ParcelType.MyInt;
import ParcelType.MyString;
import Table.SymTable;
import Table.TableEntry;
import Tables.*;
import WordAnalyse.IdentifySymbol;
import WordAnalyse.RegKey;
import WordAnalyse.Symbol;

import java.util.ArrayList;

public class LVal {
    public static String name = "<LVal>";
    public static int inDims = 0;

    private Ident ident;
    private ArrayList<Exp> expList;

    public LVal() {
        this.ident = null;
        this.expList = new ArrayList<>();
    }

    public void addExp(Exp exp) {
        this.expList.add(exp);
    }

    public void setIdent(Ident ident) {
        this.ident = ident;
    }

    public void genCode(MyInt value, boolean isLeftValue) {
        if (value != null) {      // is const, you need to calculate it right now
            if (expList.size() != 0) {    // this is an Array
                ArrayList<Integer> dimValue = new ArrayList<>();
                for (Exp exp : expList) {
                    exp.genCode(value);
                    dimValue.add(value.myInt);
                }
                value.myInt = Table.getConstArrayValue(ident, dimValue);
            } else {
                value.myInt = Table.getConstIdentValue(ident);
            }
        } else {          // not a const you need to get it when running program
            Tables.TableEntry te = Table.getAttrTe(ident.getIdentName());
            int lenOfExpList = expList.size();
            int lenOfDims = te.getDims();
            boolean isArray = lenOfExpList < lenOfDims;
            if (isArray) {
                if (te.isPara())    // for array para, its value is adr, so LOD
                    Code.addCode(CodeType.LOD, Table.getAttrLev(ident), Table.getArrayAdr(ident));
                else
                    Code.addCode(CodeType.LDA, Table.getAttrLev(ident), Table.getArrayAdr(ident));
                for (Exp exp : expList) {
                    exp.genCode(null);
                }
                if (expList.size() > 1 && /* dims are same refer to a value or addr*/
                        expList.size() == Table.getAttrTe(ident.getIdentName()).getDims()) {
                    Code.addCode(CodeType.SWP);
                    ArrayList<Integer> dims = Table.getArrayDims(ident);
                    Code.addCode(CodeType.LDC, dims.get(dims.size() - 1));
                    Code.addCode(CodeType.MUL);
                    Code.addCode(CodeType.ADD);
                } else if (expList.size() != 0 && expList.size() != Table.getAttrTe(ident.getIdentName()).getDims()) { // dims not equal, send a sub-array
                    int lastDim = ArrTable.getArrTable().get(Table.getAttrTe(ident.getIdentName()).getRef()).getUpperBounds().get(1);
                    Code.addCode(CodeType.LDC, lastDim);
                    Code.addCode(CodeType.MUL);
                    isLeftValue = true;
                } else {
                    return;
                }
                if (isLeftValue)
                    Code.addCode(CodeType.ADD);
                else
                    Code.addCode(CodeType.LAV);
            } else {
                if (lenOfDims != 0) { // elements of array
                    if (te.getObj().equals(Obj.OBJ_CONST))
                        Code.addCode(CodeType.LDC, te.getAdr());
                    else if (te.isPara())    // for array para, its value is adr, so LOD
                        Code.addCode(CodeType.LOD, Table.getAttrLev(ident), Table.getArrayAdr(ident));
                    else
                        Code.addCode(CodeType.LDA, Table.getAttrLev(ident), Table.getArrayAdr(ident));
                    for (Exp exp : expList) {
                        exp.genCode(null);
                    }

                    if (expList.size() > 1 && /* dims are same refer to a value or addr*/
                            expList.size() == Table.getAttrTe(ident.getIdentName()).getDims()) {
                        Code.addCode(CodeType.SWP);
                        ArrayList<Integer> dims = Table.getArrayDims(ident);
                        Code.addCode(CodeType.LDC, dims.get(dims.size() - 1));
                        Code.addCode(CodeType.MUL);
                        Code.addCode(CodeType.ADD);
                    }

                    if (isLeftValue)
                        Code.addCode(CodeType.ADD);
                    else {
                        if (te.getObj().equals(Obj.OBJ_CONST))
                            Code.addCode(CodeType.LCA);
                        else
                            Code.addCode(CodeType.LAV);
                    }

                } else {    // normal var
                    if (isLeftValue)
                        Code.addCode(CodeType.LDA, Table.getAttrLev(ident), te.getAdr());
                    else {
                        if (te.getObj().equals(Obj.OBJ_CONST))
                            Code.addCode(CodeType.LDC, te.getAdr());
                        else
                            Code.addCode(CodeType.LOD, te.getLev(), te.getAdr());
                    }
                }
            }
        }
    }

    public static LVal analyse(IdentifySymbol identifySymbol) {   // LVal --> Ident { '[' Exp ']' }
        Symbol sym;
        boolean judge = true;
        LVal lVal = new LVal();
        MyString identName = new MyString();
        TableEntry tmpEntry;
        int dims = 0;

        lVal.setIdent(Ident.analyse(identifySymbol, identName));
        // ERROR: check name undefined -- type c
        Error.checkNameUndefined(false, identifySymbol.getPreSym());
        // checking RParams type
        tmpEntry = SymTable.getSymByNameFromAllTab(false, identName.string);
        if (judge) {
            while (judge && identifySymbol.getCurSym().getRegKey() == RegKey.LBRACK) {
                inDims++;
                dims++;
                identifySymbol.getASymbol();
                lVal.addExp(Exp.analyse(identifySymbol));

                // ERROR -- k: ']' needed
                if (identifySymbol.getCurSym().getRegKey() != RegKey.RBRACK)
                    Error.addErrorOutPut(identifySymbol.getPreSym().getRow_Idx() + " k");
                else identifySymbol.getASymbol();
                inDims--;
            }
        }

        // checking RParams type
        if (FuncRParams.checkingType) {
            FuncRParams.tbEntryModel.add(SymTable.createTbEntryModel(tmpEntry, dims));
            if (LVal.inDims == 0)
                FuncRParams.checkingType = false;
        }
        if (judge) {
            identifySymbol.addStr(name);
        }
        return lVal;
    }

    public static boolean isMyFirst(Symbol sym) {
        if (sym.getRegKey() == RegKey.INTCON) {
            return true;
        }
        return false;
    }
}
