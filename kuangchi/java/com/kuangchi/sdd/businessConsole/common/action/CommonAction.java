package com.kuangchi.sdd.businessConsole.common.action;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.businessConsole.common.model.ConstantKeyValue;
import com.kuangchi.sdd.businessConsole.common.service.ConstantKeyValueService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jianhui.wu on 2016/2/3.
 */
@Controller("commonAction")
public class CommonAction  extends BaseActionSupport {
@Resource(name = "constantKeyValueService")
    ConstantKeyValueService constantKeyValueService;

    public void getStatusByType(){
        List<Map> list=new ArrayList<Map>();
        HttpServletRequest request=getHttpServletRequest();
        String type=request.getParameter("type");
        List<ConstantKeyValue>  constantKeyValueList=constantKeyValueService.getConstantKeyValue(type);
        for (ConstantKeyValue ckv:constantKeyValueList){
            Map<String, String> map = new HashMap<String, String>();
            map.put("VALUE", ckv.getKeyText());
            map.put("TEXT", ckv.getValueText());
            list.add(map);
        }
        printHttpServletResponse(GsonUtil.toJson(list));
    }

    @Override
    public Object getModel() {
        return null;
    }
}
