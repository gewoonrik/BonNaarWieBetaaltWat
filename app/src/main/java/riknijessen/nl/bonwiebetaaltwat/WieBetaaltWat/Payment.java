package riknijessen.nl.bonwiebetaaltwat.WieBetaaltWat;

import java.util.Date;
import java.util.Map;

/**
 * Created by rik on 07/10/15.
 */
public class Payment {
    public final User paidBy;
    public final String description;
    public final float amount;
    public final Map<User, Integer> factors;
    public final Date date;

    public Payment(User paidBy, String description, float amount, Map<User, Integer> factors, Date date) {
        this.paidBy = paidBy;
        this.description = description;
        this.amount = amount;
        this.factors = factors;
        this.date = date;
    }
}
