package gmo.controller;

import gmo.model.Doc;
import gmo.model.Msg;
import gmo.repository.DocRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.HTML;
import java.util.Date;

/**
 * Created by Quang Minh on 8/3/2016.
 */
@RestController
public class DocController {
    @Autowired
    DocRepository docRepository;


    @RequestMapping(path = "/docs/searchByTitle", method = RequestMethod.GET)
    ResponseEntity<?> searchByName(
            @RequestParam(value = "title", required = false, defaultValue = "") String title,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "15") int size
    ) {
        Pageable pageable = new PageRequest(page, size);
        Page<Doc> testEntities = docRepository.findByTitleContainingOrderByIdDesc(title, pageable);
        /*if(testEntities.getTotalElements()==0){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }*/
        return ResponseEntity.ok(testEntities);
    }

    @RequestMapping(path = "/docs/{id}", method = RequestMethod.GET)
    ResponseEntity<?> getDocById(
            @PathVariable(value = "id") long id
    ) {
        Doc doc = docRepository.findOne(id);
        if (doc == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Msg("Not Found"));
        } else {
            return ResponseEntity.ok(doc);
        }

    }

    @RequestMapping(path = "/docs/{id}", method = RequestMethod.DELETE)
    ResponseEntity<?> deleteDocById(
            @PathVariable(value = "id") long id
    ) {
        Doc doc = docRepository.findOne(id);
        if (doc == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Msg("Not Found"));
        } else {
            docRepository.delete(id);
            return ResponseEntity.ok(new Msg("Deleted"));
        }

    }
    @RequestMapping(path = "/docs/{id}", method = RequestMethod.PUT)
    ResponseEntity<?> updateDocById(
            @RequestBody Doc doc,
            @PathVariable(value = "id") long id
    ) {
        Doc oneDoc = docRepository.findOne(id);
        if ( oneDoc == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Msg("Not Found"));
        } else {
            if(doc.getTitle()!=null)
                oneDoc.setTitle(doc.getTitle());
            if(doc.getLink()!=null)
                oneDoc.setLink(doc.getLink());
            docRepository.save(oneDoc);
            return ResponseEntity.ok(new Msg("OK"));
        }

    }
    @RequestMapping(path = "/docs", method = RequestMethod.GET)
    ResponseEntity<?> getDocs(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "15") int size

    ) {
        Pageable pageable = new PageRequest(page, size);
        Page<Doc> groupPage = docRepository.findAllByOrderByIdDesc(pageable);
       /* if(groupPage.getTotalElements()==0){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }*/
        return ResponseEntity.ok(groupPage);

    }

    @RequestMapping(path = "/docs", method = RequestMethod.POST)
    ResponseEntity<?> addDoc(
            @RequestBody Doc a

    ) {
        if (a.getTitle().compareTo("") == 0 || a.getLink().compareTo("") == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Msg("title or link is blank"));

        } else {
            Date date = new Date(System.currentTimeMillis());
            a.setCreateTime(date);
            docRepository.save(a);
            return ResponseEntity.status(HttpStatus.CREATED).body(new Msg("OK"));
        }

    }
}
