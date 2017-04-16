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
            Document doc = Jsoup.connect(url).timeout(60000).
                    userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2")
                    .cookie("XSRF-TOKEN","eyJpdiI6IllBZnJSUEVaRUg1RndcL3Uza2Z4NFh3PT0iLCJ2YWx1ZSI6Ikk4NHZuSjh1RnlvWldSVmpoUGdUaDNEMG9MV1MzVXFNR3JQZ1c2SkZRYlwvNXI2SlMxSFNpTk8yNm1IZzlTN2FzZFE2OTVoblBzVCtkXC85XC95MFBtNmZRPT0iLCJtYWMiOiJjODAwNDdhNGYwMmMzYmQzNTI4MTRjYjk2MWExMjdiMjdjNDUzZDQ2NzM0YTQ1OTY1NzJiNjc0N2M2YzJkOTc5In0%3D")
                    .cookie("laravel_token","eyJpdiI6Inl3UkxKSE1XUTdESitFUzh0TXdka1E9PSIsInZhbHVlIjoiSFZYaWEwYmR5SnhZYXBmR212MlB2TDVFQ3FqUWZcL0NQelRcL3JnVGlDWGFwUmwyekxFTTAya25BWWxEMSt4ZUhOUjdzOSs5YVpXK1FENjExOERvaTZjenE5WUFyQkN5akdVTTh3UzUyWHlFT3Z3SXVuR0ZIVUpcL013c0pONDJtVHdTdGRteEZzdnBcL3prZ2NPd3ViMlBlSXN0U2s4blkyWmhrbWVyM2lGb3ZwNkNiWmdHQTlJd1BaN3Z5M0VvNXJDcDRYRlwvVWxoOWxCaTU1VWcxNU9kRmdHelBXYnh4QUFLbnFpV2Vra3I0OXU2eVBBVjNnUTZ5WjRSbnRrOW9qbVM4QVhoVWRyemtkMVwvRm1QTzRWdWRNK3c9PSIsIm1hYyI6IjhiMTk2MzNlZGMyZGFhMGEwZTI0MmIzN2UxYWMwYWYwMmRlYzUxYTk0NDlhNmY1ZWJkMzU1Yjg0NjAyZTIzY2MifQ")
                    .get();
            this.title=doc.select("div.post-meta h1").html();


            this.body=doc.select("div.post-content").html();
            this.createAt=new Date(System.currentTimeMillis());
        } catch (IOException e) {
            System.out.println("ko ket noi dc voi bai viet : " +url);

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
