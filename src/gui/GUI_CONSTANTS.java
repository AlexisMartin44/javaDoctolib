package gui;

import java.util.Arrays;
import java.util.Optional;


/**
 * class defining the constant values concerning the GUI
 * @author ulysse
 *
 */
public final class GUI_CONSTANTS 
{
	public static final int GUI_MIN_WIDTH 	= 500;
	public static final int GUI_MIN_HEIGHT 	= 500;
	
	public static final int GUI_MAX_WIDTH 	= 2000;
	public static final int GUI_MAX_HEIGHT 	= 2000;
	
	public static final String ICON_FILENAME = "rsc/icon.png";
	
	/*
	 * resources name 
	 */
	public static final String RSC_WALL_PAPER_LOGIN_PAGE 	= "";
	public static final String RSC_WALL_PAPER_GRID_PAGE 	= "";
	
	
	/*
	 * API key for maps
	 */
	public static final String MAPS_API_KEY	= "AIzaSyA82PkhDPvFUr0qqxKnA6S-HTIA-1JUFBU";
	public static final String MAPS_STYLE_API_KEY = "766b946a05bf2476";
	
	public static final boolean isCUSTOM_MAP_ACTIVATED = false;
	
	/*
	 * for the calendar GUI
	 */
	enum MONTHS
	{
		JANUARY("January", 31, 1),
		FEBRUARY("February", 28, 2),
		MARCH("March", 31, 3),
		APRIL("April", 30, 4),
		MAY("May", 31, 5),
		JUNE("June", 30, 6),
		JULY("July", 31, 7),
		AUGUST("August", 31, 8),
		SEPTEMBER("September", 30, 9),
		OCTOBER("October", 31, 10),
		NOVEMBER("November", 30, 11),
		DECEMBER("December", 30, 12);
		
		
		String name;
		int nbDays;
		int index;
		
		private MONTHS( String name, int nbDays, int index ) 
		{
			this.name = name;
			this.index = index;
			this.nbDays = nbDays;
		}
		
		public int getIndex(){	return index;	}
		public int getNbDays() { return nbDays; }
		
		@Override
		public String toString() { return name; }

		public static Optional<MONTHS> indexOf(int currentMonth) 
		{
			return Arrays.stream(values())
		            .filter(legNo -> legNo.index == currentMonth)
		            .findFirst();
		}
	}
	
	public final static int NB_ROW_CALENDAR = 5;
	public final static int NB_CLUMS_CALENDAR = 7;
}
