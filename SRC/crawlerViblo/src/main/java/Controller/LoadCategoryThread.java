package Controller;

import Entity.Category;
import Entity.Var;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.index.IndexResponse;

import java.util.ArrayList;

/**
 * Created by quangminh on 28/10/2016.
 */
public class LoadCategoryThread implements Runnable{
    ArrayList<String> stringArrayList;

    public ArrayList<String> getStringArrayList() {
        return stringArrayList;
    }

    public void setStringArrayList(ArrayList<String> stringArrayList) {
        this.stringArrayList = stringArrayList;
    }

    public LoadCategoryThread() {
    }


    @Override
    public void run() {
        ObjectMapper mapper=new ObjectMapper();
        for(String urlCategory:stringArrayList) {
            Category category = new Category(urlCategory);
            String json = null;
            try {
                if (category.getTitle().length() > 2) {
                    json = mapper.writeValueAsString(category);
                    IndexResponse response = Var.client.prepareIndex("viblo", "categories")
                            .setSource(json)
                            .get();
                    Var.categoryArrayList.add(category);
                    ControllPage.LoadPostByCategory(category);

                }
            } catch (Exception e) {
                System.out.println("Run out !!!");
                e.printStackTrace();
            }


        }

    }
}
