package com.wjh.companydemo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ibm.icu.util.StringTrieBuilder;
import com.wjh.companydemomodel.model.CompanyPo;
import com.wjh.companydemomodel.model.CompanySearchDto;
import com.wjh.utils.elasticsearch.ElasticsearchPool;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CompanyIndexService {
    Logger logger = LogManager.getLogger();
    @Value("${elasticsearch.ip}")
    String elasticsearchIp;
    @Value("${elasticsearch.port}")
    Integer elasticsearchPort;
    @Value("${elasticsearch.totalConnections}")
    Integer totalConnections;
    @Value("${elasticsearch.maxIdle}")
    Integer elasticsearchMaxIdle;
    @Value("${elasticsearch.minIdle}")
    Integer elasticsearchMinIdle;
    @Value("${elasticsearch.maxWaitInMillis}")
    Integer elasticsearchMaxWaitInMillis;


    private boolean indexExists(TransportClient client, String index) throws Exception {
        try {
            IndicesExistsRequest request = new IndicesExistsRequest(index);
            IndicesExistsResponse response = client.admin().indices().exists(request).actionGet();
            if (response.isExists()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void prepareIndex(TransportClient client, String index) throws Exception {
        if (!indexExists(client, index)) {
            client.admin().indices().prepareCreate(index).execute().actionGet();
            createMapping(client);
        }
    }


    private void deleteIndex(TransportClient client, String index) {
        client.admin().indices().prepareDelete(index).execute().actionGet();
    }

    public void deleteIndex() {
        TransportClient client = null;
        try {
            client = ElasticsearchPool.getClient(elasticsearchIp, elasticsearchPort, totalConnections, elasticsearchMaxIdle, elasticsearchMinIdle, elasticsearchMaxWaitInMillis);
            deleteIndex(client, "companyindex");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                ElasticsearchPool.returnClient(client);
            }
        }
    }


    // 创建映射
    private void createMapping(TransportClient client) throws Exception {
        // 创建ElasticSearch连接对象
        XContentBuilder builder = XContentFactory
                .jsonBuilder()
                .startObject()
                .startObject("company")
                .startObject("properties")
                .startObject("id")
                .field("type", "long")
                .field("store", "true")
                .field("index", "true")
                .endObject()
                .startObject("companyName")
                .field("type", "text")
                .field("store", "true")
                .field("analyzer", "ik_max_word")
                .field("index", "true")
                .endObject()
                .startObject("country")
                .field("type", "keyword")
                .field("store", "true")
                .field("index", "true")
                .endObject()
                .startObject("province")
                .field("type", "keyword")
                .field("store", "true")
                .field("index", "true")
                .endObject()
                .startObject("city")
                .field("type", "keyword")
                .field("store", "true")
                .field("index", "true")
                .endObject()
                .startObject("county")
                .field("type", "keyword")
                .field("store", "true")
                .field("index", "true")
                .endObject()
                .startObject("updatedBy")
                .field("type", "keyword")
                .field("store", "true")
                .field("index", "true")
                .endObject()
                .startObject("updateDate")
                .field("type", "keyword")
                .field("store", "true")
                .field("index", "true")
                .endObject()
                .startObject("createdBy")
                .field("type", "keyword")
                .field("store", "true")
                .field("index", "true")
                .endObject()
                .startObject("createDate")
                .field("type", "keyword")
                .field("store", "true")
                .field("index", "true")
                .endObject()
                .startObject("content")
                .startObject("properties")
                .startObject("manageScope")
                .field("type", "text")
                .field("analyzer", "ik_max_word")
                .field("store", "true")
                .field("index", "true")
                .endObject()
                .startObject("representative")
                .field("type", "keyword")
                .field("store", "true")
                .field("index", "true")
                .endObject()
                .startObject("score")
                .field("type", "integer")
                .field("store", "true")
                .field("index", "true")
                .endObject()
                .endObject()
                .endObject()
                .startObject("contactors")
                .startObject("properties")
                .startObject("name")
                .field("type", "keyword")
                .field("store", "true")
                .field("index", "true")
                .endObject()
                .startObject("mobile")
                .field("type", "keyword")   //不分词就用keyword
                .field("store", "true")
                .field("index", "true")
                .endObject()
                .endObject()
                .endObject()
                .endObject()
                .endObject()
                .endObject();
        PutMappingRequest mapping = Requests.putMappingRequest("companyindex").type("company").source(builder);
        client.admin().indices().putMapping(mapping).actionGet();
    }


    // 直接在ElasticSearch中建立文档，自动创建索引

    public void addCompanyIndex(Map map) {
        try {
            List<Map<String, Object>> list = new ArrayList<>();
            list.add(map);
            addCompanyIndexList(list);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    public void addCompanyIndexList(List<Map<String, Object>> mapList) {
        // 创建ElasticSearch连接对象
        TransportClient client = null;
        try {
            client = ElasticsearchPool.getClient(elasticsearchIp, elasticsearchPort, totalConnections, elasticsearchMaxIdle, elasticsearchMinIdle, elasticsearchMaxWaitInMillis);
            prepareIndex(client, "companyindex");

            BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();

            for (int i = 0; i < mapList.size(); i++) {


                Map<String, Object> map = mapList.get(i);

                //写法1
                // 创建对象要添加对象的JSON格式字符串
                XContentBuilder builder = XContentFactory.contentBuilder(XContentType.JSON)
                        .map(map);

                // 建立文档对象
                IndexRequestBuilder indexRequestBuilder = client.prepareIndex("companyindex", "company", map.get("id").toString()).setSource(builder);

                bulkRequestBuilder.add(indexRequestBuilder);
            }


            BulkResponse response = bulkRequestBuilder.execute().actionGet();
            if (response.hasFailures()) {
                Iterator<BulkItemResponse> bulkItemResponse = response.iterator();
                while (bulkItemResponse.hasNext()) {
                    logger.error(bulkItemResponse.next().getFailure().getCause());
                }
                throw new RuntimeException("添加索引失败");

            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (client != null) {
                //用完以后归还连接,千成不要忘了
                ElasticsearchPool.returnClient(client);
            }
        }

    }


    public void search(CompanySearchDto companySearchDto) {
        TransportClient client = null;
        try {
            //从连接池中获取ElasticSearch连接对象
            client = ElasticsearchPool.getClient(elasticsearchIp, elasticsearchPort, totalConnections, elasticsearchMaxIdle, elasticsearchMinIdle, elasticsearchMaxWaitInMillis);
            prepareIndex(client, "companyindex");

            HighlightBuilder highlightBuilder = new HighlightBuilder()
                    .field("companyName")
                    .field("content.manageScope")
                    .preTags("<em>")
                    .postTags("</em>");

            String searchValue = companySearchDto.getSearchValue();
            String likeValue = "*" + searchValue + "*";


            //多字字段匹配
//        MultiMatchQueryBuilder multiMatchQueryBuilder=QueryBuilders.multiMatchQuery(searchValue,"manageScope","label","companyName")
            ;








            /**
             * 模糊查询，要求匹配的字段类型为keyword,即不分词
             */
//        WildcardQueryBuilder q4=QueryBuilders.wildcardQuery("staff.name",likeValue);
//
//        MatchPhraseQueryBuilder q5=QueryBuilders.matchPhraseQuery("staff.name",searchValue);


            BoolQueryBuilder qq1 = QueryBuilders.boolQuery();
            BoolQueryBuilder qq2 = QueryBuilders.boolQuery();
            if (StringUtils.isNotBlank(searchValue)) {
                /**
                 * match会首先将搜索的短语进行分词，然后再看查询结果中是否包含这些分词
                 */
                MatchQueryBuilder q1 = QueryBuilders
                        .matchQuery("companyName", searchValue).boost(10);
                MatchQueryBuilder q2 = QueryBuilders
                        .matchQuery("content.manageScope", searchValue).boost(5);


                /**
                 * matchPhrase是查询结果中在未分词的情况下一定会包含搜索的短语
                 */


                MatchPhraseQueryBuilder mp1 = QueryBuilders.matchPhraseQuery("companyName", searchValue).boost(10);

                MatchPhraseQueryBuilder mp2 = QueryBuilders.matchPhraseQuery("content.manageScope", searchValue).boost(5);
                qq1.should(q1).should(q2).boost(1);
                qq2.should(mp1).should(mp2).boost(10);//设置该条件的优先级
            }


//        DisMaxQueryBuilder disMaxQueryBuilder=QueryBuilders.disMaxQuery().add(q1).add(q2).add(q3);


            /**
             * 这里满足qq1 的条件时，一定满足qq2 的条件，但这里为什么还要有一个should(qq2) 呢，原因就是要保证matchPhrase匹配结果要排在match匹配结果之前
             */
            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().must(qq1).should(qq2);





            if (StringUtils.isNotBlank(companySearchDto.getCountry())) {
                //过滤条件
                TermQueryBuilder qf1 = QueryBuilders
                        .termQuery("country", companySearchDto.getCountry());
                 queryBuilder.filter(qf1);
            }




            if (StringUtils.isNotBlank(companySearchDto.getProvince())) {
                //过滤条件
                TermQueryBuilder qf1 = QueryBuilders
                        .termQuery("province", companySearchDto.getProvince());
                queryBuilder.filter(qf1);
            }


            if (StringUtils.isNotBlank(companySearchDto.getCity())) {
                //过滤条件
                TermQueryBuilder qf1 = QueryBuilders
                        .termQuery("city", companySearchDto.getCity());
                queryBuilder.filter(qf1);
            }


            if (StringUtils.isNotBlank(companySearchDto.getCounty())) {
                //过滤条件
                TermQueryBuilder qf1 = QueryBuilders
                        .termQuery("county", companySearchDto.getCounty());
                queryBuilder.filter(qf1);
            }


            if (StringUtils.isNotBlank(companySearchDto.getContactorMobile())) {
                //过滤条件
                TermQueryBuilder qf1 = QueryBuilders
                        .termQuery("contactors.mobile", companySearchDto.getContactorMobile());
                queryBuilder.filter(qf1);
            }
            if (StringUtils.isNotBlank(companySearchDto.getContactorName())) {
                //过滤条件
                TermQueryBuilder qf1 = QueryBuilders
                        .termQuery("contactors.name", companySearchDto.getContactorName());
                queryBuilder.filter(qf1);
            }

            if (StringUtils.isNotBlank(companySearchDto.getRepresentative())) {
                //过滤条件
                TermQueryBuilder qf1 = QueryBuilders
                        .termQuery("content.representative", companySearchDto.getRepresentative());
                queryBuilder.filter(qf1);
            }



        /*
          elasticsearch的should()相当于 SOLR 的 OR   must 相当于 SOLR的 AND
         */


            SearchRequestBuilder request = client.prepareSearch("companyindex")
                    .setTypes("company")
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                    .setQuery(queryBuilder)
                    .highlighter(highlightBuilder)
//                .addSort("_score", SortOrder.DESC)
                    .setFrom(0)
                    .setSize(50);
            SearchResponse response = request.get();

            SearchHits searchHits = response.getHits();
            long total = searchHits.getTotalHits();
            System.out.println("总共" + total + "条");


            for (int i = 0; i < searchHits.getHits().length; i++) {

                SearchHit searchHit = searchHits.getHits()[i];

                Map<String, Object> result = JSON.parseObject(searchHit.getSourceAsString(), new TypeReference<Map<String, Object>>() {
                });

                result.put("id", searchHit.getId());

                Map<String, HighlightField> highlightMap = searchHit.getHighlightFields();

                Map<String, Object> highlightResultMap = new HashMap();

                Set<String> keySet = highlightMap.keySet();
                Iterator<String> iterator = keySet.iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    Text[] fragments = highlightMap.get(key).getFragments();
                    List<String> list = new ArrayList();
                    for (int j = 0; j < fragments.length; j++) {
                        list.add(fragments[j].toString());
                    }
                    highlightResultMap.put(key, list);
                }

                result.put("highlight", highlightResultMap);

                System.out.println(JSON.toJSONString(result));

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (client != null) {
                ElasticsearchPool.returnClient(client);
            }
        }
    }


    private void facetSearch() {
        // 创建ElasticSearch连接对象
        TransportClient client = null;
        try {
            client = ElasticsearchPool.getClient(elasticsearchIp, elasticsearchPort, totalConnections, elasticsearchMaxIdle, elasticsearchMinIdle, elasticsearchMaxWaitInMillis);

            prepareIndex(client, "companyindex");


            MatchPhraseQueryBuilder mpq1 = QueryBuilders
                    .matchPhraseQuery("companyName", "科技");
            MatchPhraseQueryBuilder mpq2 = QueryBuilders
                    .matchPhraseQuery("description", "部门");
            QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(mpq1).must(mpq2);


            AbstractAggregationBuilder provinceAggregation = AggregationBuilders.terms("provinceAgg").field("province");


            AbstractAggregationBuilder countryAggregation = AggregationBuilders.terms("countryAgg").field("country");

            AbstractAggregationBuilder staffNameAggregation = AggregationBuilders.terms("staffNameAgg").field("staff.name");


            SearchResponse response = client.prepareSearch("companyindex").setTypes("company")
                    .setQuery(queryBuilder)
                    .setSize(0)
                    .addAggregation(provinceAggregation)
                    .addAggregation(countryAggregation)
                    .addAggregation(staffNameAggregation)
                    .execute()
                    .actionGet();

            Aggregations terms = response.getAggregations();


            System.out.println(terms.asMap());

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (client != null) {
                ElasticsearchPool.returnClient(client);
            }
        }

    }

}
