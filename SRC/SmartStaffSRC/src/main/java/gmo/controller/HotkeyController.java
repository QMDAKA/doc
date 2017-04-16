package gmo.controller;

import gmo.model.Hotkey;
import gmo.repository.HotkeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by quangminh on 09/12/2016.
 */
@RestController
public class HotkeyController {
    @Autowired
    HotkeyRepository hotkeyRepository;

    @RequestMapping(path = "/hotkeys", method = RequestMethod.GET)
    ResponseEntity<?> getHotPick(
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        PageRequest pageable = new PageRequest(0, size);
        Page<Hotkey> groupPage = hotkeyRepository.findAllByOrderByTotalDesc(pageable);
        HashMap<String, List<Hotkey>> hashHotkey = new HashMap<>();
//        if (groupPage.getTotalElements() == 0) {
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
//
//        }
//        List<Hotkey> listHotkey = new ArrayList<>();
//
//        for (int i = 0; i < groupPage.getTotalElements(); i++) {
//            listHotkey.add(groupPage.getContent().get(i));
//        }
//        hashHotkey.put("hotkeys", listHotkey);
        return ResponseEntity.status(HttpStatus.OK).body(groupPage);
    }
}
