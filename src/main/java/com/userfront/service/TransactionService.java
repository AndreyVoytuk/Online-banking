package com.userfront.service;

import java.util.List;

import com.userfront.domain.PrimaryAccount;
import com.userfront.domain.PrimaryTransaction;
import com.userfront.domain.SavingsAccount;
import com.userfront.domain.SavingsTransaction;

public interface TransactionService {

	List<PrimaryTransaction> findPrimaryTransactionList(String name);
	List<SavingsTransaction> findSavingsTransactionList(String name);
	void savePrimaryDepositTransaction(PrimaryTransaction primaryTransaction);
	void saveSavingsDepositTransaction(SavingsTransaction savingsTransaction);

	void savePrimaryWithdrawTransaction(PrimaryTransaction primaryTransaction);
	void saveSavingsWithdrawTransaction(SavingsTransaction savingsTransaction);
	void betweenAccountsTransfer(String tranferFrom, String tranferTo, String amount, PrimaryAccount primaryAccount,
			SavingsAccount savingAccount) throws Exception;
}
