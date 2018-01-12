package com.userfront.controller;

import java.security.Principal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.userfront.domain.PrimaryAccount;
import com.userfront.domain.Recipient;
import com.userfront.domain.SavingsAccount;
import com.userfront.domain.User;
import com.userfront.service.TransactionService;
import com.userfront.service.UserService;

@Controller
@RequestMapping("/transfer")
public class TransferController {

	@Autowired
	private TransactionService transactionService;
	@Autowired
	private UserService userService;

	@GetMapping("/betweenAccounts")
	public String betweenAccounts(Model model) {
		model.addAttribute("transferFrom", "");
		model.addAttribute("transferTo", "");
		model.addAttribute("amount", "");

		return "betweenAccounts";
	}

	@PostMapping("/betweenAccounts")
	public String betweenAccountsPost(
			@ModelAttribute("transferFrom") String transferFrom,
			@ModelAttribute("transferTo") String transferTo,
			@ModelAttribute("amount") String amount,
			Principal principal) throws Exception {
		User user = userService.findByUsername(principal.getName());
		PrimaryAccount primaryAccount = user.getPrimaryAccount();
		SavingsAccount savingAccount = user.getSavingAccount();
		transactionService.betweenAccountsTransfer(transferFrom, transferTo, amount, primaryAccount, savingAccount);

		return "redirect:/userFront";
	}

	@GetMapping("/recipient")
	public String recipient(Model model, Principal principal) {
		List<Recipient> recipientList = transactionService.findRecipientList(principal);

		model.addAttribute("recipient", new Recipient());
		model.addAttribute("recipientList", recipientList);

		return "recipient";
	}

	@PostMapping("/recipient/save")
	public String recipientPost(@ModelAttribute("recipient") Recipient recipient, Principal principal) {
		User user = userService.findByUsername(principal.getName());

		recipient.setUser(user);
		transactionService.saveRecipient(recipient);

		return "redirect:/transfer/recipient";
	}

	@GetMapping(value="/recipient/edit")
	public String recipientEdit(@RequestParam(value="recipientName") String recipientName, Model model, Principal principal) {
		Recipient recipient = transactionService.findRecipientByName(recipientName);
		List<Recipient> recipientList = transactionService.findRecipientList(principal);

		model.addAttribute("recipient", recipient);
		model.addAttribute("recipientList", recipientList);

		return "recipient";
	}

	@GetMapping(value="/recipient/delete")
	@Transactional
	public String recipientDelete(@RequestParam(value="recipientName") String recipientName, Model model, Principal principal) {
		transactionService.deleteRecipientByName(recipientName);
		List<Recipient> recipientList = transactionService.findRecipientList(principal);

		Recipient recipient = new Recipient();
		model.addAttribute("recipient", recipient);
		model.addAttribute("recipientList", recipientList);

		return "recipient";
	}

	@GetMapping("/toSomeoneElse")
	public String toSomeoneElse(Model model, Principal principal) {
		List<Recipient> recipientList = transactionService.findRecipientList(principal);

		model.addAttribute("recipientList", recipientList);
		model.addAttribute("accounttype", "");

		return "toSomeoneElse";
	}
	@PostMapping("/toSomeoneElse")
	public String toSomeoneElse(@ModelAttribute("recipientName") String recipientName, @ModelAttribute("accountType") String accountType,
			@ModelAttribute("amount") String amount, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		Recipient recipient = transactionService.findRecipientByName(recipientName);
		transactionService.toSomeoneElseTranfer(recipient, accountType, amount, user.getPrimaryAccount(), user.getSavingAccount());

		return "redirect:/userFront";
	}
}
