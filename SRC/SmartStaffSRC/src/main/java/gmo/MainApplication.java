package gmo;


import com.fasterxml.jackson.databind.ObjectMapper;
import gmo.configuration.AppConfig;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by QUANG MINH on 3/8/2016.
 */

@Configuration
@ComponentScan(basePackageClasses = MainApplication.class)
@EnableJpaRepositories(basePackages = "gmo.repository")
@EnableTransactionManagement
@EnableAutoConfiguration
@SpringBootApplication

public class MainApplication {
    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setDatabase(Database.MYSQL);
        jpaVendorAdapter.setShowSql(true);
        jpaVendorAdapter.setGenerateDdl(true);
        return jpaVendorAdapter;
    }


    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
       /* ArrayList<String> arrayUrl = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        //QueryBuilder qb = rangeQuery("image","title").from(page);
        SearchResponse response = AppConfig.client.prepareSearch("viblo").setTypes("categories").setSize(823) //
                .addSort("totalPost", SortOrder.DESC) //
                .execute().actionGet();
        for (SearchHit hit : response.getHits()) {
            arrayUrl.add(hit.getSource().get("urlCategory").toString());
        }
       *//* ArrayList<String> miniUrl=new ArrayList<>();
        miniUrl.add("https://viblo.asia/categories/elasticsearch");
        ControllPage.LoadCategory(miniUrl);*//*
        int k = 30;


        while (true) {
            System.out.println("update ......");

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
            try {
                Thread.sleep(48*3600*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }*/


    }


}
