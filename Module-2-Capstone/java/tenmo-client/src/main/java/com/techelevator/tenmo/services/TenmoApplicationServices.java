package com.techelevator.tenmo.services;
import java.io.Console;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.view.ConsoleService;

/*******************************************************************************************************
 * This is where you code Application Services required by your solution
 * 
 * Remember:  theApp ==> ApplicationServices  ==>  Controller  ==>  DAO
********************************************************************************************************/

public class TenmoApplicationServices {
	
	  private final String BASE_URL = "http://localhost:8080";
	  private final RestTemplate restTemplate = new RestTemplate();
	  private final ConsoleService console = new ConsoleService(System.in, System.out);
	  Scanner userInput = new Scanner(System.in);
	  
	  public TenmoApplicationServices() {
		  
	  }
	  
	  //get account balance for logged in user
	  public double getBalance(String AUTH_TOKEN) {
		double newAccountBalance = 0.0;
		 try {
		    newAccountBalance = restTemplate.exchange(BASE_URL + "/accounts/balance", HttpMethod.GET, makeAuthEntity(AUTH_TOKEN), double.class).getBody();
		    } catch (RestClientResponseException ex) {
		      System.out.print(ex.getRawStatusCode() + " : " + ex.getStatusText());
		    } catch (ResourceAccessException ex) {
		      System.out.print(ex.getMessage());
		    }
		 return newAccountBalance;
	  }
	  //send from one account to another 
	  public void sendBucks(int account_from, String AUTH_TOKEN) {
		  userList(AUTH_TOKEN);
		  boolean retry = true;
		  Transfer newTransfer = new Transfer();
		  //do while -- ensure user can try again 
		  do {//this doesn't ensure that the user is on the user list at the moment 
			int account_to = console.getUserInputInteger("Enter ID of user you are sending to (0 to cancel)");
			if (account_from != account_to) {
				retry = false;
				newTransfer.setAccount_to(account_to);
			}
			else {
				retry = true;
				System.out.println("Sorry, you cannot send to yourself, try again.");	
			} 
		}while(retry);
		  
		  Account newAccount = new Account();
		  Long account_id = (long) account_from;
		  newAccount.setAccount_id(account_id);
		  newAccount.setBalance(getBalance(AUTH_TOKEN));
		  
		  //if user hasn't pressed 0 to cancel, input send amount 
		if (newTransfer.getAccount_to() != 0) {
			do {
				double amount = console.getUserInputInteger("Enter amount");

				if (newAccount.getBalance() > amount) {
					newTransfer.setAmount(amount);
					retry = false;
				} else {
					System.out.println("Insufficient Funds. Would you like to try again? (Y/N)");
					String yesNo = userInput.nextLine().trim();
					if (yesNo.equalsIgnoreCase("Y") || yesNo.equalsIgnoreCase("yes")) {
						retry = true;
					} else if (yesNo.equalsIgnoreCase("N") || yesNo.equalsIgnoreCase("no")) {
						newTransfer.setAmount(0);
						retry = false;
					}
				}
			} while (retry);

		}
		//making a new transfer with controller method
		if (newTransfer.getAmount() != 0) {
			try {
				restTemplate.postForObject(BASE_URL + "/transfers/send", makeTransferEntity(newTransfer, AUTH_TOKEN),
						Transfer.class);
				String name = userName(AUTH_TOKEN, newTransfer.getAccount_to());
				System.out.println("You sent $" + String.format("%.2f",newTransfer.getAmount()) + " to " + name);

			} catch (RestClientResponseException ex) {
				System.out.print(ex.getRawStatusCode() + " : " + ex.getStatusText());
			}
		} else {
			System.out.println("Thanks for using TEnmo. Have a great day!");
		}
		
	  }
	 
	  //lists transfers and allows user to get details
	public Transfer[] tranferList(String AUTH_TOKEN, int id) {

		Transfer[] transfers = null;
		Account newAccount = new Account();
		Long account_id = (long) id;
		newAccount.setAccount_id(account_id);
		newAccount.setBalance(getBalance(AUTH_TOKEN));
		boolean choice = true;

		try {
			transfers = restTemplate.exchange(BASE_URL + "/accounts/transfers/" + id, HttpMethod.GET,
					makeAuthEntity(AUTH_TOKEN), Transfer[].class).getBody();
		} catch (RestClientResponseException ex) {
			System.out.print(ex.getRawStatusCode() + " : " + ex.getStatusText());
		}

		System.out.println("-------------------------------------------");
		System.out.println("Transfer \n ID" + "       " + "From/To" + "       Amount");
		System.out.println("-------------------------------------------");
		for (Transfer transfer : transfers) {
			Long trans_id = transfer.getTransfer_id();
			int account_from = transfer.getAccount_from();
			int account_to = transfer.getAccount_to();
			double amount = transfer.getAmount();
			
			if (account_from != id) { //userName might be a little clunky but it works
				String userName = userName(AUTH_TOKEN, account_from);
				System.out.println(String.format("%02d", trans_id) + "       From: " + userName + "       $"
						+ String.format("%.2f", amount));
			} else if (account_to != id) {
				String userName = userName(AUTH_TOKEN, account_to);
				System.out.println(String.format("%02d", trans_id) + "       To: " + userName + "       $"
						+ String.format("%.2f", amount));
			}
		}
		//view transfer details
		int id_choice = console.getUserInputInteger("Please enter transfer ID to view details (0 to cancel)");
		if (id_choice == 0) {
			return transfers;
		} else if (id_choice != 0) {
			for (Transfer transfer : transfers) {
				if (transfer.getTransfer_id() == id_choice) {
					
					String from = userName(AUTH_TOKEN, transfer.getAccount_from());
					String to = userName (AUTH_TOKEN, transfer.getAccount_to());
					System.out.println("------------------------");
					System.out.println("Transfer Details");
					System.out.println("------------------------");
					System.out.println("ID: " + transfer.getTransfer_id());
					System.out.println("From: " + from);
					System.out.println("To: " + to);
					System.out.println("Type: " + transfer.getTransfer_type());
					System.out.println("Status: " + transfer.getTransfer_status());
					System.out.println("Amount: " + String.format("%.2f", transfer.getAmount()));
					return transfers;
				}
			}
			System.out.println("That transfer ID doesn't exist. Please try again.");
			return transfers;
		}

		return transfers;
	}
	  
	public User[] userList(String AUTH_TOKEN) {
		User[] users;
		users = restTemplate.exchange(BASE_URL + "users", HttpMethod.GET, makeAuthEntity(AUTH_TOKEN), User[].class)
				.getBody();
		System.out.println("-------------------------------------------");
		System.out.println("User \n ID" + "       " + "Name");
		System.out.println("-------------------------------------------");
		for (User user : users) {
			int id = user.getId();
			String username = user.getUsername();
			System.out.println(id + "   :     " + username);
		}
		System.out.println("                                           ");
		return users;
	}

	public Account getAccountbyId(String AUTH_TOKEN, int id) {
		Account theAccount = restTemplate
				.exchange(BASE_URL + "accounts/" + id, HttpMethod.GET, makeAuthEntity(AUTH_TOKEN), Account.class)
				.getBody();
		return theAccount;
	}
	
	//helper method to get usernames for transfers
	private String userName(String AUTH_TOKEN, int user_id) {
		User[] users;
		User newUser = new User();
		users = restTemplate.exchange(BASE_URL + "users", HttpMethod.GET, makeAuthEntity(AUTH_TOKEN), User[].class)
				.getBody();
		for (User user : users) {
			if (user_id == user.getId()) {
			newUser.setUsername(user.getUsername());
			}
		}
		return newUser.getUsername();
	}
	
	//helper method for authentication
	private HttpEntity makeAuthEntity(String AUTH_TOKEN) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(AUTH_TOKEN);
		HttpEntity entity = new HttpEntity<>(headers);
		return entity;
	}

	//helper method for sending a transfer
	private HttpEntity<Transfer> makeTransferEntity(Transfer newTransfer, String AUTH_TOKEN) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(AUTH_TOKEN);
		HttpEntity<Transfer> entity = new HttpEntity<>(newTransfer, headers);
		return entity;
	}

}
