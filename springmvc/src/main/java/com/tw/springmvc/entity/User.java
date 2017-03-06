package com.tw.springmvc.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/15.
 */
public class User implements Serializable {


    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id != null ? id.equals(user.id) : user.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;


}

