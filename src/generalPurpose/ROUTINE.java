package generalPurpose;

import java.util.Arrays;
import java.util.Optional;

public enum ROUTINE 
{
	NON("non-repetitiv", 1),
	DAILY("daily", 2),
	MONTHLY("monthly", 3),
	YEARLY("daily", 4);
	
	int id;
	String toString;
	ROUTINE( String toString, int id )
	{
		this.id = id;
		this.toString = toString;
	}
	
	public int getId()
	{
		return id;
	}
	
	@Override
	public String toString()
	{
		return toString;
	}
	
	/**
	 * code found : https://stackoverflow.com/questions/11047756/getting-enum-associated-with-int-value
	 * @param value
	 * @return
	 */
	public static Optional<ROUTINE> valueOf( int value ) 
	{
        return Arrays.stream(values())
            .filter(legNo -> legNo.id == value)
            .findFirst();
    }
}
