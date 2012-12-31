/**
*The purpose of this assignment it writing a program to keep track of contacts and 
*meetings. The application will keep track of contacts, past and future meetings, etc. 
*When the application is closed, all data must be stored in a text file called 
*”contacts.txt”. This file must be read at startup to recover all data introduced in a 
*former session.
*/

import java.util.*;
import java.io.*;


public class ContactManagerImpl { 
	private IllegalArgumentException illegalArgEx = new IllegalArgumentException();
	private Set<Contact> contactList = new HashSet<Contact>(); //contacts added to this via addContact()
	private Set<Contact> attendeeList = new HashSet<Contact>(); //contacts attending a specific meeting
	private List<Meeting> pastMeetings = new ArrayList<Meeting>();//list of past meetings
	private List<Meeting> futureMeetings = new ArrayList<Meeting>();
	private List<Meeting> allMeetings = new ArrayList<Meeting>();

	
	public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
		boolean isEmpty = false; //these booleans facilitate display of pertinent error message
		boolean falseContact = false;
		boolean falseDate = false;
		Contact element = null;//to keep track of contacts being iterated
		String unknownContacts = "The following contacts do not exist in your contact list: ";//for multiple unknowns
		Meeting futureMeeting = null;
		try {
			if (contacts.isEmpty()) {
				isEmpty = true;
			}
			Iterator<Contact> iterator = contacts.iterator();//check that contacts are known/existent against central contact list
			while (iterator.hasNext()) {
				element = iterator.next();
				if (!contactList.contains(element)) {
					falseContact = true;
					unknownContacts = unknownContacts + "\n" + element.getName();				
				}
			}
			Calendar now = Calendar.getInstance();
			if (date.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
				if (date.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR)) {
					if (date.get(Calendar.HOUR_OF_DAY) == now.get(Calendar.HOUR_OF_DAY)) {
						if (date.get(Calendar.MINUTE) <= now.get(Calendar.MINUTE)) {
							falseDate = true;
						}
					}
					else if (date.get(Calendar.HOUR_OF_DAY) < now.get(Calendar.HOUR_OF_DAY)) {
						falseDate = true;
					}
				}
				else if (date.get(Calendar.DAY_OF_YEAR) < now.get(Calendar.DAY_OF_YEAR)) {
					falseDate = true;
				}
			}
			else if (date.get(Calendar.YEAR) < now.get(Calendar.YEAR)) {
				falseDate = true;
			}
			if (isEmpty || falseContact || falseDate) {
				throw illegalArgEx;
			}				
			futureMeeting = new FutureMeetingImpl(contacts, date);
			futureMeetings.add(futureMeeting);
		}		
		catch (IllegalArgumentException illegalArgEx) {
			if (isEmpty == true) {
				System.out.println("Error: No contacts have been specified.");
			}
			if (falseContact == true) {
				System.out.println("Error: " + unknownContacts);
				//Need to consider the users options after exception is thrown - retry the creation of meeting/allow reentry of contacts
			} 
			if (falseDate == true) {
				System.out.println("Error: Invalid date. Please ensure the date and time are in the future.");
			}			 
		}
		int meetingID = futureMeeting.getId();
		return meetingID;
	}
	
	/**
	* Returns the PAST meeting with the requested ID, or null if it there is none. 
	* 
	* @param id the ID for the meeting 
	* @return the meeting with the requested ID, or null if it there is none.
	* @throws IllegalArgumentException if there is a meeting with that ID happening in the future
	*/
	public PastMeeting getPastMeeting(int id) {
		try {
			Iterator<Meeting> iteratorFM = futureMeetings.iterator();
			Meeting meeting = null;
			while (iteratorFM.hasNext()) {
				meeting = iteratorFM.next();
				if (meeting.getId() == id) {
					throw illegalArgEx;
				}
			}
		}
		catch (IllegalArgumentException ex) {
			System.out.print("Error: The meeting with this ID has not taken place yet!");
		}
		Iterator<Meeting> iteratorPM = pastMeetings.iterator();
			Meeting meeting = null;
			while (iteratorPM.hasNext()) {
				meeting = iteratorPM.next();
				if (meeting.getId() == id) {
					PastMeeting pastMeeting = (PastMeeting) meeting;
					return pastMeeting;
				}
			}			
		return null;
	}
	
	/** 
	* Returns the FUTURE meeting with the requested ID, or null if there is none. 
	* 
	* @param id the ID for the meeting 
	* @return the meeting with the requested ID, or null if it there is none. 
	* @throws IllegalArgumentException if there is a meeting with that ID happening in the past 
	*/
	public FutureMeeting getFutureMeeting(int id) {
		try {
			Iterator<Meeting> iteratorPM = pastMeetings.iterator();
			Meeting meeting = null;
			while (iteratorPM.hasNext()) {
				meeting = iteratorPM.next();
				if (meeting.getId() == id) {
					throw illegalArgEx;
				}
			}
		}
		catch (IllegalArgumentException ex) {
			System.out.print("Error: The meeting with this ID has already taken place!");
		}
		Iterator<Meeting> iteratorFM = pastMeetings.iterator();
		Meeting meeting = null;
		while (iteratorFM.hasNext()) {
			meeting = iteratorFM.next();
			if (meeting.getId() == id) {
				FutureMeeting futureMeeting = (FutureMeeting) meeting;
				return futureMeeting;
			}
		}
		return null;
	}
	
	
	public Meeting getMeeting(int id) {
		Iterator<Meeting> iterator = allMeetings.iterator();
		Meeting meeting = null;
		while (iterator.hasNext()) {
			meeting = iterator.next();
			if (meeting.getId() == id) {
				return meeting;
			}
		}
		return null;
	}
	
	/** 
	* Returns the list of future meetings scheduled with this contact. 
	* 
	* If there are none, the returned list will be empty. Otherwise, 
	* the list will be chronologically sorted and will not contain any 
	* duplicates. 
	* 
	* @param contact one of the user’s contacts 
	* @return the list of future meeting(s) scheduled with this contact (may be empty)
	* @throws IllegalArgumentException if the contact does not exist
	*/
	public List<Meeting> getFutureMeetingList(Contact contact) {
		List<Meeting> list = new ArrayList<Meeting>();//list to contain meetings attended by specified contact
		try {
			if (!contactList.contains(contact)) {
				throw illegalArgEx;
			}
			Iterator<Meeting> iterator = futureMeetings.iterator();
			Meeting meeting = null;
			while (iterator.hasNext()) { //goes through all future meetings
				meeting = iterator.next();
				Iterator<Contact> conIterator = meeting.getContacts().iterator();
				Contact item = null;
				while (conIterator.hasNext()) { //goes through contacts associated with a meeting
					item = conIterator.next();
					if (item.getId() == contact.getId()) {
						list.add(meeting);
					}
				}
			}
			list = sort(list);//need to sort list and eliminate duplicates!! Separate method for this?
			
			return list;
		}
		catch (IllegalArgumentException ex) {
			System.out.println("Error: The specified contact doesn't exist!");
		}
	return list; //may be empty
	}
	
	private List<Meeting> sort(List<Meeting> list) {
		List<Meeting> sortedList = new ArrayList<Meeting>();
		int min = 0;
		Meeting tempMeeting1 = null;
		Meeting tempMeeting2 = null;
		Meeting tempSlot = null;
		boolean sorted = false;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getDate().after(list.get(i + 1).getDate().)) {
				sorted = false
			
		
		for (int i = 0; i < list.size(); i++) {
			tempMeeting1 = list.get(i);
			tempMeeting2 = list.get(i + 1);
			if (tempMeeting1.getDate().after(tempMeeting2.getDate().getTime())) {
				tempSlot = tempMeeting1; //swaps elements over if first element has later date than second
				list.add(i, tempMeeting2);
				list.add(i + 1, tempMeeting1);
			}
		}
		return list;
	}
				
				
				
			
			
				
			
		
		
	
	
	public static void main(String[] args) {
	
		ContactManagerImpl cm = new ContactManagerImpl();
		cm.launch();
		
	}
	
	private void launch() {
		Contact tseng = new ContactImpl("Tseng");
		Contact reno = new ContactImpl("Reno");
		Contact rude = new ContactImpl("Rude");
		Contact elena = new ContactImpl("Elena");
		Contact r2d2 = new ContactImpl("R2D2");
		
		contactList.add(tseng);
		contactList.add(reno);
		contactList.add(rude);
		//contactList.add(elena);
		
		attendeeList.add(tseng);
		attendeeList.add(rude);
		attendeeList.add(elena);
		attendeeList.add(r2d2);
		
		Calendar cal = new GregorianCalendar(2012, 11, 30);
		
		//Meeting testMeet = new FutureMeetingImpl(attendeeList, cal);
		this.addFutureMeeting(attendeeList, cal);
		
		System.out.println(tseng.getId());
		System.out.println(elena.getId());
		System.out.println(reno.getId());
	
	
	
	}
	



}

// Meeting meeting = new FutureMeeting(params);
//ask user for dates in specific format, which can then be converted to create a new Calendar
//make sure that if wrong format is entered, you throw an exception.

//Don't forget to ensure class implements ContactManager when finished


















