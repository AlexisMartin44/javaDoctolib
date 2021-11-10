package gui;

import java.io.IOException;
import java.util.function.Consumer;

import comStruct.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

public class LoginPageGui 
{

	// to test usr : root  psw : root

	private BorderPane root;
	private LoginPageController controllerRef;
	
	public LoginPageGui(@SuppressWarnings("exports") Consumer<User> cons_login, 
						Consumer<Boolean> cons_register) 
	{
		try 
		{
			FXMLLoader loader = new FXMLLoader( getClass().getResource( "LoginPage.fxml" ) );
			root = loader.<BorderPane>load();
			controllerRef = loader.getController();
			controllerRef.setLoginConsumer( cons_login );
			controllerRef.setRegisterConsumer( cons_register );
		} 
		catch (IOException e) 
		{
			System.err.println("ERR during the loginPage creation : " + e.getMessage() );
			e.printStackTrace();
		}
	}


	@SuppressWarnings("exports")
	public BorderPane getGui(){		return root;	}
	public Consumer<String> getLoginErrCons() {	return controllerRef.getLoginErrCons();	}


	public void setCons_getSalt(@SuppressWarnings("exports") Consumer<User> cons_getSalt) 
	{
		controllerRef.setSaltConsumer( cons_getSalt );
	}
	public void setSalt( String salt )
	{
		controllerRef.setSalt( salt );
	}

}
