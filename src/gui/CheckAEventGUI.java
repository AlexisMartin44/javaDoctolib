package gui;

import java.io.IOException;
import java.util.function.Consumer;

import comStruct.Event;
import comStruct.Location;
import comStruct.Meeting;
import comStruct.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

public class CheckAEventGUI 
{
	private BorderPane root;
	private CheckAEventController controller;
	
	public CheckAEventGUI( @SuppressWarnings("exports") Event eventSelected, 
							@SuppressWarnings("exports") Location location, 
							@SuppressWarnings("exports") User userPat,
							@SuppressWarnings("exports") User userDoc, 
							@SuppressWarnings("exports") User currentUser, 
							@SuppressWarnings("exports") Consumer<User> cons_openProfil) 
	{
		try
		{
			FXMLLoader loader = new FXMLLoader( getClass().getResource("CheckAEventWidget.fxml") );
			root = loader.<BorderPane>load();
			controller = loader.getController();
			controller.setCurrentUser( currentUser );
			controller.setLocation( location );
			controller.setEvent( eventSelected );
			controller.setCons_OpenProfil( cons_openProfil );
			if( userPat != null )
				controller.setUserToStudy( userPat, userDoc );
		}
		catch( IOException e )
		{
			System.err.println("ERR in meeting desc creation : " + e.getMessage() );
			e.printStackTrace();
		}
	}

	@SuppressWarnings("exports")
	public BorderPane getGui() {	return root; 	}
	public void setCons_cancelEvent( @SuppressWarnings("exports") Consumer<Event> cons_cancelEvent,
									@SuppressWarnings("exports") Consumer<Event> cons_editEvent)
	{
		controller.setCons_cancelEvent( cons_cancelEvent, cons_editEvent );
	}

	public void setCons_acceptEvent( @SuppressWarnings("exports") Consumer<Event> cons_acceptEvent) 
	{
		if( cons_acceptEvent != null )
			controller.setConsAccept_Event( cons_acceptEvent );
	}

}
