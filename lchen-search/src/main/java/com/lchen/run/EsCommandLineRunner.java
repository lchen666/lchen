package com.lchen.run;

import com.lchen.entity.EsUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.stereotype.Component;

@Component
public class EsCommandLineRunner implements CommandLineRunner {

    @Autowired
    private ElasticsearchRestTemplate restTemplate;

    @Override
    public void run(String... args){
//        IndexOperations indexOps = restTemplate.indexOps(EsUser.class);
//        boolean exists = indexOps.exists();
//        if (!exists){
//            System.out.println("索引不存在,重新创建！");
//            indexOps.create();
//        }
    }
}
