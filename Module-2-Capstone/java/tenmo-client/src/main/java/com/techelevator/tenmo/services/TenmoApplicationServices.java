package com.techelevator.tenmo.services;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

/*******************************************************************************************************
 * This is where you code Application Services required by your solution
 * 
 * Remember:  theApp ==> ApplicationServices  ==>  Controller  ==>  DAO
********************************************************************************************************/

public class TenmoApplicationServices {
	
	  private final String BASE_URL = "http://localhost:8080";
	  private final RestTemplate restTemplate = new RestTemplate();
	  
	  public TenmoApplicationServices() {
	  }
	  
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
	  
	  public void sendBucks(int account_from, int account_to, double amount, String AUTH_TOKEN) {
		  Transfer newTransfer = new Transfer();
			newTransfer.setAccount_to(account_to);
			newTransfer.setAmount(amount);
			try {
			restTemplate.postForObject(BASE_URL + "/transfers/send", makeTransferEntity(newTransfer, AUTH_TOKEN), Transfer.class);
			}catch (RestClientResponseException ex) {
			System.out.print(ex.getRawStatusCode() + " : " + ex.getStatusText());
			}
	  }
	  
	  public Map<Integer,String> userList(String AUTH_TOKEN){
		Map<Integer, String> userList = new HashMap<Integer, String>();
		User[]users;
		users = restTemplate.exchange(BASE_URL + "/users", HttpMethod.GET, makeAuthEntity(AUTH_TOKEN), User[].class).getBody();
		for (User user : users) {
			int userId = user.getId();
			String username = user.getUsername();
			userList.put(userId, username);
		}
		return userList;
	  }
		 
	  private HttpEntity makeAuthEntity(String AUTH_TOKEN) {
	        HttpHeaders headers = new HttpHeaders();
	        headers.setBearerAuth(AUTH_TOKEN);
	        HttpEntity entity = new HttpEntity<>(headers);
	        return entity;
	    }
	  
	  private HttpEntity<Transfer> makeTransferEntity(Transfer newTransfer, String AUTH_TOKEN) {
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        headers.setBearerAuth(AUTH_TOKEN);
	        HttpEntity<Transfer> entity = new HttpEntity<>(newTransfer, headers);
	        return entity;
	    }

	

}
