package data_access;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import comStruct.DateTime;
import comStruct.Event;
import comStruct.Location;
import comStruct.Meeting;
import comStruct.Unavailaible;
import comStruct.User;
import generalPurpose.GENDERS;
import generalPurpose.ROUTINE;
/**
 * Set of static function used to get infos from ResultSets according to the statementOrder
 * @author ulysse
 *
 */
public class SQLResultsParser 
{
	/**
	 * Parse the ResultSet looking for users info in line with statementOrder
	 * @param res
	 * @param statementOrder
	 * @return ArrayList of user
	 * @throws SQLException
	 */
	public static ArrayList<User> userResParser( ResultSet res, ArrayList<String> statementOrder ) throws SQLException
	{
		ArrayList<User> users = new ArrayList<>();

		while( res.next() )
		{
			User u = new User();
			for(int i = 0; i < statementOrder.size(); i++)
			{
				int index = i + 1;
				switch( statementOrder.get( i ) )
				{
				case "ID" : 
					u.setId( res.getInt( index ) );
					break;
				case "NAME" :
					u.setName( res.getString( index ) );
					break;
				case "F_NAME" :
					u.setFirstName( res.getString( index ) );
					break;
				case "EMAIL" :
					u.setEmail( res.getString( index ) );
					break;
				case "H_ADRESS" :
					String home_adress = res.getString( index );
					if( !res.wasNull() )
						u.setHomeAdress( home_adress );
					break;
				case "AGE" :
					int age = res.getInt( index );
					if( !res.wasNull() )
						u.setAge( age );
					break;
				case "PASSWORD" :
					u.setPassword( res.getString( index ) );
					break;
				case "GENDER" :
					Optional<GENDERS> ret = GENDERS.valueOf( res.getInt( index ) );
					if( ret.isPresent() )	u.setGender( ret.get() );
					else 					throw new SQLException("Impossible gender stored in DB");
					break;
				case "ISDISABLE" :
					boolean isDisable = ( res.getShort( index ) == 1)? true:false;
					u.setDisable( isDisable );
					break;
				case "DOCTOR" :
					boolean isDoctor = ( res.getShort( index ) == 1)? true:false;
					u.setDoctor( isDoctor );
					break;
				case "SALT" :
					u.setSalt( res.getString( index ) );
					break;
				case "QUALIFICATION" :
					String qualification = res.getString( index );
					if( !res.wasNull() )
						u.setQualification( qualification );
					break;
				case "SPECIALISATION" :
					String specialisation = res.getString( index );
					if( !res.wasNull() )
						u.setSpecialisation( specialisation );
					break;
					
				}
			}
			users.add( u );
		}
		return users;
	}

	/**
	 * 
	 * @param results
	 * @return boolean 
	 * @throws SQLException
	 */
	public static boolean booleanResParser(ResultSet results) throws SQLException 
	{
		if( results.next() );
			if( results.getBoolean( 1 ) )
				return true;
		return false;
	}
	
	/**
	 * parse the results from different events
	 * @param res
	 * @param statementOrder
	 * @return ArrayList containing {@link Unavailable} and {@link Meeting}
	 * @throws SQLException
	 */
	public static ArrayList<Event> meetingResParser( ResultSet res, ArrayList<String> statementOrder ) throws SQLException
	{
		ArrayList<Event> meeting = new ArrayList<>();

		while( res.next() )
		{
			Event m;
			boolean isMeeting = false;
			res.getInt( SERVER_CONSTANT.TABLE_EVENT_UNAVAILABLE_ID );
			if( !res.wasNull() )	
			{
				m = new Meeting();
				isMeeting = true;
			}
			else
				m = new Unavailaible();
			
			for(int i = 0; i < statementOrder.size(); i++)
			{
				int index = i + 1;
				switch( statementOrder.get( i ) )
				{
				case "ID" : 
					m.setId( res.getInt( index ) );
					break;
				case "DATE_S" :
					m.setStartingTs( new DateTime( res.getString( index ) ));
					break;
				case "DATE_E" :
					m.setEndingTs( new DateTime( res.getString( index ) ));
					break;
				case "DOCTOR_ID" :
					m.setIdDoctor( res.getInt( index ) );
					break;
				case "PATIENT_ID" :
					if( isMeeting )
					{
						int patientId = res.getInt( index );
						if( !res.wasNull() )
						{
							((Meeting) m).setIdPatient( patientId );
						}
						else 
							throw new SQLException("parsed a patientless Meeting");
					}
					break;
				case "SYMPTOMS" :
					if( isMeeting )
					{
						String symptoms = res.getString( index );
						if( !res.wasNull() )
						{
							((Meeting) m).setSymtomes( symptoms );
						}
					}
					break;
				case "REPETED" :
					int repetition = res.getInt( index );
					if( !res.wasNull() )
					{
						Optional<ROUTINE> ret = ROUTINE.valueOf( repetition );
						if( ret.isPresent() )	m.setRoutiness( ret.get() );
						else 					throw new SQLException("Impossible routine stored in DB");
					}
					break;
				case "NOTE" :
					String note = res.getString( index );
					if( !res.wasNull() )
					{
						 m.setNote( note );
					}
					break;
				case "LOCATION" :
					if( isMeeting )
					{
						int location = res.getInt( index );
						if( !res.wasNull() )
						{
							( (Meeting) m ).setiDLocation( location );
						}
						else 
							throw new SQLException("Parssed a location less meeting");
					}
					break;
				case "TITLE" :
					m.setTitle( res.getString( index ) );
					break;
				}
			}
			meeting.add( m );
		}
		return meeting;
	}

	public static ArrayList<Location> locationResParser(ResultSet res, ArrayList<String> statementOrder) throws SQLException 
	{
		ArrayList<Location> location = new ArrayList<>();

		while( res.next() )
		{
			Location l = new Location();
			for(int i = 0; i < statementOrder.size(); i++)
			{
				int index = i + 1;
				switch( statementOrder.get( i ) )
				{
				case "ID" : 
					l.setId( res.getInt( index ) );
					break;
				case "NAME" :
					l.setName( res.getString( index ) );
					break;
				case "ADRESS" :
					l.setAdress( res.getString( index ) );
					break;
				case "EQUIPPED" :
					int isEquipped = res.getInt( index ); 
					l.setEquipped( ( (isEquipped == 1)? true:false  ) );
					break;
				}
			}
			location.add( l );
		}
		return location;
	}
	
}
