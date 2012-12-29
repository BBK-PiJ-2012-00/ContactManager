/** 
* A contact is a person we are making business with or may do in the future. 
* 
* Contacts have an ID (unique), a name (probably unique, but maybe 
* not), and notes that the user may want to save about them. 
*/
public class ContactImpl { 
	private int id;
	private String name;
	private String notes = "";
	private static int idAssigner = 99; //to facilitate assignment of an id
	private static int counter = 0; //gives true count of contacts created. May not be required.
	
	public ContactImpl(String name) {
		this.name = name;
		id = idAssigner + 1; //first contact will have id 100, second 101 etc
	}	
	

	/**
	* Returns the ID of the contact. 
	*/
	int getId() {
		return id;
	}

	/** 
	* Returns the name of the contact.
	*/
	String getName() {
		return name;
	}

/** 
* Returns our notes about the contact, if any. 
* 
* If we have not written anything about the contact, the empty 
* string is returned. 
* 
* @return a string with notes about the contact, maybe empty. 
*/
	String getNotes() {
		return notes;
	}


/** 
* Add notes about the contact. 
* 
* @param note the notes to be added 
*/
	void addNotes(String note) {
		notes = notes + note + " "; //so that notes String is not overwritten if addNotes is called more than once
	}

}