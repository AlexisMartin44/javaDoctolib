package comStruct;

import generalPurpose.ROUTINE;

public class Event 
{
	private int			id;
	private DateTime 	StartingTs;
	private DateTime 	EndingTs;
	private ROUTINE		routiness;
	private int			idDoctor;
	private String		title;
	protected boolean 	available; 
	private String		note;
	
	public DateTime getStartingTs() {
		return StartingTs;
	}
	public void setStartingTs(DateTime startingTs) {
		StartingTs = startingTs;
	}
	public DateTime getEndingTs() {
		return EndingTs;
	}
	public void setEndingTs(DateTime endingTs) {
		EndingTs = endingTs;
	}
	public ROUTINE getRoutiness() {
		return routiness;
	}
	public void setRoutiness(ROUTINE routiness) {
		this.routiness = routiness;
	}
	public int getIdDoctor() {
		return idDoctor;
	}
	public void setIdDoctor(int idDoctor) {
		this.idDoctor = idDoctor;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle(){
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
}
