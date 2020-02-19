package walkThroughATM;

import java.util.ArrayList;

public class Account {
	private String name;		//account name
	private double balance;		//balance in the account
	private String  uuid;		//Account ID
	private User holder;			//account holder
	private ArrayList<Transaction> transactions;
	
	
	
	public Account(String name, User holder, Bank theBank) {
		super();
		//set the account name and user
		this.name = name;
		this.holder = holder;
		
		//get new account uuid
		this.uuid = theBank.getNewAccountUUID();
		this.transactions = new ArrayList<Transaction>();

	}
	
	
	//return the uuid
	public String getUUID() {
		return this.uuid;
	}
	
	//get summary line for the account
	public String getSummaryLine() {
		//get the account's balance
		double balance = this.getBalance();
		
		//format the summary line depending on whether the balance is negative
		if(balance >= 0) {
			return String.format("%s : $%.2f : %s", this.uuid, balance, this.name);
		}else {
			return String.format("%s : $(%.2f) : %s", this.uuid, balance, this.name);
		}
	}
	
	
	//return the balance in the account
	public double getBalance() {
		double balance = 0;
		for(Transaction t : this.transactions) {
			balance += t.getAmount();
		}
		return balance;
	}
	
	
	//print the transaction history of the account
	public void printTransHistory() {
		System.out.printf("\nTransaction History for Account %s\n", this.uuid);
		for(int t = this.transactions.size() - 1; t >= 0; t--) {
			System.out.printf(this.transactions.get(t).getSummaryLine());
		}
		
		System.out.println();
	}
	
	//Add a new transaction in the account
	public void addTransaction(double amount, String memo) {
		//create new transaction object and add it to our list
		Transaction newTrans = new Transaction(amount, memo, this);
		this.transactions.add(newTrans);
	}
	
}
