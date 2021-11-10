package comStruct;

import java.text.Normalizer;

public class Location 
{
	private int 	id;
	private String	adress;
	private String 	name;
	private boolean equipped;
	
	
	public Location() 
	{
		id = 0;
		adress = "Unknown Island, Shark Bay WA, Australia";
		name = "Unknown Place";
		equipped=false;
	}
	
	public int getId() 
	{
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAdress()
	{
		return adress;
	}
	public void setAdress(String adress)
	{
		this.adress = adress;
	}
	
	public void setEquipped( boolean b ) 
	{
		equipped = b;
	}
	
	@Override
	public String toString()
	{
		return name;
	}
	
	/**
	 * @return url link to a maps vision of the place
	 */
	public String getMapsUrl()
	{
		String placeNameSafe = getAdress();
		
		//get rid of all accents and other crazy stuff
		placeNameSafe = Normalizer.normalize( placeNameSafe , Normalizer.Form.NFKC );
		placeNameSafe = placeNameSafe.replace(' ', '+');
		
		return placeNameSafe;
	}
	
}
