package NoneTerminal;

import MyError.Error;
import ParcelType.My_Int;
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

    public void genCode(My_Int value, boolean isLeftValue) {
        if (value != null) {      // is const, you need to calculate it right now
            if (expList.size() != 0) {    // this is an Array
                ArrayList<Integer> dimValue = new ArrayList<>();
                for (Exp exp : expList) {
                    exp.genCode(value);
                    dimValue.add(value.my_Int);
                }
                value.my_Int = Table.get_ConstArrayValue(ident, dimValue);
            } else {
                value.my_Int = Table.get_ConstIdentValue(ident);
            }
        } else {          // not a const you need to get it when running program
            Tables.TableEntry te = Table.getAttrTableEntry(ident.getIdentName());
            int lenOfExpList = expList.size();
            int lenOfDims = te.get_Dims();
            boolean isArray = lenOfExpList < lenOfDims;
            if (isArray) {
                if (te.isParam())    // for array para, its value is adr, so LOD
                    Code.addCode(CodeType.LOD, Table.get_AttrLev(ident), Table.get_ArrayAdr(ident));
                else
                    Code.addCode(CodeType.LDA, Table.get_AttrLev(ident), Table.get_ArrayAdr(ident));
                for (Exp exp : expList) {
                    exp.genCode(null);
                }
                if (expList.size() > 1 && /* dims are same refer to a value or addr*/
                        expList.size() == Table.getAttrTableEntry(ident.getIdentName()).get_Dims()) {
                    Code.addCode(CodeType.SWP);
                    ArrayList<Integer> dims = Table.get_ArrayDims(ident);
                    Code.addCode(CodeType.LDC, dims.get(dims.size() - 1));
                    Code.addCode(CodeType.MUL);
                    Code.addCode(CodeType.ADD);
                } else if (expList.size() != 0 && expList.size() != Table.getAttrTableEntry(ident.getIdentName()).get_Dims()) { // dims not equal, send a sub-array
                    int lastDim = ArrTable.getArrTable().get(Table.getAttrTableEntry(ident.getIdentName()).get_Ref()).getUpper_Bounds().get(1);
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
                    if (te.get_Obj().equals(Obj.CONST_OBJ))
                        Code.addCode(CodeType.LDC, te.get_Addr());
                    else if (te.isParam())    // for array para, its value is adr, so LOD
                        Code.addCode(CodeType.LOD, Table.get_AttrLev(ident), Table.get_ArrayAdr(ident));
                    else
                        Code.addCode(CodeType.LDA, Table.get_AttrLev(ident), Table.get_ArrayAdr(ident));
                    for (Exp exp : expList) {
                        exp.genCode(null);
                    }

                    if (expList.size() > 1 && /* dims are same refer to a value or addr*/
                            expList.size() == Table.getAttrTableEntry(ident.getIdentName()).get_Dims()) {
                        Code.addCode(CodeType.SWP);
                        ArrayList<Integer> dims = Table.get_ArrayDims(ident);
                        Code.addCode(CodeType.LDC, dims.get(dims.size() - 1));
                        Code.addCode(CodeType.MUL);
                        Code.addCode(CodeType.ADD);
                    }

                    if (isLeftValue)
                        Code.addCode(CodeType.ADD);
                    else {
                        if (te.get_Obj().equals(Obj.CONST_OBJ))
                            Code.addCode(CodeType.LCA);
                        else
                            Code.addCode(CodeType.LAV);
                    }

                } else {    // normal var
                    if (isLeftValue)
                        Code.addCode(CodeType.LDA, Table.get_AttrLev(ident), te.get_Addr());
                    else {
                        if (te.get_Obj().equals(Obj.CONST_OBJ))
                            Code.addCode(CodeType.LDC, te.get_Addr());
                        else
                            Code.addCode(CodeType.LOD, te.get_Level(), te.get_Addr());
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
        Error.checkNameUndefined(false, identifySymbol.get_PreSym());
        // checking RParams type
        tmpEntry = SymTable.get_SymByNameInAllTable(false, identName.string);
        if (judge) {
            while (judge && identifySymbol.get_CurrentSym().getRegKey() == RegKey.LBRACK) {
                inDims++;
                dims++;
                identifySymbol.getASymbol();
                lVal.addExp(Exp.analyse(identifySymbol));

                // ERROR -- k: ']' needed
                if (identifySymbol.get_CurrentSym().getRegKey() != RegKey.RBRACK)
                    Error.addErrorOutPut(identifySymbol.get_PreSym().getRow_Idx() + " k");
                else identifySymbol.getASymbol();
                inDims--;
            }
        }

        // checking RParams type
        if (FuncRParams.checkingType) {
            FuncRParams.tbEntryModel.add(SymTable.createTableEntryModel(tmpEntry, dims));
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
