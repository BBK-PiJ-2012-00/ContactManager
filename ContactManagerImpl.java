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
public class ContactManagerImpl { 
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
	public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
		boolean isEmpty = false; //these booleans facilitate display of correct error message// can do with multiple IAEs?
		boolean falseContact = false;
		boolean falseDate = false;
		Contact element = null;//to keep track of contacts being iterated
		String unknownContacts = "The following contacts do not exist in your contact list: ";//for multiple unknowns
		try {
			if (contacts.isEmpty()) {
				isEmpty = true;
				throw illegalArgEx;
			}
			Iterator<Contact> iterator = contacts.iterator();//check that contacts are known/existent against central contact list
			while (iterator.hasNext()) {
				element = iterator.next();
				if (!contactList.contains(element)) { //what if there's more than one unknown? Should flag ALL unknowns at once
					falseContact = true;
					unknownContacts = unknownContacts + element.getName() + "/n"; //check /n gives newline				
				}
			}
			if (falseContact == true) {
				throw illegalArgEx; //put separately so that multiple unknowns are listed before exception is thrown
			}
			Calendar now = Calendar.getInstance();
			if (date.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
				if (date.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR)) {
					if (date.get(Calendar.HOUR_OF_DAY) == now.get(Calendar.HOUR_OF_DAY)) {
						if (date.get(Calendar.MINUTE) <= now.get(Calendar.MINUTE)) {
							falseDate = true;
							throw illegalArgEx;
						}
					}
					else if (date.get(Calendar.HOUR_OF_DAY) < now.get(Calendar.HOUR_OF_DAY)) {
						falseDate = true;
						throw illegalArgEx;
					}
				}
				else if (date.get(Calendar.DAY_OF_YEAR) < now.get(Calendar.DAY_OF_YEAR)) {
					falseDate = true;
					throw illegalArgEx;
				}
			}
			else if (date.get(Calendar.YEAR) < now.get(Calendar.YEAR)) {
				falseDate = true;
				throw illegalArgEx;
			}
		}		
		catch (IllegalArgumentException illegalArgEx) {
			if (isEmpty == true) {
				System.out.println("Error: no contacts have been specified.");
			}
			if (falseContact == true) {
				System.out.println("Error: " + element.getName() + " does not exist.");
				//Need to consider the users options after exception is thrown - retry the creation of meeting/allow reentry of contacts
			} 
			if (falseDate == true) {
				System.out.println("Error: invalid date. Please ensure the date and time are in the future.");
			}			 
		}
		Meeting futureMeeting = new FutureMeetingImpl(contacts, date);
		return futureMeeting.getId();
	}
	



}

// Meeting meeting = new FutureMeeting(params);


















