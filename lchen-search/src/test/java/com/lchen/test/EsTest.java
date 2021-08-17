package com.lchen.test;

import com.lchen.entity.EsUser;
import com.lchen.mapper.EsUserMapper;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
public class EsTest {

    @Autowired
    EsUserMapper mapper;

    @Autowired
    ElasticsearchRestTemplate restTemplate;

    //复杂搜索 基于多字段、分词、过滤、分页、高亮
    @Test
    public void searchHigh()
    {
        //设置高亮字段
//        HighlightBuilder.Field desc=new HighlightBuilder.Field("desc")
//                //关闭多个字段匹配 true表示只有查询的字段匹配高亮  false表示多个字段都匹配
//                .requireFieldMatch(false)
//                .preTags("<span style='color:red'>")
//                .postTags("</span>");
//
//        HighlightBuilder.Field name=new HighlightBuilder.Field("name")
//                //关闭多个字段匹配
//                .requireFieldMatch(false)
//                .preTags("<span style='color:red'>")
//                .postTags("</span>");
        //高亮多个字段设置
        HighlightBuilder highlightBuilder=new HighlightBuilder();
        highlightBuilder.field("desc");
        highlightBuilder.field("name");
        highlightBuilder.requireFieldMatch(false);
        highlightBuilder.preTags("<font style='color:red'>");
        highlightBuilder.postTags("</font>");

        //简单字段查询 查询User下的索引
        NativeSearchQuery query=new NativeSearchQueryBuilder()
                //一个字段查询
                .withQuery(QueryBuilders.matchQuery("desc","张三一个"))
                //多字段查询  desc和name都设置成text ik_max_word
//                .withQuery(QueryBuilders.queryStringQuery("张三一个").field("desc").field("name"))
                //添加分页，注意页码从0开始
                ///pageable的实现类PageRequest的静态方法of
                .withPageable(PageRequest.of(0,6))
                //过滤大小 lt 小于 gt大于  lte小于等于 gte大于等于
//                .withFilter(QueryBuilders.rangeQuery("age").gte(10))
                //排序 使用字段排序返回结果中的score为null
                //根据字段排序fieldSort("字段名")   .order(SortOrder.ASC/DESC)
                .withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
//                .withHighlightFields(name,desc)
                .withHighlightBuilder(highlightBuilder)
                .build();
        //获取hits数据

        SearchHits<EsUser> search = restTemplate.search(query, EsUser.class);
        System.out.println(search);
        search.forEach(u-> System.out.println(u));
        //处理高亮
        List<SearchHit<EsUser>> searchHits = search.getSearchHits();
        if(!CollectionUtils.isEmpty(searchHits)) {
            //不为空遍历
            List<EsUser> userList=searchHits.stream().map(hit->{
                //获取高亮数据
                Map<String, List<String>> highlightFields = hit.getHighlightFields();
                //更换高亮数据,判断高亮字段是否存在
                if(!CollectionUtils.isEmpty(highlightFields.get("desc")))
                {
                    hit.getContent().setDesc(highlightFields.get("desc").get(0));
                }
                if(!CollectionUtils.isEmpty(highlightFields.get("name")))
                {
                    hit.getContent().setName(highlightFields.get("name").get(0));
                }

                return hit.getContent();


            }).collect(Collectors.toList());

            userList.forEach(u-> System.out.println(u));
        }

    }
    /**
     * 创建索引和文档，更新文档内容
     */
    @Test
    public void saveIndex()
    {
        EsUser user=new EsUser(1l,"lchen",18,"我是中国人", new Date());
        mapper.save(user);
    }

    /**
     * 批量添加或更新
     */
    @Test
    public void batchSave()
    {
        List<EsUser> userList=new ArrayList<>();
//        userList.add(new EsUser(1l,"lchen-1",20,"这是一个神奇的铁路。", new Date()));
//        userList.add(new EsUser(2l,"lchen-2",30,"Spring是一个非常好用的开源框架。", new Date()));
        userList.add(new EsUser(3l,"张三-1",15,"法外狂徒张三来了一个张三。", new Date()));
//        userList.add(new EsUser(4l,"张三-2",36,"如果怒回去一个了。", new Date()));
//        userList.add(new EsUser(5l,"李四-1",40,"这是来了吗。", new Date()));
//        userList.add(new EsUser(6l,"李四-2",60,"不回来怎么办。", new Date()));
        mapper.saveAll(userList);
    }

    /**
     * 删除操作
     */
    @Test
    public void delete()
    {
        mapper.deleteById(1l);
    }

    /**
     * 搜索
     */
    @Test
    public void search(){

        //根据id查询
        Optional<EsUser> user = mapper.findById(2l);
        System.out.println(user.isPresent()?user.get():"空值");

        System.out.println("-------------------------------------");

        //查询所有
        Iterable<EsUser> list = mapper.findAll();
        list.forEach(u-> System.out.println(u.toString()));

        System.out.println("-------------------------------------");

        //按照条件查询
       List<EsUser> list1 = mapper.findByNameOrDesc("lchen","一个");
       list1.forEach(u-> System.out.println(u.toString()));

        System.out.println("-------------------------------------");

        List<EsUser> list2 = mapper.findByDesc("张三");
        list2.forEach(u-> System.out.println(u.toString()));
    }
}
