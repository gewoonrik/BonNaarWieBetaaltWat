package riknijessen.nl.bonwiebetaaltwat;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rik on 08/10/15.
 */
public class BonParser {
    public static Map<String, Float> parse(String text)    {
        Map<String, Float> products = new HashMap<>();

        String lines[] = text.split("\\r?\\n");
        for(String line : lines)    {

        }

        return products;
    }

}
