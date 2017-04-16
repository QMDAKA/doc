package gmo.controller;

import gmo.model.Msg;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by quangminh on 07/10/2016.
 */
@RestController
public class AuthcodeController {
    @RequestMapping(path = "/authcodes/test", method = RequestMethod.POST)
    ResponseEntity<?> test(
    ) {

        return ResponseEntity.ok(new Msg("OK"));
    }


}
