package com.zz.controller;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import com.zz.entity.MUser;
import com.zz.service.MUserServiceI;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring*.xml"})
@WebAppConfiguration
@TransactionConfiguration(defaultRollback = false)
public class TestJ extends AbstractTransactionalJUnit4SpringContextTests{
	@Autowired
	private MUserServiceI muserService;
	
	public MUserServiceI getMuserService() {
		return muserService;
	}

	public void setMuserService(MUserServiceI muserService) {
		this.muserService = muserService;
	}

	@Test
	public void test(){
		List <MUser> list = muserService.getAll();
		System.out.println(list);
	}

}
