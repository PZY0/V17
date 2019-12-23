package com.qianfeng.v17searchservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qianfeng.api.ISearchService;
import com.qianfeng.mapper.TProductMapper;
import com.qianfeng.result.PageResultBean;
import com.qianfeng.result.ResultBean;
import com.qianfeng.vo.ProductPartVO;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author pangzhenyu
 * @Date 2019/11/3
 */
@Service
public class SearchServiceImpl implements ISearchService {

    @Autowired
    private TProductMapper productMapper;

    @Autowired
    private SolrClient solrClient;

    @Override
    public ResultBean synAll() {
        //List<TProduct> list = productMapper.selectAll();
        List<ProductPartVO> list = productMapper.selectPart();
        for (ProductPartVO product : list) {
            SolrInputDocument document = new SolrInputDocument();
            document.setField("id",product.getId());
            document.setField("product_name",product.getName());
            document.setField("product_price",product.getPrice());
            document.setField("product_sale_point",product.getSalePoint());
            document.setField("product_images",product.getImages());
            try {
                solrClient.add(document);
            } catch (SolrServerException | IOException e) {
                e.printStackTrace();
                return new  ResultBean(500,"同步数据失败");
            }
        }
        try {
            solrClient.commit();
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            return new  ResultBean(500,"同步数据失败");
        }
        return new  ResultBean(200,"同步数据成功");
    }

    @Override
    public ResultBean synById(Long id) {
        //TProduct product = productMapper.selectByPrimaryKey(id);
        ProductPartVO product = productMapper.selectPartById(id);
        SolrInputDocument document = new SolrInputDocument();
        document.setField("id",product.getId());
        document.setField("product_name",product.getName());
        document.setField("product_price",product.getPrice());
        document.setField("product_sale_point",product.getSalePoint());
        document.setField("product_images",product.getImages());
        try {
            solrClient.add(document);
            solrClient.commit();
            //一个solr提供多个搜索服务
            /*solrClient.add("collection2",document);
            solrClient.commit("collection2");*/
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            return new  ResultBean(500,"同步数据失败");
        }
        return new ResultBean(200,"同步数据成功");
    }

    @Override
    public ResultBean delById(Long id) {
        try {
            //solrClient.deleteById(""+id);
            solrClient.deleteById(String.valueOf(id));
            solrClient.commit();
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            return new  ResultBean(500,"同步数据失败");
        }
        return new ResultBean(200,"同步数据成功");
    }

    @Override
    public ResultBean queryByKeywords(String keywords) {
        //组装查询条件
        SolrQuery query = new SolrQuery();
        if(keywords==null || "".equals(keywords.trim())){
            query.setQuery("product_name:华为");
        }else{
            query.setQuery("product_name:"+keywords);
        }
        //2.设置高亮
        query.setHighlight(true);
        query.addHighlightField("product_name");
        query.setHighlightSimplePre("<font color='red'>");
        query.setHighlightSimplePost("</font>");
        List<ProductPartVO> list = null;
        try {
            QueryResponse response = solrClient.query(query);
            SolrDocumentList documentList = response.getResults();
            list = new ArrayList<>(documentList.size());
            //获取高亮信息
            //外层Map:关键字查询可能会有多个结果，key值对应的是id，value是对应的高亮信息
            //里层Map:设置查询的可能不止一个字段，比如name和salePoint，key是对应的字段名，value是字段名对应的高亮信息
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            for (SolrDocument document : documentList) {
                //TProduct product = new TProduct();
                ProductPartVO product = new ProductPartVO();
                product.setId(Long.parseLong(document.getFieldValue("id").toString()));
                //product.setName(document.getFieldValue("product_name").toString());
                product.setPrice(Long.parseLong(document.getFieldValue("product_price").toString()));
                product.setSalePoint(document.getFieldValue("product_sale_point").toString());
                product.setImages(document.getFieldValue("product_images").toString());
                //针对高亮做设置
                Map<String, List<String>> map = highlighting.get(document.getFieldValue("id"));
                List<String> productNameHighLighting = map.get("product_name");
                //当查询的字段是keywords，有可能查询到只能卖点高亮信息，没有name，所以这里需要判断是否为空
                if(productNameHighLighting != null && productNameHighLighting.size() > 0){
                    product.setName(productNameHighLighting.get(0));
                }else{
                    product.setName(document.getFieldValue("product_name").toString());
                }
                list.add(product);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            return new ResultBean(500,null);
        }
        return new ResultBean(200,list);
    }

    @Override
    public ResultBean queryByKeywords(String keywords, Integer pageIndex, Integer pageSize) {
        //组装查询条件
        SolrQuery query = new SolrQuery();
        if(keywords==null || "".equals(keywords.trim())){
            query.setQuery("product_name:华为");
        }else{
            query.setQuery("product_name:"+keywords);
        }
        //2.设置高亮
        query.setHighlight(true);
        query.addHighlightField("product_name");
        query.setHighlightSimplePre("<font color='red'>");
        query.setHighlightSimplePost("</font>");
        //添加分页设置
        //从哪个下标开始
        query.setStart((pageIndex-1)*pageSize);
        query.setRows(pageSize);

        long total = 0;
        List<ProductPartVO> list = null;
        try {
            QueryResponse response = solrClient.query(query);
            SolrDocumentList documentList = response.getResults();
            total = documentList.getNumFound();
            list = new ArrayList<>(documentList.size());
            //获取高亮信息
            //外层Map:关键字查询可能会有多个结果，key值对应的是id，value是对应的高亮信息
            //里层Map:设置查询的可能不止一个字段，比如name和salePoint，key是对应的字段名，value是字段名对应的高亮信息
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            for (SolrDocument document : documentList) {
                //TProduct product = new TProduct();
                ProductPartVO product = new ProductPartVO();
                product.setId(Long.parseLong(document.getFieldValue("id").toString()));
                //product.setName(document.getFieldValue("product_name").toString());
                product.setPrice(Long.parseLong(document.getFieldValue("product_price").toString()));
                product.setSalePoint(document.getFieldValue("product_sale_point").toString());
                product.setImages(document.getFieldValue("product_images").toString());
                //针对高亮做设置
                Map<String, List<String>> map = highlighting.get(document.getFieldValue("id"));
                List<String> productNameHighLighting = map.get("product_name");
                //当查询的字段是keywords，有可能查询到只能卖点高亮信息，没有name，所以这里需要判断是否为空
                if(productNameHighLighting != null && productNameHighLighting.size() > 0){
                    product.setName(productNameHighLighting.get(0));
                }else{
                    product.setName(document.getFieldValue("product_name").toString());
                }
                list.add(product);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            return new ResultBean(500,null);
        }
        PageResultBean pageResultBean = new PageResultBean();
        pageResultBean.setList(list);
        //pageResultBean.setNavigatePages(1);
        pageResultBean.setPageNum(pageIndex);
        pageResultBean.setPageSize(pageSize);
        pageResultBean.setPages((int) (total%pageSize==0?total%pageSize:total%pageSize+1));
        pageResultBean.setTotal(total);
        return new ResultBean(200,pageResultBean);
    }
}
