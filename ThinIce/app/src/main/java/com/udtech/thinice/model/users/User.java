package com.udtech.thinice.model.users;

import com.orm.SugarRecord;

import java.util.Date;

/**
 * Created by JOkolot on 03.11.2015.
 */
public class User extends SugarRecord<User> {
    private AppUser appUser;
    private FBUser fbUser;
    private TUser tUser;
    private String imageUrl;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private Boolean sex;//true - male, false female;
    private Date dateOfBirth;
    private Long weight;
    private Long height;

    public User() {
    }

    public User(AppUser appUser) {
        this.appUser = appUser;
        this.appUser.setUser(this);
        this.appUser.save();
    }

    public User(TUser tUser) {
        this.tUser = tUser;
        this.tUser.setUser(this);
        this.tUser.save();
    }

    public User(FBUser fbUser) {
        this.fbUser = fbUser;
        this.fbUser.setUser(this);
        this.fbUser.save();
    }

    public AppUser getAppUser() {
        return appUser;
    }


    public FBUser getFbUser() {
        return fbUser;
    }


    public TUser getTUser() {
        return tUser;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }
}
