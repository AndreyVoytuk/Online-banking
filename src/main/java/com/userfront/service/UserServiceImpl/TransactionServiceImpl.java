package com.userfront.service.UserServiceImpl;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.userfront.dao.PrimaryAccountDao;
import com.userfront.dao.PrimaryTransactionDao;
import com.userfront.dao.RecipientDao;
import com.userfront.dao.SavingsAccountDao;
import com.userfront.dao.SavingsTransactionDao;
import com.userfront.domain.PrimaryAccount;
import com.userfront.domain.PrimaryTransaction;
import com.userfront.domain.Recipient;
import com.userfront.domain.SavingsAccount;
import com.userfront.domain.SavingsTransaction;
import com.userfront.domain.User;
import com.userfront.service.TransactionService;
import com.userfront.service.UserService;

@Service
public class TransactionServiceImpl implements TransactionService {

	private static final String PRIMARY = "Primary";
	private static final String SAVINGS = "Savings";
	@Autowired
	UserService userService;
	@Autowired
	PrimaryTransactionDao primaryTransactionDao;
	@Autowired
	SavingsTransactionDao savingsTransactionDao;
	@Autowired
	PrimaryAccountDao primaryAccountDao;
	@Autowired
	SavingsAccountDao savingsAccountDao;
	@Autowired
	RecipientDao recipientDao;

	@Override
	public List<PrimaryTransaction> findPrimaryTransactionList(String username) {
		User user = userService.findByUsername(username);
		return user.getPrimaryAccount().getPrimaryTransactionList();
	}

	@Override
	public List<SavingsTransaction> findSavingsTransactionList(String username) {
		User user = userService.findByUsername(username);
		return user.getSavingAccount().getSavingsTransactionList();
	}

	@Override
	public void savePrimaryDepositTransaction(PrimaryTransaction primaryTransaction) {
		primaryTransactionDao.save(primaryTransaction);

	}

	@Override
	public void saveSavingsDepositTransaction(SavingsTransaction savingsTransaction) {
		savingsTransactionDao.save(savingsTransaction);

	}

	@Override
	public void savePrimaryWithdrawTransaction(PrimaryTransaction primaryTransaction) {
		primaryTransactionDao.save(primaryTransaction);

	}

	@Override
	public void saveSavingsWithdrawTransaction(SavingsTransaction savingsTransaction) {
		savingsTransactionDao.save(savingsTransaction);

	}

	@Override
	public void betweenAccountsTransfer(String transferFrom, String transferTo, String amount,
			PrimaryAccount primaryAccount, SavingsAccount savingsAccount) throws Exception {

		if(PRIMARY.equalsIgnoreCase(transferFrom) && SAVINGS.equalsIgnoreCase(transferTo)) {
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().add(new BigDecimal(amount)));
			primaryAccountDao.save(primaryAccount);
			savingsAccountDao.save(savingsAccount);

			PrimaryTransaction primaryTransaction = new PrimaryTransaction(new Date(), "Between account transfer from " + transferFrom
					+ " to " + transferTo , "Transfer", "Finished",
					Double.parseDouble(amount), primaryAccount.getAccountBalance(), primaryAccount);
			primaryTransactionDao.save(primaryTransaction);
		} else if (SAVINGS.equalsIgnoreCase(transferFrom) && PRIMARY.equalsIgnoreCase(transferTo)) {
			savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().add(new BigDecimal(amount)));
			primaryAccountDao.save(primaryAccount);
			savingsAccountDao.save(savingsAccount);

			SavingsTransaction savingsTransaction = new SavingsTransaction(new Date(), "Between account transfer from " + transferFrom
					+ " to " + transferTo , "Transfer", "Finished",
					Double.parseDouble(amount), savingsAccount.getAccountBalance(), savingsAccount);
			savingsTransactionDao.save(savingsTransaction);

		} else {
			throw new Exception("Invalid transfer");
		}

	}

	@Override
	public List<Recipient> findRecipientList(Principal principal) {
		 String name = principal.getName();

		 return recipientDao.findAll().stream()
		 	.filter(recipient -> name.equalsIgnoreCase(recipient.getUser().getUsername()))
		 	.collect(Collectors.toList());

	}

	@Override
	public Recipient saveRecipient(Recipient recipient) {
		return recipientDao.save(recipient);

	}

	@Override
	public Recipient findRecipientByName(String recipientName) {

		return recipientDao.findByName(recipientName);
	}

	@Override
	public void deleteRecipientByName(String recipientName) {
		recipientDao.deleteByName(recipientName);
	}

	@Override
	public void toSomeoneElseTranfer(Recipient recipient, String accountType, String amount,
			PrimaryAccount primaryAccount, SavingsAccount savingsAccount) {
		if("Primary".equalsIgnoreCase(accountType)) {
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			primaryAccountDao.save(primaryAccount);

			PrimaryTransaction primaryTransaction = new PrimaryTransaction(new Date(), "Transfer to recipient" + recipient.getName(), "Transfer", "Finished",
					Double.parseDouble(amount), primaryAccount.getAccountBalance(), primaryAccount);
			primaryTransactionDao.save(primaryTransaction);
		} else if ("Savings".equalsIgnoreCase(accountType)) {
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            savingsAccountDao.save(savingsAccount);

            SavingsTransaction savingsTransaction = new SavingsTransaction(new Date(), "Transfer to recipient "+recipient.getName(), "Transfer", "Finished",
            		Double.parseDouble(amount), savingsAccount.getAccountBalance(), savingsAccount);
            savingsTransactionDao.save(savingsTransaction);
        }

	}

}
