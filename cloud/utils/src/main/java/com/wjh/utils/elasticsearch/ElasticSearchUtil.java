package com.wjh.utils.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;

public class ElasticSearchUtil {

//
//    public boolean indexExists(String index) throws Exception {
//        // 从连接池中获取ElasticSearch连接对象
//        TransportClient client = ElasticsearchPool.getClient();
//        IndicesExistsRequest request = new IndicesExistsRequest(index);
//        IndicesExistsResponse response = client.admin().indices().exists(request).actionGet();
//        if (response.isExists()) {
//            return true;
//        }
//        //用完以后归还连接,千成不要忘了
//        ElasticsearchPool.returnClient(client);
//        return false;
//    }
//
//    private void createIndex() throws Exception {
//        if (!indexExists("companyindex")) {
//            // 创建ElasticSearch连接对象
//            TransportClient client = ElasticsearchPool.getClient();
//            client.admin().indices().prepareCreate("companyindex").execute().actionGet();
//            createMapping(client);
//            //用完以后归还连接,千成不要忘了
//            ElasticsearchPool.returnClient(client);
//        }
//
//    }
//
//
//    // 创建映射
//    private void createMapping(TransportClient client) throws Exception {
//        // 创建ElasticSearch连接对象
//        XContentBuilder builder = XContentFactory
//                .jsonBuilder()
//                .startObject()
//                .startObject("company")
//                .startObject("properties")
//                .startObject("companyName")
//                .field("type", "text")
//                .field("store", "true")
//                .field("analyzer", "ik_max_word")
//                .field("index", "true")
//                .endObject()
//                .startObject("label")
//                .field("type", "text")
//                .field("store", "true")
//                .field("analyzer", "ik_max_word")
//                .field("index", "true")
//                .endObject()
//                .startObject("representative")
//                .field("type", "keyword")
//                .field("store", "true")
//                .field("index", "true")
//                .endObject()
//                .startObject("financeStatus")
//                .field("type", "keyword")
//                .field("store", "true")
//                .field("index", "true")
//                .endObject()
//                .startObject("phone")
//                .field("type", "keyword")
//                .field("store", "true")
//                .field("index", "true")
//                .endObject()
//                .startObject("companyAdviserId")
//                .field("type", "keyword")
//                .field("store", "true")
//                .field("index", "true")
//                .endObject()
//                .startObject("companyAdviser")
//                .field("type", "keyword")
//                .field("store", "true")
//                .field("index", "true")
//                .endObject()
//                .startObject("companyDirectorId")
//                .field("type", "keyword")
//                .field("store", "true")
//                .field("index", "true")
//                .endObject()
//                .startObject("companyDirector")
//                .field("type", "keyword")
//                .field("store", "true")
//                .field("index", "true")
//                .endObject()
//                .startObject("address")
//                .field("type", "text")
//                .field("analyzer", "ik_max_word")
//                .field("store", "true")
//                .field("index", "true")
//                .endObject()
//                .startObject("moneySituation")
//                .field("type", "keyword")
//                .field("store", "true")
//                .field("index", "true")
//                .endObject()
//                .startObject("communicatStatus")
//                .field("type", "keyword")
//                .field("store", "true")
//                .field("index", "true")
//                .endObject()
//                .startObject("manageScope")
//                .field("type", "text")
//                .field("analyzer", "ik_max_word")
//                .field("store", "true")
//                .field("index", "true")
//                .endObject()
//                .startObject("manageType")
//                .field("type", "keyword")
//                .field("store", "true")
//                .field("index", "true")
//                .endObject()
//                .startObject("staff")
//                .startObject("properties")
//                .startObject("id")
//                .field("type", "keyword")
//                .field("store", "true")
//                .field("index", "true")
//                .endObject()
//                .startObject("name")
//                .field("type", "keyword")   //不分词就用keyword
//                .field("store", "true")
//                .field("index", "true")
//                .endObject()
//                .endObject()
//                .endObject()
//                .endObject()
//                .endObject()
//                .endObject();
//        PutMappingRequest mapping = Requests.putMappingRequest("companyindex").type("company").source(builder);
//        client.admin().indices().putMapping(mapping).actionGet();
//    }
//
//
//    // 直接在ElasticSearch中建立文档，自动创建索引
//
//    public void singleInsert() throws IOException {
//        // 创建ElasticSearch连接对象
//        TransportClient client = ElasticsearchPool.getClient();
//
//        Map map1 = new HashMap();
//        map1.put("name", "张三");
//        map1.put("id", "s1");
//
//        Map<String, Object> map2 = new HashMap();
//        map2.put("name", "李四");
//        map2.put("id", "s2");
//
//        List staffList = new ArrayList();
//        staffList.add(map1);
//        staffList.add(map2);
//
//
//        Map<String, Object> companyMap = new HashMap();
//        companyMap.put("companyName", "成都淘宝科技有限公司");
//        companyMap.put("manageScope", "研究、开发计算机软、硬件、网络技术产品、多媒体产品；计算机系统的集成、设计、调试及维护；销售自产产品；并提供计算机技术咨询、技术服务、电子商务平台技术支持；经济与商务咨询服务；成年人非证书劳动职业技能培训和成年人的非文化教育培训（涉及前置审批的项目除外）；自有物业租赁。（依法须经批准的项目，经相关部门批准后方可开展经营活动）");
//        companyMap.put("representative", "戴珊");
//        companyMap.put("staff", staffList);
//
//
//        //写法1
//        // 创建对象要添加对象的JSON格式字符串
//        XContentBuilder builder = XContentFactory.jsonBuilder()
//                .value(companyMap);
//
//
//        //写法2
////        XContentBuilder builder = XContentFactory.jsonBuilder()
////                .startObject()
////                .field("companyName","成都淘宝科技有限公司")
////                .field("description","研究、开发计算机软、硬件、网络技术产品、多媒体产品；计算机系统的集成、设计、调试及维护；销售自产产品；并提供计算机技术咨询、技术服务、电子商务平台技术支持；经济与商务咨询服务；成年人非证书劳动职业技能培训和成年人的非文化教育培训（涉及前置审批的项目除外）；自有物业租赁。（依法须经批准的项目，经相关部门批准后方可开展经营活动）")
////                .field("representative","戴珊")
////                .field("staff",staffList)
////                .endObject();
//
//
//        // 建立文档对象
//        client.prepareIndex("companyindex", "company", "1").setSource(builder).get();
//        //用完以后归还连接,千成不要忘了
//        ElasticsearchPool.returnClient(client);
//    }
//
//
//    public void bulkInsert() throws IOException {
//        // 创建ElasticSearch连接对象
//        TransportClient client = ElasticsearchPool.getClient();
//        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
//
//        for (int i = 0; i < 1000; i++) {
//            Map map1 = new HashMap();
//            map1.put("name", "张三");
//            map1.put("id", "s1");
//
//            Map<String, Object> map2 = new HashMap();
//            map2.put("name", "李四");
//            map2.put("id", "s2");
//
//            Map map3 = new HashMap();
//            map3.put("name", "张三");
//            map3.put("id", "s3");
//
//            List staffList = new ArrayList();
//            staffList.add(map1);
//            staffList.add(map2);
//            staffList.add(map3);
//
//
//            Map<String, Object> companyMap = new HashMap();
//            companyMap.put("companyName", "成都淘宝科技有限公司");
//            companyMap.put("manageScope", "研究、开发计算机软、硬件、网络技术产品、多媒体产品；计算机系统的集成、设计、调试及维护；销售自产产品；并提供计算机技术咨询、技术服务、电子商务平台技术支持；经济与商务咨询服务；成年人非证书劳动职业技能培训和成年人的非文化教育培训（涉及前置审批的项目除外）；自有物业租赁。（依法须经批准的项目，经相关部门批准后方可开展经营活动）");
//            companyMap.put("representative", "戴珊");
//
//            if (i < 100) {
//                companyMap.put("province", "湖北");
//            } else {
//                companyMap.put("province", "四川");
//            }
//            if (i < 600) {
//                companyMap.put("country", "中国");
//            } else {
//                companyMap.put("country", "美国");
//            }
//            companyMap.put("staff", staffList);
//
//            //写法1
//            // 创建对象要添加对象的JSON格式字符串
//            XContentBuilder builder = XContentFactory.jsonBuilder()
//                    .value(companyMap);
//
//            // 建立文档对象
//            IndexRequestBuilder indexRequestBuilder = client.prepareIndex("companyindex", "company", "" + i).setSource(builder);
//
//            bulkRequestBuilder.add(indexRequestBuilder);
//        }
//
//
//        bulkRequestBuilder.execute().actionGet();
//
//        //用完以后归还连接,千成不要忘了
//        ElasticsearchPool.returnClient(client);
//    }
//
//
//    private void search() throws UnknownHostException {
//        //从连接池中获取ElasticSearch连接对象
//        TransportClient client = ElasticsearchPool.getClient();
//        HighlightBuilder highlightBuilder = new HighlightBuilder()
//                .field("manageScope")
//                .field("companyName")
//                .field("label")
//                .preTags("<em>")
//                .postTags("</em>");
//
//        String searchValue = "工程技术开发";
//        String likeValue = "*" + searchValue + "*";
//
//
//        //多字字段匹配
////        MultiMatchQueryBuilder multiMatchQueryBuilder=QueryBuilders.multiMatchQuery(searchValue,"manageScope","label","companyName")
//        ;
//
//
//        MatchQueryBuilder q1 = QueryBuilders
//                .matchQuery("companyName", searchValue).boost(10);
//        MatchQueryBuilder q2 = QueryBuilders
//                .matchQuery("manageScope", searchValue).boost(5);
//        MatchQueryBuilder q3 = QueryBuilders.matchQuery("label", searchValue).boost(1);
//
//
//        MatchPhraseQueryBuilder mp1 = QueryBuilders.matchPhraseQuery("companyName", searchValue).boost(10);
//
//        MatchPhraseQueryBuilder mp2 = QueryBuilders.matchPhraseQuery("manageScope", searchValue).boost(5);
//
//        MatchPhraseQueryBuilder mp3 = QueryBuilders.matchPhraseQuery("label", searchValue).boost(1);
//
//
//        /**
//         * 模糊查询，要求匹配的字段类型为keyword,即不分词
//         */
////        WildcardQueryBuilder q4=QueryBuilders.wildcardQuery("staff.name",likeValue);
////
////        MatchPhraseQueryBuilder q5=QueryBuilders.matchPhraseQuery("staff.name",searchValue);
//
//
//        QueryBuilder qq1 = QueryBuilders.boolQuery().should(q1).should(q2).should(q3);
//
//        QueryBuilder qq2 = QueryBuilders.boolQuery().should(mp1).should(mp2).should(mp3);
//
//
////        DisMaxQueryBuilder disMaxQueryBuilder=QueryBuilders.disMaxQuery().add(q1).add(q2).add(q3);
//
//        //过滤条件
//        TermQueryBuilder qf1 = QueryBuilders
//                .termQuery("communicatStatus", "未沟通");
//
//
//        /*
//          elasticsearch的should()相当于 SOLR 的 OR   must 相当于 SOLR的 AND
//         */
//
//
//        QueryBuilder queryBuilder = QueryBuilders.boolQuery().should(qq2).must(qq1).filter(qf1);
//
//
//        SearchRequestBuilder request = client.prepareSearch("companyindex")
//                .setTypes("company")
//                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
//                .setQuery(queryBuilder)
//                .highlighter(highlightBuilder)
////                .addSort("_score", SortOrder.DESC)
//                .setFrom(0)
//                .setSize(50);
//        SearchResponse response = request.get();
//
//        SearchHits searchHits = response.getHits();
//        long total = searchHits.getTotalHits();
//        System.out.println("总共" + total + "条");
//
//
//        for (int i = 0; i < searchHits.getHits().length; i++) {
//
//            SearchHit searchHit = searchHits.getHits()[i];
//
//            Map<String, Object> result = JSON.parseObject(searchHit.getSourceAsString(), new TypeReference<Map<String, Object>>() {
//            });
//
//            result.put("id", searchHit.getId());
//
//            Map<String, HighlightField> highlightMap = searchHit.getHighlightFields();
//
//            Map<String, Object> highlightResultMap = new HashMap();
//
//            Set<String> keySet = highlightMap.keySet();
//            Iterator<String> iterator = keySet.iterator();
//            while (iterator.hasNext()) {
//                String key = iterator.next();
//                Text[] fragments = highlightMap.get(key).getFragments();
//                List<String> list = new ArrayList();
//                for (int j = 0; j < fragments.length; j++) {
//                    list.add(fragments[j].toString());
//                }
//                highlightResultMap.put(key, list);
//            }
//
//            result.put("highlight", highlightResultMap);
//
//            System.out.println(JSON.toJSONString(result));
//
//        }
//        //用完以后归还连接,千成不要忘了
//        ElasticsearchPool.returnClient(client);
//    }
//
//
//    private void facetSearch() throws UnknownHostException {
//        // 创建ElasticSearch连接对象
//        TransportClient client = ElasticsearchPool.getClient();
//
//
//        MatchPhraseQueryBuilder mpq1 = QueryBuilders
//                .matchPhraseQuery("companyName", "科技");
//        MatchPhraseQueryBuilder mpq2 = QueryBuilders
//                .matchPhraseQuery("description", "部门");
//        QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(mpq1).must(mpq2);
//
//
//        AbstractAggregationBuilder provinceAggregation = AggregationBuilders.terms("provinceAgg").field("province");
//
//
//        AbstractAggregationBuilder countryAggregation = AggregationBuilders.terms("countryAgg").field("country");
//
//        AbstractAggregationBuilder staffNameAggregation = AggregationBuilders.terms("staffNameAgg").field("staff.name");
//
//
//        SearchResponse response = client.prepareSearch("companyindex").setTypes("company")
//                .setQuery(queryBuilder)
//                .setSize(0)
//                .addAggregation(provinceAggregation)
//                .addAggregation(countryAggregation)
//                .addAggregation(staffNameAggregation)
//                .execute()
//                .actionGet();
//
//        Aggregations terms = response.getAggregations();
//
//
//        System.out.println(terms.asMap());
//
//        //用完以后归还连接,千成不要忘了
//        ElasticsearchPool.returnClient(client);
//
//    }
//
//
//    public static void main(String[] args) throws Exception {
//        try {
//
//            ElasticSearchUtil elasticSearchUtil = new ElasticSearchUtil();
//
////
//            elasticSearchUtil.createIndex();
////            elasticSearchUtil.bulkInsert();
//////            elasticSearchUtil.facetSearch();
////            elasticSearchUtil.bulkInsert2();
//
//            elasticSearchUtil.search();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


}
