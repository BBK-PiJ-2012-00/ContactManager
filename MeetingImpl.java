import java.util.Calendar;
import java.util.GregorianCalendar; 
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

/** 
* A class to represent meetings 
* 
* Meetings have unique IDs, scheduled date and a list of participating contacts 
*/ 
public class MeetingImpl implements Meeting {
	private int meetingId;
	private Calendar meetingDate;
	private Set<Contact> attendees = new HashSet<Contact>();
	private static int idAssigner = 0;
	
	public MeetingImpl(Set<Contact> contacts, Calendar date) {
		//add contents of contacts to attendees - more robust than using pointer to manager class
		meetingDate = date;
		Iterator<Contact> iterator = contacts.iterator();
		while (iterator.hasNext()) {
			attendees.add(iterator.next());
		}
		meetingId = idAssigner + 1;
		incrementIdAssigner();
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