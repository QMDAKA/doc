package gmo.ultil;

import gmo.configuration.AppConfig;
import gmo.model.User;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by quangminh on 03/01/2017.
 */
public class GetRequestUntil {
    public static User getStaffInfor(String email){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        User u=new User();
        u.setUsername(email);
        HttpClient client = HttpClientBuilder.create().build();
        String url= AppConfig.getStaffInfoServer()+"api/user-info?email="+email;
        HttpGet getResource=new HttpGet(url);
        HttpResponse responseOfResourceServer = null;

        try {
            responseOfResourceServer = client.execute(getResource);
            HttpEntity entity = responseOfResourceServer.getEntity();
            String response = EntityUtils.toString(entity, "UTF-8");

            JSONObject jsonObject = new JSONObject(response);
            JSONArray data= jsonObject.getJSONArray("data");
            JSONObject staffInfor = data.getJSONObject(0).getJSONObject("staff");
            u.setBirthday(formatter.parse(staffInfor.getString("birthday")));
            u.setWorkedAt(formatter.parse(staffInfor.getString("start_work")));


        } catch (IOException e) {
            e.printStackTrace();


        } catch (ParseException e) {
            e.printStackTrace();
        }catch (Exception e){
            u.setBirthday(0);
            u.setWorkedAt(0);
        }


        return u;
    }
}
