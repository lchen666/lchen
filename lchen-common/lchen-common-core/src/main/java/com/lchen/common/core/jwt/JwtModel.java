package com.lchen.common.core.jwt;

import lombok.Getter;
import lombok.Setter;

/**
 * token里面自带的用户对象信息
 */
@Getter
@Setter
public class JwtModel {

    public JwtModel(){}

    public JwtModel(Long userId, String phone, String userName){
        this.userId = userId;
        this.phone = phone;
        this.userName = userName;
    }

    private Long userId;

    private String phone;

    private String userName;

}
