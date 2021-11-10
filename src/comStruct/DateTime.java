package comStruct;

public class DateTime 
{
	private int 	day;
	private int 	month;
	private int 	year;
	private int 	hour;
	private int 	minute;
	
	@Override
	public String toString()
	{
		return 	year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day) 
				+ " " + String.format("%02d", hour) + ":" + String.format("%02d", minute) + ":00";
	}
	public DateTime() {}
	
	/**
	 * FORMAT needed : "YY-MM-DD HH:MM"
	 * parse SQL DATETIME string to get a DateTime in java
	 * @param dateTime
	 */
	public DateTime( String dateTime ) 
	{
		int index1 = dateTime.indexOf('-');
		year = Integer.parseInt( dateTime.substring( 0, index1 ) );
		int index2 = dateTime.indexOf('-', index1 + 1);
		month = Integer.parseInt( dateTime.substring( index1+1, index2 ) );
		index1 = dateTime.indexOf(' ', index2 + 1);
		day = Integer.parseInt( dateTime.substring( index2+1, index1 ) );
		
		index2 = dateTime.indexOf(':', index1 + 1);
		hour = Integer.parseInt( dateTime.substring( index1+1, index2 ) );
		index1 = dateTime.indexOf(':', index2 + 1);
		minute = Integer.parseInt( dateTime.substring( index2+1, index1 ) );
	}
	
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public int getMinute() {
		return minute;
	}
	public void setMinute(int minute) {
		this.minute = minute;
	}
}
