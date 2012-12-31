/**
*The purpose of this assignment it writing a program to keep track of contacts and 
*meetings. The application will keep track of contacts, past and future meetings, etc. 
*When the application is closed, all data must be stored in a text file called 
*”contacts.txt”. This file must be read at startup to recover all data introduced in a 
*former session.
*/

import java.util.Calendar;
import java.util.GregorianCalendar;
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

	//Changed so that user sees all error messages at once, rather than having to correct 1 at a time
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
				//throw illegalArgEx;
			}
			Iterator<Contact> iterator = contacts.iterator();//check that contacts are known/existent against central contact list
			while (iterator.hasNext()) {
				element = iterator.next();
				if (!contactList.contains(element)) { //what if there's more than one unknown? Should flag ALL unknowns at once
					falseContact = true;
					unknownContacts = unknownContacts + element.getName() + "/n"; //check /n gives newline				
				}
			}
			//if (falseContact == true) {
			//	throw illegalArgEx; //put separately so that multiple unknowns are listed before exception is thrown
			//}
			Calendar now = Calendar.getInstance();
			if (date.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
				if (date.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR)) {
					if (date.get(Calendar.HOUR_OF_DAY) == now.get(Calendar.HOUR_OF_DAY)) {
						if (date.get(Calendar.MINUTE) <= now.get(Calendar.MINUTE)) {
							falseDate = true;
							//throw illegalArgEx;
						}
					}
					else if (date.get(Calendar.HOUR_OF_DAY) < now.get(Calendar.HOUR_OF_DAY)) {
						falseDate = true;
						//throw illegalArgEx;
					}
				}
				else if (date.get(Calendar.DAY_OF_YEAR) < now.get(Calendar.DAY_OF_YEAR)) {
					falseDate = true;
					//throw illegalArgEx;
				}
			}
			else if (date.get(Calendar.YEAR) < now.get(Calendar.YEAR)) {
				falseDate = true;
				//throw illegalArgEx;
			}
			if (isEmpty == true) {
				System.out.println("Error: no contacts have been specified.");
			}
			if (falseContact == true) {
				System.out.println("Error: " + unknownContacts);
			}
			if (falseDate == true) {
				System.out.println("Error: invalid date. Please ensure the date and time are in the future.");
			}
			if (isEmpty || falseContact || falseDate) {
				throw illegalArgEx;
			}				
			futureMeeting = new FutureMeetingImpl(contacts, date);//must come after exceptions are thrown; only created if no exceptions are thrown
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
		futureMeeting = new FutureMeetingImpl(contacts, date);
		int meetingID = futureMeeting.getId();
		return meetingID;
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
		
		contactList.add(tseng);
		contactList.add(reno);
		contactList.add(rude);
		//contactList.add(elena);
		
		//attendeeList.add(tseng);
		//attendeeList.add(rude);
		
		Calendar cal = new GregorianCalendar(2013, 5, 2);
		
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


















