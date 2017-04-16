package gmo.ultil;

import gmo.model.Authcode;
import gmo.model.User;
import gmo.repository.AuthcodeRepository;
import gmo.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by quangminh on 15/09/2016.
 */
public class CheckRequest {
    public int checkAuthcodeInDB(Authcode authcode) {
        if (authcode == null) {
            return -1;
        } else {
            return 1;
        }
    }
/**
 * 
 * 
 * 
 */
    
    public int checkTimeAuthcode(Authcode authcode, AuthcodeRepository authcodeRepository, UserRepository userRepository) {
        long currentTime = System.currentTimeMillis();
        long expiredTime=12*24*3600*1000;
        if (currentTime > authcode.getCreateAt() + expiredTime) {
            User user = authcode.getUser();
            user.getAuthcodeList().remove(authcode);
            userRepository.save(user);

            authcode.setUser(null);
            authcodeRepository.save(authcode);
            authcodeRepository.delete(authcode);
            return -1;
        } else {
            return 1;
        }
    }

    public long check(HttpServletRequest request, AuthcodeRepository authcodeRepository, UserRepository userRepository) {
        String authcode = request.getHeader("authcode");
        if (authcode == null) {
            return 404;
        } else {
            Authcode au = findByCode(authcode, authcodeRepository);
            if (checkAuthcodeInDB(au) == -1) {
                return 404;
            }
            //check thoi gian authcode het han, nhung tam thoi ngung cung cap dich vu
            if (checkTimeAuthcode(au, authcodeRepository, userRepository) == -1) {
                return 400;
            }
            return au.getUser().getId();

        }
    }

    Authcode findByCode(String code, AuthcodeRepository authRepo) {
        Authcode au = new Authcode();
        for (Authcode a : authRepo.findAll()) {
            if (a.getCode().compareTo(code) == 0) {
                return a;
            }

        }
        return null;
    }


}
