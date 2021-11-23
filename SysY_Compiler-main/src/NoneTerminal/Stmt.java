package NoneTerminal;

import MyError.Error;
import NoneTerminal.StmtPack.*;
import ParcelType.*;
import Tables.ConStrTable;
import WordAnalyse.*;

import java.util.ArrayList;

public class Stmt implements BlockItemInter {
    public static int ifStmtCount = 0;
    public static int whileStmtCount = 0;
    public static int elseStmtCount = 0;
    public static String name_stmt = "<Stmt>";

    public static boolean isMyFirst(Symbol symbol) {
        RegKey regKey = symbol.getRegKey();
        boolean isSEMICN = regKey == RegKey.SEMICN;
        boolean isIFTK = regKey == RegKey.IFTK;
        boolean isBREAKTK = regKey == RegKey.BREAKTK;
        boolean isWHILETK = regKey == RegKey.WHILETK;
        boolean isCONTINUETK = regKey == RegKey.CONTINUETK;
        boolean isRETURNTK = regKey == RegKey.RETURNTK;
        boolean isPRINTFTK = regKey == RegKey.PRINTFTK;
        boolean Lval_isMyFirst = LVal.isMyFirst(symbol);
        boolean Block_isMyFirst = Block.isMyFirst(symbol);
        boolean Exp_isMyFirst = Exp.isMyFirst(symbol);
        return isSEMICN || isIFTK || isBREAKTK ||
                isWHILETK || isCONTINUETK || isRETURNTK || isPRINTFTK ||
                Lval_isMyFirst || Block_isMyFirst || Exp_isMyFirst;
    }

    public static boolean assign_Exist(IdentifySymbol identifySymbol) {
        Symbol symbol = identifySymbol.get_CurrentSym();
        int rowPre = symbol.getRow_Idx();
        int rowNow = symbol.getRow_Idx();
        boolean exist = false;
        int count = 0;
        while (symbol != null &&
                symbol.getRegKey() != RegKey.ASSIGN &&
                symbol.getRegKey() != RegKey.SEMICN &&
                rowNow == rowPre) {
            rowPre = symbol.getRow_Idx();
            identifySymbol.getASymbol();
            symbol = identifySymbol.get_CurrentSym();
            rowNow = symbol.getRow_Idx();
            count = count + 1;
        }
        RegKey regKey = symbol.getRegKey();
        if (rowNow != rowPre) {
            exist = false;
        }
        else if (symbol != null && regKey== RegKey.ASSIGN) {
            exist = true;
        }
        identifySymbol.spitSym(count);
        return exist;
    }

    public static Stmt analyse(IdentifySymbol identifySymbol) {
        Block.hasReturnStmt = false;
        Stmt stmt = null;
        // ast Tree node
        boolean judge = true;

        Symbol symbol = identifySymbol.get_CurrentSym();
        RegKey regKey = symbol.getRegKey();
        boolean isIDENFR = regKey == RegKey.IDENFR;
        boolean isLBRACE = regKey == RegKey.LBRACE;
        boolean isIFTK = regKey == RegKey.IFTK;
        boolean isWHILETK = regKey == RegKey.WHILETK;
        boolean isBREAKorCONTINUE = (regKey == RegKey.BREAKTK
                || regKey == RegKey.CONTINUETK);
        boolean isRETURNTK = regKey == RegKey.RETURNTK;
        boolean isPRINTFTK = regKey == RegKey.PRINTFTK;
        if (isIDENFR) {
            if (assign_Exist(identifySymbol)) {
                // LVal '=' 'getint' '(' ')' ';'
                // LVal '=' Exp ';'
                Symbol lvalSym = symbol;
                LVal lVal = LVal.analyse(identifySymbol);
                // h: can't assign a value to const
                Error.checkAssignValueToConst(lvalSym);

                symbol = identifySymbol.get_CurrentSym();
                regKey = symbol.getRegKey();
                judge = regKey == RegKey.ASSIGN;

                if (judge) {
                    symbol = identifySymbol.getASymbol();
                    regKey = symbol.getRegKey();
                    boolean isGETINTTK = regKey == RegKey.GETINTTK;
                    if (isGETINTTK) {
                        symbol = identifySymbol.getASymbol();
                        regKey = symbol.getRegKey();
                        boolean isLPARENT = regKey == RegKey.LPARENT;
                        judge = isLPARENT;

                        identifySymbol.getASymbol();

                        stmt = new ReadStmt(lVal);

                        // j: ')' needed
                        Symbol curSymbol = identifySymbol.get_CurrentSym();
                        regKey = curSymbol.getRegKey();
                        boolean isRPARENT = regKey == RegKey.RPARENT;
                        if (isRPARENT) {
                            symbol = identifySymbol.getASymbol();
                        }
                        else {
                            Symbol preSymbol = identifySymbol.get_PreSym();
                            int rowidx = preSymbol.getRow_Idx();
                            Error.add_ErrorOutPut(rowidx + " j");
                        }

                        // i: ';' needed
                        regKey = symbol.getRegKey();
                        boolean isSEMICN = regKey == RegKey.SEMICN;
                        if (isSEMICN) {
                            identifySymbol.getASymbol();
                        }
                        else {
                            Symbol preSymbol = identifySymbol.get_PreSym();
                            int rowidx = preSymbol.getRow_Idx();
                            Error.add_ErrorOutPut(rowidx + " i");
                        }
                    } else {
                        Exp exp = Exp.analyse(identifySymbol);
                        stmt = new AssignStmt(lVal, exp);
                        symbol = identifySymbol.get_CurrentSym();
                        // i: ';' needed
                        regKey = symbol.getRegKey();
                        boolean isSEMICN = regKey == RegKey.SEMICN;
                        if (!isSEMICN) {
                            Symbol preSymbol = identifySymbol.get_PreSym();
                            int rowidx = preSymbol.getRow_Idx();
                            Error.add_ErrorOutPut(rowidx + " i");
                        }
                        else {
                            identifySymbol.getASymbol();
                        }
                    }
                }
            } else {
                //      -> Exp ';'
                Exp exp = Exp.analyse(identifySymbol);
                stmt = new ExpStmt(exp);
                symbol = identifySymbol.get_CurrentSym();
                regKey = symbol.getRegKey();
                boolean isSEMICN = regKey == RegKey.SEMICN;
                // i: ';' needed
                if (isSEMICN) {
                    identifySymbol.getASymbol();
                }
                else {
                    Symbol preSymbol = identifySymbol.get_PreSym();
                    int rowidx = preSymbol.getRow_Idx();
                    Error.add_ErrorOutPut(rowidx + " i");
                }
            }
        } else if (isLBRACE) {
            // Block
            Block block = Block.analyse(identifySymbol);
            stmt = new BlockStmt(block);
        } else if (isIFTK) {
            // ->  'if' '(' Cond ')' Stmt ['else' Stmt]
            ifStmtCount = ifStmtCount + 1;
            Stmt ifStatement;
            Stmt elseStatement = null;
            Cond condition = null;
            symbol = identifySymbol.getASymbol();
            regKey = symbol.getRegKey();
            judge = regKey == RegKey.LPARENT;

            if (judge) {
                identifySymbol.getASymbol();
                condition = Cond.analyse(identifySymbol);
            }
            // j: ')' needed
            Symbol curSymbol = identifySymbol.get_CurrentSym();
            regKey = curSymbol.getRegKey();
            if (regKey == RegKey.RPARENT) {
                identifySymbol.getASymbol();
            }
            else {
                Symbol preSymbol = identifySymbol.get_PreSym();
                int rowidx = preSymbol.getRow_Idx();
                Error.add_ErrorOutPut(rowidx + " j");
            }
            ifStatement = Stmt.analyse(identifySymbol);

            curSymbol = identifySymbol.get_CurrentSym();
            regKey = curSymbol.getRegKey();
            if (regKey == RegKey.ELSETK) {
                elseStmtCount = elseStmtCount + 1;
                symbol = identifySymbol.getASymbol();
                elseStatement = Stmt.analyse(identifySymbol);
                elseStmtCount = elseStmtCount - 1;
            }
            ifStmtCount = ifStmtCount - 1;
            stmt = new IfStmt(condition, ifStatement, elseStatement);
        } else if (isWHILETK) {
            // --> 'while' '(' Cond ')' Stmt
            whileStmtCount = whileStmtCount + 1;
            Cond condition = null;
            symbol = identifySymbol.getASymbol();
            regKey = symbol.getRegKey();
            judge = regKey == RegKey.LPARENT;
            if (judge) {
                identifySymbol.getASymbol();
                condition = Cond.analyse(identifySymbol);
            }
            // j: ')' needed
            Symbol curSymbol = identifySymbol.get_CurrentSym();
            regKey = curSymbol.getRegKey();
            if (regKey == RegKey.RPARENT) {
                identifySymbol.getASymbol();
            }
            else {
                Symbol preSymbol = identifySymbol.get_PreSym();
                int rowidx = preSymbol.getRow_Idx();
                Error.add_ErrorOutPut(rowidx + " j");
            }
            Stmt whileStmt = Stmt.analyse(identifySymbol);
            whileStmtCount = whileStmtCount - 1;
            stmt = new WhileStmt(condition, whileStmt);
        } else if (isBREAKorCONTINUE) {
            // --> 'break' ';'
            // --> 'continue' ';'
            regKey = symbol.getRegKey();
            if (regKey == RegKey.BREAKTK) {
                stmt = new BreakStmt();
            }
            else if (regKey == RegKey.CONTINUETK){
                stmt = new ContinueStmt();
            }

            // m:   there is no loop
            regKey = symbol.getRegKey();
            if (Stmt.whileStmtCount == 0 &&
                    (regKey == RegKey.BREAKTK || regKey == RegKey.CONTINUETK)) {
                int rowidx = symbol.getRow_Idx();
                Error.add_ErrorOutPut(rowidx + " m");
            }
            symbol = identifySymbol.getASymbol();
            regKey = symbol.getRegKey();
            //i: ';' needed
            if (regKey == RegKey.SEMICN) {
                identifySymbol.getASymbol();
            }
            else {
                Symbol preSymbol = identifySymbol.get_PreSym();
                int rowidx = preSymbol.getRow_Idx();
                Error.add_ErrorOutPut(rowidx + " i");
            }
        } else if (isRETURNTK) {
            // --> 'return' [Exp] ';'
            Exp exp = null;
            Symbol retSym = symbol;
            boolean noStmtLeft = Block.block_Layers == 1;
            noStmtLeft = noStmtLeft && whileStmtCount == 0;
            noStmtLeft = noStmtLeft && ifStmtCount == 0;
            noStmtLeft = noStmtLeft && elseStmtCount == 0;
            if (noStmtLeft) {
                Block.hasReturnStmt = true;
            }
            symbol = identifySymbol.getASymbol();
            regKey = symbol.getRegKey();
            if (regKey == RegKey.RBRACE) {
                // i: ';' needed
                Symbol curSymbol = identifySymbol.get_CurrentSym();
                regKey = curSymbol.getRegKey();
                if (regKey != RegKey.SEMICN) {
                    Symbol preSymbol = identifySymbol.get_PreSym();
                    int rowidx = preSymbol.getRow_Idx();
                    Error.add_ErrorOutPut(rowidx + " i");
                }
            } else if (symbol.getRegKey() != RegKey.SEMICN) {
                exp = Exp.analyse(identifySymbol);
                // -> f:  void func have  return value
                if (FuncDef.InFuncDef && !FuncDef.haveReturnValue) {
                    int rowidx = retSym.getRow_Idx();
                    Error.add_ErrorOutPut(rowidx + " f");
                }
                // i: ';' needed
                Symbol curSymbol = identifySymbol.get_CurrentSym();
                regKey = curSymbol.getRegKey();
                if (regKey != RegKey.SEMICN) {
                    Symbol preSymbol = identifySymbol.get_PreSym();
                    int rowidx = preSymbol.getRow_Idx();
                    Error.add_ErrorOutPut(rowidx + " i");
                }
                else {
                    identifySymbol.getASymbol();
                }
            } else {
                //is semicn
                identifySymbol.getASymbol();
            }
            stmt = new ReturnExp(exp);
        } else if (isPRINTFTK) {
            // -> 'printf' '(' FormatString {',' Exp} ')' ';'
            String formatString = "";
            int actualExpCount = 0;
            My_Int rightExpCount = new My_Int();
            Symbol error_Sym = symbol;
            ArrayList<Exp> exp_List = new ArrayList<>();

            symbol = identifySymbol.getASymbol();
            regKey = symbol.getRegKey();
            judge = regKey == RegKey.LPARENT;
            if (judge) {
                symbol = identifySymbol.getASymbol();
                formatString = symbol.get_IdentName();
                regKey = symbol.getRegKey();
                judge = regKey == RegKey.STRCON;
                // a: formatString error
                Error.checkFormatStr(symbol, rightExpCount);
            }
            if (judge) {
                symbol = identifySymbol.getASymbol();
                while (symbol.getRegKey() == RegKey.COMMA) {
                    actualExpCount = actualExpCount + 1;
                    identifySymbol.getASymbol();
                    Exp exp = Exp.analyse(identifySymbol);
                    exp_List.add(exp);
                    symbol = identifySymbol.get_CurrentSym();
                }
                // l: rightExpCount != actualExpCount
                if (actualExpCount != rightExpCount.my_Int) {
                    int row_idx = error_Sym.getRow_Idx();
                    Error.add_ErrorOutPut(row_idx + " l");
                }
            }

            stmt = new PrintStmt(ConStrTable.addConString(formatString), exp_List);

            // j: ')' needed
            Symbol curSymbol = identifySymbol.get_CurrentSym();
            regKey = curSymbol.getRegKey();
            if (regKey == RegKey.RPARENT) {
                symbol = identifySymbol.getASymbol();
            }
            else {
                Symbol preSymbol = identifySymbol.get_PreSym();
                int rowidx = preSymbol.getRow_Idx();
                Error.add_ErrorOutPut(rowidx + " j");
            }
            // i: ';' missing
            regKey = symbol.getRegKey();
            if (regKey != RegKey.SEMICN) {
                Symbol preSymbol = identifySymbol.get_PreSym();
                int rowidx = preSymbol.getRow_Idx();
                Error.add_ErrorOutPut(rowidx + " i");
            }
            else {
                identifySymbol.getASymbol();
            }

        } else if (symbol.getRegKey() != RegKey.SEMICN) {
            // -> [Exp] ';'
            Exp exp = Exp.analyse(identifySymbol);
            stmt = new ExpStmt(exp);

            symbol = identifySymbol.get_CurrentSym();
            regKey = symbol.getRegKey();
            // i: ';' needed
            if (regKey != RegKey.SEMICN) {
                Symbol preSymbol = identifySymbol.get_PreSym();
                int rowidx = preSymbol.getRow_Idx();
                Error.add_ErrorOutPut(rowidx + " i");
            }
            else {
                identifySymbol.getASymbol();
            }
        } else {  // ';'
            stmt = new ExpStmt(null);
            identifySymbol.getASymbol();
        }

        if (judge) {
            identifySymbol.addStr(name_stmt);
        }

        return stmt;
    }

    public void genCode(){
        //
        // TODO: calling children's genCode...nothing
    }
}
