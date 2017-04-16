package Entity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

/**
 * Created by quangminh on 16/03/2017.
 */
public class Post {
    Date createAt;
    String title;
    String body;
    String url;
    String byCategory;
    String image;
    boolean isDup;

    public  Post(){}
    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getByCategory() {
        return byCategory;
    }

    public void setByCategory(String byCategory) {
        this.byCategory = byCategory;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isDup() {
        return isDup;
    }

    public void setDup(boolean dup) {
        isDup = dup;
    }
    public  Post(String url){
        try {
            Document document = Jsoup.connect(url)
                    .timeout(70000)
                    .header("User-Agent","Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36")
                    .get();
            this.title =document.select("h1 strong").html();
            this.body = document.select("section#content").html();
            try {
                SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yy");

                String arr[] = document.select("p.date").text().split(" ");
                String date = arr[arr.length-1];
                this.createAt=sdf.parse(date);
            }catch (Exception ex){

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
