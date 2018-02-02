package com.zz.dao;

import com.zz.entity.YkProductPlace;

public interface YkProductPlaceMapper {
    int deleteByPrimaryKey(Long id);

    int insert(YkProductPlace record);

    int insertSelective(YkProductPlace record);

    YkProductPlace selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(YkProductPlace record);

    int updateByPrimaryKey(YkProductPlace record);
}