package gui;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.Consumer;

import comStruct.DateTime;
import comStruct.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import comStruct.User;

public class EditProfileWidgetGui 
{

	private BorderPane root;
	private EditProfileWidgetController controller;
	
	public EditProfileWidgetGui( @SuppressWarnings("exports") User currentUser, 
								@SuppressWarnings("exports") Consumer<User> cons_updateProfile,
								boolean canEditProfile ) 
	{
		
		try 
		{
			FXMLLoader loader = new FXMLLoader( getClass().getResource("EditProfileWidget.fxml") );
			root = loader.<BorderPane>load();
			controller = loader.getController();
			
			if( canEditProfile )
				controller.setUpdateProfileConsumer( cons_updateProfile );
			controller.setCurrentUser( currentUser );
			controller.setLabels( canEditProfile );
			
			
		}
		catch( IOException e )
		{
			System.err.println( "ERR in the creation of the edit profile widget : " + e.getMessage() );
			e.printStackTrace();
		}
	}
	
	
	@SuppressWarnings("exports")
	public BorderPane getGui() { return root; }
	
	public void setCons_getSalt(@SuppressWarnings("exports") Consumer<User> cons_getSalt) 
	{
		controller.setSaltConsumer( cons_getSalt );
	}
	
	public void setSalt( String salt )
	{
		controller.setSalt( salt );
	}

}
