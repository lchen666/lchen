package com.lchen.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.lchen.common.core.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author lchen
 * @since 2021-04-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    @Excel(name = "用户序号", cellType = Excel.ColumnType.NUMERIC, prompt = "用户编号")
    private Long userId;

    /**
     * 用户名
     */
    @Excel(name = "登录名称")
    private String userName;

    /**
     * 手机号
     */
    @Excel(name = "手机号")
    private String phone;

    /**
     * 密码
     */
    @Excel(name = "密码")
    private String password;

    /**
     * 头像
     */
    @Excel(name = "头像")
    private String avatar;

    /**
     * 是否删除
     */
    @Excel(name = "帐号状态", readConverterExp = "0=正常,1=停用")
    private Integer delFlag;


    @Override
    protected Serializable pkVal() {
        return this.userId;
    }

}
