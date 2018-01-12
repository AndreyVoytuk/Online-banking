package com.userfront.service;

import java.security.Principal;
import java.util.List;

import com.userfront.domain.PrimaryAccount;
import com.userfront.domain.PrimaryTransaction;
import com.userfront.domain.Recipient;
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
	List<Recipient> findRecipientList(Principal principal);
	Recipient saveRecipient(Recipient recipient);
	Recipient findRecipientByName(String recipientName);
	void deleteRecipientByName(String recipientName);
	void toSomeoneElseTranfer(Recipient recipient, String accountType, String amount, PrimaryAccount primaryAccount,
			SavingsAccount savingAccount);
}
