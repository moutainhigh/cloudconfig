package com.xkd.model.vo;

import com.xkd.model.groups.SaveValidateGroup;
import com.xkd.model.groups.UpdateValidateGroup;
import com.xkd.model.vo.MeetingFieldVo;
import com.xkd.model.vo.MeetingTicketVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by dell on 2018/4/20.
 */
@ApiModel
public class MeetingVo {

    @NotNull(groups = {UpdateValidateGroup.class})
    private String id;
    @NotNull(groups = {SaveValidateGroup.class})
    private String meetingName;
    @NotNull(groups = {SaveValidateGroup.class})
    private String startTime;
    @NotNull(groups = {SaveValidateGroup.class})
    private String endTime;
    @NotNull(groups = {SaveValidateGroup.class})
    private String province;
    @NotNull(groups = {SaveValidateGroup.class})
    private String city;
    @NotNull(groups = {SaveValidateGroup.class})
    private String county;
    @NotNull(groups = {SaveValidateGroup.class})
    private String address;
    private String responsibleUserId;
    @NotNull(groups = {SaveValidateGroup.class})
    private String logo;
    private String detail;
    private String ticketRemark;
    @NotNull(groups = {SaveValidateGroup.class})
    private Integer isFree;
    @NotNull(groups = {SaveValidateGroup.class})
    private Integer flag;

    @NotNull(groups = {SaveValidateGroup.class})
    private List<MeetingFieldVo> meetingFieldVoList;

    @NotNull(groups = {SaveValidateGroup.class})
    private List<MeetingTicketVo> meetingTicketVoList;


    @ApiModelProperty(value = "id",required = true,example = "123456")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @ApiModelProperty(value = "会务名称",required = true,example = "十九大会议")
    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    @ApiModelProperty(value = "开始时间",required = true,example = "2012-10-11 10:10:10")
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @ApiModelProperty(value = "结束时间",required = true,example = "2012-10-10 10:10:10")
    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @ApiModelProperty(value = "省",required = true,example = "湖北省")
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @ApiModelProperty(value = "市",required = true,example = "武汉市")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @ApiModelProperty(value = "区",required = true,example = "东西湖区")
    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }
    @ApiModelProperty(value = "地址",required = true,example = "东西湖区15号")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @ApiModelProperty(value = "负责人Id ",required = true,example = "818")
    public String getResponsibleUserId() {
        return responsibleUserId;
    }

    public void setResponsibleUserId(String responsibleUserId) {
        this.responsibleUserId = responsibleUserId;
    }

    @ApiModelProperty(value = "会务Logo",required = true,example = "https://www.baidu.com/link?url=sM2Tys2MoTx06-L6CUirisVKPz9BTzRW8uMdCmZevBzeJeTBLJvIjEIcodtgyvLQnRY8ShEiNqAb9QFfRTKdPk_mi_-knfwcakNMrCDZgCPN7HALhRnOkW7WBdFAqDuiovFVniy-m8KPLr7ZQNX5y7_KwjJpRj6njbomjtviDzfHgO-RKid0vNsCZ99rOjLwJNOtDlV_AXU83CI2ch6EZgaDF-GTQ1-2HwHD9bTHPwJ9gvQM9k6RItz0tb1yqWiKatCuEc4pxRC1TjFq916zucms_TN_rHgtj-vTzF9Jm6I7fo2Xmg-qpVWbFV5Xxz08duHcLQ6wrSSBXVShzoH_7l2kefoucLt84Gx81Z8skH3l36RexJvHiV22KP1ZbYUTVgTeFAq6DQvGoVFAoenXB7o_338XqfL2OMsiYltZZbVTvmDOv7hZhv7n_LQ-viwS3lJFwNlOuDhgxAe6KcaYaQJVh9uLxNsYu3Thw1270kfkXkqc2bPOs_nqocokqUwzJ14ikqZ0g3I1XhDoX4flAjLvzsFYh0Dpc2fsXoyTatTsmJoNJFgJ4PREsTXJI2z3s5L27H89yvof6OE05pf4_q&timg=https%3A%2F%2Fss0.bdstatic.com%2F94oJfD_bAAcT8t7mm9GUKT-xh_%2Ftimg%3Fimage%26quality%3D100%26size%3Db4000_4000%26sec%3D1524215101%26di%3D761db5051c70f52b37c63d46bd6bea6a%26src%3Dhttp%3A%2F%2Fpic.nipic.com%2F2007-12-29%2F200712299383135_2.jpg&click_t=1524215101715&s_info=1583_745&wd=&eqid=9ac4b8670001d41e000000065ad9ad3c")
    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
    @ApiModelProperty(value = "id",required = true,example = "123456")
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @ApiModelProperty(value = "票务权益",required = true,example = "郑重声明：该票只能自己持有")
    public String getTicketRemark() {
        return ticketRemark;
    }

    public void setTicketRemark(String ticketRemark) {
        this.ticketRemark = ticketRemark;
    }

    @ApiModelProperty(value = "是否免费 0 免费  1 收费",required = true,example = "0")
    public Integer getIsFree() {
        return isFree;
    }

    public void setIsFree(Integer isFree) {
        this.isFree = isFree;
    }

    public List<MeetingFieldVo> getMeetingFieldVoList() {
        return meetingFieldVoList;
    }

    public void setMeetingFieldVoList(List<MeetingFieldVo> meetingFieldVoList) {
        this.meetingFieldVoList = meetingFieldVoList;
    }

    public List<MeetingTicketVo> getMeetingTicketVoList() {
        return meetingTicketVoList;
    }

    public void setMeetingTicketVoList(List<MeetingTicketVo> meetingTicketVoList) {
        this.meetingTicketVoList = meetingTicketVoList;
    }

    public Integer getFlag() {
        return flag;
    }
    @ApiModelProperty(value = "会议状态  0  未开始  1 已开始  2 未开始  3 停止",required = true,example = "0")
    public void setFlag(Integer flag) {
        this.flag = flag;
    }
}
