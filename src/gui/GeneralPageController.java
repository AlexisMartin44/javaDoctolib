package gui;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

public class GeneralPageController implements Initializable
{
	//tabSelector
	private @FXML TabPane tabPane;
	//calendar Tab
	private @FXML BorderPane 	calendarPlaceHolder;
	private @FXML BorderPane 	meetingSpecPlaceHolder;
	private @FXML BorderPane 	notifListPlaceHolder;
	private @FXML BorderPane 	notifSpecPlaceHolder;
	private @FXML BorderPane	profilePlaceHolder;
	private @FXML BorderPane	dataProfilePlaceHolder;
	private @FXML Label			lblId;
	
	
	private Consumer<Boolean> cons_logOut;
	private Consumer<Boolean> cons_reload;
	
	private @FXML void reload()
	{
		cons_reload.accept( true );
	}
	private @FXML void logOut()
	{
		cons_logOut.accept( true );
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {}
	public GeneralPageController() {}
	
	public void setCalendar( CalendarWidgetGui calendar )
	{	
		calendarPlaceHolder.setCenter( calendar.getGui() );
		BorderPane.setAlignment(calendar.getGui(), Pos.CENTER);
	}
	

	public void setUserName(String firstName) {	lblId.setText( firstName );	}
	
	public void setConsLogout( Consumer<Boolean> cons_logOut ) { this.cons_logOut = cons_logOut; }
	public void setConsReload( Consumer<Boolean> cons_reload ) { this.cons_reload = cons_reload; }
	
	/**
	 * set the specs selected event in the specs panel
	 * @param descEvent
	 */
	public void setSelectedEvent( CheckAEventGUI descEvent ) 
	{
		meetingSpecPlaceHolder.setCenter( descEvent.getGui() );
		BorderPane.setAlignment( descEvent.getGui(), Pos.CENTER );
	}
	
	/**
	 * set the panel of choice in the specs panel
	 * @param choiceEvent
	 */
	public void setChoiceEvent(AskForMeetingGUI choiceEvent) 
	{
		meetingSpecPlaceHolder.setCenter( choiceEvent.getGui() );
		BorderPane.setAlignment( choiceEvent.getGui(), Pos.CENTER );
	}
	
	/**
	 * set the content of the panel of specs to null
	 */
	public void unshowDesc() 
	{
		if( meetingSpecPlaceHolder != null )
			meetingSpecPlaceHolder.setCenter( null );
		if( notifSpecPlaceHolder != null )
			notifSpecPlaceHolder.setCenter( null );
		
	}
	
	public void setProfile(EditProfileWidgetGui gui)
	{
		profilePlaceHolder.setCenter( gui.getGui() );
		BorderPane.setAlignment(gui.getGui(), Pos.CENTER);
	}
	
	public void setDataProfile( DataProfileWidgetGui gui)
	{
		dataProfilePlaceHolder.setCenter( gui.getGui() );
		BorderPane.setAlignment( gui.getGui() , Pos.CENTER );
	}

	public void setNotifPage(NotifWidgetGui gui) 
	{
		notifListPlaceHolder.setCenter( gui.getGui() );
	}

	public void setSelectedEventReq(CheckAEventGUI descEvent) 
	{
		notifSpecPlaceHolder.setCenter( descEvent.getGui() );
		BorderPane.setAlignment( descEvent.getGui(), Pos.CENTER );
	}
	
	// code from https://stackoverflow.com/questions/6902377/javafx-tabpane-how-to-set-the-selected-tab
	public void setSelectedTabProfil() 
	{
		SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
		selectionModel.select( 2 );
	}

}
