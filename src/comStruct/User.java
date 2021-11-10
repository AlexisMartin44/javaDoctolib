package comStruct;

import java.text.Normalizer;

import generalPurpose.GENDERS;

public class User 
{
	private int id;
	private int age;
	private String 	name;
	private String 	firstName;
	private String	email;
	private String 	homeAdress;
	private String qualification;
	private String specialisation;
	private String 	password;
	private String  salt;
	private GENDERS gender;
	private boolean isDoctor = false;
	private boolean isHandicaped = false;
	
	
	
	
	public User() {}
	public User( 	String name, 
					String firstName, 
					String password, 
					String email,
					String adress,
					String qualification,
					String specialisation,
					int age,
					GENDERS gender, 
					boolean isDoctor, 
					boolean isHandicaped ) 
	{
		this.name = name; 
		this.firstName = firstName;
		this.password = password;
		this.email = email;
		this.homeAdress = adress;
		this.qualification = qualification;
		this.specialisation = specialisation;
		this.age = age;
		this.gender = gender;
		this.isDoctor = isDoctor;
		this.isHandicaped = isHandicaped;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getAdress() {
		return homeAdress;
	}
	public void setAdress( String adress ) {
		this.homeAdress = adress;
	}
	public String getQualification(){
		return qualification;
	}
	public void setQualification( String qualification) {
		this.qualification = qualification;
	}
	public String getSpecialisation(){
		return specialisation;
	}
	public void setSpecialisation( String specialisation) {
		this.specialisation = specialisation;
	}
	public int getAge(){
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public GENDERS getGender() {
		return gender;
	}
	public void setGender(GENDERS gender) {
		this.gender = gender;
	}
	public boolean isDoctor() {
		return isDoctor;
	}
	public void setDoctor(boolean isDoctor) {
		this.isDoctor = isDoctor;
	}
	public boolean isDisable() {
		return isHandicaped;
	}
	public void setDisable(boolean isHandicaped) {
		this.isHandicaped = isHandicaped;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setId(int id) 
	{
		this.id = id;
	}
	public int getId() 
	{
		return id;
	}
	public String getSalt() 
	{
		return salt;
	}
	public void setSalt(String salt) 
	{
		this.salt = salt;
	}
	public String getHomeAdress() {
		return homeAdress;
	}
	public void setHomeAdress(String homeAdress) {
		this.homeAdress = homeAdress;
	}
	
	
	
	public String toString()
	{
		return name + " " + firstName + (( specialisation == null)? "" : (" " +  specialisation) );
	}
	
	public String getHomeAdressMapsUrl()
	{
		String placeNameSafe = getHomeAdress();
		
		//get rid of all accents and other crazy stuff
		placeNameSafe = Normalizer.normalize( placeNameSafe , Normalizer.Form.NFKC );
		placeNameSafe = placeNameSafe.replace(' ', '+');
		
		return placeNameSafe;
	}
	
}
