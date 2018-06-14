package com.xkd.mapper;

import com.xkd.model.Address;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
