
import java.util.Calendar;
import java.util.Set;


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
	
	
	public void addNotes(String notes) {
		this.notes = this.notes + " " + notes;
	}	

	
	public String getNotes() {
		return notes;
	}

}