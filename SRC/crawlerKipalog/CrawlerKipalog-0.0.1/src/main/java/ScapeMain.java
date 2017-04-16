import Controller.ControllPage;
import Entity.Category;
import Entity.Url;
import Entity.Var;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by quangminh on 16/03/2017.
 */
public class ScapeMain  {
    public static void main(String args[]) throws IOException {
        try {
            Var.client = new TransportClient()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        ArrayList<Category> urlCategories = new ArrayList<>();

        Document documentInCategoriesPage=ControllPage.getDocumentInCategoriesPage(Url.categories);
        ObjectMapper mapper = new ObjectMapper();
        String json = null;

        Elements elements = documentInCategoriesPage.select("body > main > div > div > div.ui.divided.list.tag-list div.item ");
        for(Element e:elements){
            Category category=new Category();
            category.setImage(e.select("h3 img").attr("abs:src"));
            category.setTitle(e.select("h3 a").html());
            category.setName(e.select("h3 a").html());
            category.setUrlCategory(e.select("h3 a").attr("abs:href"));
            category.setTotalPost(Integer.parseInt(e.select("div.posts").text().replace(" bài viết","")));
            json = mapper.writeValueAsString(category);
            IndexResponse response = Var.client.prepareIndex("kipalog", "categories")
                    .setSource(json)
                    .get();
            ControllPage.LoadPost(category);
        }









    }
}
