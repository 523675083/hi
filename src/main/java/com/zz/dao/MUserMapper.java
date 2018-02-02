package com.zz.dao;

import java.util.List;

import com.zz.entity.MUser;

public interface MUserMapper {
    int deleteByPrimaryKey(String id);

    int insert(MUser record);

    int insertSelective(MUser record);

    MUser selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MUser record);

    int updateByPrimaryKey(MUser record);
    
    List<MUser> getAll();
}