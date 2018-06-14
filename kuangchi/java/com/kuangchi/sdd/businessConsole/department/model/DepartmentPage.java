package com.kuangchi.sdd.businessConsole.department.model;

import com.kuangchi.sdd.base.model.BaseModelSupport;

/**
 * Created by jianhui.wu on 2016/2/17.
 */
public class DepartmentPage  extends BaseModelSupport {
    private  String depId;
    private  String staffNum;
    private  String empId;
    private  String yhDm;
    private  String yhNo;
    private String yhMc;
    private String gwDm;
    private String gwMc;
    private String bmDm;
    private String cardNum;
    private String xb;
    private String rzsj;
    private String copyCardNumsArr;//被复制的卡号
    
    
    
    public String getCopyCardNumsArr() {
		return copyCardNumsArr;
	}
	public void setCopyCardNumsArr(String copyCardNumsArr) {
		this.copyCardNumsArr = copyCardNumsArr;
	}
	public String getXb() {
		return xb;
	}
	public void setXb(String xb) {
		this.xb = xb;
	}
	public String getRzsj() {
		return rzsj;
	}
	public void setRzsj(String rzsj) {
		this.rzsj = rzsj;
	}

	private String layerDeptNum; // 分层部门代码，用于筛选显示部门	by yuman.gao
	public String getLayerDeptNum() {
		return layerDeptNum;
	}
	public void setLayerDeptNum(String layerDeptNum) {
		this.layerDeptNum = layerDeptNum;
	}
	
	private String hireState;
	
	
   /* private String jsDm;
    


    public String getJsDm() {
		return jsDm;
	}

	public void setJsDm(String jsDm) {
		this.jsDm = jsDm;
	}*/

	public String getHireState() {
		return hireState;
	}
	public void setHireState(String hireState) {
		this.hireState = hireState;
	}
	public String getStaffNum() {
		return staffNum;
	}

	public void setStaffNum(String staffNum) {
		this.staffNum = staffNum;
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public String getYhNo() {
		return yhNo;
	}

	public void setYhNo(String yhNo) {
		this.yhNo = yhNo;
	}

	public String getDepId() {
        return depId;
    }

    public void setDepId(String depId) {
        this.depId = depId;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getYhDm() {
        return yhDm;
    }

    public void setYhDm(String yhDm) {
        this.yhDm = yhDm;
    }

    public String getYhMc() {
        return yhMc;
    }

    public void setYhMc(String yhMc) {
        this.yhMc = yhMc;
    }

    public String getGwDm() {
        return gwDm;
    }

    public void setGwDm(String gwDm) {
        this.gwDm = gwDm;
    }

    public String getBmDm() {
        return bmDm;
    }

    public void setBmDm(String bmDm) {
        this.bmDm = bmDm;
    }
	public String getGwMc() {
		return gwMc;
	}
	public void setGwMc(String gwMc) {
		this.gwMc = gwMc;
	}
    
}
