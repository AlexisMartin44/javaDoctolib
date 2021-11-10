package gui;

import java.io.IOException;
import java.util.function.Consumer;

import comStruct.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

public class RegisterPageGui 
{

	// to test usr : uly@gmail.com  psw : pass

	private BorderPane root;
	private RegisterPageController controllerRef;
	
	public RegisterPageGui(@SuppressWarnings("exports") Consumer<User> new_registerConsumer, Consumer<Boolean> cons_backLog) 
	{
		try 
		{
			FXMLLoader loader = new FXMLLoader( getClass().getResource( "RegisterPage.fxml" ) );
			root = loader.<BorderPane>load();
//    	root.setPrefSize( ENV_VARS.GUI_WIDTH, ENV_VARS.GUI_HEIGHT );
			controllerRef = loader.getController();
			controllerRef.setRegisterConsumer( new_registerConsumer );
			controllerRef.setBackLogConsumer( cons_backLog );
		} 
		catch (IOException e) 
		{
			System.err.println("ERR during the registerPage creation : " + e.getMessage() );
			e.printStackTrace();
		}
	}
	
	
	@SuppressWarnings("exports")
	public BorderPane getGui(){		return root;	}


	public Consumer<String> getRegisterErrCons() {	return controllerRef.getRegisterErrCons();	} 

}
