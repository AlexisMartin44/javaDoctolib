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

public class DataProfileWidgetGui 
{

	private BorderPane root;
	private DataProfileWidgetController controller;

	
	public DataProfileWidgetGui( User user, Consumer<Integer> cons_getUserById ) 
	{
		
		try 
		{
			FXMLLoader loader = new FXMLLoader( getClass().getResource("DataProfileWidget.fxml") );
			root = loader.<BorderPane>load();
			controller = loader.getController();
			controller.setUser( user );
			
			controller.setConsUser( cons_getUserById );
			
			
			
		}
		catch( IOException e )
		{
			System.err.println( "ERR in the creation of the data profile widget : " + e.getMessage() );
			e.printStackTrace();
		}
	}
	
	public void setAppointments(  @SuppressWarnings("exports") ArrayList<Event> events )
	{
		controller.setAppointments( events );
	}
	
	
	@SuppressWarnings("exports")
	public BorderPane getGui() { return root; }
	public void setCurrentUserStudied( User u)
	{
		controller.setCurrentUserStudied( u );
	}

}
