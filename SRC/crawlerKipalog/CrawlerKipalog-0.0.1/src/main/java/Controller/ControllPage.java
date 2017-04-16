package Controller;

import Entity.Category;
import Entity.Url;
import Entity.Var;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by quangminh on 17/03/2017.
 */
public class ControllPage {
    public static Document getDocumentInCategoriesPage(String url) {
        try {
            Document documentCategories = Jsoup.connect(url).timeout(7000)
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .header("Accept-Encoding", "gzip, deflate, sdch, br")
                    .header("Accept-Language", "en-US,en;q=0.8")
                    .header("Cache-Control", "max-age=0")
                    .header("Connection", "keep-alive")
                    .header("Cookie", "remember_user_token=W1siQzRaS2FDNVJTWHdRYUpTMDloNkxhZyJdLCIkMmEkMTAkcmhaeG94V3kyd3d4bDlCcU1pMHdNLiJd--4e930afa8141597032eda46605e40754f74d662b;")
                    .header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36").get();
            return documentCategories;
        } catch (IOException e) {
            System.out.println("Cant connect " + url);
            e.printStackTrace();
        }
        return null;

    }

    public static List<String> urlPostByCategory(String urlCategory){
        List<String> urlArr=new ArrayList<>();

        HttpClient client = HttpClientBuilder.create().build();

        HttpGet getResource=new HttpGet(urlCategory);

        getResource.addHeader("Accept-Language","vi-VN,vi;q=0.8,fr-FR;q=0.6,fr;q=0.4,en-US;q=0.2,en;q=0.2");
        getResource.addHeader("Accept-Encoding","gzip, deflate, sdch, br");
        getResource.addHeader("Cache-Control","max-age=0");
        getResource.addHeader("Connection","keep-alive");
        getResource.addHeader("X-Requested-With"," XMLHttpRequest");
        getResource.addHeader("User-Agent"," Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");

        HttpResponse responseOfResourceServer = null;
        try {
            responseOfResourceServer = client.execute(getResource);
            HttpEntity entity = responseOfResourceServer.getEntity();
            String response = EntityUtils.toString(entity, "UTF-8");
            JSONArray jsonArray = new JSONArray(response);
            for (int i= 0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String urlPath=jsonObject.getString("path");
                urlArr.add(Url.baseUrl+urlPath);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return  urlArr;
    }

    public static void LoadPost(Category category){
        LoadPostThread run=new LoadPostThread();
        run.setCategory(category);
        Thread thread=new Thread(run);
        thread.start();
    }




}
