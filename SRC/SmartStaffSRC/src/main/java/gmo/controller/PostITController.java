package gmo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import gmo.model.Hotkey;
import gmo.model.Msg;
import gmo.repository.HotkeyRepository;
import org.apache.lucene.queryparser.xml.FilterBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.elasticsearch.index.query.QueryBuilders.*;
//import static org.elasticsearch.index.query.FilterBuilders.*;

import java.util.ArrayList;
import java.util.HashMap;

import static gmo.configuration.AppConfig.client;

/**
 * Created by quangminh on 21/10/2016.
 */
@RestController
public class PostITController {
    @Autowired
    HotkeyRepository hotkeyRepository;

    //find categories by title

    /**
     *
     *
     */

    @RequestMapping(path = "/postITs/categories", method = RequestMethod.GET)
    ResponseEntity<?> categories(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size,
            @RequestParam(value = "query", required = false, defaultValue = " ") String query

    ) {

        ObjectMapper mapper = new ObjectMapper();
        //QueryBuilder qb = rangeQuery("image","title").from(page);
        SearchResponse response = null;
        if (query.length() < 2) {
            response = client.prepareSearch("kipalog").setTypes("categories").setFrom(page * size).setSize(size) //
                    .addSort("totalPost", SortOrder.DESC) //
                    .execute().actionGet();
        } else {
            query.replace("%20", " ");
            QueryBuilder qb = QueryBuilders.queryStringQuery(query).analyzeWildcard(true).field("body").field("title").minimumShouldMatch("75%");
            response = client.prepareSearch("kipalog").setTypes("categories").setQuery(qb)
                    .setFrom(page * size).setSize(size)
                    .addSort("totalPost", SortOrder.DESC) //
                    .execute().actionGet();
        }
        SearchHits searchHits = response.getHits();
        HashMap<String, Object> content = new HashMap<>();
        ArrayList<HashMap<String, Object>> mapArrayList = new ArrayList<>();
        for (SearchHit hit : searchHits) {

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("image", hit.getSource().get("image"));
            hashMap.put("title", hit.getSource().get("title").toString().toLowerCase());
            hashMap.put("name", hit.getSource().get("name").toString());
            mapArrayList.add(hashMap);

        }
        content.put("content", mapArrayList);
        return ResponseEntity.status(HttpStatus.OK).body(content);
    }

    //get list post by category's name
    @RequestMapping(path = "/postITs/categories/{category}", method = RequestMethod.GET)
    ResponseEntity<?> category(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "15") int size,
            @PathVariable(value = "category") String category
    ) {

        ObjectMapper mapper = new ObjectMapper();
        SearchResponse response = client.prepareSearch("kipalog").setTypes(category)
                .setFrom(page * size).setSize(size).addFields("image", "title", "byCategory") //
                .addSort("createAt", SortOrder.DESC)
                .execute().actionGet();
        SearchHits searchHits = response.getHits();
        HashMap<String, Object> content = new HashMap<>();
        ArrayList<HashMap<String, Object>> mapArrayList = new ArrayList<>();
        for (SearchHit hit : searchHits) {
            try {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("id", hit.getId());
                hashMap.put("title", hit.getFields().get("title").getValues().get(0));
                hashMap.put("image", hit.getFields().get("image").getValues().get(0));
                hashMap.put("byCategory",category);
                mapArrayList.add(hashMap);
            } catch (NullPointerException e) {

            }


        }
        content.put("content", mapArrayList);

        return ResponseEntity.status(HttpStatus.OK).body(content);


        //return ResponseEntity.ok("Authcode correct");
    }


    //content of post
    @RequestMapping(path = "/postITs/categories/{category}/{postID}", method = RequestMethod.GET)
    ResponseEntity<?> post(
            @PathVariable(value = "category") String category,
            @PathVariable(value = "postID") String postID
    ) {

        ObjectMapper mapper = new ObjectMapper();
        //QueryBuilder qb = rangeQuery("image","title").from(page);
        GetResponse response = client.prepareGet("kipalog", category, postID).execute()
                .actionGet();

        //client.close();
        return ResponseEntity.status(HttpStatus.OK).body(response.getSource());


        //return ResponseEntity.ok("Authcode correct");
    }
    //search post by keyword
    @RequestMapping(path = "/postITs/search", method = RequestMethod.GET)
    ResponseEntity<?> search(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "15") int size,
            @RequestParam(value = "query", required = true) String query

    ) {

        ObjectMapper mapper = new ObjectMapper();
        query.replace("%20", " ");

        QueryBuilder qb = QueryBuilders.queryStringQuery(query).analyzeWildcard(true).field("body").minimumShouldMatch("75%");
        QueryBuilder queryBuilder = QueryBuilders.filteredQuery(qb, FilterBuilders.termFilter("dup", false));
        SearchResponse response = client.prepareSearch("kipalog").setQuery(queryBuilder).setFrom(page * size).setSize(size).addFields("image", "title", "name","byCategory") //
                .addSort("createAt", SortOrder.DESC) //
                .execute().actionGet();
        SearchHits searchHits = response.getHits();
        if (searchHits.totalHits() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Msg("Not Found"));
        }
        try {
            if (page == 0) {
                if (hotkeyRepository.findByWord(query) != null) {
                    Hotkey kTMP = hotkeyRepository.findByWord(query);
                    kTMP.setTotal(kTMP.getTotal() + 1);
                    hotkeyRepository.save(kTMP);
                } else {
                    Hotkey hotkey = new Hotkey();
                    hotkey.setTotal(1);
                    hotkey.setWord(query);
                    hotkeyRepository.save(hotkey);
                }
            }
        } catch (Exception e) {

        }

        HashMap<String, Object> content = new HashMap<>();
        ArrayList<HashMap<String, Object>> mapArrayList = new ArrayList<>();
        for (SearchHit hit : searchHits) {
            try {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("title", hit.getFields().get("title").getValues().get(0));
                hashMap.put("image", hit.getFields().get("image").getValues().get(0));
                hashMap.put("byCategory", hit.getFields().get("byCategory").getValues().get(0));
                hashMap.put("id", hit.getId());
                mapArrayList.add(hashMap);
            } catch (NullPointerException e) {

            }


        }
        content.put("content", mapArrayList);
        return ResponseEntity.status(HttpStatus.OK).body(content);


    }

    //search post of [category] by keyword
    @RequestMapping(path = "/postITs/{category}/search", method = RequestMethod.GET)
    ResponseEntity<?> searchByCatelogy(
            @PathVariable(value = "category") String category,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "15") int size,
            @RequestParam(value = "query", required = true) String query

    ) {

        ObjectMapper mapper = new ObjectMapper();
        query.replace("%20", " ");
        QueryBuilder qb = QueryBuilders.queryStringQuery(query).analyzeWildcard(true).field("body").field("title")

                .minimumShouldMatch("75%");
        SearchResponse response = client.prepareSearch("kipalog").setTypes(category).setQuery(qb)
                .setFrom(page * size).setSize(size).addFields("image", "title", "byCategory")
                .addSort("createAt", SortOrder.DESC)
                .execute().actionGet();
        SearchHits searchHits = response.getHits();
        if (searchHits.totalHits() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        HashMap<String, Object> content = new HashMap<>();
        ArrayList<HashMap<String, Object>> mapArrayList = new ArrayList<>();
        for (SearchHit hit : searchHits) {
            try {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("id", hit.getId());
                hashMap.put("title", hit.getFields().get("title").getValues().get(0));
                hashMap.put("image", hit.getFields().get("image").getValues().get(0));
                //hashMap.put("byCategory",hit.getFields().get("byCategory").getValues().get(0));
                mapArrayList.add(hashMap);
            } catch (NullPointerException e) {

            }


        }
        content.put("content", mapArrayList);
        return ResponseEntity.status(HttpStatus.OK).body(content);


    }


}
