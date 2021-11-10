package comStruct;

public class Meeting extends Event 
{
	private String 		symtomes = "";
	private int 		idPatient;
	private int			idLocation;

	public Meeting()
	{
		super();
	}
	

	public String getSymtomes() {
		return symtomes;
	}
	public void setSymtomes(String symtomes) {
		this.symtomes = symtomes;
	}
	public int getIdPatient() {
		return idPatient;
	}
	public void setIdPatient(int idPatient) {
		this.idPatient = idPatient;
	}
	public int getIdLocation() {
		return idLocation;
	}
	public void setiDLocation(int location) {
		this.idLocation = location;
	}
}
