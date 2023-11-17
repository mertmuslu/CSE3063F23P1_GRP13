public abstract class Person {
	private String firstName;
	private String lastName;
	public Person(String firstName, String lastName) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	
	public String createEmailAddress(Person person) {
		return getFirstName() + getLastName() + "@marun.edu.tr" ;
	}
	
}