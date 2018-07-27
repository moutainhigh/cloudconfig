package com.wjh.idconfiguration.model;

import com.wjh.common.model.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;


@Service
public class IdGenerator {


    @Autowired
    IdService idService;




    static   LinkedList<Long> linkedList=new LinkedList();

    public synchronized Long generateId() {
        if (linkedList.size()==0){
            ResponseModel<List<Long>> responseModel=idService.batchGenerateId();
            List<Long>  idList= (List<Long>) responseModel.getResModel();
            linkedList.addAll(idList);
        }
        return linkedList.pop();
    }

}
