package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;

import comStruct.DateTime;
import comStruct.Event;
import comStruct.Location;
import comStruct.Meeting;
import comStruct.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

public class GeneralPageGui 
{

	private BorderPane root;
	private GeneralPageController controller;
	
	private User currentUser;
	
	private CalendarWidgetGui calendar;
	private Consumer<User> cons_getUserEvents;
	private Consumer<Event> cons_getDoctorAvailable;
	
	
	private CheckAEventGUI descEvent;
	private Consumer<Integer> cons_getUserById;
	private User currentStudiedEventPatient;
	
	private Consumer<Integer> cons_getDoctorById;
	
	private EditProfileWidgetGui profile;
	private Consumer<User> cons_updateProfile;
	
	private Consumer<Event> cons_editEvent;
	private Consumer<Event> cons_editEventReq;
	
	private Consumer<User> setCons_getSalt;
	
	private DataProfileWidgetGui dataProfile;
	
	private Consumer<Event> cons_cancelEvent;
	private Consumer<Event> cons_cancelMeetingReq;
	
	private Consumer<Meeting> cons_addMeetingReq;
	private Consumer<Event> cons_addEvent;
	
	private Consumer<Event> cons_changeTableEvent;
	
	private Consumer<User> cons_openProfil = user ->{
		controller.setSelectedTabProfil();
		refreshProfile( user );
	};
	
	private NotifWidgetGui notifPage;
	private Consumer<User> cons_getNotifs;
	
	private ArrayList<Event> com_User_events;
	private ArrayList<Location> location;
	
	private AskForMeetingGUI choiceEvent;
	
	public GeneralPageGui(@SuppressWarnings("exports") User currentUser, 
							Consumer<Boolean> cons_logOut) 
	{
		this.currentUser = currentUser;
		
		
		try 
		{
			FXMLLoader loader = new FXMLLoader( getClass().getResource( "GeneralPage.fxml" ) );
			root = loader.<BorderPane>load();
			controller = loader.getController();
			
			controller.setConsLogout( cons_logOut );
			
			
			controller.setUserName( currentUser.getFirstName() );
		} 
		catch (IOException e) 
		{
			System.err.println("ERR during the creation of the General Page : " + e.getMessage() );
			e.printStackTrace();
		}
	}
	


	@SuppressWarnings("exports")
	public BorderPane getGui() { return root; }
	
	/**
	 * communication tunnel for events from main to general
	 * @param com_User_events
	 */
	public void setCom_User_Event( @SuppressWarnings("exports") ArrayList<Event> com_User_events ) { this.com_User_events = com_User_events; }
	public void setLocalisations( @SuppressWarnings("exports") ArrayList<Location> loc ) { location = loc; }
	public void setCurrentStudiedPatient( @SuppressWarnings("exports") User u  ) { currentStudiedEventPatient = u; }
	public void setDoctorDataProfile( @SuppressWarnings("exports") User u ) { dataProfile.setCurrentUserStudied( u );}
	
	public void setConsumers( 	Consumer<Boolean> cons_reload, 
								@SuppressWarnings("exports") Consumer<User> cons_getUserEvents,
								@SuppressWarnings("exports") Consumer<Event> cons_cancelEvent,
								@SuppressWarnings("exports") Consumer<Event> cons_cancelMeetingReq,
								@SuppressWarnings("exports") Consumer<Meeting> cons_addMeetingReq,
								@SuppressWarnings("exports") Consumer<Event> cons_addEvent,
								@SuppressWarnings("exports") Consumer<Event> cons_changeTableEvent,
								@SuppressWarnings("exports") Consumer<Event> cons_getDoctorAvailable,
								@SuppressWarnings("exports") Consumer<User> cons_getNotifs,
								Consumer<Integer> cons_getUserById,
								@SuppressWarnings("exports") Consumer<Event> cons_editEvent,
								@SuppressWarnings("exports") Consumer<Event> cons_editEventReq,
								@SuppressWarnings("exports") Consumer<User> cons_updateProfile,
								@SuppressWarnings("exports") Consumer<User> setCons_getSalt,
								@SuppressWarnings("exports") Consumer<Integer> cons_getDoctorById)
	{
		controller.setConsReload( cons_reload );
		this.cons_getUserEvents = cons_getUserEvents;
		this.cons_cancelEvent = cons_cancelEvent;
		this.cons_addMeetingReq = cons_addMeetingReq;
		this.cons_cancelMeetingReq = cons_cancelMeetingReq;
		this.cons_addEvent = cons_addEvent;
		this.cons_changeTableEvent = cons_changeTableEvent;
		this.cons_getDoctorAvailable = cons_getDoctorAvailable;
		this.cons_getNotifs = cons_getNotifs;
		this.cons_updateProfile = cons_updateProfile;
		this.cons_getUserById = cons_getUserById;
		this.cons_editEvent = cons_editEvent;
		this.cons_editEventReq = cons_editEventReq;
		this.setCons_getSalt = setCons_getSalt;
		this.cons_getDoctorById = cons_getDoctorById;
		
		
	}
	
	public void setSalt( String salt )
	{
		profile.setSalt( salt );
	}
	
	public void refreshEvents() 
	{  
		cons_getUserEvents.accept( currentUser );
		
		calendar = new CalendarWidgetGui( com_User_events, 
											eventSelected -> showDesc( eventSelected ),
											daySelected -> showEventPicker( daySelected ));
		controller.setCalendar( calendar );
	}
	
	public void refreshProfile( @SuppressWarnings("exports") User user )
	{
		if( user == null )
			profile = new EditProfileWidgetGui( currentUser, cons_updateProfile, true );
		else 
			profile = new EditProfileWidgetGui( user, cons_updateProfile, false );
		
		profile.setCons_getSalt( setCons_getSalt );
		
		controller.setProfile( profile ); 
		
		dataProfile = new DataProfileWidgetGui( currentUser, cons_getDoctorById );
		dataProfile.setAppointments( com_User_events );
		
		controller.setDataProfile( dataProfile );
	}
	
	public void refreshNotifications()
	{
		cons_getNotifs.accept( currentUser );
		
		notifPage = new NotifWidgetGui( com_User_events,
										notifSelected -> showDescNotif( notifSelected ),
										cons_cancelMeetingReq,
										currentUser.isDoctor() );
		
		controller.setNotifPage( notifPage );
	}

	private void showEventPicker( DateTime daySelected ) 
	{
		choiceEvent = new AskForMeetingGUI( daySelected.getDay(), 
											daySelected.getMonth(), 
											daySelected.getYear(), 
											location,
											currentUser );
		
		if( !currentUser.isDoctor() )
		{
			choiceEvent.setcons_getDoctorAvailable( cons_getDoctorAvailable );
			choiceEvent.setCons_addMeetingReq( cons_addMeetingReq );
		}
		else 
			choiceEvent.setCons_addEvent( cons_addEvent );
		controller.setChoiceEvent( choiceEvent );
	}


	private void showDesc( Event eventSelected ) 
	{
		User uP = null;
		User uD = null;
		
		Location loc = new Location();
		if( eventSelected instanceof Meeting )
		{
			for( Location l : location )
				if( l.getId() == ((Meeting) eventSelected).getIdLocation() )
					loc = l;
			cons_getUserById.accept( ((Meeting) eventSelected).getIdPatient() );
			uP = currentStudiedEventPatient;
			cons_getUserById.accept( ((Meeting) eventSelected).getIdDoctor() );
			uD = currentStudiedEventPatient;
		}
		
		descEvent = new CheckAEventGUI( eventSelected, loc, uP, uD, currentUser, cons_openProfil );
		controller.setSelectedEvent( descEvent );
		descEvent.setCons_cancelEvent( cons_cancelEvent, cons_editEvent );
		descEvent.setCons_acceptEvent( null );
	}

	//close the Description Layout
	public void unshowDesc() 
	{
		controller.unshowDesc();
	}

	public void setAvalaibleDocs(@SuppressWarnings("exports") ArrayList<User> avalaibleDocs) 
	{
		choiceEvent.setAvalaibleDocs( avalaibleDocs );
	}
	
	
	// NOTIF PAGE 
	private void showDescNotif( Event eventReqSelected )
	{
		User uP = null;
		User uD = null;
		
		Location loc = new Location();
		if( eventReqSelected instanceof Meeting )
		{
			for( Location l : location )
				if( l.getId() == ((Meeting) eventReqSelected).getIdLocation() )
					loc = l;
			cons_getUserById.accept( ((Meeting) eventReqSelected).getIdPatient() );
			uP = currentStudiedEventPatient;
			cons_getUserById.accept( ((Meeting) eventReqSelected).getIdDoctor() );
			uD = currentStudiedEventPatient;
		}
		
		descEvent = new CheckAEventGUI( eventReqSelected, loc, uP, uD, currentUser, cons_openProfil );
		controller.setSelectedEventReq( descEvent );
		descEvent.setCons_cancelEvent( cons_cancelMeetingReq, cons_editEventReq );
		descEvent.setCons_acceptEvent( cons_changeTableEvent );
	}

}
