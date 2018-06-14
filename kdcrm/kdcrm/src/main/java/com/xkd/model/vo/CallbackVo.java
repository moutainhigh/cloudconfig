package com.xkd.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by dell on 2017/11/30.
 */
@ApiModel
public class CallbackVo {
    String time;
    String content;

    @ApiModelProperty(value = "时间",required = true,example = "2012-10-10 10:10:10")
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    @ApiModelProperty(value = "内容",required = true,example = "牛B企业")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
