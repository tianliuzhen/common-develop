package com.aaa.commondevelop.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author liuzhen.tian
 * @version 1.0 BaseMain.java  2020/7/27 19:56
 */
@Data
public class BaseMain {

    private Date date;

    // @JsonSerialize(using = LocalDateTimeSerializer.class)
    // @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    // @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime localDateTime;
}
