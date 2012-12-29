import java.util.Calendar;
import java.util.GregorianCalendar; 
import java.util.Set;

/** 
* A class to represent meetings 
* 
* Meetings have unique IDs, scheduled date and a list of participating contacts 
*/ 
public class MeetingImpl {
	private int id;
	Calendar meetingDate;
	Set<Contact> attendees = null;
	



/** 
* Returns the id of the meeting.
* 
* @return the id of the meeting.
*/
	int getId() {
		return id;
	}


/** 
* Return the date of the meeting. 
* 
* @return the date of the meeting. 
*/
	Calendar getDate() {
		String dateString = "";
		int day = meetingDate.get(Calendar.DAY_OF_WEEK);
		int month = meetingDate.get(Calendar.MONTH);
		int year = meetingDate.get(Calendar.YEAR);
		dateString = dateString + day + "." + month + "." + year;
		return dateString;
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
Set<Contact> getContacts();

}