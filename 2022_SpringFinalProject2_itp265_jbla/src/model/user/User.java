package model.user;

public abstract class User {

	// instance variables
	protected String firstName;
	protected String lastName;
	private String userName;
	private String password;
	private UserType userType;
	
	// constructors
	public User(String firstName, String lastName, String userName, 
			String password, UserType userType) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.password = password;
		this.userType = userType;
	}

	// accessors and mutators
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
	
	
}
