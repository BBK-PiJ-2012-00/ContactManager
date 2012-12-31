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
	private List<PastMeeting> pastMeetings = new ArrayList<PastMeeting>();//list of past meetings
	private List<FutureMeeting> futureMeetings = new ArrayList<FutureMeeting>();

	//also add to a list/set?
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
		futureMeeting = new FutureMeetingImpl(contacts, date);
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
	PastMeeting getPastMeeting(int id) {
		try {
			Iterator<FutureMeeting> iteratorFM = futureMeetings.iterator();
			FutureMeeting futureMeeting = null;
			while (iteratorFM.hasNext()) {
				futureMeeting = iteratorFM.next();
				if (futureMeeting.getId() == id) {
					throw illegalArgEx;
				}
			}
		}
		catch (IllegalArgumentException ex) {
			System.out.print("Error: The meeting with this ID has not taken place yet!");
		}
		Iterator<PastMeeting> iteratorPM = pastMeetings.iterator();
			PastMeeting pastMeeting = null;
			while (iteratorPM.hasNext()) {
				pastMeeting = iteratorPM.next();
				if (pastMeeting.getId() == id) {
					return pastMeeting;
				}
			}			
		return null;
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


















