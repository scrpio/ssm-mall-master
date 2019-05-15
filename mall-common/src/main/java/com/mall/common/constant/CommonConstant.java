package com.mall.common.constant;

public interface CommonConstant {
    String CONTEXT_TOKEN = "Authorization";
    String CONTEXT_USERNAME = "contextUsername";
    String CONTEXT_PASSWORD = "contextPassword";
    String JWT_PRIVATE_KEY = "wwwstorecom";
    String RENEWAL_TIME = "renewalTime";
    String TOKEN = "token";
    /**
     * 限流标识
     */
    String LIMIT_ALL = "XMALL_LIMIT_ALL";
    /**
     * 词典库修改标识
     */
    String EXT_KEY = "DICT_EXT_KEY";

    /**
     * 词典库修改标识
     */
    String STOP_KEY = "DICT_STOP_KEY";

    /**
     * 扩展词库缓存key
     */
    String LAST_MODIFIED = "Last-Modified";

    /**
     * 停用词库缓存key
     */
    String ETAG = "ETAG";

    /**
     * 扩展词库
     */
    Integer DICT_EXT = 1;

    /**
     * 停用词库
     */
    Integer DICT_STOP = 0;
}
