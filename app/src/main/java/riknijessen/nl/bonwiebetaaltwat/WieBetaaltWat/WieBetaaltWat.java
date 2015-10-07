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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
                .add("password", username)
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
                .url(baseUrl+"/index.php?page=dashboard")
                .get()
                .build();
        Response response = client.newCall(request).execute();
        Document doc = Jsoup.parse(response.body().string());
        Elements elements = doc.select(".view-lists tbody tr");
        List<WBWList> lists = new ArrayList<>();
        for(Element element : elements) {
            int id = Integer.parseInt(element.select("a").attr("href").replaceAll("[\\D]", ""));
            String title = element.select("a").text();
            lists.add(new WBWList(id,title, element.select("td").get(1).text()));
        }
        return lists;
    }




}
