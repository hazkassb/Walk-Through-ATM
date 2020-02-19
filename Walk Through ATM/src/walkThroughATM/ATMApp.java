package walkThroughATM;
//https://www.youtube.com/watch?v=k0BofouWX-o --> 1:55`00
import java.util.Scanner;

public class ATMApp {
	public static void main(String[] args) {
		
		//init scanner
		Scanner scan = new Scanner(System.in);
		
		
		//init bank
		Bank theBank = new Bank("Bank of America");
		
		//Add a user, which also creates a savings account
		User aUser = theBank.addUser("John", "Doe", "1234");
		
		//Add a checking account for our user
		Account newAccount = new Account("Checking", aUser, theBank);
		aUser.addAccount(newAccount);
		theBank.addAccount(newAccount);
		
		User currentUser;
		while(true) {
			//Stay in the login prompt until successful login
			currentUser = ATMApp.mainMenuPrompt(theBank, scan);
			
			//Stay in the menu until user quits
			ATMApp.printUserMenu(currentUser, scan);
			
		}
		
	}
	
	public static User mainMenuPrompt(Bank theBank, Scanner scan) {
		//inits
		String userID;
		String pin;
		User authenticUser;
		
		//Prompt user for user ID/pin combo until a correct one is reached
		do {
			System.out.printf("\n\nWelcome to %s\n\n", theBank.getName());
			System.out.print("Enter user ID: ");
			userID = scan.nextLine();
			
			System.out.print("Enter pin: ");
			pin = scan.nextLine();
			
			//try to get the user object corresponding to the ID and pin combo
			authenticUser = theBank.userLogin(userID, pin);
			if(authenticUser == null) {
				System.out.println("Incorrect user ID/pin combination. " + "Please try again.");
			}
		} while(authenticUser == null); //continue looping until successful login
		
		return authenticUser;
	}
	
	public static void printUserMenu(User theUser, Scanner scan) {
		//print a summary of the user's account
		theUser.printAccountsSummary();
		
		//init
		int choice;
		
		//user menu
		do {
			System.out.printf("Welcome to %s, what would you like to do?\n", theUser.getFirstName());
			System.out.println(" 1) Show account transaction history");
			System.out.println(" 2) Withdrawal");
			System.out.println(" 3) Deposit");
			System.out.println(" 4) Transfer");
			System.out.println(" 5) Quit");
			System.out.println();
			
			System.out.print("Enter choice: ");
			choice = scan.nextInt();
			
			if(choice < 1 || choice > 5) {
				System.out.println("Invalid choice. Please choose 1-5");
			}
			
		} while(choice < 1 || choice > 5);
		
		//process the choice
		switch(choice) {
		case 1:
			ATMApp.showTransactionHistory(theUser, scan);
			break;
		case 2:
			ATMApp.withdrawFunds(theUser, scan);
			break;
		case 3:
			ATMApp.depositFunds(theUser, scan);
			break;
		case 4:
			ATMApp.transferFunds(theUser, scan);
			break;
		//no need for a default case because the while loop takes care of the unwanted cases already
		}
		
		//redisplay this menu unless the user wants to quit
		if(choice != 5) {		
			ATMApp.printUserMenu(theUser, scan); 		//recursion--> calling printUserMenu inside printUserMenu (itself) 
		}
		
	}
	
	
	//show the transaction history for an account
	public static void showTransactionHistory(User theUser, Scanner scan) {
		int theAcct;
		do {
			//get account whose transaction history to look at
			System.out.printf("Enter the number (1-%d) of the account \nwhose transactions you want to see", 
					theUser.numOfAccounts());
			theAcct = scan.nextInt() - 1;
			
			if (theAcct < 0 || theAcct >= theUser.numOfAccounts()) {
				System.out.println("Invalid account. Please try again.");
			}
		} while(theAcct < 0 || theAcct >= theUser.numOfAccounts());
		
		//print the transaction history
		theUser.printAcctTransactionsHistory(theAcct);
	}
	
	//Process transferring funds from one account to another
	public static void transferFunds(User theUser, Scanner scan) {
		//inits
		int fromAcct = 0;
		int toAcct = 0;
		double amount;
		double acctBal;
		
		//get the account to transfer from
		do {
			System.out.printf("Enter the number (1-%d) of the account\n" +
		"to transfer from: ", theUser.numOfAccounts());
			
			fromAcct = scan.nextInt() - 1;
			if(fromAcct < 0 || fromAcct >= theUser.numOfAccounts()) {
				System.out.println("Invalid account. Please try again.");
			}
		} while(fromAcct < 0 || fromAcct >= theUser.numOfAccounts());		
		acctBal = theUser.getAcctBalance(fromAcct);
		
		
		//Get the account to transfer to
		do {
			System.out.printf("Enter the number (1-%d) of the account\n" +
		"to transfer to: ", theUser.numOfAccounts());
			
			toAcct = scan.nextInt() - 1;
			if(toAcct < 0 || toAcct >= theUser.numOfAccounts()) {
				System.out.println("Invalid account. Please try again.");
			}
		} while(toAcct < 0 || toAcct >= theUser.numOfAccounts());	
		
		//Get the amount to transfer
		do {
			System.out.printf("Enter the amount to transfer (max $%.2f): $", acctBal);
			amount =scan.nextDouble();
			if(amount < 0) {
				System.out.println("Amount must be greater than zero.");
			} else if(amount > acctBal) {
				System.out.printf("Amount must not be greater than \n" + 
			"balance of $%.2f.\n", acctBal);
			}
		} while(amount < 0 || amount > acctBal);
		
		//Finally, do the transfer
		theUser.addAccountTransaction(fromAcct, -1*amount, String.format(
				"Transfer to account %s", theUser.getAcctUUID(toAcct)));
		theUser.addAccountTransaction(toAcct, amount, String.format(
				"Transfer to account %s", theUser.getAcctUUID(fromAcct)));
	}
	
	
	//Process a fund withdraw from an account
	public static void withdrawFunds(User theUser, Scanner scan) {
		//inits
		int fromAcct;
		double amount;
		double acctBal;
		String memo;
		
		//get the account to transfer from
		do {
			System.out.printf("Enter the number (1-%d) of the account\n" +
		"to withdraw from: ", theUser.numOfAccounts());
			
			fromAcct = scan.nextInt() - 1;
			if(fromAcct < 0 || fromAcct >= theUser.numOfAccounts()) {
				System.out.println("Invalid account. Please try again.");
			}
		} while(fromAcct < 0 || fromAcct >= theUser.numOfAccounts());		
		acctBal = theUser.getAcctBalance(fromAcct);
		
		//Get the amount to transfer
		do {
			System.out.printf("Enter the amount to transfer (max $%.2f): $", acctBal);
			amount =scan.nextDouble();
			if(amount < 0) {
				System.out.println("Amount must be greater than zero.");
			} else if(amount > acctBal) {
				System.out.printf("Amount must not be greater than $%.2f.\n" + 
			"balance of $.2f.\n", acctBal);
			}
		} while(amount < 0 || amount > acctBal);
		
		//gobble up the rest of previous input
		scan.nextLine();
		
		//get a memo
		System.out.print("Enter a memo: ");
		memo = scan.nextLine();
		
		//Finally, do the withdrawal
		theUser.addAccountTransaction(fromAcct, -1*amount, memo);
	}
	
	
	//Process a fund deposit to an account
	public static void depositFunds(User theUser, Scanner scan) {
		//inits
		int toAcct;
		double amount;
		double acctBal;
		String memo;
		
		//get the account to transfer from
		do {
			System.out.printf("Enter the number (1-%d) of the account\n" +
		"to deposit in: ", theUser.numOfAccounts());
			
			toAcct = scan.nextInt() - 1;
			if(toAcct < 0 || toAcct >= theUser.numOfAccounts()) {
				System.out.println("Invalid account. Please try again.");
			}
		} while(toAcct < 0 || toAcct >= theUser.numOfAccounts());		
		acctBal = theUser.getAcctBalance(toAcct);
		
		
		//gobble up the rest of previous input
		scan.nextLine();
		
		
		//Get the amount to transfer
		do {
			System.out.printf("Enter the amount to transfer (max $%.2f): $", acctBal);
			amount =scan.nextDouble();
			if(amount < 0) {
				System.out.println("Amount must be greater than zero.");
			} 
		} while(amount < 0);
		
		//gobble up the rest of previous input
		scan.nextLine();
		
		//get a memo
		System.out.print("Enter a memo: ");
		memo = scan.nextLine();
		
		//Finally, do the withdrawal
		theUser.addAccountTransaction(toAcct, amount, memo);
		
	}
	
}
