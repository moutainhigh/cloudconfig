package com.wjh.menuoperate.mapper;

import com.wjh.menuoperateservicemodel.model.MenuPo;
import com.wjh.menuoperateservicemodel.model.MenuVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuMapper {

    int insert(MenuPo menuPo);

    int update(MenuPo menuPo);


    List<MenuVo> search(@Param("menuName") String menuName, @Param("start") int start, @Param("pageSize") int pageSize);

    int searchCount(@Param("menuName") String menuName);

    int delete(@Param("id") Long id);


}
