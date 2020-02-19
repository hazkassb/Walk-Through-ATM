package walkThroughATM;

import java.util.ArrayList;
import java.util.Random;

public class Bank {
	private String name;			//The bank's name
	private ArrayList<User> users;		//List of users of the bank
	private ArrayList<Account> accounts;	//List of accounts with the bank
	
	
	//Create a new bank object with empty lists of users and accounts
	public Bank(String name) {
		this.name = name;
		this.users = new ArrayList<User>();
		this.accounts = new ArrayList<Account>();
	}
	
	//generate a new uuid for user
	public String getNewUserUUID() {
		//inits
		String uuid;
		Random rng = new Random();
		int len = 6;
		boolean nonUnique;
		
		do {
			//continue looping until we get a unique ID
			uuid = "";
			for(int i = 0; i < len; i++) {
				uuid += ((Integer)rng.nextInt(10)).toString();
			}
			//Check to make it is unique
			nonUnique = false;
			for (User u : this.users) {
				if(uuid.compareTo(u.getUUID()) == 0) {
					nonUnique = true;
					break;
				}
			}
			
		} while(nonUnique);
		
		return uuid;
		
	}
	
	public String getNewAccountUUID() {
		//inits
		String uuid;
		Random rng = new Random();
		int len = 10;
		boolean nonUnique;
		
		do {
			//continue looping until we get a unique ID
			uuid = "";
			for(int i = 0; i < len; i++) {
				uuid += ((Integer)rng.nextInt(10)).toString();
			}
			//Check to make it is unique
			nonUnique = false;
			for (Account a : this.accounts) {
				if(uuid.compareTo(a.getUUID()) == 0) {
					nonUnique = true;
					break;
				}
			}
			
		} while(nonUnique);
		
		return uuid;
	}
	
//	Add an account to the bank
	public void addAccount( Account anAccount) {
		this.accounts.add(anAccount);
	}
	
	public User addUser(String firstName, String lastName,  String pin) {
		//create a new user object and add it to our list
		User newUser = new User(firstName, lastName, pin, this);
		this.users.add(newUser);
		
		//create a savings account for the user and add to User and Bank accounts lists
		Account newAccount = new Account("Savings", newUser, this);
		
		//add to holder and bank lists
		newUser.addAccount(newAccount);
		this.accounts.add(newAccount);

		return newUser;
	}
	
	//Check whether user entered a valid login detail
	public User userLogin(String userID, String pin) {
		//search through list of users
		for (User u: this.users) {
			//check if user ID is correct
			if(u.getUUID().compareTo(userID) == 0 && u.validatePin(pin)){
				return u;
			}
		}
		
		//if we haven't found the user or have incorrect pin
		return null;
	}
	
	//Get the name of the bank
	public String getName() {
		return this.name;
	}
	
}
