package com.xkd.model;

import java.util.List;

public class InvitationPages {

	private String id;
	private String invitationId; // 邀请函ID',
	private String bgImgSrc; // 背景图片url路径',
	private int nodeName; // 章节',
	private String bgColor;// 背景颜色',
	private List<InvitationPagesElement> elements;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInvitationId() {
		return invitationId;
	}

	public void setInvitationId(String invitationId) {
		this.invitationId = invitationId;
	}

	public String getBgImgSrc() {
		return bgImgSrc;
	}

	public void setBgImgSrc(String bgImgSrc) {
		this.bgImgSrc = bgImgSrc;
	}

	

	public int getNodeName() {
		return nodeName;
	}

	public void setNodeName(int nodeName) {
		this.nodeName = nodeName;
	}

	public String getBgColor() {
		return bgColor;
	}

	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	public List<InvitationPagesElement> getElements() {
		return elements;
	}

	public void setElements(List<InvitationPagesElement> elements) {
		this.elements = elements;
	}

	

}
