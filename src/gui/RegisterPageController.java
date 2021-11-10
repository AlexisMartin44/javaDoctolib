package gui;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import comStruct.User;
import generalPurpose.GENDERS;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterPageController implements Initializable
{
		
	private @FXML TextField 			txtField_email;
	private @FXML TextField 			txtField_names;
	private @FXML TextField 			txtField_firstName;
	private @FXML PasswordField 		pswField_password;
	private @FXML PasswordField 		pswField_password_Verification;
	private @FXML ChoiceBox<GENDERS>	choiceBoxGender;
	private @FXML Label					lblErr;
	private @FXML Button				btnReg;
	
	User userCreated = new User();
	
	private @FXML void isEmployee()
	{
		userCreated.setDoctor( !userCreated.isDoctor() );
	}
	
	private @FXML void isDisable()
	{
		userCreated.setDisable( !userCreated.isDisable() );
	}
	
	private @FXML void register()
	{	
		String email = txtField_email.getText();
		String name = txtField_names.getText();
		String first_name = txtField_firstName.getText();
		String password = pswField_password.getText();
		String password_verif = pswField_password_Verification.getText();
		GENDERS gender = choiceBoxGender.getValue();
		
		if( !password.equals(password_verif) )
		{
			errorDisplay(" passwords are not the same ");
			return;
		}
		
		if( email == "" || name == "" || first_name == "" || password == "" || password_verif == "")
		{
			errorDisplay("empty input");
			return;
		}
		
		else
		{
			userCreated.setEmail(email);
			userCreated.setName(name);
			userCreated.setGender(gender);
			userCreated.setFirstName(first_name);
			
			// code from https://www.baeldung.com/java-md5
			MessageDigest md;
			try 
			{
				md = MessageDigest.getInstance("MD5");

				//salt
				SecureRandom random = new SecureRandom();
				byte[] salt = new byte[16];
				random.nextBytes( salt );
				String saltStr = Main.bitStringConverter( salt );
				userCreated.setSalt( saltStr );
				md.update( saltStr.getBytes() );
				
				md.update( password.getBytes() );
				
				byte[] digest = md.digest();
				String hash = Main.bitStringConverter( digest );
				userCreated.setPassword( hash );
				
				new_register.accept( userCreated );
			} 
			catch ( NoSuchAlgorithmException e ) 
			{
				System.err.println("ERR in the creation of the hash of the password");
				e.printStackTrace();
			}
		}
	}
	
	
	private @FXML void backToLog() {
		cons_backLog.accept( true );
	}
	
	private @FXML void hover()
	{
		btnReg.setScaleX( 1.2 );
		btnReg.setScaleY( 1.2 );
	}
	private @FXML void stopHover()
	{
		btnReg.setScaleX( 1 );
		btnReg.setScaleY( 1 );
	}
	
	
	private Consumer< User > new_register;
	private Consumer< Boolean > cons_backLog;
	
	public void setRegisterConsumer( @SuppressWarnings("exports") Consumer<User> new_register ){ this.new_register = new_register; }
	public void setBackLogConsumer(  Consumer <Boolean> cons_backLog) {this.cons_backLog = cons_backLog; }
	
	public RegisterPageController() {}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		choiceBoxGender.getItems().setAll( GENDERS.values() );
		choiceBoxGender	.getSelectionModel()
						.selectedItemProperty()
						.addListener( 
						new ChangeListener<GENDERS>() {
							@Override
							public void changed(ObservableValue<? extends GENDERS> ov, GENDERS value, GENDERS newValue) {
								if( newValue != null )
									userCreated.setGender( newValue  );
							}
						});
	}

	
	private void errorDisplay( String err )
	{
		lblErr.setVisible( true ); 
		lblErr.setText( err );
		ShakeTransition st = new ShakeTransition( lblErr , t->lblErr.setVisible( false ) );
		st.playFromStart();
	}
	
	public Consumer<String> getRegisterErrCons() 
	{
		return str ->{ 
			errorDisplay( str );
		};
	}
	
}
