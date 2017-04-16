package gmo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gmo.configuration.AppConfig;
import gmo.model.Doc;
import gmo.model.Msg;
import gmo.model.New;
import gmo.model.User;
import gmo.repository.NewRepository;
import gmo.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

/**
 * Created by Quang Minh on 8/3/2016.
 */
@RestController
public class NewController {
    @Autowired
    NewRepository newRepository;
    @Autowired
    private HttpServletRequest request;

    @Autowired
    UserRepository userRepository;


    @RequestMapping(path = "/news/{id}", method = RequestMethod.GET)
    ResponseEntity<?> getNewById(
            @PathVariable(value = "id") long id
    ) {
        New n = newRepository.findOne(id);
        if (n == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Msg("Not Found"));
        } else {
            return ResponseEntity.ok(n);
        }
    }

    @RequestMapping(path = "/news/{id}", method = RequestMethod.DELETE)
    ResponseEntity<?> deleteNewById(
            @PathVariable(value = "id") long id
    ) {
        New n = newRepository.findOne(id);
        if (n == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Msg("Not Found"));
        } else {

            newRepository.delete(id);
            return ResponseEntity.ok(new Msg("OK"));
        }
    }
    @RequestMapping(path = "/news/{id}", method = RequestMethod.PUT)
    ResponseEntity<?> updateNewById(
            @RequestBody Object  object,
            @PathVariable(value = "id") long id
    ) throws JsonProcessingException {
        New aNew = newRepository.findOne(id);

        if (aNew == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Msg("Not Found"));
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            String ucc = objectMapper.writeValueAsString(object);
            JSONObject json = new JSONObject(ucc);
            if(json.has("title"))
                aNew.setTitle(json.getString("title"));
            if(json.has("body"))
                aNew.setBody(json.getString("body"));
            if(json.has("image"))
                aNew.setImage(json.getString("image"));
            newRepository.save(aNew);
            return ResponseEntity.ok(new Msg("OK"));
        }
    }
    @RequestMapping(path = "/news/searchByTitle", method = RequestMethod.GET)
    ResponseEntity<?> searchByName(
            @RequestParam(value = "title", required = false, defaultValue = "") String title,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "15") int size
    ) {
        Pageable pageable = new PageRequest(page, size);
        Page<New> testEntities = newRepository.findByTitleContainingOrderByIdDesc(title, pageable);
       /* if(testEntities.getTotalElements()==0){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }*/
        return ResponseEntity.ok(testEntities);
    }

    @RequestMapping(path = "/news", method = RequestMethod.GET)
    ResponseEntity<?> getNews(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "15") int size

    ) {
        Pageable pageable = new PageRequest(page, size);
        Page<New> groupPage = newRepository.findAllByOrderByIdDesc(pageable);
        //return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);

        /*if(groupPage.getTotalElements()==0){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }*/
        return ResponseEntity.ok(groupPage);

    }


    @RequestMapping(path = "/news", method = RequestMethod.POST)
    ResponseEntity<?> addNew(
            @RequestBody Object object

    ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String ucc = objectMapper.writeValueAsString(object);
        JSONObject json = new JSONObject(ucc);
        New n=new New();
        n.setBody(json.getString("body"));
        n.setImage(json.getString("image"));
        n.setTitle(json.getString("title"));
        n.setCreateTime(new Date(System.currentTimeMillis()));
        newRepository.save(n);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Msg("OK"));
    }

}
