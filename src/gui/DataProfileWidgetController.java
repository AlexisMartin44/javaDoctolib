package gui;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import application.Main;
import comStruct.DateTime;
import comStruct.Event;
import gui.GUI_CONSTANTS.MONTHS;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import generalPurpose.GENDERS;
import comStruct.User;


public class DataProfileWidgetController implements Initializable
{

	private @FXML VBox vbox_pastAppointments;
	private @FXML VBox vbox_futureAppointments;
	private @FXML VBox vbox_stats;
	
	private Consumer<Integer> cons_getUserById;
	
	private User currentUserStudied;
	private User currentUser;
	
	
	public DataProfileWidgetController() {}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		
	}
	
	public void setAppointments( @SuppressWarnings("exports") ArrayList<Event> events )
	{
		
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		sdf.format( today );
		
		for(int i=0; i < events.size(); i++)
		{
			
			Rectangle rectangle1 = new Rectangle();
			rectangle1.setHeight( 89 );
			rectangle1.setWidth( 579 );
			rectangle1.setArcHeight( 5.0 );
			rectangle1.setArcWidth( 5.0 );
			rectangle1.setStroke( Color.BLACK );
			rectangle1.setFill( Color.WHITE );
			
			Rectangle rectangle2 = new Rectangle();
			rectangle2.setHeight( 25 );
			rectangle2.setWidth( 579 );
			rectangle2.setArcHeight( 5.0 );
			rectangle2.setArcWidth( 5.0 );
			rectangle2.setStroke( Color.BLACK );
			rectangle2.setFill( Color.DODGERBLUE );
			
			
			
			
			
			
			//Label localisation = new Label( events.get( i ).get);
			Label date = new Label( events.get(i).getStartingTs().toString() + " - " + events.get(i).getEndingTs().toString() );
			Label title = new Label( events.get( i ).getTitle() );
			
			
			
			date.setLayoutX( 25.0 );
			date.setLayoutY( 6.0 );
			date.setTextFill( Color.WHITE );
			title.setTextFill( Color.WHITE );
			title.setLayoutX( 300.0 );
			title.setLayoutY( 6.0 );
			
			AnchorPane anchorPane = new AnchorPane();
			anchorPane.setMaxSize(579, 89);
			anchorPane.isCenterShape();
			anchorPane.setPadding( new Insets(10, 0, 0, 0) );
			
			anchorPane.getChildren().addAll( rectangle1, rectangle2, date, title );
			
			if( !currentUser.isDoctor() )
			{
				cons_getUserById.accept( events.get( i ).getIdDoctor() );
				
				Label doctor = new Label( "Dr " + currentUserStudied.getName());
				doctor.setLayoutX( 322.0 );
				doctor.setLayoutY( 50.0 );
				anchorPane.getChildren().add( doctor );
				
				if( !currentUserStudied.getQualification().equals( "Unknown") )
				{
					Label qualification = new Label( "Qualification : " + currentUserStudied.getQualification() );
					qualification.setLayoutX( 55.0 );
					qualification.setLayoutY( 40.0 );
					anchorPane.getChildren().add( qualification );
				}
				
				if( !currentUserStudied.getSpecialisation().equals( "Unknown") )
				{
					Label specialisation = new Label( "Specialisation :" + currentUserStudied.getSpecialisation() );
					specialisation.setLayoutX( 55.0 );
					specialisation.setLayoutY( 60.0 );
					anchorPane.getChildren().add( specialisation );
				}
			}
			else
			{
				/*cons_getUserById.accept( events.get( i ).get );
				
				Label patient = new Label( "Dr " + currentUserStudied.getName());
				patient.setLayoutX( 322.0 );
				patient.setLayoutY( 50.0 );
				anchorPane.getChildren().add( doctor );*/
			}
			
			
			
			try
			{
				Date day = sdf.parse( events.get( i ).getStartingTs().toString() );
				if( today.before( day ) )
					vbox_futureAppointments.getChildren().addAll( anchorPane );
				else
					vbox_pastAppointments.getChildren().addAll( anchorPane );
			}
			catch(ParseException e)
			{
				e.printStackTrace();
			}
			
			
		}
	}
	
	public void setConsUser( Consumer<Integer> cons_getUserById )
	{
		this.cons_getUserById = cons_getUserById;
	}
	
	public void setCurrentUserStudied( User u )
	{
		currentUserStudied = u;
	}
	
	public void setUser( User user ) { this.currentUser = user;}
	
}
