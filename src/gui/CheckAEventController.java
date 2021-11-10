package gui;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import comStruct.DateTime;
import comStruct.Event;
import comStruct.Location;
import comStruct.Meeting;
import comStruct.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class CheckAEventController implements Initializable
{
	private @FXML Button	acceptBtn;
	
	private @FXML DatePicker datePickerS;
	private @FXML DatePicker datePickerE;
	
	private @FXML Label lblHourS;
	private @FXML Label lblHourE;
	
	private @FXML Label lblPatientNameTitle;
	private @FXML Label lblPatientName;
	
	private @FXML Label lblStaffNameTitle;
	private @FXML Label lblStaffName;
	
	private @FXML Label lblSymptomsTitle;
	private @FXML Label lblSymptoms;
	
	private @FXML Label lblNoteTitle;
	private @FXML TextArea txtFNotes;
	private @FXML Button btnNotes;
	
	private @FXML Label lblTitle;
	private @FXML Label lblAdress;
	
	private @FXML SplitPane splitPaneH;
	private @FXML WebView webViewMaps;
	private WebEngine wengine;
	
	private @FXML void accept()
	{
		cons_acceptEvent.accept( eventshown );
	}
	
	private @FXML void cancel()
	{
		cons_cancelEvent.accept( eventshown );
	}
	
	private @FXML void update()
	{
		eventshown.setNote( txtFNotes.getText() );
		cons_editEvent.accept( eventshown );
	}
	
	private Consumer<Event> cons_cancelEvent;
	private Event eventshown;
	
	private Location location;
	private Consumer<Event> cons_acceptEvent;
	
	private User currentUser;

	Consumer<User> cons_openProfil;
	Consumer<Event> cons_editEvent;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		splitPaneH.getDividers().get(0).positionProperty().addListener( (o, oldPos, newPos) -> refreshMaps() );
	}
	public CheckAEventController() {}
	
	
	public void setEvent( @SuppressWarnings("exports") Event event ) 
	{
		eventshown = event;
		
		lblTitle.setText( event.getTitle() );
		
		DateTime eventTs = event.getStartingTs();
		DateTime eventTe = event.getEndingTs();
		datePickerS.setValue( LocalDate.of( eventTs.getYear(), eventTs.getMonth(), eventTs.getDay() ) );
		datePickerE.setValue( LocalDate.of( eventTe.getYear(), eventTe.getMonth(), eventTe.getDay() ) );
		
		lblHourS.setText( eventTs.getHour() + ":" + eventTs.getMinute() );
		lblHourE.setText( eventTe.getHour() + ":" + eventTe.getMinute() );
		
		if( event.getClass().equals( Meeting.class ) )
		{
			txtFNotes.setText( ( (Meeting) event).getNote() );
			lblSymptoms.setText( ( (Meeting) event).getSymtomes() );
			txtFNotes.setVisible(true);
			lblNoteTitle.setVisible( true );
			btnNotes.setVisible( true );
			lblSymptoms.setVisible(true);
			lblSymptomsTitle.setVisible(true);
		}
		
		lblAdress.setText( location.getName() );
		wengine = webViewMaps.getEngine();
		refreshMaps();
	}
	
	public void setLocation( @SuppressWarnings("exports") Location location )
	{
		this.location = location;
	}
	
	private void refreshMaps()
	{
		if( location != null )
			wengine.loadContent( getMapsEmbedString() );
	}	
	private String getMapsEmbedString()
	{
		
		int height = (int) webViewMaps.getBoundsInParent().getHeight();
		int width = (int) webViewMaps.getBoundsInParent().getWidth();
		String query;
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
	
	public void setCons_cancelEvent( @SuppressWarnings("exports") Consumer<Event> cons_cancelEvent, 
									@SuppressWarnings("exports") Consumer<Event> cons_editEvent )
	{
		this.cons_cancelEvent = cons_cancelEvent;
		this.cons_editEvent = cons_editEvent;
	}
	public void setConsAccept_Event(@SuppressWarnings("exports") Consumer<Event> cons_acceptEvent) 
	{
		this.cons_acceptEvent = cons_acceptEvent;
		acceptBtn.setVisible( true );
	}
	
	public void setCons_OpenProfil(@SuppressWarnings("exports") Consumer<User> cons_openProfil ) 
	{
		this.cons_openProfil = cons_openProfil;
	}
	
	public void setUserToStudy(@SuppressWarnings("exports") User userPat,
							@SuppressWarnings("exports") User userDoc ) 
	{
		lblStaffNameTitle.setVisible( true );
		lblStaffName.setVisible( true );
		lblStaffName.setText( userDoc.toString() );
		lblStaffName.setOnMouseClicked( e -> cons_openProfil.accept( userDoc ) );
		
		lblPatientNameTitle.setVisible( true );
		lblPatientName.setVisible( true );
		lblPatientName.setText( userPat.toString() );
		lblPatientName.setOnMouseClicked( e -> cons_openProfil.accept( userPat ) );
		
		
	}
	
	public void setCurrentUser( @SuppressWarnings("exports") User currentUser )
	{
		this.currentUser = currentUser;
	}
}
