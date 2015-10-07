package riknijessen.nl.bonwiebetaaltwat.WieBetaaltWat;

/**
 * Created by rik on 07/10/15.
 */
public class User {
    public final int id;
    public final String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        return !(name != null ? !name.equals(user.name) : user.name != null);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
