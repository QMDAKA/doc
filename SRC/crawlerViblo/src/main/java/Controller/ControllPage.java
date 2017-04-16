package Controller;

import Entity.Category;

import Entity.Post;
import Entity.Var;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.lucene.queryparser.xml.FilterBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import org.elasticsearch.search.sort.SortOrder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.elasticsearch.index.query.QueryBuilders.*;

import static Entity.Var.client;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

/**
 * Created by quangminh on 07/10/2016.
 */
public class ControllPage {
   public static Document getDocumentInCategoriesPage(String url) throws IOException {

       Document document= Jsoup.connect(url).header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
               .timeout(70000)
               .header("Accept-Language","vi-VN,vi;q=0.8,fr-FR;q=0.6,fr;q=0.4,en-US;q=0.2,en;q=0.2")
               .header("Cookie","remember_82e5d2c56bdd0811318f0cf078b78bfc=eyJpdiI6IkVcL0Y2SFwvaVNWNFYwcmFQYzU0aUZUQT09IiwidmFsdWUiOiJQNks3Q01Ld3hVbHF5OUdTd0JzbUFRQnVnd085YURBWm5FK01cLzdTckpTVXd2VnhSVmxoOUltQ3lmK2RuNHRGUEZDRjZrSFdwdVdGdEkrZkVIRVM1M012bGFcL3ZIRVY2eDdaWE1DZ3JNU2k4PSIsIm1hYyI6IjlmN2I1Nzg5YzQ2ZDVkYmRlZmM0ZGEyMjY2Y2MyZWIwYWFkMTk4MzMxMDY4OWQ1MDFmYWI3OTYxYjNmZjZiNTgifQ%3D%3D; _ga=GA1.2.1627860303.1475825973; ")
               .header("Upgrade-Insecure-Requests","1")
               .header("User-Agent","Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36")
               .get();
       return document;
   }
   public static Document getDocmentInItemCategoryPage(String url) throws IOException {

           Document document= Jsoup.connect(url).header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                   .timeout(70000)
                   .header("Accept-Encoding","gzip, deflate, sdch, br")
                   .header("Accept-Language","vi-VN,vi;q=0.8,fr-FR;q=0.6,fr;q=0.4,en-US;q=0.2,en;q=0.2")
                   .header("Cache-Control","max-age=0")
                   .header("Connection","keep-alive")
                   .header("Cookie","remember_82e5d2c56bdd0811318f0cf078b78bfc=eyJpdiI6IkVcL0Y2SFwvaVNWNFYwcmFQYzU0aUZUQT09IiwidmFsdWUiOiJQNks3Q01Ld3hVbHF5OUdTd0JzbUFRQnVnd085YURBWm5FK01cLzdTckpTVXd2VnhSVmxoOUltQ3lmK2RuNHRGUEZDRjZrSFdwdVdGdEkrZkVIRVM1M012bGFcL3ZIRVY2eDdaWE1DZ3JNU2k4PSIsIm1hYyI6IjlmN2I1Nzg5YzQ2ZDVkYmRlZmM0ZGEyMjY2Y2MyZWIwYWFkMTk4MzMxMDY4OWQ1MDFmYWI3OTYxYjNmZjZiNTgifQ%3D%3D; _ga=GA1.2.1627860303.1475825973; ")
                   .header("Referer","https://viblo.asia/categories")
                   .header("Upgrade-Insecure-Requests","1")
                   .header("User-Agent","Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36")
                   .get();
           return document;
       }

    public static void LoadCategory(ArrayList<String> arrayList) {
        LoadCategoryThread run = new LoadCategoryThread();
        run.setStringArrayList(arrayList);
        Thread t = new Thread(run);
        t.start();
    }
    public static void LoadPostByCategory(Category category){
        try {

            ObjectMapper mapper=new ObjectMapper();
            for(int i=0;i<category.getPostUrls().size();i++) {

                String urlPost = category.getPostUrls().get(i);
                String title = category.getTitle().toLowerCase();

                Post post = new Post(urlPost, category.getName(), category.getImage());
                /*QueryBuilder qb=QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
                        FilterBuilders.termFilter("title",post.getTitle()));
*/
//                QueryBuilder queryBuilder=QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), FilterBuilders.termFilter("title",post.getTitle()));
                try{
                    ////sua lai bang duplicate ID
                QueryBuilder qb = QueryBuilders.queryStringQuery(post.getTitle()).analyzeWildcard(true).field("title").minimumShouldMatch("100%");
                SearchResponse isDupOrNot = client.prepareSearch("viblo").setQuery(qb).setSize(1).addField("title") //
                        .execute().actionGet();

                if(isDupOrNot.getHits().totalHits()>0){
                    post.setDup(true);
                }
                else{
                    post.setDup(false);
                }
                }catch (Exception ex){
                    post.setDup(true);
                }

                if(post.getBody().length()>3) {
                    String json = mapper.writeValueAsString(post);
                    IndexResponse response = client.prepareIndex("viblo", category.getName())
                            .setSource(json)
                            .get();
                    System.out.println(response);
                }

            }
        } catch (JsonProcessingException e) {
            System.out.println("loi dinh dang JSON");
        } catch (NullPointerException ex){
            System.out.println("Bai viet rong");

        }
    }






    public static Category subCategory(Category category,int start,int end){
        Category sub=new Category();
        List<String> miniImageArr=category.getImageByPost().subList(start,end);
        List<String> miniUrlArr=category.getPostUrls().subList(start,end);
        sub.setTitle(category.getTitle());
        sub.setImageByPost(miniImageArr);
        sub.setPostUrls(miniUrlArr);

        return sub;

    }

    public static Date stringToDate(String dateInPost){
        Date date=new Date();
        String arr[]=dateInPost.split(" ");
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        String dateToParse=new String();

        switch (arr[1]){
            case "January,":dateToParse=1+"/";break;
            case "February,":dateToParse=2+"/";break;
            case "March,":dateToParse=3+"/";break;
            case "April,":dateToParse=4+"/";break;
            case "May,":dateToParse=5+"/";break;
            case "June,":dateToParse=6+"/";break;
            case "July,":dateToParse=7+"/";break;
            case "August,":dateToParse=8+"/";break;
            case "September,":dateToParse=9+"/";break;
            case "October,":dateToParse=10+"/";break;
            case "November,":dateToParse=11+"/";break;
            case "December,":dateToParse=12+"/";break;

        }
        char[] day=arr[0].toCharArray();
        day[arr[0].length()-2]=' ';
        day[arr[0].length()-1]=' ';

        String text = new String(day);
        text=text.trim();
        dateToParse+=text+"/"+arr[2];
        try {
            date = formatter.parse(dateToParse);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }
   }

