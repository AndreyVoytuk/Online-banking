package com.userfront.service.UserServiceImpl;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.userfront.dao.PrimaryAccountDao;
import com.userfront.dao.SavingsAccountDao;
import com.userfront.domain.PrimaryAccount;
import com.userfront.domain.SavingsAccount;
import com.userfront.service.AccountService;
import com.userfront.service.UserService;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

	private static int nextAccountNumber = 11223345;

	@Autowired
	private PrimaryAccountDao primaryAccountDao;

	@Autowired
	private SavingsAccountDao savingsAccountDao;

	@Autowired
	private UserService userService;


	@Override
	public PrimaryAccount createPrimaryAccount() {
		PrimaryAccount primaryAccount = new PrimaryAccount ();
		primaryAccount.setAccountBalance(new BigDecimal(0.0));
		primaryAccount.setAccountNumber(accountGen());

		primaryAccountDao.save(primaryAccount);
		return primaryAccountDao.findByAccountNumber(primaryAccount.getAccountNumber());
	}

	@Override
	public SavingsAccount createSavingsAccount() {
		SavingsAccount savingsAccount = new SavingsAccount ();
		savingsAccount.setAccountBalance(new BigDecimal(0.0));
		savingsAccount.setAccountNumber(accountGen());

		savingsAccountDao.save(savingsAccount);
		return savingsAccountDao.findByAccountNumber(savingsAccount.getAccountNumber());
	}

	private int accountGen() {
		return ++nextAccountNumber;
	}



}
