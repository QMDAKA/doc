package gmo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Quang Minh on 8/11/2016.
 */
@Entity
public class User {
    @Id
    @GeneratedValue
    Long id;

    @JsonView(Views.Public.class)
    @Column(unique = true)
    String username;

    String password;

    Date birthday;
    Date workedAt;

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getWorkedAt() {
        return workedAt;
    }

    public void setWorkedAt(Date workedAt) {
        this.workedAt = workedAt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
    List<Authcode> authcodeList = new ArrayList<Authcode>();


    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
    List<New> listnew = new ArrayList<New>();

    public List<New> getListnew() {
        return listnew;
    }

    public void setListnew(List<New> listnew) {
        this.listnew = listnew;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public List<Authcode> getAuthcodeList() {
        return authcodeList;
    }

    public void setAuthcodeList(List<Authcode> authcodeList) {
        this.authcodeList = authcodeList;
    }

    public void setBirthday(long birthday) {
        this.birthday = new Date(birthday);
    }

    public void setWorkedAt(long workedAt) {
        this.workedAt = new Date(workedAt);
    }
}
