package com.mall.manager.service.impl;

import com.google.gson.Gson;
import com.mall.manager.service.SearchItemService;
import com.mall.common.exception.StoreException;
import com.mall.common.util.HttpUtil;
import com.mall.manager.dao.SearchItemMapper;
import com.mall.manager.model.Vo.EsCount;
import com.mall.manager.model.Vo.EsInfo;
import com.mall.manager.model.Vo.SearchItem;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Service
public class SearchItemServiceImpl implements SearchItemService {

    private final static Logger log = LoggerFactory.getLogger(SearchItemServiceImpl.class);

    @Autowired
    private SearchItemMapper searchItemMapper;

//    @Value("${ES_CONNECT_IP}")
    private String ES_CONNECT_IP="192.168.229.128";

//    @Value("${ES_NODE_CLIENT_PORT}")
    private String ES_NODE_CLIENT_PORT="9200";

//    @Value("${ES_CLUSTER_NAME}")
    private String ES_CLUSTER_NAME="ming";

//    @Value("${ITEM_INDEX}")
    private String ITEM_INDEX="item";

//    @Value("${ITEM_TYPE}")
    private String ITEM_TYPE="itemList";

    @Autowired
    private TransportClient client;

    @Override
    public int importAllItems() {
        try {
//            Settings settings = Settings.builder()
//                    .put("cluster.name", ES_CLUSTER_NAME).build();
//            TransportClient client = new PreBuiltTransportClient(settings)
//                    .addTransportAddress(new TransportAddress(InetAddress.getByName(ES_CONNECT_IP), 9300));

            //批量添加
            BulkRequestBuilder bulkRequest = client.prepareBulk();
            //查询商品列表
            List<SearchItem> itemList = searchItemMapper.getItemList();
            //遍历商品列表
            for (SearchItem searchItem : itemList) {
                String image = searchItem.getProductImageBig();
                if (image != null && !"".equals(image)) {
                    String[] strings = image.split(",");
                    image = strings[0];
                } else {
                    image = "";
                }
                searchItem.setProductImageBig(image);
                bulkRequest.add(client.prepareIndex("item", "itemList", String.valueOf(searchItem.getProductId()))
                        .setSource(jsonBuilder()
                                .startObject()
                                .field("productId", searchItem.getProductId())
                                .field("salePrice", searchItem.getSalePrice())
                                .field("productName", searchItem.getProductName())
                                .field("subTitle", searchItem.getSubTitle())
                                .field("productImageBig", searchItem.getProductImageBig())
                                .field("categoryName", searchItem.getCategoryName())
                                .field("cid", searchItem.getCid())
                                .endObject()
                        )
                );
            }
            BulkResponse bulkResponse = bulkRequest.get();
            log.info("更新索引成功");
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new StoreException("导入ES索引库出错，请再次尝试");
        }
        return 1;
    }

    @Override
    public EsInfo getEsInfo() {
        String healthUrl = "http://" + ES_CONNECT_IP + ":" + ES_NODE_CLIENT_PORT + "/_cluster/health";//
        String countUrl = "http://" + ES_CONNECT_IP + ":" + ES_NODE_CLIENT_PORT + "/_count";//
        String healthResult = HttpUtil.sendGet(healthUrl);
        String countResult = HttpUtil.sendGet(countUrl);
        if (StringUtils.isBlank(healthResult) || StringUtils.isBlank(countResult)) {
            throw new StoreException("连接集群失败，请检查ES运行状态");
        }
        EsInfo esInfo = new EsInfo();
        EsCount esCount = new EsCount();
        try {
            esInfo = new Gson().fromJson(healthResult, EsInfo.class);
            esCount = new Gson().fromJson(countResult, EsCount.class);
            esInfo.setCount(esCount.getCount());
        } catch (Exception e) {
            e.printStackTrace();
            throw new StoreException("获取ES信息出错");
        }
        return esInfo;
    }
}
