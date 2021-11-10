package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;

import comStruct.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

public class NotifWidgetGui 
{
	private BorderPane root;
	private NotifPageController controller;
	
	public NotifWidgetGui( @SuppressWarnings("exports") ArrayList<Event> notifs,
							@SuppressWarnings("exports") Consumer<Event> cons_selectEventReq,
							@SuppressWarnings("exports") Consumer<Event> cons_cancelMeetingReq, 
							Boolean isDoctor )
	{
		try
		{
			FXMLLoader loader = new FXMLLoader( getClass().getResource("NotifWidget.fxml") );
			root = loader.<BorderPane>load();
			controller = loader.getController();
			controller.addAll( notifs, isDoctor );
			controller.setcon_selectNotif( cons_selectEventReq );
			controller.setcon_cancelNotif( cons_cancelMeetingReq );
		}
		catch( IOException e )
		{
			System.err.println("ERR in notification widget creation : " + e.getMessage() );
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("exports")
	public BorderPane getGui()
	{
		return root;
	}

}
