
/**
* Used for storing the static idAssigner variables from the Meeting and Contact classes
* so that the ID values are not lost when the program is closed. Loss of these values would
* compromise ID uniqueness, as the static variables would be 0 upon re-opening the ContactManager
* program and duplicate IDs would be assigned to new Meetings and Contacts.
*/
public interface IdStore {

	/**
	* Used to save the value of the static idAssigner from
	* the Contact class. Should be used immediately prior to
	* saving Contact data to file.
	*
	* @param idAssignerValue the value of idAssigner from Contact
	*/ 
	void saveContactIdAssigner(int idAssignerValue);
	
	/**
	* Used to save the value of the static idAssigner from
	* the Meeting class. Should be used immediately prior to
	* saving Meeting data to file.
	*
	* @param idAssignerValue the value of idAssigner from Meeting
	*/ 
	void saveMeetingIdAssigner(int idAssignerValue);

	/**
	* Used to retrieve the saved value of the Contact idAssigner
	* when Contact data is loaded from a file. 
	*
	* @return the value of the Contact idAssigner saved previously.
	*/
	int getContactIdAssigner();

	/**
	* Used to retrieve the saved value of the Meeting idAssigner
	* when Meeting data is loaded from a file.
	*
	* @return the value of the Meeting idAssigner saved previously.
	*/
	int getMeetingIdAssigner();

}