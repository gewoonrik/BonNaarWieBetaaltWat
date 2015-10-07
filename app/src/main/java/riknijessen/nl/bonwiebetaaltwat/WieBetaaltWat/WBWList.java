package riknijessen.nl.bonwiebetaaltwat.WieBetaaltWat;

import java.util.List;

/**
 * Created by rik on 07/10/15.
 */
public class WBWList {

    public final int id;
    public final String name;
    public final String balance;
    public final List<User> members;

    public WBWList(int id, String name, String balance, List<User> members) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.members = members;
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
