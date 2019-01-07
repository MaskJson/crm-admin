package com.moving.admin.bean;

import java.io.Serializable;

public class TokenInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    public TokenInformation() {

    }

    public TokenInformation(Long id) {
        this.id = id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

}
