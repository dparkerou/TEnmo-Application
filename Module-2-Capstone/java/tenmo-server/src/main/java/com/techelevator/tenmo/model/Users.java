package com.techelevator.tenmo.model;

public class Users {
	
		private Long user_id;
		private String username;
		private String password_hash;
		
		public Long getUser_id() {
			return user_id;
		}
		public void setUser_id(Long user_id) {
			this.user_id = user_id;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword_hash() {
			return password_hash;
		}
		public void setPassword_hash(String password_hash) {
			this.password_hash = password_hash;
		}
		
		@Override
		public String toString() {
			return "Users: " + user_id + " Username: " + username + " Password: " + password_hash;
		}
		
		
		

}
