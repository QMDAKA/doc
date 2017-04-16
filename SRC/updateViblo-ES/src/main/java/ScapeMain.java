
import Controller.ControllPage;
import Entity.Post;
import Entity.Category;
import Entity.Var;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.json.JSONObject;

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

    public static void main(String[] args) throws IOException, InterruptedException {
        Var.client = new TransportClient()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
        ArrayList<String> arrayUrl = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        //QueryBuilder qb = rangeQuery("image","title").from(page);
        SearchResponse response = Var.client.prepareSearch("viblo").setTypes("categories").setSize(823) //
                .addSort("totalPost", SortOrder.DESC) //
                .execute().actionGet();
        for (SearchHit hit : response.getHits()) {
            arrayUrl.add(hit.getSource().get("urlCategory").toString());
        }
       /* ArrayList<String> miniUrl=new ArrayList<>();
        miniUrl.add("https://viblo.asia/categories/elasticsearch");
        ControllPage.LoadCategory(miniUrl);*/
        int k = 30;


        for (int i = 0; i < k; i++) {
            ArrayList<String> miniUrlArr = new ArrayList<>();
            if (i > arrayUrl.size()) {
                break;
            }
            for (int j = 0; j < arrayUrl.size(); j++) {
                if (j % k == i) {
                    miniUrlArr.add(arrayUrl.get(j));
                }
            }
            ControllPage.LoadCategory(miniUrlArr);

        }


    }
}
