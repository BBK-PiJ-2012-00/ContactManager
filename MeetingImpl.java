
import java.util.Calendar;
import java.util.GregorianCalendar; 
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.io.Serializable;


public class MeetingImpl implements Meeting, Serializable {
	private int meetingId;
	private Calendar meetingDate;
	private Set<Contact> attendees = new HashSet<Contact>();
	private static int idAssigner = 0;
	
	
	public MeetingImpl(Set<Contact> contacts, Calendar date) {
		meetingDate = date;
		attendees.addAll(contacts);
		meetingId = idAssigner + 1;
		incrementIdAssigner();
	}
	
	
	/**
	* For use when a FutureMeeting is converted to a
	* PastMeeting, in order to retain the same ID.
	*/
	public MeetingImpl(Set<Contact> contacts, Calendar date, int id) {
		meetingDate = date;
		attendees.addAll(contacts);
		meetingId = id;
	}
	
	
	private static void incrementIdAssigner() {
		idAssigner = idAssigner + 1;
	}
	
	
	//to be used when data is loaded from file to restore value of idAssigner
	public static void restoreIdAssigner(int idValue) {
		idAssigner = idValue;
	}
	
	
	public static int getIdAssigner() {
		return idAssigner;
	}
	

	public int getId() {
		return meetingId;
	}
	
	
	public Calendar getDate() {		
		return meetingDate;
	}			


	public Set<Contact> getContacts() {
		return attendees;
	}		

}