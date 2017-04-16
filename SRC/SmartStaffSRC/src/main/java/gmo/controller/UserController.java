package gmo.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unboundid.ldap.sdk.*;
import gmo.model.Authcode;
import gmo.model.Msg;
import gmo.model.User;
import gmo.ultil.BlowFishDeEnCode;
import gmo.repository.AuthcodeRepository;
import gmo.repository.UserRepository;

import gmo.ultil.GetRequestUntil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Quang Minh on 8/11/2016.
 */
@RestController
public class UserController {
    @Autowired
    AuthcodeRepository authcodeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private HttpServletRequest request;

    @RequestMapping(path = "/users/login", method = RequestMethod.POST)
    ResponseEntity<?> login(
            @RequestBody Object object
    ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String ucc = objectMapper.writeValueAsString(object);
        JSONObject json = new JSONObject(ucc);
        User user = new User();
        user.setPassword(json.getString("password"));
        user.setUsername(json.getString("username"));
        String email = user.getUsername();
        String uid = email.substring(0, email.indexOf("@"));
        String domain = email.substring(email.indexOf("@") + 1);
        String dc1 = domain.substring(0, domain.indexOf("."));
        String dc2 = domain.substring(domain.indexOf(".") + 1);

        String hostname = "mail.runsystem.net";
        String dn = "uid=" + uid + ",ou=people,dc=" + dc1 + ",dc=" + dc2;
        String password = BlowFishDeEnCode.decrypt(user.getPassword());
        if (password == null) {
            password = user.getPassword();
            //return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't Decode that password");
        }
        int port = 389;
        LDAPConnection ldapConnection;
        try {
            ldapConnection =
                    new LDAPConnection(hostname, port);
        } catch (final LDAPException lex) {
            System.err.println("failed to connect to "
                    + hostname + " " +
                    lex.getMessage());


            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(new Msg("Request time out. Check your LDAP"));

        }
        System.out.println("connecttion successful" + ldapConnection.getConnectedIPAddress());

        try {
            final BindRequest bindRequest =
                    new SimpleBindRequest(dn, password);
            BindResult bindResult =
                    ldapConnection.bind(bindRequest);

            if (bindResult.getResultCode() == ResultCode.SUCCESS) {
                String authCode = toMD5(System.currentTimeMillis() + "");

                user.setBirthday(GetRequestUntil.getStaffInfor(json.getString("username")).getBirthday());
                user.setBirthday(GetRequestUntil.getStaffInfor(json.getString("username")).getWorkedAt());

                makeAuthCodeWithUser(user, authCode);
                System.out.println("--------------authcode: " + authCode);
                System.out.println("--------------user: " + user.getBirthday());

                HashMap<String, Object> hashMap = new HashMap<>();

                HashMap<String, String> staffInforHashMap = new HashMap<>();
                staffInforHashMap.put("email", user.getUsername());
             /*   staffInforHashMap.put("birthday",GetRequestUntil.getStaffInfor(json.getString("username")).getBirthday().getTime()+"");
                staffInforHashMap.put("worked_at",GetRequestUntil.getStaffInfor(json.getString("username")).getWorkedAt().getTime()+"");
               */
                staffInforHashMap.put("birthday", System.currentTimeMillis() + "");
                staffInforHashMap.put("worked_at", System.currentTimeMillis() + "");

                staffInforHashMap.put("authcode", authCode);

                hashMap.put("staff", staffInforHashMap);
                System.out.println("--------------staff: " + staffInforHashMap.toString());

                return ResponseEntity.ok(hashMap);

            }

            if (bindResult.hasResponseControl()) {
                Control[] controls =
                        bindResult.getResponseControls();
                // handle response controls ...
            }
            ldapConnection.close();
        } catch (final LDAPException lex) {
            System.err.println("bind failed\n" + lex.getExceptionMessage());
            ldapConnection.close();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Msg("Login failed"));
        }

        return ResponseEntity.ok(new Msg("Ok"));

    }

    @RequestMapping(path = "/users/", method = RequestMethod.PATCH)
    ResponseEntity<?> updateInfor(
            @RequestBody Object object
    ) {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = (User) request.getAttribute("true_user");
        String ucc = null;
        try {
            ucc = objectMapper.writeValueAsString(object);
            JSONObject json = new JSONObject(ucc);
            user.setBirthday(Long.parseLong(json.getString("birthday")));
            user.setWorkedAt(Long.parseLong(json.getString("workedAt")));
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        }

    }


    @RequestMapping(path = "/users/login2", method = RequestMethod.POST)
    ResponseEntity<?> login2(
            @RequestBody Object object
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String ucc = objectMapper.writeValueAsString(object);
        JSONObject json = new JSONObject(ucc);
        User user = new User();
        user.setPassword(json.getString("password"));
        user.setUsername(json.getString("username"));
        user.setBirthday(GetRequestUntil.getStaffInfor(json.getString("username")).getBirthday());
        user.setWorkedAt(GetRequestUntil.getStaffInfor(json.getString("username")).getWorkedAt());
        String password = BlowFishDeEnCode.decrypt(user.getPassword());
        if (password == null) {
            password = user.getPassword();
        }
        String authCode = toMD5(System.currentTimeMillis() + "");
        makeAuthCodeWithUser(user, authCode);

        HashMap<String, Object> hashMap = new HashMap<>();

        HashMap<String, String> staffInforHashMap = new HashMap<>();
        staffInforHashMap.put("email", user.getUsername());
        staffInforHashMap.put("birthday", System.currentTimeMillis() + "");
        staffInforHashMap.put("worked_at", System.currentTimeMillis() + "");
        staffInforHashMap.put("authcode", authCode);

        hashMap.put("staff", staffInforHashMap);
        return ResponseEntity.ok(hashMap);
    }


    @RequestMapping(path = "/users/{id1}/listnew", method = RequestMethod.GET)
    ResponseEntity<?> getListNew(
            @PathVariable(value = "id1") long id1
    ) {
        return ResponseEntity.ok(userRepository.findOne(id1).getListnew());
    }

    void makeAuthCodeWithUser(User userGuest, String authcode) {
        User user = userRepository.findByUsername(userGuest.getUsername());
        if (user == null) {
            user = userGuest;
        }
        Authcode code = new Authcode();
        code.setCreateAt(System.currentTimeMillis());
        code.setCode(authcode);
        user.getAuthcodeList().add(code);
        code.setUser(user);

        userRepository.save(user);
        // authcodeRepository.save(code);
    }

    public String toMD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

}
