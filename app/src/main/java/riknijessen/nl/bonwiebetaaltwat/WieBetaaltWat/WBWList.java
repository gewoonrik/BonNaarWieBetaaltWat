package riknijessen.nl.bonwiebetaaltwat.WieBetaaltWat;

/**
 * Created by rik on 07/10/15.
 */
public class WBWList {

    public final int id;
    public final String name;
    public final String balance;

    public WBWList(int id, String name, String balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "WBWList{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", balance='" + balance + '\'' +
                '}';
    }
}
