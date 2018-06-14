package com.xkd.model.vo;

import com.xkd.model.groups.SaveValidateGroup;
import com.xkd.model.groups.UpdateValidateGroup;
import io.swagger.annotations.ApiModelProperty;
import org.apache.ibatis.annotations.Update;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by dell on 2018/4/20.
 */
public class MeetingTicketVo {
    private String id;
    private String meetingId;
    @NotNull(groups = {SaveValidateGroup.class})
    private String ticketTypeName;
    @NotNull(groups = {SaveValidateGroup.class})
    private BigDecimal price;
    @NotNull(groups = {SaveValidateGroup.class})
    private Integer amount;

    @ApiModelProperty(value = "id，修改会务时如果Id不为空，表示新增，为空表示新增",required = true,example = "123456")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @ApiModelProperty(value = "会务Id",required = true,example = "123456")
    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    @ApiModelProperty(value = "票务类型名称",required = true,example = "一等票")
    public String getTicketTypeName() {
        return ticketTypeName;
    }

    public void setTicketTypeName(String ticketTypeName) {
        this.ticketTypeName = ticketTypeName;
    }

    @ApiModelProperty(value = "价格",required = true,example = "598")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @ApiModelProperty(value = "总数量",required = true,example = "100")
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
