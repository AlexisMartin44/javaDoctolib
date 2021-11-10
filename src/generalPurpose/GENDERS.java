package generalPurpose;

import java.util.Arrays;
import java.util.Optional;

/**
 * enum helping to deal with different genders 
 * and translating int gender type into String
 * @author ulysse
 *
 */
public enum GENDERS 
{
	MALE("Male", 1), 
	FEMALE("Female", 2),
	TRANSGENDER("Transgender", 3), 
	GENDERQUEER("Genderqueer", 4), 
	AGENDER("Agender", 5), 
	GENDERLESS("Genderless", 6),
	NON_BINARY("Non-binary", 7),
	CIS_MAN("Cis Man", 8),
	CIS_WOMAN("Cis Woman", 9),
	TRANS_MAN("Trans Man", 10),
	TRANS_WOMAN("Trans Woman", 11),
	THIRD_GENDER("Third Gender", 12),
	BIGENDER("Bigender", 13),
	GENDERFLUID("Genderfluid", 14);
	
	
	private String string_version;
	private int id;
	private GENDERS( String string_version, int id )
	{
		this.string_version = string_version;
		this.id = id;
	}
	
	@Override
	public String toString()
	{
		return string_version;
	}
	
	public int toInt()
	{
		return id;
	}
	
	/**
	 * code found : https://stackoverflow.com/questions/11047756/getting-enum-associated-with-int-value
	 * @param value
	 * @return
	 */
	public static Optional<GENDERS> valueOf(int value) 
	{
        return Arrays.stream(values())
            .filter(legNo -> legNo.id == value)
            .findFirst();
    }
}
