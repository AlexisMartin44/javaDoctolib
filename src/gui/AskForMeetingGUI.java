package gui;


import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;

import comStruct.Event;
import comStruct.Location;
import comStruct.Meeting;
import comStruct.Unavailaible;
import comStruct.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

public class AskForMeetingGUI 
{

	private BorderPane root;
	private AskForMeetingWidgetController controller;
	
	public AskForMeetingGUI(int selectedDay, 
							int selectedMonth, 
							int selectedYear, 
							@SuppressWarnings("exports") ArrayList<Location> locations,
							@SuppressWarnings("exports") User currentUser ) 
	{
		try
		{
			FXMLLoader loader = new FXMLLoader( getClass().getResource("AskForMeetingWidget.fxml") );
			root = loader.<BorderPane>load();
			controller = loader.getController();
			controller.setLocations( locations );
			controller.setCurrentUser( currentUser );
		}
		catch( IOException e )
		{
			System.err.println("ERR in meeting req widget creation : " + e.getMessage() );
			e.printStackTrace();
		}
	}

	@SuppressWarnings("exports")
	public BorderPane getGui() { return root;	}
	
	public void setCons_addMeetingReq(@SuppressWarnings("exports") Consumer<Meeting> cons_addMeetingReq) 
	{
		controller.setcons_addMeetingReq( cons_addMeetingReq );
	}
	
	public void setCons_addEvent(@SuppressWarnings("exports") Consumer<Event> cons_addEvent ) 
	{
		controller.setcons_addEvent( cons_addEvent );
	}
	public void setcons_getDoctorAvailable(@SuppressWarnings("exports") Consumer<Event> cons_getDoctorAvailable) 
	{
		controller.setcons_getDoctorAvailable( cons_getDoctorAvailable );
	}
	public void setAvalaibleDocs(@SuppressWarnings("exports") ArrayList<User> avalaibleDocs) 
	{
		controller.setAvailaibleDocs( avalaibleDocs );
	}

}
