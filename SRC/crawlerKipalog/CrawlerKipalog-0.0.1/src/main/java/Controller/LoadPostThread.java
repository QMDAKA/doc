package Controller;


import Entity.Category;
import Entity.Post;
import Entity.Var;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.io.IOException;
import java.util.List;

import static Entity.Var.client;

/**
 * Created by quangminh on 17/03/2017.
 */
public class LoadPostThread implements Runnable {
    Category category;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public void run() {
        ObjectMapper mapper = new ObjectMapper();
        String json;
        for (int i = 1; i < (category.getTotalPost() / 20 + 1); i++) {
            List<String> urlOfPostByPaging = ControllPage.urlPostByCategory(category.getUrlCategory() + "/pagination?page=" + i);
            for (String url : urlOfPostByPaging) {
                Post post = new Post(url);
                System.out.println(url);
                try {
                    post.setImage(category.getImage());
                    post.setByCategory(category.getName());
                    ////sua lai bang duplicate ID
                    QueryBuilder qb = QueryBuilders.queryStringQuery(post.getTitle()).analyzeWildcard(true).field("title").minimumShouldMatch("100%");
                    SearchResponse isDupOrNot = client.prepareSearch("kipalog").setQuery(qb).setSize(1).addField("title") //
                            .execute().actionGet();

                    if (isDupOrNot.getHits().totalHits() > 0) {
                        post.setDup(true);
                    } else {
                        post.setDup(false);
                    }
                } catch (Exception ex) {
                    post.setDup(true);
                }
                try {
                    json = mapper.writeValueAsString(post);
                    IndexResponse response = Var.client.prepareIndex("kipalog", category.getName())
                            .setSource(json)
                            .get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
