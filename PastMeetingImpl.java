import java.util.Calendar;
import java.util.Set;
/** 
* A meeting that was held in the past. 
* 
* It includes your notes about what happened and what was agreed. 
*/
public class PastMeetingImpl extends MeetingImpl implements PastMeeting { 
	private String notes = "";
	
	public PastMeetingImpl(Set<Contact> contacts, Calendar date, String notes) {
		super(contacts, date);
		this.notes = this.notes + " " + notes;
	}
	
	/**
	* For use when a FutureMeeting is converted to a
	* PastMeeting, in order to retain the same ID.
	*/
	public PastMeetingImpl(Set<Contact> contacts, Calendar date, String notes, int id) {
		super(contacts, date, id);
		this.notes = this.notes + " " + notes;
	}
	
	public void addNotes(String text) {
		this.notes = this.notes + " " + text;
	}
	

	/**
	* Returns the notes from the meeting. 
	* 
	* If there are no notes, the empty string is returned. 
	* 
	* @return the notes from the meeting. 
	*/
	public String getNotes() {
		return notes;
	}

}