package walkThroughATM;

import java.util.Date;

public class Transaction {
	private double amount;		//The amount of this transaction
	private Date timeStamp;		//The time and date of this transaction
	private String memo;		//A memo for this transaction
	private Account inAccount;		//The account in which the transaction was performed
	
	public Transaction(double amount, Account inAccount) {
		this.amount = amount;
		this.inAccount = inAccount;
		this.timeStamp = new Date();
		this.memo = "";
	}
	
	public Transaction(double amount, String memo, Account inAccount) {
		//call the two-arg constructor first
		this(amount, inAccount);
		
		//set the memo
		this.memo = memo;
	}
	
	
	//get the amount of the transaction
	public double getAmount() {
		return this.amount;
	}
	
	
	//Get a string summarising the transaction
	public String getSummaryLine() {
		if (this.amount >= 0) {
			return String.format("%s : $%0.2f : %s", this.timeStamp.toString(), 
					this.amount, this.memo);
		}else {
			return String.format("%s : $%(0.2f) : %s", this.timeStamp.toString(), 
					this.amount, this.memo); 
		}
	}
}
