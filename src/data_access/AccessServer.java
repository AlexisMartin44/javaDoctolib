package data_access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import comStruct.Event;
import comStruct.Location;
import comStruct.Meeting;
import comStruct.Unavailaible;
import comStruct.User;

/**
 * connection on the SQL server
 * @author ulysse
 *
 */
public class AccessServer 
{
	
	private Connection connection;	
	private StatementCreator stat_creat;
	
	/**
	 * create the connection with the server
	 */
	public AccessServer() 
	{
		try 
		{
			connection = DriverManager.getConnection( 	"jdbc:mysql://" 
														+ SERVER_CONSTANT.SERVER_HOST 
														+ ":" + SERVER_CONSTANT.SERVER_PORT
														+ "/" + SERVER_CONSTANT.SQL_NAME
														+ "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
														SERVER_CONSTANT.SERVER_USERNAME,
														SERVER_CONSTANT.SERVER_PASSWORD );
			stat_creat = new StatementCreator( connection );
		}
		catch (SQLException e) 
		{
			System.err.println("ERR in the access of the server");
			e.printStackTrace();
		}
	}
	/**
	 * check if email already exists and if not put it in the DB
	 * @param user
	 * @return true if the user was added to the DB, false otherwise, can return false if the email already exists
	 */
	public boolean addUser( User user )
	{
		if( user != null )
		{
			if( 	user.getName() != null 
					&& user.getEmail() != null 
					&& user.getFirstName() != null
					&& user.getGender() != null
					&& user.getPassword() != null
					&& user.getSalt() != null )
			{
			
				short isDoctor = (short) (user.isDoctor() ? 1 : 0);
				short isDisable = (short) (user.isDisable() ? 1 : 0);
				
				try 
				{
					PreparedStatement st_check = stat_creat.checkUserExists( user.getEmail() );
					st_check.execute();
					ResultSet results = st_check.getResultSet();
					
					boolean res = SQLResultsParser.booleanResParser( results );
					if ( res )
						throw new Error("email already exists");

					
					PreparedStatement st_add = stat_creat.addUser( 	user.getName(), 
																	user.getFirstName(),
																	user.getEmail(),
																	user.getPassword(),
																	user.getSalt(),
																	user.getAge(),
																	user.getGender().toInt(), 
																	isDisable, 
																	isDoctor);
					int ret2 = st_add.executeUpdate();
					if (ret2 == 0)
						throw new SQLException("0 records affected");
					
					return true;			
				} 
				catch (SQLException e) 
				{
					System.err.println("SQL error durring adding a User : " + e.getMessage());
					e.printStackTrace();
					return false;
				}
			}
		}
			
		return false;
	}
	
	public void updateProfile( User user )
	{
		if( user != null)
		{
			try
			{
				PreparedStatement st_check = stat_creat.checkUserExists( user.getEmail() );
				st_check.execute();
				ResultSet results = st_check.getResultSet();
				
				boolean res = SQLResultsParser.booleanResParser( results );
				if ( res )
				{

					PreparedStatement st_add;
					if( user.getPassword() != null)
					{
						st_add = stat_creat.updateProfile( 	user.getName(), 
								user.getFirstName(),
								user.getPassword(),
								user.getAdress(),
								user.getQualification(),
								user.getSpecialisation(),
								user.getAge(),
								user.getGender().toInt(),
								user.getEmail());
					}
					else
					{
						st_add = stat_creat.updateProfileWithoutPassword( 	user.getName(), 
								user.getFirstName(),
								user.getAdress(),
								user.getQualification(),
								user.getSpecialisation(),
								user.getAge(),
								user.getGender().toInt(),
								user.getEmail());
					}
										
					int ret2 = st_add.executeUpdate();
					if (ret2 == 0)
						throw new SQLException("0 records affected");
				}
				else
					throw new Error("user doesn't exists");
				
				
			}
			catch(SQLException e)
			{
				System.err.println("SQL error durring update an User : " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * add a meeting to the DataBase
	 * @param eventToAdd
	 * @return true if request properly added otherwise false
	 */
	public boolean addEvent( Event eventToAdd )
	{
		if( eventToAdd != null )
		{
			if( 	eventToAdd.getStartingTs() != null
					&& eventToAdd.getEndingTs() != null 
					&& eventToAdd.getRoutiness() != null)
			{	
				try 
				{
					PreparedStatement st = null;
					if( eventToAdd instanceof Meeting )
					{
						Meeting meeting = (Meeting) eventToAdd;
						meetingReqShielding( meeting );
						
						st = stat_creat.addMeeting( 	meeting.getStartingTs().toString(), 
																		meeting.getEndingTs().toString(), 
																		meeting.getIdDoctor(), 
																		meeting.getIdPatient(),
																		meeting.getTitle(),
																		meeting.getSymtomes(),
																		meeting.getRoutiness().getId(),
																		meeting.getNote(),
																		meeting.getIdLocation() );
					}
					else 
					{
						Unavailaible unavailable = (Unavailaible) eventToAdd;
						
						st = stat_creat.addUnavailable( unavailable.getStartingTs().toString(), 
														unavailable.getEndingTs().toString(), 
														unavailable.getIdDoctor(), 
														unavailable.getTitle(),
														unavailable.getRoutiness().getId());
					}
					int ret = st.executeUpdate();
					if (ret == 0)
						throw new SQLException("0 records affected");
					
					return true;
					
				} 
				catch (SQLException e) 
				{
					System.err.println("SQL error durring adding a Meeting : " + e.getMessage());
					e.printStackTrace();
					return false;
				}
			}
		}
		return false;
	}
	
	/**
	 * update the notes of an event 
	 * @param event
	 * @return true if done, false otherwise
	 */
	public boolean updateEvent(Event event) 
	{
		if( event != null )
		{
			if( 	event.getStartingTs() != null
					&& event.getEndingTs() != null 
					&& event.getRoutiness() != null)
			{	
				try 
				{
					PreparedStatement st = null;
					
					st = stat_creat.updateEventNotes( event.getId(), event.getNote() );
					int ret = st.executeUpdate();
					if (ret == 0)
						throw new SQLException("0 records affected");
					
					return true;
					
				} 
				catch (SQLException e) 
				{
					System.err.println("SQL error durring updating a Meeting : " + e.getMessage());
					e.printStackTrace();
					return false;
				}
			}
		}
		return false;
		
	}
	/**
	 * updates the notes of an eventreq
	 * @param event
	 * @return true if updated else false
	 */
	public boolean updateEventReq(Event event) 
	{
		if( event != null )
		{
			if( 	event.getStartingTs() != null
					&& event.getEndingTs() != null 
					&& event.getRoutiness() != null)
			{	
				try 
				{
					PreparedStatement st = null;
					
					st = stat_creat.updateMeetingReqNotes( event.getId(), event.getNote() );
					int ret = st.executeUpdate();
					if (ret == 0)
						throw new SQLException("0 records affected");
					
					return true;
					
				} 
				catch (SQLException e) 
				{
					System.err.println("SQL error durring updating a Meeting : " + e.getMessage());
					e.printStackTrace();
					return false;
				}
			}
		}
		return false;
		
	}
	
	
	/**
	 * add a meeting req to the DB
	 * @param meeting
	 * @return true if request properly added false otherwise
	 */
	public boolean addMeetingReq( Meeting meeting )
	{
		if( meeting != null )
		{
			if( 	meeting.getStartingTs() != null
					&& meeting.getEndingTs() != null 
					&& meeting.getRoutiness() != null)
			{	
				try 
				{
					meetingReqShielding( meeting );
					
					PreparedStatement st = stat_creat.addMeetingReq(meeting.getStartingTs().toString(), 
																	meeting.getEndingTs().toString(), 
																	meeting.getIdDoctor(), 
																	meeting.getIdPatient(),
																	meeting.getTitle(),
																	meeting.getSymtomes(),
																	meeting.getRoutiness().getId(),
																	meeting.getNote(),
																	meeting.getIdLocation() );
							
					int ret = st.executeUpdate();
					if (ret == 0)
						throw new SQLException("0 records affected");
					
					return true;			
				} 
				catch (SQLException e) 
				{
					System.err.println("SQL error durring adding a MeetingReq : " + e.getMessage());
					e.printStackTrace();
					return false;
				}
			}
		}
		return false;
	}
	
	private boolean meetingReqShielding( Meeting meeting ) throws SQLException
	{
		// check id corresponds to Doctor and Patient
		PreparedStatement st_docCheck = stat_creat.checkUserDoctor( meeting.getIdDoctor() );
		st_docCheck.execute();
		boolean isDoc = SQLResultsParser.booleanResParser( st_docCheck.getResultSet() );
		
		PreparedStatement st_patCheck = stat_creat.checkUserNonDoctor( meeting.getIdPatient() );
		st_patCheck.execute();
		boolean isPat = SQLResultsParser.booleanResParser( st_patCheck.getResultSet() );
		
		if( !isDoc || !isPat )
			throw new SQLException("tried to create a meeting with non Doctor or non Patient users");
		
		/*
		// check if both are available
		PreparedStatement st_check_DocAvailable = stat_creat.checkUserAvailable( true, meeting.getIdDoctor(),
																meeting.getStartingTs().toString(), 
																meeting.getEndingTs().toString());
		st_check_DocAvailable.execute();
		boolean isDocAvailable = SQLResultsParser.booleanResParser( st_check_DocAvailable.getResultSet() );
		
		
		PreparedStatement st_check_PatAvailable = stat_creat.checkUserAvailable( false, meeting.getIdPatient(),
																	meeting.getStartingTs().toString(), 
																	meeting.getEndingTs().toString() );
		st_check_PatAvailable.execute();
		boolean isPatAvailable = SQLResultsParser.booleanResParser( st_check_PatAvailable.getResultSet() );
		
		if( !isDocAvailable || !isPatAvailable )
			throw new SQLException("tried to create an event when doctor or patient were not available");
		*/
		return true;
	}
	
	public ArrayList<Event> getAllNotifsOfUser(User user) 
	{
		try 
		{
			PreparedStatement st = stat_creat.getAllEventsReqOfUserById( user.getId() );
			st.execute();
			ResultSet results = st.getResultSet();
			
			ArrayList<String> statementOrder = stat_creat.getEvent_statementOrder();
			ArrayList<Event> notifs = SQLResultsParser.meetingResParser( results, statementOrder);
			return notifs;
		}
		catch (SQLException e) 
		{
			System.err.println("SQL error durring getting all User : " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	
	public String getSaltUsersByMail( User user )
	{
		if( user != null)
		{
			if( user.getEmail() != null )
			{
				try 
				{
					
					PreparedStatement st_check = stat_creat.checkUserExists( user.getEmail() );
					st_check.execute();
					ResultSet results_check = st_check.getResultSet();
					
					boolean res = SQLResultsParser.booleanResParser( results_check );
					if ( !res )
						throw new Error("email doesn't exists");
					
					PreparedStatement st = stat_creat.getSaltUsersByMail( user.getEmail() );
					st.execute();
					
					ResultSet results = st.getResultSet();
					ArrayList<User> users = SQLResultsParser.userResParser( results, stat_creat.getSaltUsersByMail_statementOrder() );
					if( users.isEmpty() )
						throw new SQLException( "didn't get results user" );
					
					User selectedUser = users.get( 0 );
					
					if( selectedUser.getSalt() == null )
						throw new SQLException( "non salted user" );
					
					return selectedUser.getSalt();
				}
				catch(SQLException e)
				{
					System.err.println("SQL error durring getting all User : " + e.getMessage());
					e.printStackTrace();
					return "";
				}
			}
		}
		return "";
	}
	
	
	/**
	 * identify the user
	 * @return true if user identified, false otherwise
	 */
	public boolean checkLogin( User user )
	{
		if( user != null)
		{
			if( user.getEmail() != null && user.getPassword() != null  )
			{
				String email = user.getEmail();
				String password = user.getPassword();
				try 
				{
					PreparedStatement st = stat_creat.checkLogin( email, password );
					st.execute();
					ResultSet results = st.getResultSet();
					
					boolean res = SQLResultsParser.booleanResParser( results );
					
					return res;
				}
				catch(SQLException e)
				{
					System.err.println("SQL error durring getting all User : " + e.getMessage());
					e.printStackTrace();
					return false;
				}
			}
		}
		return false;
	}
	
	public User getUserById(Integer userId) 
	{
		if( userId != null)
		{
			try 
			{
				PreparedStatement st = stat_creat.getUsersById( userId );
				st.execute();
				ResultSet results = st.getResultSet();
				ArrayList<String> statementOrder = stat_creat.getUser_statementOrder();
				ArrayList<User> res = SQLResultsParser.userResParser(results, statementOrder);
				if( !res.isEmpty() ) 
					return res.get( 0 );
				else 
					return null;
			}
			catch(SQLException e)
			{
				System.err.println("SQL error durring getting the user : " + e.getMessage());
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
	
	
	/**
	 * get a User by it's mail -> used in the register and login
	 * @param user
	 * @return the user
	 */
	public User getUserByMail(User user) 
	{
		if( user != null)
		{
			if( user.getEmail() != null )
			{
				String email = user.getEmail();
				try 
				{
					PreparedStatement st = stat_creat.getUsersByMail( email );
					st.execute();
					ResultSet results = st.getResultSet();
					ArrayList<String> statementOrder = stat_creat.getUser_statementOrder();
					ArrayList<User> res = SQLResultsParser.userResParser(results, statementOrder);
					if( !res.isEmpty() )
						return res.get( 0 );
					else return null;
				}
				catch(SQLException e)
				{
					System.err.println("SQL error durring getting the user : " + e.getMessage());
					e.printStackTrace();
					return null;
				}
			}
		}
		return null;
	}
	
	/**
	 * @param event
	 * @return ArrayList of user doctor available on this time period
	 */
	public ArrayList<User> getDoctorByAvailable( Event event )
	{
		try 
		{
			PreparedStatement st = stat_creat.getAvaliableDoctorByDate( event.getStartingTs().toString(), 
																		event.getEndingTs().toString() );
			st.execute();
			ResultSet results = st.getResultSet();
			
			ArrayList<String> statementOrder = stat_creat.getUser_statementOrder();
			ArrayList<User> users = SQLResultsParser.userResParser( results, statementOrder );
			return users;
		}
		catch (SQLException e) 
		{
			System.err.println("SQL error durring getting all Doctors availaible : " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * get all the events of a user from it's id 
	 * @param user
	 * @return an arrayList of Event 
	 */
	public ArrayList<Event> getEventById(User user) 
	{
		if( user != null)
		{
			try 
			{
				PreparedStatement st = stat_creat.getAllEventsOfUser( user.getId() );
				st.execute();
				ResultSet results = st.getResultSet();
				ArrayList<String> statementOrder = stat_creat.getEvent_statementOrder();
				ArrayList<Event> res = SQLResultsParser.meetingResParser( results, statementOrder );
				
				return res;
			}
			catch(SQLException e)
			{
				System.err.println("SQL error durring getting the events : " + e.getMessage());
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
	
	/**
	 * Erase an event of the DB
	 * @param event
	 * @return true if the event was deleted
	 */
	public boolean ereaseEventById(Event event) 
	{
		if( event != null)
		{
			try 
			{
				PreparedStatement st = stat_creat.ereaseEventById( event.getId() );
				int ret = st.executeUpdate();
				if (ret == 0)
					throw new SQLException("0 records affected");
				return true;
			}
			catch( SQLException e )
			{
				System.err.println("SQL error durring ereasing the events : " + e.getMessage());
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
	
	/**
	 * Erase an eventReq of the DB
	 * @param eventReq
	 * @return true if the eventReq was deleted
	 */
	public boolean ereaseEventReqById(Event event) 
	{
		if( event != null)
		{
			try 
			{
				PreparedStatement st = stat_creat.ereaseEventReqById( event.getId() );
				int ret = st.executeUpdate();
				if (ret == 0)
					throw new SQLException("0 records affected");
				return true;
			}
			catch( SQLException e )
			{
				System.err.println("SQL error durring ereasing the events req : " + e.getMessage());
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
	
	public ArrayList<Location> getAllLocation() 
	{
		try 
		{
			PreparedStatement st = stat_creat.getAllLocation();
			st.execute();
			ResultSet results = st.getResultSet();
			ArrayList<String> statementOrder = stat_creat.getLocation_statementOrder();
			ArrayList<Location> location = SQLResultsParser.locationResParser( results, statementOrder );
			
			
			return location;
		}
		catch(SQLException e)
		{
			System.err.println("SQL error durring getting the events : " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	/**
	 * close the connection with the DB
	 * @return
	 */
	public boolean close() 
	{
		try 
		{
			connection.close();
			return true;
		} 
		catch (SQLException e) 
		{
			System.err.println("SQL error durring closing connection : " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	
	
}
