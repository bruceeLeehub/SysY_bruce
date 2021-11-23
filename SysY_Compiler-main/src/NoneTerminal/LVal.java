package NoneTerminal;

import MyError.Error;
import ParcelType.*;
import Table.SymTable;
import Table.TableEntry;
import Tables.*;
import WordAnalyse.*;

import java.util.ArrayList;

public class LVal {
    public Ident ident = null;
    public ArrayList<Exp> exp_List = new ArrayList<>();
    public static String name_LVal = "<LVal>";
    public static int in_Dims = 0;

    public static LVal analyse(IdentifySymbol identifySymbol) {
        //      LVal -> Ident { '[' Exp ']' }
        MyString identName = new MyString();
        LVal lVal = new LVal();
        lVal.ident = (Ident.analyse(identifySymbol, identName));
        // c:  check name undefined
        Symbol symbol = identifySymbol.get_PreSym();
        boolean isFunc = false;
        Error.checkNameUndefined(isFunc, symbol);
        // checking RParams type
        int dims = 0;
        TableEntry tmpEntry = SymTable.get_SymByNameInAllTable(false, identName.string);


        while (identifySymbol.get_CurrentSym().getRegKey() == RegKey.LBRACK) {
            dims = dims + 1;
            in_Dims = in_Dims + 1;
            identifySymbol.getASymbol();
            Exp exp = Exp.analyse(identifySymbol);
            lVal.exp_List.add(exp);

            // k: ']' needed
            symbol = identifySymbol.get_CurrentSym();
            RegKey regKey = symbol.getRegKey();
            boolean isRBACK = (regKey == RegKey.RBRACK);
            if (isRBACK) {
                identifySymbol.getASymbol();
            }
            else {
                int rowinx = identifySymbol.get_PreSym().getRow_Idx();
                Error.add_ErrorOutPut(rowinx + " k");
            }
            in_Dims = in_Dims - 1;
        }

        // check RParamList type
        if (FuncRParams.TypeCheck) {
            FuncRParams.tbEntryModel.add(SymTable.createTableEntryModel(tmpEntry, dims));
            if (LVal.in_Dims == 0) {
                FuncRParams.TypeCheck = false;
            }
        }
        identifySymbol.addStr(name_LVal);
        return lVal;
    }

    public static boolean isMyFirst(Symbol sym) {
        RegKey regKey = sym.getRegKey();
        return regKey == RegKey.INTCON;
    }

    public LVal() {
    }

    public void genCode(My_Int value, boolean isLeftValue) {
        if (value != null) {      // need to calculate now ,is const
            if (exp_List.size() == 0) {
                value.my_Int = Table.get_ConstIdentValue(ident);
            } else {    // this is an Array
                ArrayList<Integer> dimValue = new ArrayList<>();
                for (Exp exp : this.exp_List) {
                    exp.genCode(value);
                    dimValue.add(value.my_Int);
                }
                value.my_Int = Table.get_ConstArrayValue(ident, dimValue);
            }
        } else {          // need to get it when running program     not a const
            Tables.TableEntry tableEntry = Table.getAttrTableEntry(ident.getId_Name());
            int lenOfExpList = exp_List.size();
            int lenOfDims = tableEntry.get_Dims();
            //boolean isArray = lenOfExpList < lenOfDims;
            if (lenOfExpList < lenOfDims) {  // isArray
                if (tableEntry.isParam())    // for array para, its value is adr, so LOD
                {
                    int x = Table.get_AttrLev(ident);
                    int y = Table.get_ArrayAdr(ident);
                    Code.addCode(CodeType.LOD, x, y);
                }
                else {
                    int x = Table.get_AttrLev(ident);
                    int y = Table.get_ArrayAdr(ident);
                    Code.addCode(CodeType.LDA, x, y);
                }
                for (Exp exp : this.exp_List) {
                    exp.genCode(null);
                }
                int size = exp_List.size();
                int dimsize = Table.getAttrTableEntry(ident.getId_Name()).get_Dims();
                if (size > 1 &&
                        /* dims are same refer to a value or addr*/
                        size == dimsize) {
                    Code.addCode(CodeType.SWP);
                    ArrayList<Integer> dims = Table.get_ArrayDims(ident);
                    Code.addCode(CodeType.LDC, dims.get(dims.size() - 1));
                    Code.addCode(CodeType.MUL);
                    Code.addCode(CodeType.ADD);
                } else if (size != 0 && size != dimsize) {
                    // dims not equal, send a sub-array
                    ArrTableEntry arrTableEntry = ArrTable.getArrTable().get(Table.getAttrTableEntry(ident.getId_Name()).get_Ref());
                    int lastDim = arrTableEntry.getUpper_Bounds().get(1);
                    Code.addCode(CodeType.LDC, lastDim);
                    Code.addCode(CodeType.MUL);
                    isLeftValue = true;
                } else {
                    return;
                }
                if (isLeftValue) {
                    Code.addCode(CodeType.ADD);
                }
                else {
                    Code.addCode(CodeType.LAV);
                }
            } else {
                if (lenOfDims != 0) {
                    // elements of array
                    if (tableEntry.get_Obj().equals(Obj.CONST_OBJ)) {
                        int y = tableEntry.get_Addr();
                        Code.addCode(CodeType.LDC, y);
                    }
                    else if (tableEntry.isParam())
                        // FOR array para, its value is adr, so LOD
                    {
                        int x = Table.get_AttrLev(ident);
                        int y = Table.get_ArrayAdr(ident);
                        Code.addCode(CodeType.LOD, x, y);
                    }
                    else {
                        int x = Table.get_AttrLev(ident);
                        int y = Table.get_ArrayAdr(ident);
                        Code.addCode(CodeType.LDA, x, y);
                    }
                    for (Exp exp : exp_List) {
                        exp.genCode(null);
                    }
                    int size = exp_List.size();
                    int dimsize = Table.getAttrTableEntry(ident.getId_Name()).get_Dims();
                    if (size > 1 && /* dims are same refer to a value or addr*/
                            size == dimsize) {
                        Code.addCode(CodeType.SWP);
                        ArrayList<Integer> dims = Table.get_ArrayDims(ident);
                        Code.addCode(CodeType.LDC, dims.get(dims.size() - 1));
                        Code.addCode(CodeType.MUL);
                        Code.addCode(CodeType.ADD);
                    }

                    if (isLeftValue) {
                        Code.addCode(CodeType.ADD);
                    }
                    else {
                        boolean isConstObj = (tableEntry.get_Obj().equals(Obj.CONST_OBJ));
                        if (isConstObj) {
                            Code.addCode(CodeType.LCA);
                        }
                        else {
                            Code.addCode(CodeType.LAV);
                        }
                    }

                } else {    // normal var
                    if (isLeftValue) {
                        int x = Table.get_AttrLev(ident);
                        int y = tableEntry.get_Addr();
                        Code.addCode(CodeType.LDA, x, y);
                    }
                    else {
                        if (tableEntry.get_Obj().equals(Obj.CONST_OBJ)) {
                            int y = tableEntry.get_Addr();
                            Code.addCode(CodeType.LDC, y);
                        }
                        else {
                            int x = tableEntry.get_Level();
                            int y = tableEntry.get_Addr();
                            Code.addCode(CodeType.LOD, x,y);
                        }
                    }
                }
            }
        }
    }
}
