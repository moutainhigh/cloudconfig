package com.xkd.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xkd.model.Address;

public interface AddressMapper {

	Address selectAddressByUserId(@Param("userId") String userId);

	Integer updateAddressBySql(@Param("sql") String sql);

	Integer updateAddressInfoByUserId(Address address);

	Integer updateAddressInfoById(Address address);

	List<Address> selectAllAddress();

	Integer saveUserAddress(Address address);

	List<Address> selectAddressByArea(@Param("area") String area);

	Integer updateAddressInfoByIdByInvitation(Address address);
	
}
