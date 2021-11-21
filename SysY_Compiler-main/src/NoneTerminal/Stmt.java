package NoneTerminal;

import MyError.Error;
import NoneTerminal.StmtPack.*;
import ParcelType.My_Int;
import Tables.ConStrTable;
import WordAnalyse.IdentifySymbol;
import WordAnalyse.RegKey;
import WordAnalyse.Symbol;

import java.util.ArrayList;

public class Stmt implements BlockItemInter {
    public static String name = "<Stmt>";
    public static int whileStmtCnt = 0;
    public static int ifStmtCnt = 0;
    public static int elseStmtCnt = 0;

    public void genCode(){
        // TODO: this is nothing calling children's genCode...
    }

    public static Stmt analyse(IdentifySymbol identifySymbol) {
        Symbol sym;
        boolean judge = true;
        Stmt stmt = null;  // ast Tree node

        Block.hasReturnStmt = false;
        sym = identifySymbol.getCurSym();
        if (sym.getRegKey() == RegKey.IDENFR) {
            if (assignExist(identifySymbol)) {  // LVal '=' Exp ';' | LVal '=' 'getint' '(' ')' ';'
                Symbol lvaSym = sym;
                LVal lVal = LVal.analyse(identifySymbol);
                if (judge) {
                    // ERROR -- h: you can not assign a value to const type
                    Error.checkAssignValueToConst(lvaSym);
                    sym = identifySymbol.getCurSym();
                    judge &= sym.getRegKey() == RegKey.ASSIGN;

                }
                if (judge) {
                    sym = identifySymbol.getASymbol();
                    if (sym.getRegKey() == RegKey.GETINTTK) {
                        sym = identifySymbol.getASymbol();
                        judge &= sym.getRegKey() == RegKey.LPARENT;
                        identifySymbol.getASymbol();

                        stmt = new ReadStmt(lVal);

                        // ERROR -- j: ')' needed
                        if (identifySymbol.getCurSym().getRegKey() != RegKey.RPARENT)
                            Error.addErrorOutPut(identifySymbol.getPreSym().getRow_Idx() + " j");
                        else
                            sym = identifySymbol.getASymbol();

                        // ERROR -- i: ';' needed
                        if (sym.getRegKey() != RegKey.SEMICN)
                            Error.addErrorOutPut(identifySymbol.getPreSym().getRow_Idx() + " i");
                        else
                            identifySymbol.getASymbol();

                    } else {
                        stmt = new AssignStmt(lVal, Exp.analyse(identifySymbol));
                        if (judge) {
                            sym = identifySymbol.getCurSym();
                            // ERROR -- i: ';' needed
                            if (sym.getRegKey() != RegKey.SEMICN)
                                Error.addErrorOutPut(identifySymbol.getPreSym().getRow_Idx() + " i");
                            else
                                identifySymbol.getASymbol();
                        }
                    }
                }
            } else { // Exp ';'
                stmt = new ExpStmt(Exp.analyse(identifySymbol));
                if (judge) {
                    sym = identifySymbol.getCurSym();
                    // ERROR -- i: ';' needed
                    if (sym.getRegKey() != RegKey.SEMICN)
                        Error.addErrorOutPut(identifySymbol.getPreSym().getRow_Idx() + " i");
                    else
                        identifySymbol.getASymbol();
                }
            }
        } else if (sym.getRegKey() == RegKey.LBRACE) { // Block
            stmt = new BlockStmt(Block.analyse(identifySymbol));
        } else if (sym.getRegKey() == RegKey.IFTK) {    // 'if' '(' Cond ')' Stmt ['else' Stmt]
            Cond cond = null;
            Stmt ifStmt = null;
            Stmt elseStmt = null;
            ifStmtCnt++;
            sym = identifySymbol.getASymbol();
            judge &= sym.getRegKey() == RegKey.LPARENT;
            if (judge) {
                identifySymbol.getASymbol();
                cond = Cond.analyse(identifySymbol);
            }
            // ERROR -- j: ')' needed
            if (identifySymbol.getCurSym().getRegKey() != RegKey.RPARENT)
                Error.addErrorOutPut(identifySymbol.getPreSym().getRow_Idx() + " j");
            else
                identifySymbol.getASymbol();
            ifStmt = Stmt.analyse(identifySymbol);

            if (identifySymbol.getCurSym().getRegKey() == RegKey.ELSETK) {
                elseStmtCnt++;
                sym = identifySymbol.getASymbol();
                elseStmt = Stmt.analyse(identifySymbol);
                elseStmtCnt--;
            }
            ifStmtCnt--;

            stmt = new IfStmt(cond, ifStmt, elseStmt);
        } else if (sym.getRegKey() == RegKey.WHILETK) {    // 'while' '(' Cond ')' Stmt
            Cond cond = null;
            Stmt whileStmt = null;
            whileStmtCnt++;
            sym = identifySymbol.getASymbol();
            judge &= sym.getRegKey() == RegKey.LPARENT;
            if (judge) {
                identifySymbol.getASymbol();
                cond = Cond.analyse(identifySymbol);
            }
            // ERROR -- j: ')' needed
            if (identifySymbol.getCurSym().getRegKey() != RegKey.RPARENT)
                Error.addErrorOutPut(identifySymbol.getPreSym().getRow_Idx() + " j");
            else
                identifySymbol.getASymbol();
            whileStmt = Stmt.analyse(identifySymbol);

            whileStmtCnt--;
            stmt = new WhileStmt(cond, whileStmt);
        } else if (sym.getRegKey() == RegKey.BREAKTK ||
                sym.getRegKey() == RegKey.CONTINUETK) {   // 'break' ';' | 'continue' ';'

            if (sym.getRegKey() == RegKey.BREAKTK)
                stmt = new BreakStmt();
            else stmt = new ContinueStmt();

            // ERROR: 'break' or 'continue' appeared when there is no loop -- type m
            if (Stmt.whileStmtCnt == 0 &&
                    (sym.getRegKey() == RegKey.BREAKTK || sym.getRegKey() == RegKey.CONTINUETK))
                Error.addErrorOutPut(sym.getRow_Idx() + " m");
            sym = identifySymbol.getASymbol();

            // ERROR -- i: ';' needed
            if (sym.getRegKey() != RegKey.SEMICN)
                Error.addErrorOutPut(identifySymbol.getPreSym().getRow_Idx() + " i");
            else
                identifySymbol.getASymbol();

        } else if (sym.getRegKey() == RegKey.RETURNTK) {  // 'return' [Exp] ';'
            Symbol retSym = sym;
            Exp exp = null;
            if (Block.block_Layers == 1 && whileStmtCnt == 0 && ifStmtCnt == 0 && elseStmtCnt == 0)
                Block.hasReturnStmt = true;
            sym = identifySymbol.getASymbol();
            if (sym.getRegKey() == RegKey.RBRACE) {
                // ERROR -- i: ';' needed
                if (identifySymbol.getCurSym().getRegKey() != RegKey.SEMICN)
                    Error.addErrorOutPut(identifySymbol.getPreSym().getRow_Idx() + " i");
            } else if (sym.getRegKey() != RegKey.SEMICN) {
                exp = Exp.analyse(identifySymbol);
                if (judge) {
                    // ERROR -- f: a void func have a return value
                    if (FuncDef.isInFuncDef && FuncDef.haveRetVal == false)
                        Error.addErrorOutPut(retSym.getRow_Idx() + " f");
                }
                if (judge) {
                    // ERROR -- i: ';' needed
                    if (identifySymbol.getCurSym().getRegKey() != RegKey.SEMICN)
                        Error.addErrorOutPut(identifySymbol.getPreSym().getRow_Idx() + " i");
                    else
                        identifySymbol.getASymbol();
                }
            } else {
                identifySymbol.getASymbol();
            }
            stmt = new ReturnExp(exp);
        } else if (sym.getRegKey() == RegKey.PRINTFTK) {  // 'printf' '(' FormatString {',' Exp} ')' ';'
            My_Int numOfExpExpected = new My_Int();
            int numOfExpActually = 0;
            Symbol errorSym = sym;
            String formatString = "";
            ArrayList<Exp> expList = new ArrayList<>();

            sym = identifySymbol.getASymbol();
            judge &= sym.getRegKey() == RegKey.LPARENT;
            if (judge) {
                sym = identifySymbol.getASymbol();
                judge &= sym.getRegKey() == RegKey.STRCON;
                formatString = sym.get_IdentName();
                // ERROR: check formatString error of type a
                Error.checkFormatStr(sym, numOfExpExpected);
            }
            if (judge) {
                sym = identifySymbol.getASymbol();
                while (judge && sym.getRegKey() == RegKey.COMMA) {
                    numOfExpActually++;
                    identifySymbol.getASymbol();
                    expList.add(Exp.analyse(identifySymbol));
                    sym = identifySymbol.getCurSym();
                }
                // ERROR: numOfExpExpected not matches numOfExpActually -- type l
                if (numOfExpActually != numOfExpExpected.my_Int)
                    Error.addErrorOutPut(errorSym.getRow_Idx() + " l");
            }

            stmt = new PrintStmt(ConStrTable.addConString(formatString), expList);

            // ERROR -- j: ')' needed
            if (identifySymbol.getCurSym().getRegKey() != RegKey.RPARENT)
                Error.addErrorOutPut(identifySymbol.getPreSym().getRow_Idx() + " j");
            else
                sym = identifySymbol.getASymbol();
            // ERROR -- i: ';' needed
            if (sym.getRegKey() != RegKey.SEMICN)
                Error.addErrorOutPut(identifySymbol.getPreSym().getRow_Idx() + " i");
            else
                identifySymbol.getASymbol();

        } else if (sym.getRegKey() != RegKey.SEMICN) {  // [Exp] ';'
            stmt = new ExpStmt(Exp.analyse(identifySymbol));
            if (judge) {
                sym = identifySymbol.getCurSym();
                // ERROR -- i: ';' needed
                if (sym.getRegKey() != RegKey.SEMICN)
                    Error.addErrorOutPut(identifySymbol.getPreSym().getRow_Idx() + " i");
                else
                    identifySymbol.getASymbol();
            }
        } else {  // ';'
            stmt = new ExpStmt(null);
            identifySymbol.getASymbol();
        }

        if (judge) {
            identifySymbol.addStr(name);
        }

        return stmt;
    }

    public static boolean isMyFirst(Symbol sym) {
        if ((sym.getRegKey() == RegKey.SEMICN ||
                sym.getRegKey() == RegKey.IFTK ||
                sym.getRegKey() == RegKey.BREAKTK ||
                sym.getRegKey() == RegKey.WHILETK ||
                sym.getRegKey() == RegKey.CONTINUETK ||
                sym.getRegKey() == RegKey.RETURNTK ||
                sym.getRegKey() == RegKey.PRINTFTK ||
                LVal.isMyFirst(sym) || Block.isMyFirst(sym) || Exp.isMyFirst(sym))) {
            return true;
        }
        return false;
    }

    public static boolean assignExist(IdentifySymbol identifySymbol) {
        int cnt = 0;
        Symbol sym = identifySymbol.getCurSym();
        boolean exist = false;
        int rowPre = sym.getRow_Idx(), rowNow = sym.getRow_Idx();
        while (sym != null &&
                sym.getRegKey() != RegKey.ASSIGN &&
                sym.getRegKey() != RegKey.SEMICN &&
                rowNow == rowPre) {
            rowPre = sym.getRow_Idx();
            identifySymbol.getASymbol();
            sym = identifySymbol.getCurSym();
            cnt++;
            rowNow = sym.getRow_Idx();
        }
        if (rowNow != rowPre)
            exist = false;
        else if (sym != null && sym.getRegKey() == RegKey.ASSIGN)
            exist = true;
        identifySymbol.spitSym(cnt);
        return exist;
    }
}
