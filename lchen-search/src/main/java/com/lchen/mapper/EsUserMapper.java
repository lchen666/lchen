package com.lchen.mapper;

import com.lchen.entity.EsUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface EsUserMapper extends ElasticsearchRepository<EsUser, Long> {

    /**
     * 搜索name或者desc   name or desc
     * @param name
     * @param desc
     * @return
     */
    List<EsUser> findByNameOrDesc(String name, String desc);


    List<EsUser> findByDesc(String desc);
}
