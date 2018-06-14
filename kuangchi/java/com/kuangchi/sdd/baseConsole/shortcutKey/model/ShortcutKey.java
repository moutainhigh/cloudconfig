package com.kuangchi.sdd.baseConsole.shortcutKey.model;

import com.kuangchi.sdd.base.model.BaseModelSupport;

public class ShortcutKey extends BaseModelSupport {
	private Integer id;
	private String yh_dm;   	//用户代码
	private String cd_dm;   	//菜单代码;
	private String name;   		//菜单名称
	private String url; 		//菜单url
	private String image_id; 	//快捷菜单图标id
	private String remark; 		//备注
	private String create_time; //创建时间
	private String CDFlag;		//菜单标记（标记所属的模块）1:基础数据 2:门禁系统 3:考勤系统 4:梯控系统 5:消费系统 6:访客系统
	private String js_dm;   	//角色代码
	
	
	public String getJs_dm() {
		return js_dm;
	}
	public void setJs_dm(String js_dm) {
		this.js_dm = js_dm;
	}
	public String getImage_id() {
		return image_id;
	}
	public void setImage_id(String image_id) {
		this.image_id = image_id;
	}
	public String getYh_dm() {
		return yh_dm;
	}
	public void setYh_dm(String yh_dm) {
		this.yh_dm = yh_dm;
	}
	public String getCd_dm() {
		return cd_dm;
	}
	public void setCd_dm(String cd_dm) {
		this.cd_dm = cd_dm;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getCDFlag() {
		return CDFlag;
	}
	public void setCDFlag(String cDFlag) {
		CDFlag = cDFlag;
	}
	
	
}
