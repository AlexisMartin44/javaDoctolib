package data_access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * 
 * @author ulysse
 *
 */
public class StatementCreator 
{
	
	private Connection connection;
	
	//User req
	private PreparedStatement prepSt_addUser;
	@Deprecated private PreparedStatement prepSt_getAllUsers;
	private PreparedStatement prepSt_getSaltUserByMail;
	private PreparedStatement prepSt_getUserByMail;
	private PreparedStatement prepSt_updateProfile;
	private PreparedStatement  prepSt_updateProfileWithoutPassword;
	private PreparedStatement prepSt_getUserById;
	
	//login
	private PreparedStatement prepSt_checkLogin;
	private PreparedStatement prepSt_checkEmailExist;
	
	//Events req
	private PreparedStatement prepSt_getAllEventsOfUser;
	private PreparedStatement prepSt_getAllEventsReqOfUser;
	private PreparedStatement prepSt_addMeeting;
	private PreparedStatement prepSt_addUnavailable;
	private PreparedStatement prepSt_addMeetingReq;
	private PreparedStatement prepSt_updateEventNotes;
	private PreparedStatement prepSt_updateMeetingReqNotes;
	
	private PreparedStatement prepSt_EreaseEventById;
	private PreparedStatement prepSt_EreaseEventReqById;
	
	
	private PreparedStatement prepSt_checkUserDoctor;
	
	private PreparedStatement prepSt_checkDocAvailable;
	private PreparedStatement prepSt_checkPatAvailable;
	
	private PreparedStatement prepSt_getDocAvailable;
	
	//location
	private PreparedStatement prepSt_getAllLocation;
	
	public StatementCreator( Connection con ) throws SQLException 
	{
		connection = con;
		generatePreparedStatements();
	}
	
	private void generatePreparedStatements() throws SQLException
	{
		if( connection != null )
		{	
			//adduser
			prepSt_addUser = connection.prepareStatement( 
					"INSERT INTO " + SERVER_CONSTANT.TABLE_USER
					+ " (NAME, F_NAME, EMAIL, PASSWORD, SALT, AGE, GENDER, HANDICAPPED, DOCTOR)"
					+ " VALUES"
					+ " (?, ?, ?, ?, ?, ?, ?, ?, ?)" );
			
			prepSt_getSaltUserByMail = connection.prepareStatement( 
					"SELECT SALT"
					+ " FROM " + SERVER_CONSTANT.TABLE_USER
					+ " WHERE EMAIL=?");
					
			prepSt_getAllUsers = connection.prepareStatement(
					"SELECT ID, NAME, F_NAME, EMAIL, H_ADRESS, QUALIFICATION, SPECIALISATION, AGE, GENDER"
					+ " FROM " + SERVER_CONSTANT.TABLE_USER );
			
			prepSt_getUserById = connection.prepareStatement(
					"SELECT ID, NAME, F_NAME, EMAIL, H_ADRESS, QUALIFICATION, SPECIALISATION, AGE, GENDER, HANDICAPPED, DOCTOR"
					+ " FROM " + SERVER_CONSTANT.TABLE_USER 
					+ " WHERE ID=?"  );
			
			prepSt_getUserByMail = connection.prepareStatement(
					"SELECT ID, NAME, F_NAME, EMAIL, H_ADRESS, QUALIFICATION, SPECIALISATION, AGE, GENDER, HANDICAPPED, DOCTOR"
					+ " FROM " + SERVER_CONSTANT.TABLE_USER 
					+ " WHERE EMAIL=?"  );
			
			prepSt_checkEmailExist = connection.prepareStatement(
					"SELECT EXISTS"
					+" (SELECT * FROM " + SERVER_CONSTANT.TABLE_USER 
					+" WHERE EMAIL = ? )");
			
			prepSt_checkLogin = connection.prepareStatement( 
					"SELECT EXISTS" 
					+ " (SELECT * FROM " + SERVER_CONSTANT.TABLE_USER
					+ " WHERE EMAIL = ? AND PASSWORD = ? )" );
			
			prepSt_getAllEventsOfUser = connection.prepareStatement(
					"SELECT ID, DATE_S, DATE_E, DOCTOR_ID, CLIENT_ID, SYMPTOMS, REPETED, NOTE, LOCATION, TITLE"
					+ " FROM " + SERVER_CONSTANT.TABLE_EVENT
					+ " WHERE CLIENT_ID=? OR DOCTOR_ID=?");
		
			prepSt_getAllEventsReqOfUser = connection.prepareStatement(
					"SELECT ID, DATE_S, DATE_E, DOC_ID, PAT_ID, SYMPTOMS, REPETED, NOTE, LOCATION, TITLE"
					+ " FROM " + SERVER_CONSTANT.TABLE_MEETING_REQ
					+ " WHERE PAT_ID=? OR DOC_ID=?");
					
			
			prepSt_addMeeting = connection.prepareStatement(
					"INSERT INTO " + SERVER_CONSTANT.TABLE_EVENT
					+ " (DATE_S, DATE_E, DOCTOR_ID, CLIENT_ID, TITLE, SYMPTOMS, REPETED, NOTE, LOCATION)"
					+ " VALUES"
					+ " (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			
			prepSt_addUnavailable = connection.prepareStatement(
					"INSERT INTO " + SERVER_CONSTANT.TABLE_EVENT
					+ " (DATE_S, DATE_E, DOCTOR_ID, TITLE, REPETED)"
					+ " VALUES"
					+ " (?, ?, ?, ?, ?)");
			
			prepSt_addMeetingReq = connection.prepareStatement(
					"INSERT INTO " + SERVER_CONSTANT.TABLE_MEETING_REQ
					+ " (DATE_S, DATE_E, DOC_ID, PAT_ID, TITLE, SYMPTOMS, REPETED, NOTE, LOCATION)"
					+ " VALUES"
					+ " (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			
			prepSt_updateEventNotes = connection.prepareStatement(
					"UPDATE " + SERVER_CONSTANT.TABLE_EVENT
					+ " SET NOTE=?"
					+ " WHERE ID=?");
			
			prepSt_updateMeetingReqNotes = connection.prepareStatement(
					"UPDATE " + SERVER_CONSTANT.TABLE_MEETING_REQ
					+ " SET NOTE=?"
					+ " WHERE ID=?");
			
			prepSt_EreaseEventById = connection.prepareStatement(
					"DELETE FROM " + SERVER_CONSTANT.TABLE_EVENT
					+ " WHERE "
					+ " ID = ?");
			
			prepSt_EreaseEventReqById = connection.prepareStatement(
					"DELETE FROM " + SERVER_CONSTANT.TABLE_MEETING_REQ 
					+ " WHERE " 
					+ " ID = ?");
			
			prepSt_checkUserDoctor = connection.prepareStatement(
					"SELECT EXISTS"
					+" (SELECT * FROM " + SERVER_CONSTANT.TABLE_USER 
					+" WHERE ( ID = ? AND DOCTOR = ? ) )");
			
			//TODO redo both of them
			prepSt_checkDocAvailable = connection.prepareStatement(
					"SELECT EXISTS"
					+" (SELECT * FROM " + SERVER_CONSTANT.TABLE_EVENT 
					+" WHERE DOCTOR_ID = ? "
					+ "AND ( ( DATE_S >= ? AND DATE_S <= ?)"
					+ " OR ( DATE_S <= ? AND DATE_E >= ?) "
					+ " OR ( DATE_E >= ? AND DATE_E <= ?) ) )");
			
			prepSt_checkPatAvailable = connection.prepareStatement(
					"SELECT EXISTS( " +
					
						"SELECT DISTINCT" + 
						"    u.ID," + 
						"    u.NAME," + 
						"    u.F_NAME," + 
						"    u.EMAIL," + 
						"    u.H_ADRESS," +
						"    u.QUALIFICATION," +
						"    u.SPECIALISATION," +
						"    u.AGE," +	
						"    u.GENDER," + 
						"    u.HANDICAPPED," + 
						"    u.DOCTOR" + 
						" FROM" + 
						"    USER u" + 
						" LEFT JOIN EVENT e ON" + 
						"    u.ID = e.DOCTOR_ID" + 
						" WHERE (u.DOCTOR = 1 AND u.ID = ?)" + 
						" AND ( ((" + 
						"        e.DATE_S >= ? AND e.DATE_S <= ?" + 
						"    ) OR(" + 
						"        e.DATE_S <= ? AND e.DATE_E >= ?" + 
						"    ) OR(" + 
						"        e.DATE_E >= ? AND e.DATE_E <= ?" + 
						"    )" + 
						")" + 
						"    OR e.ID IS NULL  )"
					
					+" (SELECT * FROM " + SERVER_CONSTANT.TABLE_EVENT 
					+" WHERE CLIENT_ID = ? "
					+ "AND ( ( DATE_S >= ? AND DATE_S <= ?)"
					+ " OR ( DATE_S <= ? AND DATE_E >= ?) "
					+ " OR ( DATE_E >= ? AND DATE_E <= ?) ) )");
					
			prepSt_getDocAvailable = connection.prepareStatement(
					"SELECT DISTINCT" + 
					"    u.ID," + 
					"    u.NAME," + 
					"    u.F_NAME," + 
					"    u.EMAIL," + 
					"    u.H_ADRESS," +
					"    u.QUALIFICATION," +
					"    u.SPECIALISATION," +
					"    u.AGE," +
					"    u.GENDER," + 
					"    u.HANDICAPPED," + 
					"    u.DOCTOR" + 
					" FROM" + 
					"    USER u" + 
					" LEFT JOIN EVENT e ON" + 
					"    u.ID = e.DOCTOR_ID" + 
					" WHERE u.DOCTOR = 1" + 
					" AND ( NOT((" + 
					"        e.DATE_S >= ? AND e.DATE_S <= ?" + 
					"    ) OR(" + 
					"        e.DATE_S <= ? AND e.DATE_E >= ?" + 
					"    ) OR(" + 
					"        e.DATE_E >= ? AND e.DATE_E <= ?" + 
					"    )" + 
					")" + 
					"    OR e.ID IS NULL  )");
			
			prepSt_getAllLocation = connection.prepareStatement(
					"SELECT ID, NAME, ADRESS, EQUIPPED"
					+ " FROM " + SERVER_CONSTANT.TABLE_LOCATION
					);
			
			prepSt_updateProfile = connection.prepareStatement(
					"UPDATE " + SERVER_CONSTANT.TABLE_USER
					+ " SET NAME=?,"
						+ "F_NAME=?,"
						+ "PASSWORD=?,"
						+ "H_ADRESS=?,"
						+ "QUALIFICATION=?,"
						+ "SPECIALISATION=?,"
						+ "AGE=?,"
						+ "GENDER=?"
					+ " WHERE EMAIL=?"
					);
			
			prepSt_updateProfileWithoutPassword = connection.prepareStatement(
					"UPDATE " + SERVER_CONSTANT.TABLE_USER
					+ " SET NAME=?,"
						+ "F_NAME=?,"
						+ "H_ADRESS=?,"
						+ "QUALIFICATION=?,"
						+ "SPECIALISATION=?,"
						+ "AGE=?,"
						+ "GENDER=?"
					+ " WHERE EMAIL=?"
					);
			
		}
		else 
			throw new SQLException("tried to generate PreparedStatement before creating connection");
	}
	
//	----------- REGISTER ------------------
	
	/**
	 * return the preparedStatement with the args
	 * @param name
	 * @param firstName
	 * @param email
	 * @param password
	 * @param salt 
	 * @param homeAdress
	 * @param age
	 * @param gender
	 * @param isHandicaped
	 * @param isDoctor
	 * @return PreparedStatement
	 * @throws SQLException
	 */
	public PreparedStatement addUser( 	String name, 
										String firstName, 
										String email, 
										String password, 
										String salt, 
										int age,
										int gender, 
										short isHandicaped, 
										short isDoctor ) throws SQLException
	{
		int i = 1;
		prepSt_addUser.clearParameters();
		prepSt_addUser.setString(i++, name);
		prepSt_addUser.setString(i++, firstName);
		prepSt_addUser.setString(i++, email);
		prepSt_addUser.setString(i++, password);
		prepSt_addUser.setString(i++, salt);
		prepSt_addUser.setInt(i++, age);
		prepSt_addUser.setInt(i++, gender);
		prepSt_addUser.setShort(i++, isHandicaped);
		prepSt_addUser.setShort(i++, isDoctor);
		
		return prepSt_addUser;
	}
	
//	-------------LOGIN-----------
	
	/**
	 * return a statement usable to get the salt of a User
	 * @param mail
	 * @return PreparedStatement
	 * @throws SQLException
	 */
	public PreparedStatement getSaltUsersByMail(String mail) throws SQLException 
	{
		prepSt_getSaltUserByMail.clearParameters();
		prepSt_getSaltUserByMail.setString( 1, mail );
		return prepSt_getSaltUserByMail;
	}
	
	public ArrayList<String> getSaltUsersByMail_statementOrder()
	{
		ArrayList< String > arr = new ArrayList<>();
		arr.add("SALT");
		return arr;
	}
	
	/**
	 * 
	 * @param email
	 * @param password
	 * @return preparedStatement to checkLogin
	 * @throws SQLException
	 */
	public PreparedStatement checkLogin( String email, String password ) throws SQLException 
	{
		int i = 1;
		prepSt_checkLogin.clearParameters();
		prepSt_checkLogin.setString(i++, email);
		prepSt_checkLogin.setString(i++, password);
		return prepSt_checkLogin;
	}
	
	public PreparedStatement checkUserExists(String email) throws SQLException 
	{
		prepSt_checkEmailExist.clearParameters();
		prepSt_checkEmailExist.setString(1, email);
		return prepSt_checkEmailExist;
	}
	
	/**
	 * return a statement usable to get all the users primely info
	 * @return
	 */
	public PreparedStatement getAllUsers() 
	{
		return prepSt_getAllUsers;
	}
	/**
	 * return the statementorder for getAllUsers Statement
	 * @return statementOrder
	 */
	public ArrayList<String> getAllUsers_statementOrder()
	{
		ArrayList< String > arr = new ArrayList<>();
		arr.add("ID");
		arr.add("NAME");
		arr.add("F_NAME");
		arr.add("EMAIL");
		arr.add("H_ADRESS");
		arr.add("QUALIFICATION");
		arr.add("SPECIALISATION");
		arr.add("AGE");
		arr.add("GENDER");
		return arr;
	}
	
		
//	------------USER------------
	
	public PreparedStatement getUsersById(Integer userId) throws SQLException 
	{
		prepSt_getUserById.clearParameters();
		prepSt_getUserById.setInt( 1 , userId);
		return prepSt_getUserById;
	}
	
	/**
	 * return a statement usable to get a user by name
	 * @param name
	 * @return PreparedStatement
	 * @throws SQLException
	 */
	public PreparedStatement getUsersByMail(String mail) throws SQLException 
	{
		prepSt_getUserByMail.clearParameters();
		prepSt_getUserByMail.setString( 1, mail );
		return prepSt_getUserByMail;
	}
	
	public PreparedStatement getAvaliableDoctorByDate(String dateS, String dateE) throws SQLException 
	{
		PreparedStatement st = prepSt_getDocAvailable;
		
		int i = 1;
		st.clearParameters();
		
		st.setString(i++, dateS);
		st.setString(i++, dateE);
		
		st.setString(i++, dateS);
		st.setString(i++, dateE);
		
		st.setString(i++, dateS);
		st.setString(i++, dateE);
		
		return st;	
	}
	
	/**
	 * return the statementOrder for getUsersByMail Statement
	 * @return statementOrder
	 */
	public ArrayList<String> getUser_statementOrder()
	{
		ArrayList< String > arr = new ArrayList<>();
		arr.add("ID");
		arr.add("NAME");
		arr.add("F_NAME");
		arr.add("EMAIL");
		arr.add("H_ADRESS");
		arr.add("QUALIFICATION");
		arr.add("SPECIALISATION");
		arr.add("AGE");
		arr.add("GENDER");
		arr.add("HANDICAPPED");
		arr.add("DOCTOR");
		
		return arr;
	}	
	
	public PreparedStatement updateProfile( 	String name, 
			String firstName,
			String password, 
			String homeAdress,
			String qualification,
			String specialisation,
			int age,
			int gender,
			String email) throws SQLException
	{
		int i = 1;
		prepSt_updateProfile.clearParameters();
		prepSt_updateProfile.setString(i++, name);
		prepSt_updateProfile.setString(i++, firstName);
		prepSt_updateProfile.setString(i++, password);
		prepSt_updateProfile.setString(i++, homeAdress);
		prepSt_updateProfile.setString(i++, qualification);
		prepSt_updateProfile.setString(i++, specialisation);
		prepSt_updateProfile.setInt(i++, age);
		prepSt_updateProfile.setInt(i++, gender);
		prepSt_updateProfile.setString(i++, email);
		
		return prepSt_updateProfile;
	}
	
	public PreparedStatement updateProfileWithoutPassword( 	String name, 
			String firstName,
			String homeAdress,
			String qualification,
			String specialisation,
			int age,
			int gender,
			String email) throws SQLException
	{
		int i = 1;
		prepSt_updateProfileWithoutPassword.clearParameters();
		prepSt_updateProfileWithoutPassword.setString(i++, name);
		prepSt_updateProfileWithoutPassword.setString(i++, firstName);
		prepSt_updateProfileWithoutPassword.setString(i++, homeAdress);
		prepSt_updateProfileWithoutPassword.setString(i++, qualification);
		prepSt_updateProfileWithoutPassword.setString(i++, specialisation);
		prepSt_updateProfileWithoutPassword.setInt(i++, age);
		prepSt_updateProfileWithoutPassword.setInt(i++, gender);
		prepSt_updateProfileWithoutPassword.setString(i++, email);
		
		return prepSt_updateProfileWithoutPassword;
	}

//	--------------EVENTS----------
	/**
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public PreparedStatement getAllEventsOfUser( int id ) throws SQLException
	{
		prepSt_getAllEventsOfUser.clearParameters();
		prepSt_getAllEventsOfUser.setInt( 1, id);
		prepSt_getAllEventsOfUser.setInt( 2, id);
		return prepSt_getAllEventsOfUser;	
	}
	
	public PreparedStatement getAllEventsReqOfUserById( int id ) throws SQLException
	{
		prepSt_getAllEventsReqOfUser.clearParameters();
		prepSt_getAllEventsReqOfUser.setInt( 1, id);
		prepSt_getAllEventsReqOfUser.setInt( 2, id);
		return prepSt_getAllEventsReqOfUser;	
	}
	
	public ArrayList<String> getEvent_statementOrder()
	{
		ArrayList< String > arr = new ArrayList<>();
		arr.add("ID");
		arr.add("DATE_S");
		arr.add("DATE_E");
		arr.add("DOCTOR_ID");
		arr.add("PATIENT_ID");
		arr.add("SYMPTOMS");
		arr.add("REPETED");
		arr.add("NOTE");
		arr.add("LOCATION");
		arr.add("TITLE");
		return arr;
	}	
			
	public PreparedStatement addMeeting( 	String StartingTs, 
											String EndingTs,
											int idDoctor,
											int idPatient,
											String title, 
											String symptoms,
											int routiness, 
											String notes,
											int location ) throws SQLException
	{
		int i = 1;
		prepSt_addMeeting.clearParameters();
		prepSt_addMeeting.setString( i++, StartingTs );
		prepSt_addMeeting.setString( i++, EndingTs );
		prepSt_addMeeting.setInt( i++, idDoctor );
		prepSt_addMeeting.setInt( i++, idPatient );
		prepSt_addMeeting.setString( i++, title );
		prepSt_addMeeting.setString( i++, symptoms );
		prepSt_addMeeting.setInt( i++, routiness );
		prepSt_addMeeting.setString( i++, notes );
		prepSt_addMeeting.setInt( i++, location );
		return prepSt_addMeeting;	
	}
	
	
	
	
	
	public PreparedStatement addMeetingReq( String StartingTs, 
											String EndingTs,
											int idDoctor,
											int idPatient,
											String title, 
											String symptoms,
											int routiness, 
											String notes,
											int location ) throws SQLException
	{
		int i = 1;
		prepSt_addMeetingReq.clearParameters();
		prepSt_addMeetingReq.setString( i++, StartingTs );
		prepSt_addMeetingReq.setString( i++, EndingTs );
		prepSt_addMeetingReq.setInt( i++, idDoctor );
		prepSt_addMeetingReq.setInt( i++, idPatient );
		prepSt_addMeetingReq.setString( i++, title );
		prepSt_addMeetingReq.setString( i++, symptoms );
		prepSt_addMeetingReq.setInt( i++, routiness );
		prepSt_addMeetingReq.setString( i++, notes );
		prepSt_addMeetingReq.setInt( i++, location );
		return prepSt_addMeetingReq;	
	}
	
	public PreparedStatement addUnavailable(String ts, 
											String te, 
											int idDoctor, 
											String title, 
											int routiness) throws SQLException 
	{
		int i = 1;
		prepSt_addUnavailable.clearParameters();
		prepSt_addUnavailable.setString( i++, ts );
		prepSt_addUnavailable.setString( i++, te );
		prepSt_addUnavailable.setInt( i++, idDoctor );
		prepSt_addUnavailable.setString( i++, title );
		prepSt_addUnavailable.setInt( i++, routiness );
		return prepSt_addUnavailable;
	}
	
	public PreparedStatement updateEventNotes( int id, String notes ) throws SQLException
	{
		int i = 1;
		prepSt_updateEventNotes.clearParameters();
		prepSt_updateEventNotes.setString( i++, notes );
		prepSt_updateEventNotes.setInt( i++, id );
		return prepSt_updateEventNotes;	
	}
	
	public PreparedStatement updateMeetingReqNotes( int id, String notes ) throws SQLException
	{
		int i = 1;
		prepSt_updateMeetingReqNotes.clearParameters();
		prepSt_updateMeetingReqNotes.setString( i++, notes );
		prepSt_updateMeetingReqNotes.setInt( i++, id );
		return prepSt_updateMeetingReqNotes;	
	}
	
	
	
	
	public PreparedStatement checkUserDoctor( int id ) throws SQLException
	{
		return checkUserDoctorness( true, id );
	}
	
	public PreparedStatement checkUserNonDoctor( int id ) throws SQLException
	{
		return checkUserDoctorness( false, id );
	}
	
	private PreparedStatement checkUserDoctorness( boolean isDoctor, int id ) throws SQLException
	{
		prepSt_checkUserDoctor.clearParameters();
		prepSt_checkUserDoctor.setInt( 1, id );
		prepSt_checkUserDoctor.setInt( 2, ( isDoctor ? 1 : 0 ) );
		return prepSt_checkUserDoctor;	
	}
	
	
	public PreparedStatement checkUserAvailable( boolean isDoctor, int id, String dateS, String dateE) throws SQLException
	{
		PreparedStatement st = prepSt_checkPatAvailable;
		if(isDoctor)
			st = prepSt_checkDocAvailable;
		
		int i = 1;
		st.clearParameters();
		st.setInt( i++, id);
		
		st.setString(i++, dateS);
		st.setString(i++, dateE);
		
		st.setString(i++, dateS);
		st.setString(i++, dateE);
		
		st.setString(i++, dateS);
		st.setString(i++, dateE);
		
		return st;	
	}

	public PreparedStatement ereaseEventById( int id ) throws SQLException
	{
		PreparedStatement st = prepSt_EreaseEventById;
		st.setInt( 1, id );
		return st;
	}
	public PreparedStatement ereaseEventReqById( int id ) throws SQLException
	{
		PreparedStatement st = prepSt_EreaseEventReqById;
		st.setInt( 1, id );
		return st;
	}
	

	public ArrayList<String> getLocation_statementOrder() 
	{
		ArrayList<String> arr = new ArrayList<>(); 
		arr.add("ID");
		arr.add("NAME");
		arr.add("ADRESS");
		arr.add("EQUIPPED");
		return arr;
	}

	public PreparedStatement getAllLocation() 
	{
		return prepSt_getAllLocation;
	}

}
