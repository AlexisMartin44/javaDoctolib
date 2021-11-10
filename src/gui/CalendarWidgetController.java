package gui;

import java.net.URL;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

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


public class CalendarWidgetController implements Initializable
{

	private @FXML GridPane grid;
	private @FXML Label lblMonth;
	private @FXML Label lblYear;
	private @FXML ChoiceBox<MONTHS> cBoxMonth;
	

	private MONTHS currentShowingMonth;
	private int currentShowingYear;
	
	//date of today
	private int today_day;
	private int today_month;
	private int today_year;
	
	private Consumer<MONTHS> cons_changeMonth;
	
	
	private ArrayList<VBox> vboxsDay = new ArrayList<>();
	private Consumer<Event> cons_onSelectEvent;
	private Consumer<DateTime> cons_onSelectDay;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		cBoxMonth.getItems().setAll( MONTHS.values() );
		cBoxMonth.getSelectionModel()
				.selectedItemProperty()
				.addListener( 
				new ChangeListener<MONTHS>() {
					@Override
					public void changed(ObservableValue<? extends MONTHS> ov, 
															MONTHS value, 
															MONTHS newValue) {
						if( newValue != null )
						{
							currentShowingMonth = newValue;
							cons_changeMonth.accept( newValue );
							lblMonth.setText( newValue.toString() );
						}
					}
				});
	}
	
	public CalendarWidgetController() {}
	
	@SuppressWarnings("exports")
	public MONTHS getSelectedMonth(){ return cBoxMonth.getValue(); }
	
	/**
	 * change the month to be treated by the calendar
	 * @param month
	 */
	public void setDate(int year, int month, int day) 
	{
		this.today_year = year;
		this.today_month = month;
		this.today_day = day;
		
		currentShowingYear = year;
		Optional<MONTHS> m =  MONTHS.indexOf( month );
		if( m.isPresent() )
			currentShowingMonth = m.get();
		lblMonth.setText( currentShowingMonth.toString() );
		lblYear.setText( currentShowingYear + "" );
		cBoxMonth.setValue( currentShowingMonth ); 	
	}
	
	public void setConsChangeMonth( @SuppressWarnings("exports") Consumer<MONTHS> cons_changMonth) 
	{
		this.cons_changeMonth = cons_changMonth; 
	}

	
	/**
	 * set the events for the grid to show and arrange them in a form that is pleasing to the eye
	 * @param eventsForTheMonth
	 */
	public void setEvents( @SuppressWarnings("exports") ArrayList<Event> eventsForTheMonth ) 
	{ 
		for( int i = 0; i < vboxsDay.size(); i++ )
		{
			grid.getChildren().remove( vboxsDay.get( i ) );
		}
		vboxsDay.clear();
		
		
		
		// https://www.baeldung.com/java-get-day-of-week
		Calendar cal = new GregorianCalendar( currentShowingYear, currentShowingMonth.getIndex()-1, 1);
		int dayOffset = (cal.get( Calendar.DAY_OF_WEEK )-2 )% 7;
		
		// https://stackoverflow.com/questions/8940438/number-of-days-in-particular-month-of-particular-year
		YearMonth yearMonthObject = YearMonth.of(currentShowingYear, currentShowingMonth.index );
		int daysInMonth = yearMonthObject.lengthOfMonth();
		
		// solution from https://stackoverflow.com/questions/4403542/how-does-java-do-modulus-calculations-with-negative-numbers/4403556
		if (dayOffset < 0)
		{
		    dayOffset += 7;
		}		
		
		if( currentShowingMonth != null )
		{
			for(int i = 0; i < daysInMonth; i++)
			{
				VBox vbox = new VBox();
				//add the day number
				Label dayLabel = new Label("" + (i+1));
				VBox.setMargin( dayLabel, new Insets(10, 0, 0, 10) );
//				dayLabel.setOpacity( 0.5 );
				
				DateTime dt = new DateTime();
				dt.setMonth( currentShowingMonth.getIndex() );
				dt.setDay( i+1 );
				
				if( ( currentShowingMonth.getIndex() == today_month )
					&& (currentShowingYear == today_year)
					&& ((i+1) == today_day) )
				{
					dayLabel.setStyle("-fx-underline: true;");
				}
				
				//check if the date is from the past
				if( (currentShowingMonth.getIndex() >= today_month) 
						&& (currentShowingYear >= today_year)
						&& (i+1) >= today_day )
				{
					dayLabel.setOnMouseClicked( n -> cons_onSelectDay.accept( dt ) );
				}
				
				vbox.getChildren().add( dayLabel );
				grid.add( vbox, (i + dayOffset)%GUI_CONSTANTS.NB_CLUMS_CALENDAR , ((i +dayOffset)/GUI_CONSTANTS.NB_CLUMS_CALENDAR) + 1);
				vboxsDay.add( vbox );
			}
			
			eventsForTheMonth.forEach( e -> {
				int day = e.getStartingTs().getDay();
				if( day > vboxsDay.size() )
					throw new IndexOutOfBoundsException( "tried to add an event at a false day for this month :" + day );
				
				Label lbl = new Label( e.getTitle() + "" );
				lbl.setOnMouseClicked( i -> cons_onSelectEvent.accept( e ) );
				vboxsDay.get( day - 1 ).getChildren().add( lbl );
			});
		}
		else throw new NullPointerException( "tried to add events before selecting the month" );
	}

	public void setCons_onSelect(@SuppressWarnings("exports") Consumer<Event> cons_onSelectEvent, 
								@SuppressWarnings("exports") Consumer<DateTime> cons_onSelectDay
								) 
	{
		this.cons_onSelectEvent = cons_onSelectEvent;
		this.cons_onSelectDay = cons_onSelectDay;
	}

	
}
