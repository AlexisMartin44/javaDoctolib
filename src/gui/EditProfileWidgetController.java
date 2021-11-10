package gui;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;


import application.Main;
import comStruct.DateTime;
import comStruct.Event;
import gui.GUI_CONSTANTS.MONTHS;
import javafx.geometry.Pos;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import generalPurpose.GENDERS;
import comStruct.User;
import javafx.geometry.Insets;


public class EditProfileWidgetController implements Initializable
{

	private @FXML PasswordField pswField_password;
	private @FXML PasswordField pswField_passwordVerification;
	private @FXML TextField txtField_names;
	private @FXML TextField txtField_firstName;
	private @FXML TextField txtField_adress;
	private @FXML TextField txtField_age;
	private @FXML ChoiceBox<GENDERS> choiceBoxGender;
	private @FXML VBox vbox_data;
	private @FXML Button editButton;
	
	private TextField txtField_qualification;
	private TextField txtField_specialisation;
	
	
	private User currentUser;
	private Consumer< User > updateProfile;
	
	private Consumer< User > 	getSaltConsumer;
	private String salt;
	
	public EditProfileWidgetController() {}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{		
		choiceBoxGender.getItems().setAll( GENDERS.values() );
	}
	
	public void setLabels( boolean canEditProfile )
	{
		if( currentUser.isDoctor() )
		{
			HBox hboxQualification = new HBox();
			HBox hboxSpecialisation = new HBox();
						
			txtField_qualification = new TextField();
			txtField_specialisation = new TextField();
			txtField_qualification.setPrefSize( 150.0 , 25.0 );
			txtField_qualification.setMinHeight( 25.0 );
			txtField_specialisation.setPrefSize( 150.0 , 25.0 );
			txtField_specialisation.setMinHeight( 25.0 );
			txtField_qualification.setPromptText( currentUser.getQualification() );
			txtField_qualification.setAlignment( Pos.CENTER );
			txtField_qualification.setPadding( new Insets( 0, 50, 0, 50 ) );
			txtField_specialisation.setPromptText( currentUser.getSpecialisation() );
			txtField_specialisation.setAlignment( Pos.CENTER );
			txtField_specialisation.setPadding( new Insets( 0, 50, 0, 50 ) );
			
			if( !canEditProfile )
			{
				txtField_qualification.setDisable( true );
				txtField_specialisation.setDisable( true );
			}
			
			hboxQualification.setSpacing( 50 );
			hboxSpecialisation.setSpacing( 50 );
			
			Label qualificationLabel = new Label( "Qualification" );
			qualificationLabel.setVisible( false );
			
			Label specialisationLabel = new Label( "Specialisation" );
			specialisationLabel.setVisible( false );
			
			
			hboxQualification.getChildren().addAll( new Label( "Qualification" ), txtField_qualification, qualificationLabel );
			hboxSpecialisation.getChildren().addAll( new Label( "Specialisation" ), txtField_specialisation, specialisationLabel);
			hboxQualification.setAlignment( Pos.CENTER );
			hboxSpecialisation.setAlignment( Pos.CENTER );
			vbox_data.getChildren().add( 4, hboxQualification);
			vbox_data.getChildren().add( 5, hboxSpecialisation);
			
			
		}
		
		vbox_data.setSpacing( 15 );
		
		txtField_names.setPromptText( currentUser.getName() );
		txtField_firstName.setPromptText( currentUser.getFirstName() );
		txtField_adress.setPromptText( currentUser.getAdress() );
		choiceBoxGender.setValue( currentUser.getGender() );

		if( currentUser.getAge() != 0 )
			txtField_age.setPromptText( String.valueOf( currentUser.getAge() ) );
		else
			txtField_age.setPromptText( "Unknown" );
		
		if( !canEditProfile )
		{
			pswField_password.setDisable( true );
			pswField_passwordVerification.setDisable( true );
			txtField_names.setDisable( true );
			txtField_firstName.setDisable( true );
			txtField_adress.setDisable( true );
			txtField_age.setDisable( true );
			choiceBoxGender.setDisable( true );
			editButton.setVisible( false );
			vbox_data.getChildren().remove( 7 );
			vbox_data.getChildren().remove( 6 );
		}
	}
	

	
	public void setCurrentUser(User currentUser)
	{
		this.currentUser = currentUser;
	}

	public @FXML void edit()
	{
		String name = txtField_names.getText();
		String first_name = txtField_firstName.getText();
		String password = pswField_password.getText();
		String passwordVerification = pswField_passwordVerification.getText();
		String adress = txtField_adress.getText();
		String qualification = "";
		String specialisation = "";
		int age = 0;
		GENDERS gender = choiceBoxGender.getValue();
		
		if( !txtField_age.getText().equals( "" ) )
			age = Integer.parseInt( txtField_age.getText() );
		
		if( currentUser.isDoctor() )
		{
			qualification = txtField_qualification.getText();
			specialisation = txtField_specialisation.getText();
		}
		
		if( !name.equals( "" ) )
			currentUser.setName( name );
		if( !first_name.equals( "" ) )
			currentUser.setFirstName( first_name );
		if( !adress.equals( "" ) )
			currentUser.setAdress( adress );
		if( !qualification.equals( "" ))
			currentUser.setQualification( qualification );
		if( !specialisation.equals( "" ) )
			currentUser.setSpecialisation( specialisation );
		if( !txtField_age.getText().equals( "" ) )
			currentUser.setAge( age );
		
		if( gender != null )
			currentUser.setGender( gender );
		
		if( !password.equals( "" ) && !passwordVerification.equals( "" ) && password.equals( passwordVerification ))
		{
			MessageDigest md;
			getSaltConsumer.accept( currentUser );
			try 
			{
				md = MessageDigest.getInstance("MD5");
				md.update( salt.getBytes() );
				md.update( password.getBytes() );
				byte[] digest = md.digest();
				String hash = Main.bitStringConverter( digest );
				currentUser.setPassword( hash );
			} 
			catch (NoSuchAlgorithmException e) 
			{
				System.err.println("ERR in the creation of the hash of the password");
				e.printStackTrace();
			}
		}
		
		
		updateProfile.accept( currentUser );
	}
	
	public void setSalt( String salt ) { this.salt = salt; }
	public void setUpdateProfileConsumer( @SuppressWarnings("exports") Consumer<User> cons_updateProfile ){ this.updateProfile = cons_updateProfile; }
	
	public void setSaltConsumer(@SuppressWarnings("exports") Consumer<User> cons_getSalt) 
	{
		getSaltConsumer = cons_getSalt;
	}
	
}
