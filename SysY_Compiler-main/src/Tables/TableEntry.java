package Tables;

public class TableEntry {
    private Obj obj;
    private Typ typ;
    private int dims;
    private int ref;    // for array - abtab & func - btab
    private int lev;    // for level
    private int adr;    // for var - relative address & func - entry address & const - values
    private boolean isPara;


    public TableEntry(Obj obj, Typ typ, int dims, int ref, int lev, int adr) {
        this.obj = obj;
        this.typ = typ;
        this.dims = dims;
        this.ref = ref;
        this.lev = lev;
        this.adr = adr;
        this.isPara = false;
    }

    public TableEntry(Obj obj, Typ typ, int dims, int ref, int lev, int adr,
                      boolean isPara) {
        this.obj = obj;
        this.typ = typ;
        this.dims = dims;
        this.ref = ref;
        this.lev = lev;
        this.adr = adr;
        this.isPara = isPara;
    }

    public boolean isPara(){
        return this.isPara;
    }

    public Obj getObj() {
        return obj;
    }

    public Typ getTyp() {
        return typ;
    }

    public int getDims() {
        return dims;
    }

    public int getRef() {
        return ref;
    }

    public int getLev() {
        return lev;
    }

    public int getAdr() {
        if(this.obj.equals(Obj.CONST_OBJ) && this.dims != 0)
            return this.ref;
        return adr;
    }
}
