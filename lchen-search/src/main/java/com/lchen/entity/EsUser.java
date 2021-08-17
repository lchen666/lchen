package com.lchen.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@AllArgsConstructor
@Document(indexName = "es_user", shards = 5)
public class EsUser {

    @Id
    private Long id;

    @Field(type= FieldType.Text, analyzer = "ik_max_word")
    private String name;

    @Field(type= FieldType.Integer)
    private Integer age;

    @Field(type= FieldType.Text, store = true, analyzer = "ik_max_word")
    private String desc;

    @Field(type = FieldType.Date, format = DateFormat.basic_date_time)
    private Date createTime;

}
