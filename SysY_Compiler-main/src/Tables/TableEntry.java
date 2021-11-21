package Tables;

public class TableEntry {
    private final int dims;
    private final boolean isParam;
    private final int ref;    // FOR  ARRAY - ABTAB & FUNC - BTAB
    private final int level;    // FOR LEVEL
    private final Obj obj;
    private final Typ typ;
    private final int addr;    // FOR VAR - RELATIVE ADDRESS & func - entry address & const - values


    public TableEntry(Obj obj, Typ typ, int dims, int ref, int level, int addr,
                      boolean isParam) {
        this.dims = dims;
        this.ref = ref;
        this.level = level;
        this.addr = addr;
        this.isParam = isParam;
        this.obj = obj;
        this.typ = typ;
    }

    public int get_Level() {
        return this.level;
    }

    public int get_Addr() {
        boolean isConstObj = (this.obj.equals(Obj.CONST_OBJ));
        boolean dimsNotZero = (this.dims != 0);
        if(isConstObj && dimsNotZero) {
            return this.ref;
        } else {
            return this.addr;
        }
    }

    public int get_Dims() {
        return this.dims;
    }

    public Typ get_Typ() {
        return this.typ;
    }

    public int get_Ref() {
        return this.ref;
    }

    public boolean isParam(){
        return this.isParam;
    }

    public Obj get_Obj() {
        return this.obj;
    }


    public TableEntry(Obj obj, Typ type, int dims, int reff, int level, int addr) {
        this.isParam = false;
        this.dims = dims;
        this.ref = reff;
        this.level = level;
        this.obj = obj;
        this.typ = type;
        this.addr = addr;
    }
}
