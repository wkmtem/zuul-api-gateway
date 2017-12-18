package com.nsntc.zuul.bean;

import lombok.Data;
import java.util.Date;

/**
 * Class Name: RedisUser
 * Package: com.nsntc.zuul.bean
 * Description: reids user
 * @author wkm
 * Create DateTime: 2017/12/18 下午10:25
 * Version: 1.0
 */
@Data
public class RedisUser {

    /**
     * 主键
     */
    private String id;

    /**
     * 企业id
     */
    private String enterpriseId;

    /**
     * 用户账号
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realname;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 电话
     */
    private String cellphone;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 头像地址
     */
    private String headUrl;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 权限
     */
    private String permission;

    /**
     * 创建时间
     */
    private Date createStamp;

    /**
     * 修改时间
     */
    private Date updateStamp;
}
