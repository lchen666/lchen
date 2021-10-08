package com.lchen.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@ApiModel(value = "系统登录请求参数")
@Getter
@Setter
public class LoginFormDto implements Serializable {

    private static final long serialVersionUID = -7760447904274066866L;
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    @NotEmpty(message = "userName is not null")
    @Size(min = 5, message = "userName length less than 5")
    private String userName;

    /**
     * 用户密码
     */
    @ApiModelProperty(value = "密码")
    @NotEmpty(message = "password is not null")
    private String password;

    /**
     * uuid
     */
    @ApiModelProperty(value = "uuid")
    @NotEmpty(message = "uuid is not null")
    private String uuId;

    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码")
    @NotEmpty(message = "verCode is not null")
    private String verCode;

}
