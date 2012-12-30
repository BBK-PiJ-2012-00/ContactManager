/**
*The purpose of this assignment it writing a program to keep track of contacts and 
*meetings. The application will keep track of contacts, past and future meetings, etc. 
*When the application is closed, all data must be stored in a text file called 
*”contacts.txt”. This file must be read at startup to recover all data introduced in a 
*former session.
*/

import java.util.Calendar;
import java.util.List; 
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.io.*;

/** 
* A class to manage your contacts and meetings. 
*/
public class ContactManagerImpl implements ContactManager { 
	private IllegalArgumentException illegalArgEx = new IllegalArgumentException();
	private Set<Contact> contactList = new HashSet<Contact>(); //contacts added to this via addContact()
	private Set<Contact> attendeeList = new HashSet<Contact>(); //contacts attending a specific meeting

	/**
	* Add a new meeting to be held in the future. 
	* 
	* @param contacts a list of contacts that will participate in the meeting 
	* @param date the date on which the meeting will take place 
	* @return the ID for the meeting 
	* @throws IllegalArgumentException if the meeting is set for a time in the past,
	* or if any contact is unknown / non-existent.
	*/
	int addFutureMeeting(Set<Contact> contacts, Calendar date) {
		//Check that contacts exist
		//Checks that the date is not in the past
		boolean isEmpty = false; //these booleans facilitate display of correct error message
		boolean falseContact = false;
		String unknownContacts = "The following contacts do not exist in your contact list: ";//for multiple unknowns
		try {
			if (contacts.isEmpty()) {
				isEmpty = true;
				throw illegalArgEx;
			}
			Iterator<Contact> iterator = contacts.iterator();//check that contacts are known/existent against central contact list
			while (iterator.hasNext()) {
				Contact element = iterator.next();
				if (!contactList.contains(element)) { //what if there's more than one unknown? Should flag ALL unknowns at once
					falseContact = true;
					unknownContacts = unknownContacts + element.getName() + "/n" //check /n gives newline				
					throw illegalArgEx;
				}
			}		
		}
		catch (IllegalArgumentException illegalArgEx) {
			if (isEmpty == true) {
				System.out.println("Error: no contacts have been specified.");
			}
			if (falseContact == true) {
				System.out.println("Error: " + unknownContact.getName() + " does not exist.");
				//Need to consider the users options after exception is thrown - retry the creation of meeting/allow reentry of contacts
			}  
		}
		Meeting futureMeeting = new FutureMeetingImpl(contacts, date);
		return futureMeeting.getID();
	}
	

/**
* Returns the PAST meeting with the requested ID, or null if it there is none. 
* 
* @param id the ID for the meeting 
* @return the meeting with the requested ID, or null if it there is none.
* @throws IllegalArgumentException if there is a meeting with that ID happening in the future
*/
PastMeeting getPastMeeting(int id);

/** 
* Returns the FUTURE meeting with the requested ID, or null if there is none. 
* 
* @param id the ID for the meeting 
* @return the meeting with the requested ID, or null if it there is none. 
* @throws IllegalArgumentException if there is a meeting with that ID happening in the past 
*/
FutureMeeting getFutureMeeting(int id);

/**
* Returns the meeting with the requested ID, or null if there is none.
*
* @param id the ID for the meeting
* @return the meeting with the requested ID, or null if there is none.
*/
Meeting getMeeting(int id);

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
List<Meeting> getFutureMeetingList(Contact contact);

/** 
* Returns the list of meetings that are scheduled for, or that took 
* place on, the specified date 
* 
* If there are none, the returned list will be empty. Otherwise, 
* the list will be chronologically sorted and will not contain any 
* duplicates. 
* 
* @param date the date 
* @return the list of meetings 
*/
List<Meeting> getFutureMeetingList(Calendar date);

/** 
* Returns the list of past meetings in which this contact has participated. 
* 
* If there are none, the returned list will be empty. Otherwise, 
* the list will be chronologically sorted and will not contain any 
* duplicates. 
* 
* @param contact one of the user’s contacts 
* @return the list of future meeting(s) scheduled with this contact (maybe empty). 
* @throws IllegalArgumentException if the contact does not exist
*/ 
List<PastMeeting> getPastMeetingList(Contact contact);

/** 
* Create a new record for a meeting that took place in the past. 
* 
* @param contacts a list of participants 
* @param date the date on which the meeting took place 
* @param text messages to be added about the meeting. 
* @throws IllegalArgumentException if the list of contacts is 
* empty, or any of the contacts does not exist 
* @throws NullPointerException if any of the arguments is null 
*/
void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text);

/** 
* Add notes to a meeting. 
* 
* This method is used when a future meeting takes place, and is 
* then converted to a past meeting (with notes). 
* 
* It can be also used to add notes to a past meeting at a later date. 
* 
* @param id the ID of the meeting 
* @param text messages to be added about the meeting. 
* @throws IllegalArgumentException if the meeting does not exist 
* @throws IllegalStateException if the meeting is set for a date in the future 
* @throws NullPointerException if the notes are null 
*/
void addMeetingNotes(int id, String text);

/** 
* Create a new contact with the specified name and notes. 
*
* @param name the name of the contact. 
* @param notes notes to be added about the contact.
* @throws NullPointerException if the name or the notes are null
*/ 
void addNewContact(String name, String notes);

/** 
* Returns a list containing the contacts that 
* 
* @param ids an arbitrary number of contact IDs 
* @return a list containing the contacts that correspond to the IDs. 
* @throws IllegalArgumentException if any of the IDs does not correspond to a real contact 
*/
Set<Contact> getContacts(int... ids);

/** 
* Returns a list with the contacts whose name contains that string. 
* 
* @param name the string to search for 
* @return a list with the contacts whose name contains that string. 
* @throws NullPointerException if the parameter is null 
*/
Set<Contact> getContacts(String name);

/** 
* Save all data to disk. 
* 
* This method must be executed when the program is 
* closed and when/if the user requests it. 
*/
void flush();

}

// Meeting meeting = new FutureMeeting(params);


















