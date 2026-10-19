package com.carl.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName tcc_fence_log
 */
@TableName(value ="tcc_fence_log")
@Data
public class TccFenceLog {
    /**
     * global id
     */
    @TableId(type = IdType.INPUT)
    private String xid;

    /**
     * branch id
     */
    @TableId
    private Long branchId;

    /**
     * action name
     */
    private String actionName;

    /**
     * status(tried:1;committed:2;rollbacked:3;suspended:4)
     */
    private Integer status;

    /**
     * create time
     */
    private Date gmtCreate;

    /**
     * update time
     */
    private Date gmtModified;
}