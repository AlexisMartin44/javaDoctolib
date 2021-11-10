package data_access;

public final class SERVER_CONSTANT 
{
	public final static String SERVER_HOST = "mysql-joaquime-le-rebel.alwaysdata.net";
	public final static String SQL_NAME = "joaquime-le-rebel_projet";
	public final static String SERVER_PORT = "3306";
	public final static String SERVER_USERNAME = "218802";
	public final static String SERVER_PASSWORD = "nV97imQv289WD2f";
	
	public final static String TABLE_USER = "USER";
	public static final String TABLE_EVENT = "EVENT";
	public static final String TABLE_MEETING_REQ = "EVENT_REQ";
	public static final String TABLE_LOCATION = "LOCATION";
	
	//constant index that identifies if it's a meeting or a unavailable event
	public static final int TABLE_EVENT_UNAVAILABLE_ID = 5;
}
