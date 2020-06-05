package com.fashionbrot.demo.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class User {
    @TableId(value = "id",type = IdType.INPUT)
    private Long id;

    private String name;
    private Integer age;
    private String email;
    @TableField("createTime")
    private Date createTime;
}
