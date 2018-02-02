package com.zz.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.zz.dao.MUserMapper;
import com.zz.entity.MUser;


@Service("muserService")
public class MUserServiceImpl implements MUserServiceI{

	private MUserMapper muserMapper;
	@Autowired
	private TransactionTemplate productMysqlTransactionTemplate;
		


	public MUserMapper getMuserMapper() {
		return muserMapper;
	}

	@Autowired
	public void setMuserMapper(MUserMapper muserMapper) {
		this.muserMapper = muserMapper;
	}
	
	@Override
	public List<MUser> getAll() {
		
		return muserMapper.getAll();
	}

	@Override
	public int in11sert(final MUser muser) {
		Integer flag=productMysqlTransactionTemplate.execute(new TransactionCallback<Integer>() {

			@Override
			public Integer doInTransaction(TransactionStatus transactionStatus) {
				Integer inte=1;
				try {
						muserMapper.insert(muser);
				} catch (Exception e) {
					inte=0;
					transactionStatus.setRollbackOnly();
				}
				
				return inte;
			}
			
		});
		
		return flag;
	}

	@Override
	public int update(MUser muser) {
		
		return muserMapper.updateByPrimaryKey(muser);
	}

	@Override
	public int delete(String id) {
	
		return muserMapper.deleteByPrimaryKey(id);
	}

	@Override
	public MUser selectByPrimaryKey(String id) {
		
		return muserMapper.selectByPrimaryKey(id);
	}

}
