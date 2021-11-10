package gui;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.function.Consumer;

import application.Main;
import comStruct.User;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginPageController 
{
	
	private @FXML TextField 		txtField_email;
	private @FXML PasswordField 	pswField_password;
	private @FXML Label 			lblLoginError;
	
	private @FXML Button			btnLogin;
	private @FXML Button			btnReg;
	
	private @FXML void login()
	{
		String email = txtField_email.getText();
		String password = pswField_password.getText();
		
		if( email == "" || password == "" )
		{
			errorDisplay( "empty password or login" );
			return;
		}
		
		User user = new User();
		user.setEmail( email );
		
		// code from https://www.baeldung.com/java-md5
		try 
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			
			getSaltConsumer.accept( user );
			if( salt != null && password != null)
			{
				md.update( salt.getBytes() );
	
				
				md.update( password.getBytes() );
				
				
				byte[] digest = md.digest();
				String hash = Main.bitStringConverter( digest );
				user.setPassword( hash );
				
				loginConsumer.accept( user );
			}
		} 
		catch ( NoSuchAlgorithmException e ) 
		{
			System.err.println("ERR in the creation of the hash of the password");
			e.printStackTrace();
		}
	}
	
	private @FXML void register()
	{
		registerConsumer.accept( true );
	}
	
	private @FXML void log_hover()
	{
		btnLogin.setScaleX( 1.2 );
		btnLogin.setScaleY( 1.2 );
	}
	private @FXML void log_stopHover()
	{
		btnLogin.setScaleX( 1 );
		btnLogin.setScaleY( 1 );
	}
	
	private @FXML void reg_hover()
	{
		btnReg.setScaleX( 1.2 );
		btnReg.setScaleY( 1.2 );
	}
	private @FXML void reg_stopHover()
	{
		btnReg.setScaleX( 1 );
		btnReg.setScaleY( 1 );
	}
	
	
	private Consumer< User > 	loginConsumer;
	private Consumer< User > 	getSaltConsumer;
	private Consumer< Boolean > registerConsumer;
	private String salt;
	public LoginPageController() {}
	
	
	public void setSalt( String salt ) { this.salt = salt; }
	public void setLoginConsumer( @SuppressWarnings("exports") Consumer<User> cons_login ){ this.loginConsumer = cons_login; }
	public void setRegisterConsumer( Consumer< Boolean > registerConsumer ){ this.registerConsumer = registerConsumer; }

	public Consumer<String> getLoginErrCons() 
	{
		return str -> {
			errorDisplay( str );
		};
	}

	private void errorDisplay( String err )
	{
		lblLoginError.setVisible( true ); 
		lblLoginError.setText( err );
		ShakeTransition st = new ShakeTransition( lblLoginError , t->lblLoginError.setVisible( false ) );
		st.playFromStart();
	}
	public void setSaltConsumer(@SuppressWarnings("exports") Consumer<User> cons_getSalt) 
	{
		getSaltConsumer = cons_getSalt;
	}
}
