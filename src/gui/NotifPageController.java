package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import comStruct.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class NotifPageController implements Initializable 
{

	@FXML private VBox vBoxList;
	
	private Consumer<Event> cons_selectEventReq;
	private Consumer<Event> cons_cancelEventReq;
	
	public NotifPageController() {}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {}
	

	public void addAll( @SuppressWarnings("exports") ArrayList<Event> notifs, boolean isDoctor )
	{
		boolean colorBoolean = false;
		for( Event e : notifs )
		{
			vBoxList.getChildren().clear();
			
			HBox hbox = new HBox();
			hbox.setPrefHeight( 30 );
			//set alternate color on the background
			if( colorBoolean )
				hbox.setStyle( "-fx-background-color:#75abd2;" );
			else
				hbox.setStyle( "-fx-background-color:#6594b6;" );
			colorBoolean = !colorBoolean;	
			
			hbox.prefWidth( GUI_CONSTANTS.GUI_MAX_WIDTH );
			Label title = new Label( e.getTitle() );
			Label dateS = new Label( e.getStartingTs().toString() );
			Label dateE = new Label( e.getEndingTs().toString() );
			
			HBox.setMargin( title, new Insets(0, 0, 0, 20 ) );
			HBox.setMargin( dateS, new Insets(0, 0, 0, 20 ) );
			HBox.setMargin( dateE, new Insets(0, 0, 0, 20 ) );
			
			hbox.getChildren().addAll( title, dateS, dateE );
			if( isDoctor )
			{
				Button btnAccept = new Button("SELECT");
				btnAccept.setOnAction( action -> cons_selectEventReq.accept( e ) );
				Button btnDecline = new Button("DECLINE");
				btnDecline.setOnAction( action -> cons_cancelEventReq.accept( e ) );
				hbox.getChildren().addAll( btnAccept, btnDecline );			
				HBox.setMargin( btnAccept, new Insets(0, 0, 0, 20 ) );
				HBox.setMargin( btnDecline, new Insets(0, 0, 0, 20 ) );
			}
			vBoxList.getChildren().add( hbox );
		}
	}

	public void setcon_selectNotif(@SuppressWarnings("exports") Consumer<Event> cons_selectEventReq) 
	{
		this.cons_selectEventReq = cons_selectEventReq;
	}
	public void setcon_cancelNotif(@SuppressWarnings("exports") Consumer<Event> cons_cancelEventReq) 
	{
		this.cons_cancelEventReq = cons_cancelEventReq;
	}
	
}
