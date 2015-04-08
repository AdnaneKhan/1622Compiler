package SymTable;

/**
 * Created by adnankhan on 4/1/15.
 */
public class KeyWrapper {

    public String toHash;
    public int type;

    public KeyWrapper(String id, int type) {
        toHash = id;
        this.type= type;
    }


    // What this does is hashes the string and adds either 1, 2 or 3 to the resultant hashcode.
    // This allows identical strings to hash to identical values only if they have samme var and
    // samme type
    @Override
    public int hashCode() {
        return toHash.hashCode() + type;
    }


    public boolean equals(Object toCheck) {
        if ((toCheck instanceof KeyWrapper) && (this.type == ((KeyWrapper) toCheck).type) && this.toHash.equals(((KeyWrapper) toCheck).toHash)) {
            return true;
        }
        return false;
    }

}
