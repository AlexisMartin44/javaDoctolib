package gui;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.Consumer;

import comStruct.DateTime;
import comStruct.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

public class CalendarWidgetGui 
{
	
	private ArrayList<Event> events;
	private BorderPane root;
	private CalendarWidgetController controller;
	
	public CalendarWidgetGui( @SuppressWarnings("exports") ArrayList<Event> eventsToShow, 
								@SuppressWarnings("exports") Consumer<Event> cons_onSelectEvent,
								@SuppressWarnings("exports") Consumer<DateTime> cons_onSelectDay) 
	{
		if( eventsToShow != null )
			this.events = eventsToShow;
		else 
			this.events = new ArrayList<>();
		
		try 
		{
			FXMLLoader loader = new FXMLLoader( getClass().getResource("CalendarWidget.fxml") );
			root = loader.<BorderPane>load();
			controller = loader.getController();
					
//			root.setMaxHeight( GUI_CONSTANTS.GUI_MAX_HEIGHT );
//			root.setMinHeight( GUI_CONSTANTS.GUI_MIN_HEIGHT );
//			
//			root.setMaxWidth( GUI_CONSTANTS.GUI_MAX_WIDTH );
//			root.setMinHeight( GUI_CONSTANTS.GUI_MIN_WIDTH );
			
			controller.setConsChangeMonth( m -> setCalendarEvents() );
			controller.setCons_onSelect( cons_onSelectEvent, cons_onSelectDay );
			setCalendarForToday();
			setCalendarEvents();
		}
		catch( IOException e )
		{
			System.err.println( "ERR in the creation of the calendar Widget : " + e.getMessage() );
			e.printStackTrace();
		}
	}
	
	private void setCalendarForToday() throws IOException 
	{
		controller.setDate( LocalDate.now().getYear(), LocalDate.now().getMonth().getValue(), LocalDate.now().getDayOfMonth() );
	}

	public void update() 
	{
		
	}

	private void setCalendarEvents()
	{
		ArrayList< Event > eventsForTheSelectedMonth = new ArrayList<>();
		
		events.forEach( e -> {
			if( e.getStartingTs().getMonth() == controller.getSelectedMonth().getIndex() )
				eventsForTheSelectedMonth.add( e );
		});
		
		//TODO check for repetitive events
		
		controller.setEvents( eventsForTheSelectedMonth );
	}
	
	@SuppressWarnings("exports")
	public BorderPane getGui() { return root; }

}
