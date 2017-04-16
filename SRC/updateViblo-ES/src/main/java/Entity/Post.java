package Entity;

import Controller.ControllPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by quangminh on 07/10/2016.
 */
public class Post {
    Date createAt;
    String title;
    String body;
    String url;
    String byCategory;
    String image;
    boolean isDup;

    public boolean isDup() {
        return isDup;
    }

    public void setDup(boolean dup) {
        isDup = dup;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Post(String url,String category,String image) {
        try {
            byCategory=category;
            this.image=image;
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Document doc = Jsoup.connect(url).timeout(60000).
                    userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2")
                    .header("Cookie","remember_82e5d2c56bdd0811318f0cf078b78bfc=eyJpdiI6IkVcL0Y2SFwvaVNWNFYwcmFQYzU0aUZUQT09IiwidmFsdWUiOiJQNks3Q01Ld3hVbHF5OUdTd0JzbUFRQnVnd085YURBWm5FK01cLzdTckpTVXd2VnhSVmxoOUltQ3lmK2RuNHRGUEZDRjZrSFdwdVdGdEkrZkVIRVM1M012bGFcL3ZIRVY2eDdaWE1DZ3JNU2k4PSIsIm1hYyI6IjlmN2I1Nzg5YzQ2ZDVkYmRlZmM0ZGEyMjY2Y2MyZWIwYWFkMTk4MzMxMDY4OWQ1MDFmYWI3OTYxYjNmZjZiNTgifQ%3D%3D; _ga=GA1.2.1627860303.1475825973; ")

                    .get();
            this.title=doc.select("p.post-title").html();


            this.body=doc.select("section.markdownContent").html();
            String dateInPost=doc.select("div.post-in-theme>span").html();
            String[] datetime=dateInPost.split(" ");
            try {
                this.createAt = formatter.parse(datetime[1]);
            } catch (ParseException e1) {
                this.createAt= ControllPage.stringToDate(dateInPost);
                //this.createAt=new Date(System.currentTimeMillis());;
            }catch (Exception e2){
            }
        } catch (IOException e) {

        }
    }

    public Post() {
    }

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





}
