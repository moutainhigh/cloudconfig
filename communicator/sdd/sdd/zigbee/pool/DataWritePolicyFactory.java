package com.kuangchi.sdd.zigbee.pool;

import com.kuangchi.sdd.zigbee.handler.DataWritePolicy;
import com.kuangchi.sdd.zigbee.model.Data;
import com.kuangchi.sdd.zigbee.model.DataWritePolicyEE01;
import com.kuangchi.sdd.zigbee.model.DataWritePolicyEE02;
import com.kuangchi.sdd.zigbee.model.DataWritePolicyEE03;
import com.kuangchi.sdd.zigbee.model.DataWritePolicyEE04;
import com.kuangchi.sdd.zigbee.model.DataWritePolicyEE05;
import com.kuangchi.sdd.zigbee.model.DataWritePolicyEE06;
import com.kuangchi.sdd.zigbee.model.DataWritePolicyEE07;
import com.kuangchi.sdd.zigbee.model.DataWritePolicyEE08;
import com.kuangchi.sdd.zigbee.model.DataWritePolicyEE09;
import com.kuangchi.sdd.zigbee.model.DataWritePolicyEE0A;
import com.kuangchi.sdd.zigbee.model.DataWritePolicyEE0B;
import com.kuangchi.sdd.zigbee.model.DataWritePolicyEE0C;
import com.kuangchi.sdd.zigbee.model.DataWritePolicyEE0D;
import com.kuangchi.sdd.zigbee.model.DataWritePolicyEE0E;

/**
 * 写数据策略工厂类
 * 
 * */
public class DataWritePolicyFactory {
        public static DataWritePolicy getDataWritePolicy(Data data){
        	    if (data.getHeader()!=0xee) {
					 return null;
				}
        	    if (data.getCmd()==0x01) {
					return new DataWritePolicyEE01();
				}else if(data.getCmd()==0x02){
					return new DataWritePolicyEE02();
				}else if(data.getCmd()==0x03){
					return new DataWritePolicyEE03();
				}else if(data.getCmd()==0x04){
					return new DataWritePolicyEE04();
				}else if(data.getCmd()==0x05){
					return new DataWritePolicyEE05();
				}else if(data.getCmd()==0x06){
					return new DataWritePolicyEE06();
				}else if(data.getCmd()==0x07){
					return new DataWritePolicyEE07();
				}else if(data.getCmd()==0x08){
					return new DataWritePolicyEE08();
				}else if(data.getCmd()==0x09){
					return new DataWritePolicyEE09();
				}else if(data.getCmd()==0x0a){
					return new DataWritePolicyEE0A();
				}else if(data.getCmd()==0x0b){
					return new DataWritePolicyEE0B();
				}else if(data.getCmd()==0x0c){
					return new DataWritePolicyEE0C();
				}else if(data.getCmd()==0x0d){
					return new DataWritePolicyEE0D();
				}else if(data.getCmd()==0x0e){
					return new DataWritePolicyEE0E();
				}else{
					return null;
				}
        	    
        }
}
