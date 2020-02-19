package walkThroughATM;

import java.util.ArrayList;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {
	private String firstName;		//First name of the user
	private String lastName;		//Last name of the user
	private String uuid;			//ID number of the user
	private byte pinHash[];			//MD5 hash of the user's pin number
	private ArrayList<Account> accounts;		//The list of accounts for this user
	
	
	
	public User(String firstName, String lastName, String pin, Bank theBank) {
		super();
		
		//set user's name
		this.firstName = firstName;
		this.lastName = lastName;
		this.uuid = pin;
		
		//store the pin's MD5 hash, rather than the original value, for security reasons
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			this.pinHash = md.digest(pin.getBytes());
		} catch (NoSuchAlgorithmException e) {
			System.err.println("error, caught NoSuchAlgorithmException");
			e.printStackTrace();
			System.exit(1);
		}
		
		
		//get a new universal ID for the User
		this.uuid = theBank.getNewUserUUID();
		
		
		//create empty list of account
		this.accounts = new ArrayList<Account>();
		
		//print log message
		System.out.printf("New USER %s, %s with ID %s created.\n", lastName, firstName, this.uuid);
		
		
	}
	
	public void addAccount(Account anAccount) {
		this.accounts.add(anAccount);
		
	}
	
	//return the User's UUID
	public String getUUID() {
		return this.uuid;
	}
	
	//Validate pin enter by user
	public boolean validatePin(String apin) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			return MessageDigest.isEqual(md.digest(apin.getBytes()), this.pinHash);
		} catch (NoSuchAlgorithmException e) {
			System.err.println("error, caught NoSuchAlgorithmException");
			e.printStackTrace();
			System.exit(1);
		}
		return false;
	}
	
	
	//return the user's first name
	public String getFirstName() {
		return this.firstName;
	}
	
	//Prints summaries for the accounts of this user
	public void printAccountsSummary() {
		System.out.printf("\n\n%s's accounts summary\n", this.firstName);
		for(int a = 0; a < this.accounts.size(); a++) {
			System.out.printf("%d) %s\n", a+1, this.accounts.get(a).getSummaryLine());
		}
		
		System.out.println();
	}
	
	//get the number of accounts for the user
	public int numOfAccounts() {
		return this.accounts.size();
	}
	
	//Print transaction history for a particular account
	public void printAcctTransactionsHistory(int acctIndex) {
		this.accounts.get(acctIndex).printTransHistory();
	}
	
	//Get the balance of a particular account
	public double getAcctBalance(int acctIndex) {
		return this.accounts.get(acctIndex).getBalance();
	}
	
	//Get the UUID of a particular account
	public String getAcctUUID(int acctIndex) {
		return this.accounts.get(acctIndex).getUUID();
	}
	
	
	//
	public void addAccountTransaction(int acctIndex, double amount, String memo) {
		this.accounts.get(acctIndex).addTransaction(amount, memo);
	}

}
