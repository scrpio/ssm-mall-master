package com.mall.manager.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

@TableName("tb_base")
public class Base implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(value = "web_name")
    private String webName;

    @TableField(value = "key_word")
    private String keyWord;

    private String description;

    @TableField(value = "source_path")
    private String sourcePath;

    @TableField(value = "upload_path")
    private String uploadPath;

    private String copyright;

    @TableField(value = "count_code")
    private String countCode;

    @TableField(value = "has_log_notice")
    private boolean hasLogNotice;

    @TableField(value = "log_notice")
    private String logNotice;

    @TableField(value = "has_all_notice")
    private boolean hasAllNotice;

    @TableField(value = "all_notice")
    private String allNotice;

    private String notice;

    @TableField(value = "update_log")
    private String updateLog;

    @TableField(value = "front_url")
    private String frontUrl;

    @TableField(value = "view_count")
    private long viewCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWebName() {
        return webName;
    }

    public void setWebName(String webName) {
        this.webName = webName == null ? null : webName.trim();
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord == null ? null : keyWord.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath == null ? null : sourcePath.trim();
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath == null ? null : uploadPath.trim();
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright == null ? null : copyright.trim();
    }

    public String getCountCode() {
        return countCode;
    }

    public void setCountCode(String countCode) {
        this.countCode = countCode == null ? null : countCode.trim();
    }

    public boolean getHasLogNotice() {
        return hasLogNotice;
    }

    public void setHasLogNotice(boolean hasLogNotice) {
        this.hasLogNotice = hasLogNotice;
    }

    public String getLogNotice() {
        return logNotice;
    }

    public void setLogNotice(String logNotice) {
        this.logNotice = logNotice == null ? null : logNotice.trim();
    }

    public boolean getHasAllNotice() {
        return hasAllNotice;
    }

    public void setHasAllNotice(boolean hasAllNotice) {
        this.hasAllNotice = hasAllNotice;
    }

    public String getAllNotice() {
        return allNotice;
    }

    public void setAllNotice(String allNotice) {
        this.allNotice = allNotice == null ? null : allNotice.trim();
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice == null ? null : notice.trim();
    }

    public String getUpdateLog() {
        return updateLog;
    }

    public void setUpdateLog(String updateLog) {
        this.updateLog = updateLog == null ? null : updateLog.trim();
    }

    public String getFrontUrl() {
        return frontUrl;
    }

    public void setFrontUrl(String frontUrl) {
        this.frontUrl = frontUrl == null ? null : frontUrl.trim();
    }

    public long getViewCount() {
        return viewCount;
    }

    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }
}