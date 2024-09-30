package com.banking.accounts.mapper;

import com.banking.accounts.dto.AccountsDto;
import com.banking.accounts.entity.Accounts;


public class AccountMapper
{
	public static AccountsDto mapToAccountsDto(final Accounts source, final AccountsDto target) {
		target.setAccountNumber(source.getAccountNumber());
		target.setAccountType(source.getAccountType());
		target.setBranchAddress(source.getBranchAddress());
		return target;
	}

	public static Accounts mapToAccounts(final AccountsDto source, final Accounts target)
	{
		target.setAccountNumber(source.getAccountNumber());
		target.setAccountType(source.getAccountType());
		target.setBranchAddress(source.getBranchAddress());
		return target;
	}
}
