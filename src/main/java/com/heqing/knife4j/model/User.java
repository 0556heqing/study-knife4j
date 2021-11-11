package com.heqing.knife4j.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author heqing
 * @date 2021/8/17 11:36
 */
@ApiModel(value="User", reference ="用户信息", description="用户信息实体类")
@Data
public class User implements Serializable {

    @ApiModelProperty(value="用户名",name="name",example="heqing")
    private String name;

    @ApiModelProperty(value="年龄",name="age",example="30")
    private Integer age;

    @ApiModelProperty(value="地址",name="address",example="安庆，凉亭")
    private String address;

    @ApiModelProperty(value="创建时间",name="createTime",hidden=true)
    private Date createTime = new Date();

}
