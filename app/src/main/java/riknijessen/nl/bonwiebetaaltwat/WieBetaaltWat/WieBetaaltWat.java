package riknijessen.nl.bonwiebetaaltwat.WieBetaaltWat;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by rik on 07/10/15.
 */
public class WieBetaaltWat {
    private final static String baseUrl = "https://www.wiebetaaltwat.nl";
    private OkHttpClient client;
    private boolean loggedIn = false;
    private String username;
    public WieBetaaltWat() {
        client = new OkHttpClient();
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        client.setCookieHandler(cookieManager);
    }

    public void login(final String username, final String password) throws IOException {
        RequestBody formBody = new FormEncodingBuilder()
                .add("action", "login")
                .add("username", username)
                .add("password", password)
                .add("login_submit", "Inloggen")
                .build();

        this.username = username;
        Request request = new Request.Builder()
                .url(baseUrl+"/index.php")
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();

        if(!response.body().string().contains("Uitloggen"))  {
            loggedIn = true;
        }

    }


    public List<WBWList> getLists() throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + "/index.php?page=dashboard")
                .get()
                .build();
        Response response = client.newCall(request).execute();
        Document doc = Jsoup.parse(response.body().string());
        Elements elements = doc.select(".view-lists tbody tr");
        List<WBWList> lists = new ArrayList<>();
        for(Element element : elements) {
            int id = Integer.parseInt(element.select("a").attr("href").replaceAll("[\\D]", ""));
            String title = element.select("a").text();
            List<User> members = getUsers(id);
            lists.add(new WBWList(id,title, element.select("td").get(1).text(), members));
        }
        return lists;
    }

    private List<User> getUsers(int listId) throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + "/index.php?lid="+listId+"&page=transaction&type=add")
                .get()
                .build();
        Response response = client.newCall(request).execute();
        Document doc = Jsoup.parse(response.body().string());
        Elements users = doc.select("#payment_by option");
        List<User> resUsers = new ArrayList<>();
        for(Element user : users) {
            int id = Integer.parseInt(user.attr("value"));
            String name = user.text();
            resUsers.add(new User(id, name));
        }
        return resUsers;
    }

    public void addPayment(WBWList list, Payment payment) throws IOException {
        FormEncodingBuilder builder =
                new FormEncodingBuilder()
                .add("action", "add_transaction")
                .add("lid", String.valueOf(list.id))
                .add("payment_by", String.valueOf(payment.paidBy.id))
                .add("description", payment.description)
                .add("submit_add", "Verwerken")
                .add("amount", String.valueOf(payment.amount))
                .add("date", new SimpleDateFormat("dd-MM-yyyy").format(payment.date));
        for (Map.Entry<User, Integer> pair : payment.factors.entrySet()) {
            builder.add("factor[" + pair.getKey().id + "]", String.valueOf(pair.getValue()));
        }
        Request request = new Request.Builder()
                .url(baseUrl+"/index.php")
                .post(builder.build())
                .build();
        Response response = client.newCall(request).execute();

    }


}