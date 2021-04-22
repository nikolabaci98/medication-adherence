// this class will need to use the remote db jar file
abstract public class User {
	private String userID;
	private String username;
	private String password;
	private String fullname;
	public User() {
		this.userID = null;
		this.fullname = null;
		this.username = null;
		this.password = null;
	}
	public User(String uname, String pword, String fname) {
		this.username = uname;
		this.password = pword;
		this.fullname = fname;
		//setUserName();
		//setUserID(); --> to generate a unique code for the user's id
	}
	public String getUserID() {
		return userID;
	}	
	public String getUserName() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public String getFullName() {
		return fullname;
	}
	/*public void setUserID(String userID) {
		*this will require the connection to the database
		*basically we will first generate a 6 digit code composed of numbers
		*then we will search the database for this code to see if it exist
		*if so, generate a new code and try again, if not that code will be the userID
	}*/
	public void setUserName(String uname) {
		this.username = uname;
	}
	/*public boolean isUnique(String username) {
		*if username is found in db return false, otherwise return true
	}*/
	public void setPassword(String pword) {
		this.password = pword;
	}
	public void setFullName(String fname) {
		this.fullname = fname;
	}
}
