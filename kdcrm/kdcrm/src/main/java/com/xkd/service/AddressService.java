package com.xkd.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xkd.mapper.AddressMapper;
import com.xkd.model.Address;

@Service
public class AddressService {

	@Autowired
	private AddressMapper addressMapper;
	
	public Address selectAddressByUserId(String userId){
		
		Address address = addressMapper.selectAddressByUserId(userId);
		
		return address;
	}

	public Integer updateAddressBySql(String sql) {
		
		Integer num = addressMapper.updateAddressBySql(sql);
		
		return num;
	}

	public Integer updateAddressInfoByUserId(Address address) {
		
		Integer num = addressMapper.updateAddressInfoByUserId(address);
		
		return num;
		
	}

	public Integer updateAddressInfoById(Address address) {
		
		Integer num = addressMapper.updateAddressInfoById(address);
		
		return num;
		
	}

	public List<Address> selectAllAddress() {
		
		List<Address> addressList = addressMapper.selectAllAddress();
		
		return addressList;
	}

	public Integer saveUserAddress(Address address) {
		
		Integer num  = addressMapper.saveUserAddress(address);
		
		return num;
	}

	public List<Address> selectAddressByArea(String area) {
		
		List<Address> addressList = addressMapper.selectAddressByArea(area);
		
		return addressList;
	}

	public Integer updateAddressInfoByIdByInvitation(Address address) {
		
		Integer num  = addressMapper.updateAddressInfoByIdByInvitation(address);
		
		return num;
	}
	
}
