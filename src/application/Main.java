package application;
	
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.function.Consumer;

import comStruct.Event;
import comStruct.Location;
import comStruct.User;
import data_access.AccessServer;
import gui.GUI_CONSTANTS;
import gui.GeneralPageGui;
import gui.LoginPageGui;
import gui.RegisterPageGui;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;


public class Main extends Application 
{
	
	private AccessServer sqlComm = new AccessServer();
	private BorderPane root = new BorderPane();
	private Stage primaryStage;
	
	private User currentUser;
	
	
	private Consumer< String > loginError;
	private Consumer< String > registerError;
	
	private Consumer< User > cons_login = user -> {
		try 
		{
			if( sqlComm.checkLogin( user ) )
			{
				User cache_user = sqlComm.getUserByMail( user );
				if( cache_user != null )
				{
					currentUser = cache_user;
					goToGenGUI();
				}
			}
			else 
			{
				loginError.accept( "Login Failed" );
			}
		}
		catch( Error e )
		{
			loginError.accept( e.getMessage() );
		}
	};
	
	private Consumer<User> cons_updateProfile = user -> {
		sqlComm.updateProfile( user );
	};
	
	
	private Consumer<User> cons_register = user -> {
		try 
		{
			if( sqlComm.addUser( user ) )
			{
				User cache_user = sqlComm.getUserByMail( user );
				if( cache_user != null )
				{
					currentUser = cache_user;
					goToGenGUI();
				}
			}
			else 
			{
				registerError.accept( "user not added" );
			}
		}
		catch( Error e )
		{
			registerError.accept( e.getMessage() );
		}
			
	};
	

	private void goToGenGUI()
	{
		primaryStage.setTitle( "calandup" );
		GeneralPageGui genPage = new GeneralPageGui( currentUser, b->goToLogin() );
		//getter of events 
		
		genPage.setConsumers( 
				//reload pages
				b -> {
					genPage.refreshEvents();
					genPage.refreshProfile( null );
					genPage.refreshNotifications();
					genPage.unshowDesc();
				},
				// get the user events from the user id
				user -> {
					ArrayList<Event> ev = sqlComm.getEventById( user );
					if( ev != null ) 
						genPage.setCom_User_Event( ev );
					ArrayList<Location> loc = sqlComm.getAllLocation( );
					if( loc != null )
						genPage.setLocalisations( loc );
				}, 
				// cancel the Event from the EVENT table 
				eventToCancel -> {
					if(eventToCancel != null)
						sqlComm.ereaseEventById( eventToCancel );
					genPage.unshowDesc();
					genPage.refreshEvents();
					genPage.refreshNotifications();
				},
				// cancel the Event from the EVENT_REQ table 
				meetingReqToCancel ->{
					if(meetingReqToCancel != null)
						sqlComm.ereaseEventReqById( meetingReqToCancel );
					genPage.unshowDesc();
					genPage.refreshEvents();
					genPage.refreshNotifications();
				},
				// add the Meeting to the EVENT_REQ table 
				meetingToAdd -> { 
					if( meetingToAdd != null )
						sqlComm.addMeetingReq( meetingToAdd );
					genPage.unshowDesc();
					genPage.refreshEvents();
					genPage.refreshNotifications();
				},
				// add event to the EVENT table
				eventToAdd -> { 
					if( eventToAdd != null )
						sqlComm.addEvent( eventToAdd );
					genPage.unshowDesc();
					genPage.refreshEvents();
					genPage.refreshNotifications();
				},
				// add event to the EVENT table and erase it from EVENT_REQ
				eventToChangeTable -> { 
					if( eventToChangeTable != null )
					{
						sqlComm.addEvent( eventToChangeTable );
						sqlComm.ereaseEventReqById( eventToChangeTable );
					}
					
					genPage.unshowDesc();
					genPage.refreshEvents();
					genPage.refreshNotifications();
				},
				// get the available docs from the EVENT table 
				event -> {
					if( event != null )
					{
						ArrayList<User> avalaibleDocs = sqlComm.getDoctorByAvailable(event);
						genPage.setAvalaibleDocs( avalaibleDocs );
					}
				},
				// get the notifications of users 
				user -> {
					if( user != null )
					{
						ArrayList<Event> avalaibleDocs = sqlComm.getAllNotifsOfUser( user );
						genPage.setCom_User_Event( avalaibleDocs );
					}
				},
				// get a single user from it's ID
				userId -> {
					User studiedPatient = sqlComm.getUserById( userId );
					genPage.setCurrentStudiedPatient( studiedPatient );
				},
				// update the event
				event -> {
					sqlComm.updateEvent( event );
					genPage.refreshEvents();
					genPage.refreshNotifications();
				},
				// update the event request
				event -> {
					sqlComm.updateEventReq( event );
					genPage.refreshEvents();
					genPage.refreshNotifications();
				},
				cons_updateProfile,
				user ->{
					String salt = sqlComm.getSaltUsersByMail( user );
					genPage.setSalt( salt );
				},
				userId -> {
					User studiedPatient = sqlComm.getUserById( userId );
					genPage.setDoctorDataProfile( studiedPatient );
				});

		
		
		genPage.refreshEvents();
		genPage.refreshProfile( null );
		genPage.refreshNotifications();
		root.setCenter( genPage.getGui() );
	}
	
	private void goToLogin()
	{
		primaryStage.setTitle( "calandup - Login Page" );
		LoginPageGui log = new LoginPageGui( cons_login, b->goToRegister() );
		loginError = log.getLoginErrCons();
		
		log.setCons_getSalt( user->{
			try 
			{
				String salt = sqlComm.getSaltUsersByMail( user );
				if( salt == "" )
				{
					loginError.accept( "salt not on the table" );
					return;
				}
				log.setSalt( salt );
			}
			catch( Error e )
			{
				loginError.accept( e.getMessage() );
			}
		});
		
		
		root.setCenter( log.getGui() );
	}

	private void goToRegister()
	{
		primaryStage.setTitle( "calandup - Register Page" );
		RegisterPageGui reg = new RegisterPageGui( cons_register, b->goToLogin() );
		registerError = reg.getRegisterErrCons();
		root.setCenter(reg.getGui());
	}
	
	
	@Override
	public void start(@SuppressWarnings("exports") Stage primaryStage) 
	{
		try 
		{
			this.primaryStage = primaryStage;
			//setup the max/min size and stuff
			primaryStage.setResizable( true );
			primaryStage.setMaxHeight( GUI_CONSTANTS.GUI_MAX_HEIGHT );
			primaryStage.setMaxWidth( GUI_CONSTANTS.GUI_MAX_WIDTH );
			primaryStage.setMinWidth( GUI_CONSTANTS.GUI_MIN_WIDTH );
			primaryStage.setMinHeight( GUI_CONSTANTS.GUI_MIN_HEIGHT );
			
			//set the icon
			FileInputStream inputstream = new FileInputStream( GUI_CONSTANTS.ICON_FILENAME ); 
			primaryStage.getIcons().add( new Image(inputstream) );
			
			Scene scene = new Scene( root, 800, 600 );
			scene.getStylesheets().add( getClass().getResource( "application.css" ).toExternalForm() );
			
			goToLogin();
			
			
			primaryStage.setScene( scene );
			primaryStage.show();
			
			// properly close everything
			primaryStage.setOnCloseRequest( e -> {
						sqlComm.close();
				    	Platform.exit();
				        System.exit(0);
					});
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}
		catch(Error e) 
		{
			System.err.println("ERR during RunTime : " + e.getMessage() );
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) 
	{
		launch(args);
	}
	
	
	public static String bitStringConverter( byte[] bytes )
	{
		String str = "";
		for( byte b : bytes ) 
		{
            str += String.format("%02X", b);
        }
		return str;
	}
}
