package com.userfront.service.UserServiceImpl;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.userfront.dao.PrimaryAccountDao;
import com.userfront.dao.PrimaryTransactionDao;
import com.userfront.dao.SavingsAccountDao;
import com.userfront.dao.SavingsTransactionDao;
import com.userfront.domain.PrimaryAccount;
import com.userfront.domain.PrimaryTransaction;
import com.userfront.domain.SavingsAccount;
import com.userfront.domain.SavingsTransaction;
import com.userfront.domain.User;
import com.userfront.service.AccountService;
import com.userfront.service.UserService;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

	private static final String SAVINGS = "Savings";

	private static final String PRIMARY = "Primary";

	private static int nextAccountNumber = 11223345;

	@Autowired
	private PrimaryAccountDao primaryAccountDao;

	@Autowired
	private SavingsAccountDao savingsAccountDao;

	@Autowired
	private UserService userService;

	@Autowired
	TransactionServiceImpl transactionServiceImpl;

	@Override
	public PrimaryAccount createPrimaryAccount() {
		PrimaryAccount primaryAccount = new PrimaryAccount();
		primaryAccount.setAccountBalance(new BigDecimal(0.0));
		primaryAccount.setAccountNumber(accountGen());

		primaryAccountDao.save(primaryAccount);
		return primaryAccountDao.findByAccountNumber(primaryAccount.getAccountNumber());
	}

	@Override
	public SavingsAccount createSavingsAccount() {
		SavingsAccount savingsAccount = new SavingsAccount();
		savingsAccount.setAccountBalance(new BigDecimal(0.0));
		savingsAccount.setAccountNumber(accountGen());

		savingsAccountDao.save(savingsAccount);
		return savingsAccountDao.findByAccountNumber(savingsAccount.getAccountNumber());
	}

	private int accountGen() {
		return ++nextAccountNumber;
	}

	@Override
	public void deposit(String accountType, double amount, Principal principal) {
		User user = userService.findByUsername(principal.getName());

		if (PRIMARY.equalsIgnoreCase(accountType)) {
			PrimaryAccount primaryAccount = user.getPrimaryAccount();
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().add(new BigDecimal(amount)));
			primaryAccountDao.save(primaryAccount);

			PrimaryTransaction primaryTransaction = new PrimaryTransaction(new Date(), "Deposit to Primary account",
					"Account", "Finished", amount, primaryAccount.getAccountBalance(), primaryAccount);
			transactionServiceImpl.savePrimaryDepositTransaction(primaryTransaction);

		} else if (SAVINGS.equalsIgnoreCase(accountType)) {
			SavingsAccount savingsAccount = user.getSavingAccount();
			savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().add(new BigDecimal(amount)));
			savingsAccountDao.save(savingsAccount);

			SavingsTransaction savingsTransaction = new SavingsTransaction(new Date(), "Deposit to Savings account",
					"Account", "Finished", amount, savingsAccount.getAccountBalance(), savingsAccount);
			transactionServiceImpl.saveSavingsDepositTransaction(savingsTransaction);
		}

	}

	@Override
	public void withdraw(String accountType, double amount, Principal principal) {
		User user = userService.findByUsername(principal.getName());

		if (PRIMARY.equalsIgnoreCase(accountType)) {
			PrimaryAccount primaryAccount = user.getPrimaryAccount();
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			primaryAccountDao.save(primaryAccount);

			PrimaryTransaction primaryTransaction = new PrimaryTransaction(new Date(), "Withdraw from Primary account",
					"Account", "Finished", amount, primaryAccount.getAccountBalance(), primaryAccount);
		} else if (SAVINGS.equalsIgnoreCase(accountType)) {
			SavingsAccount savingsAccount = user.getSavingAccount();
			savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			savingsAccountDao.save(savingsAccount);

			SavingsTransaction savingsTransaction = new SavingsTransaction(new Date(), "Withdraw from Savings account",
					"Account", "Finished", amount, savingsAccount.getAccountBalance(), savingsAccount);
		}


	}

}
