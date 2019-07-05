package com.moving.admin.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class TokenInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long roleId;

    private Long loginTime;

    public TokenInformation() {

    }

    public TokenInformation(Long id, Long roleId, Long loginTime) {
        this.id = id;
        this.roleId = roleId;
        this.loginTime = loginTime;
    }

}
