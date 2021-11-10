package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import comStruct.DateTime;
import comStruct.Event;
import comStruct.Location;
import comStruct.Meeting;
import comStruct.Unavailaible;
import comStruct.User;
import generalPurpose.ROUTINE;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class AskForMeetingWidgetController implements Initializable
{
	private @FXML TextField txtFtitle;
	
	
	private @FXML VBox		vboxMeetingInfo;
		private @FXML TextArea txtArSymptoms;
		private @FXML TextArea txtArNotes;
		private @FXML ChoiceBox<User> choiceBoxAvailableDoc;
		
	private @FXML Label lblHourS;
	private @FXML Label lblHourE;

	private @FXML DatePicker datePickerS;	
	private @FXML DatePicker datePickerE;
	

	private @FXML ChoiceBox<Location> choiceBoxLocation;
	private @FXML Label lblAdress;
	
	private @FXML SplitPane splitPaneH;
	private @FXML WebView WebViewMaps;
	
	
	private DateTime ts = new DateTime();
	private DateTime te = new DateTime();
	
	private int minS;
	private int hS;
	private int minE;
	private int hE;
	
	private @FXML void upHS()
	{
		if( ++hS >= 24 )
			hS = 0;
		refreshHourLabels();
	}
	private @FXML void downHS()
	{
		if( --hS < 0 )
			hS = 23;
		refreshHourLabels();
	}
	private @FXML void upMS()
	{
		if( ++minS >= 60 )
			minS = 0;
		refreshHourLabels();
	}
	private @FXML void downMS()
	{
		if( --minS < 0 )
			minS = 59;
		refreshHourLabels();
	}
	
	private @FXML void upHE()
	{
		if( ++hE >= 24 )
			hE = 0;
		refreshHourLabels();
	}
	private @FXML void downHE()
	{
		if( --hE < 0 )
			hE = 23;
		refreshHourLabels();
	}
	private @FXML void upME()
	{
		if( ++minE >= 60 )
			minE = 0;
		refreshHourLabels();
	}
	private @FXML void downME()
	{
		if( --minE < 0 )
			minE = 59;
		refreshHourLabels();
	}
	
	private void refreshHourLabels()
	{
		lblHourS.setText( hS + ":" + minS );
		lblHourE.setText( hE + ":" + minE );
		click();
	}
	
	
	private @FXML void send()
	{
		Event event;
		if( !currentUser.isDoctor())
			event = new Meeting();
		else 
			event = new Unavailaible();
		
		//time and date setting
		if( datePickerS.getValue() != null && datePickerE.getValue() != null )
		{
			//check if date Starting is greater than endDate
			if( datePickerS.getValue().compareTo( datePickerE.getValue() ) > 0 )
				return;
			// check if the hour Starting is greater the the hour ending
			if( datePickerS.getValue().compareTo( datePickerE.getValue() ) == 0 )
				if( hS > hE || ( hS == hE && minS > minE ) )
					return;
			
			DateTime dts = new DateTime();
			dts.setDay( datePickerS.getValue().getDayOfMonth() );
			dts.setMonth( datePickerS.getValue().getMonthValue() );
			dts.setYear( datePickerS.getValue().getYear() );
			
			dts.setHour( hS );
			dts.setMinute( minS );
		
		
			DateTime dte = new DateTime();
			dte.setDay( datePickerE.getValue().getDayOfMonth() );
			dte.setMonth( datePickerE.getValue().getMonthValue() );
			dte.setYear( datePickerE.getValue().getYear() );
			
			dte.setHour( hE );
			dte.setMinute( minE );
			
			event.setStartingTs( dts );
			event.setEndingTs( dte );
		}
		else return;
		

		event.setIdDoctor( currentUser.getId() );
		
		event.setRoutiness( ROUTINE.NON );
		
		
		if( txtFtitle.getText() != "" )
			event.setTitle( txtFtitle.getText() );
		else return;
		
		if( !currentUser.isDoctor() )
		{
			Meeting meeting = (Meeting) event;
			if( choiceBoxLocation.getValue() != null )
				meeting.setiDLocation( choiceBoxLocation.getValue().getId() );
			else return;

			meeting.setNote( txtArNotes.getText() );
			meeting.setSymtomes( txtArSymptoms.getText() );
			
			if( chosenDoc != null )
				meeting.setIdDoctor( choiceBoxAvailableDoc.getValue().getId() );
			else 
				return;
			meeting.setIdPatient( currentUser.getId() );
			cons_addMeetingReq.accept( meeting );
		}
		else 
		{
			Unavailaible unavailaible = (Unavailaible) event;
			cons_addUnavailable.accept( unavailaible );
		}
			
	}
	
	@FXML private void click()
	{
		//time and date setting
		if( datePickerS.getValue() != null && datePickerE.getValue() != null )
		{
			//check if date Starting is greater than endDate
			if( datePickerS.getValue().compareTo( datePickerE.getValue() ) > 0 )
				return;
			// check if the hour Starting is greater the the hour ending
			if( datePickerS.getValue().compareTo( datePickerE.getValue() ) == 0 )
				if( hS > hE || ( hS == hE && minS > minE ) )
					return;
		
			ts.setDay( datePickerS.getValue().getDayOfMonth() );
			ts.setMonth( datePickerS.getValue().getMonthValue() );
			ts.setYear( datePickerS.getValue().getYear() );
			
			ts.setHour( hS );
			ts.setMinute( minS );
		
		
			te.setDay( datePickerE.getValue().getDayOfMonth() );
			te.setMonth( datePickerE.getValue().getMonthValue() );
			te.setYear( datePickerE.getValue().getYear() );
			
			te.setHour( hE );
			te.setMinute( minE );
		}
		else return;
		
		if( datePickerS.getValue() != null && datePickerE.getValue() != null && !currentUser.isDoctor() )
			getAvalaibleDoc();
	}
	
	
	//engine dealing with the webView for maps view
	private WebEngine wengine;
	
	private Consumer<Meeting> cons_addMeetingReq;
	private Consumer<Event> cons_addUnavailable;
	private Consumer<Event> cons_getDoctorAvailable;
	
	private ArrayList<User> availableDocs;
	private Location location;	
	private User currentUser;
	private User chosenDoc;
	
	
	public AskForMeetingWidgetController() {}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		wengine = WebViewMaps.getEngine();
		
		//choice box listener
		choiceBoxLocation.getSelectionModel()
							.selectedItemProperty()
							.addListener( (ObservableValue<? extends Location> observable, Location oldValue, Location newValue) ->
								{
									if( newValue != null )
									{
										location = newValue;
										lblAdress.setText( location.getName() );
										refreshMaps();
									}
								});
		choiceBoxAvailableDoc.getSelectionModel()
							.selectedItemProperty()
							.addListener( (ObservableValue<? extends User> observable, User oldValue, User newValue) ->
								{
									if( newValue != null )
										chosenDoc = newValue;
								});
		
		splitPaneH.getDividers().get(0).positionProperty().addListener( (o, oldPos, newPos) -> refreshMaps() );
	}
	
	private void setGuiForDoctor()
	{
		vboxMeetingInfo.setVisible( false );
		choiceBoxLocation.setDisable( true );
	}
	
	
	
	/**
	 * has to be called every time the gui of hour and time is modified 
	 */
	private void getAvalaibleDoc()
	{
		Event e = new Event();
		e.setStartingTs( ts );
		e.setEndingTs( te );
		cons_getDoctorAvailable.accept( e );
		choiceBoxAvailableDoc.setVisible( true );
		choiceBoxAvailableDoc.getItems().clear();
		if( availableDocs != null )
			choiceBoxAvailableDoc.getItems().addAll( availableDocs );
	}
	
	
	private void refreshMaps()
	{
		if( location != null )
			wengine.loadContent( getMapsEmbedString() );
	}
	
	/**
	 * supposed to return an <iframe> of maps embed containing the location
	 * @return
	 */
	private String getMapsEmbedString()
	{
		int height = (int) WebViewMaps.getBoundsInParent().getHeight();
		int width = (int) WebViewMaps.getBoundsInParent().getWidth();
		String query = "";
		if( currentUser.getHomeAdress() == "" || currentUser.getHomeAdress() == "Unknown" )
			query = 	"<iframe width=\"" + width + "\" height=\"" + height + "\""
						+ "frameborder=\"0\" style=\"border:0\""
						+ "src=\"https://www.google.com/maps/embed/v1/search?"
						+ "&key=" + GUI_CONSTANTS.MAPS_API_KEY
						+ "&q=" + location.getMapsUrl()
						+ "\"></iframe>";
		else 
			query = "<iframe width=\"" + width + "\" height=\"" + height + "\""
					+ "frameborder=\"0\" style=\"border:0\""
					+ "src=\"https://www.google.com/maps/embed/v1/directions?"
					+ "&key=" + GUI_CONSTANTS.MAPS_API_KEY
					+ "&origin=" + currentUser.getHomeAdressMapsUrl()
					+ "&destination=" + location.getMapsUrl()
					+ "&units=metric"
					+ "\"></iframe>";
		return query;
	}

	public void setCurrentUser(@SuppressWarnings("exports") User user)
	{
		currentUser = user;
		if( currentUser.isDoctor() )
			setGuiForDoctor();
	}
	
	public void setcons_addMeetingReq(@SuppressWarnings("exports") Consumer<Meeting> cons_addMeetingReq) 
	{
		this.cons_addMeetingReq = cons_addMeetingReq; 
	}
	public void setcons_getDoctorAvailable(@SuppressWarnings("exports") Consumer<Event> cons_getDoctorAvailable) 
	{
		this.cons_getDoctorAvailable = cons_getDoctorAvailable;
	}
	
	public void setLocations( @SuppressWarnings("exports") ArrayList<Location> locations )
	{
		choiceBoxLocation.getItems().addAll( locations );
	}

	public void setAvailaibleDocs(@SuppressWarnings("exports") ArrayList<User> avalaibleDocs) 
	{
		this.availableDocs = avalaibleDocs;
	}
	public void setcons_addEvent(@SuppressWarnings("exports") Consumer<Event> cons_addUnavailable) 
	{
		this.cons_addUnavailable = cons_addUnavailable;
	}
}
