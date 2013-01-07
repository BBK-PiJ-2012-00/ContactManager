import java.util.Calendar;
import java.util.GregorianCalendar; 
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.io.Serializable;

/** 
* A class to represent meetings 
* 
* Meetings have unique IDs, scheduled date and a list of participating contacts 
*/ 
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

	public int getId() {
		return meetingId;
	}
	
	public Calendar getDate() {
		/**
		PUT IN MANAGER CLASS
		String dateString = "";
		int day = meetingDate.get(Calendar.DAY_OF_WEEK);
		int month = meetingDate.get(Calendar.MONTH);
		int year = meetingDate.get(Calendar.YEAR);
		dateString = dateString + day + "." + month + "." + year;
		return dateString;
		*/
		return meetingDate;
	}
		
			

/** 
* Return the details of people that attended the meeting. 
* 
* The list contains a minimum of one contact (if there were 
* just two people: the user and the contact) and may contain an 
* arbitraty number of them. 
* 
* @return the details of people that attended the meeting. 
*/
	public Set<Contact> getContacts() {
		return attendees;
	}
		

}