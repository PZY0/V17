package com.qianfeng.v17searchservice;

import com.qianfeng.api.ISearchService;
import com.qianfeng.result.ResultBean;
import com.qianfeng.vo.ProductPartVO;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
class V17SearchServiceApplicationTests {

    @Autowired
    private SolrClient solrClient;

    @Autowired
    private ISearchService searchService;

    @Test
    void synAll(){
        ResultBean resultBean = searchService.synAll();
        System.out.println(resultBean.getData());
    }

    @Test
    void synById(){
        ResultBean resultBean = searchService.synById(11L);
        System.out.println(resultBean.getData());
    }

    @Test
    void queryKeywords(){
        ResultBean resultBean = searchService.queryByKeywords("小米9");
        List<ProductPartVO> list = (List<ProductPartVO>) resultBean.getData();
        for (ProductPartVO product : list) {
            System.out.println(product.getName());
        }
    }

    @Test
    void addOrUpdate() throws IOException, SolrServerException {
        //创建ducument对象
        SolrInputDocument document = new SolrInputDocument();
        //设置相关的属性
        //前后添加的id相同时，则此次添加会变成修改相同id的信息，添加的id不同时才会添加信息
        document.setField("id",4);
        document.setField("product_name","超级旗舰");
        document.setField("product_price",2999);
        document.setField("product_sale_point","90赫兹");
        document.setField("product_images","666");
        //保存
        //solrClient.add(document);
        //solrClient.commit();
        solrClient.add("collection2",document);
        solrClient.commit("collection2");
    }

    @Test
    void query() throws IOException, SolrServerException {
        //组装查询条件
        SolrQuery solrQuery = new SolrQuery();
        //条件会分词
        solrQuery.setQuery("product_name:一加旗舰");
        //执行查询
        QueryResponse response = solrClient.query(solrQuery);
        //得到结果
        SolrDocumentList list = response.getResults();
        for (SolrDocument document : list) {
            System.out.println(document.getFieldValue("product_name")+"-->"+document.getFieldValue("product_price"));
        }
    }

    @Test
    void del() throws IOException, SolrServerException {
        //删除也会分词，deleteByQuery会把查询到的分词都删除
        //solrClient.deleteByQuery("product_name:一加旗舰");
        solrClient.deleteById("1");
        solrClient.commit();
    }

}
