
import Controller.ControllPage;
import Entity.Post;
import Entity.Category;
import Entity.Var;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.hateoas.alps.Doc;
import Entity.Category;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by quangminh on 05/10/2016.
 */
public class ScapeMain {

    public static void main(String[] args) throws IOException {
        Var.client = new TransportClient()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));


        ObjectMapper mapper = new ObjectMapper();
        int pageCount = 1;
        ArrayList<String> urlCategories = new ArrayList<>();
        while (true) {
            Document response = ControllPage.getDocumentInCategoriesPage("https://viblo.asia/tags?page=" + pageCount);
            Elements elements= response.select("div.col-sm-6  div.tag-details a");
            for (Element e : elements) {
                urlCategories.add(e.attr("abs:href"));
                System.out.println(e.attr("abs:href"));
            }
            if (pageCount == 5)
                break;
            pageCount++;

        }
        //set k to number of thread
        int k=30;
        for(int i=0;i<k;i++){
            ArrayList<String> miniUrlArr=new ArrayList<>();
            if(i>urlCategories.size()){
                break;
            }
            for(int j=0;j<urlCategories.size();j++){
                if(j%k==i){
                    miniUrlArr.add(urlCategories.get(j));
                }
            }
            ControllPage.LoadCategory(miniUrlArr);

        }
    }


}
