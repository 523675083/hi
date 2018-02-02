package com.zz.service;

import java.util.List;

import com.zz.entity.MUser;



public interface MUserServiceI {

	List<MUser> getAll();
	
	MUser selectByPrimaryKey(String id);
	
    int in11sert(MUser muser);
    
    int update(MUser muser);
    
    int delete(String id);
}
