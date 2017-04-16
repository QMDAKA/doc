package Entity;

import Controller.ControllPage;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.jboss.logging.annotations.Pos;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by quangminh on 07/10/2016.
 */
public class Category {
    String title;
    String urlCategory;
    int totalPost;
    String image;
    String name;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getTotalPost() {
        return totalPost;
    }

    public void setTotalPost(int totalPost) {
        this.totalPost = totalPost;
    }

    @JsonIgnore
    List<String> imageByPost = new ArrayList<>();
    @JsonIgnore
    List<String> postUrls = new ArrayList<>();

    public List<String> getPostUrls() {
        return postUrls;
    }

    public void setPostUrls(List<String> postUrls) {
        this.postUrls = postUrls;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlCategory() {
        return urlCategory;
    }

    public void setUrlCategory(String urlCategory) {
        this.urlCategory = urlCategory;
    }

    public List<String> getImageByPost() {
        return imageByPost;
    }

    public void setImageByPost(List<String> imageByPost) {
        this.imageByPost = imageByPost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category(String url) {
        urlCategory = url;
        name=url.substring(24);
        Document doc = null;
        try {
            doc = ControllPage.getDocmentInItemCategoryPage(url);
            title = doc.select("div#main-content div.tag-details h3").html();
            image = doc.select("div#main-content div.tag-item-image img").attr("abs:src");

            String postCount = doc.select("div#main-content div.tag-details strong").get(0).html();
            totalPost = Integer.parseInt(postCount.split(" ")[0]);
            for (int count = 1; count < (totalPost / 15)+1 ; count++) {
                Document docByCount = ControllPage.getDocmentInItemCategoryPage(url + "?page=" + count);
                Elements elements = docByCount.select("div.card div.post-title-box h1>a");
                for (Element e : elements) {
                    postUrls.add(e.attr("abs:href"));
                    System.out.println(e.attr("abs:href"));

                }

            }
            Var.eventForCategories++;
            System.out.println("da load dc "+ Var.eventForCategories);

        } catch (IOException e) {
            System.out.println("ko ket noi dc category : "+urlCategory);
        }

    }

    public Category() {
    }
}
