package com.jianguo.userthriftservice.domain;

public class Teacher extends User {
    private String intro;
    private Integer stars;

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }
}
