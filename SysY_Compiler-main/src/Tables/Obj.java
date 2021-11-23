package Tables;

public enum Obj {
    CONST_OBJ, VAR_OBJ, FUNC_OBJ;

    public static Obj getConstObj() {
        return CONST_OBJ;
    }

    public static Obj getVarObj() {
        return VAR_OBJ;
    }

    public static Obj getFuncObj() {
        return FUNC_OBJ;
    }
}
