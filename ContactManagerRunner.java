
import java.util.*;
import java.io.*;

public class ContactManagerRunner {

	public static void main(String[] args) {
	
		ContactManagerImpl contactManager = new ContactManagerImpl();	
		
		ContactManagerUtilities.displayWelcome();
		contactManager.loadData(); //load data from previous sessions (if there is any)	
		
		//temporary variables used throughout switch statements
		int id;
		Contact contact;
		String entry = "";
		Calendar date;
		Set<Contact> attendees;
		Set<Contact> contacts;
		int[] attendeeArray;
		
		boolean finished = false;
		
		while (!finished) {		
			int userSelection = ContactManagerUtilities.chooseMainMenuOption();
			switch (userSelection) {
				
				case 1: //Main menu: Add future meeting
						System.out.println("\n");
						System.out.println("*** Add a Future Meeting ***");
						attendeeArray = ContactManagerUtilities.selectAttendees(contactManager.getContactList());						
						if (attendeeArray == null) {//occurs if user opts to quit, or if contactList is empty
							break;//return to main menu
						}
						attendees = contactManager.getContacts(attendeeArray);
						date = ContactManagerUtilities.createDateAndTime();
						if (date == null) {
							break;//return to main menu
						}
						contactManager.addFutureMeeting(attendees, date);
						break;//return to main menu 
						
						
				case 2: // Main menu: Look up meeting
						System.out.println("\n");
						System.out.println("*** Look up a Meeting ***");
						int userChoice = ContactManagerUtilities.lookUpMeetingOptions();
						switch (userChoice) {									
									
							case 1: // Look up Meeting: Search meetings by date
									System.out.println("\n");
									System.out.println("*** Look up a Meeting -- Search by Date");
									date = ContactManagerUtilities.createDate();
									if (date == null) {
										break;//return to main menu
									}
									List<Meeting> foundMeetings = contactManager.getMeetingList(date);
									if (foundMeetings.isEmpty()) {
										System.out.println("No meetings found.");
										break;//return to main menu
									}
									else {
										ContactManagerUtilities.printMeetingList(foundMeetings);
									}
									break;//return to main menu
									
									
							case 2: // Look up Meeting: Search meetings by meeting ID
									System.out.println("\n");
									System.out.println("*** Look up a Meeting -- Search by Meeting ID");
									System.out.println("Please enter a meeting ID: ");
									entry = System.console().readLine();
									if (entry.equals("back")) {
										break;
									}
									id = ContactManagerUtilities.validateNumber(entry);								
									Meeting meeting = contactManager.getMeeting(id);
									if (meeting != null) {
										ContactManagerUtilities.printMeetingDetails(meeting);
										break;
									}
									else {
										System.out.println("No meetings matching that date found!");
										break;
									}
									
							
							case 3: // Look up Meeting: Search future meetings by contact
									System.out.println("\n");
									System.out.println("*** Look up a Meeting -- Search Future Meetings by Contact");
									int userSubChoice = ContactManagerUtilities.searchByContactOptions();
									
									switch (userSubChoice) {								
																					
										case 1: // Look up Meeting sub-menu: search by contact ID
												System.out.print("Please enter a contact's ID: ");
												entry = System.console().readLine();
												if (entry.equals("back")) {
													break;
												}
												id = ContactManagerUtilities.validateNumber(entry);
												contact = contactManager.getContact(id);
												if (contact == null) {
													break;
												}											
												List<Meeting> fMeetings = contactManager.getFutureMeetingList(contact);
												if (fMeetings.isEmpty()) {
													System.out.println("No meetings found.");
													break;
												}
												ContactManagerUtilities.printMeetingList(fMeetings);//print details of meetings
												break; 
												
												
										case 2: // Look up Meeting sub-menu: Search by contact name
												System.out.print("Please enter a contact's name: ");
												entry = System.console().readLine();
												if (entry.equals("back")) {
													break;
												}
												contacts = contactManager.getContacts(entry);
												if (contacts.isEmpty()) {
													System.out.println("No contacts found.");
													break;
												}
												System.out.println("Contacts matching this name: ");
												for (Contact c : contacts) {
													System.out.println(c.getName() + "\t" + "ID: " + c.getId());
												}
												System.out.print("Enter the ID of the contact you wish to select: ");
												entry = System.console().readLine();
												if (entry.equals("back")) {
													break;
												}
												id = ContactManagerUtilities.validateNumber(entry);
												contact = contactManager.getContact(id);
												if (contact == null) {
													break;
												}																			
												fMeetings = contactManager.getFutureMeetingList(contact);
												if (fMeetings.isEmpty()) {
													System.out.println("No meetings found.");
													break;
												}
												ContactManagerUtilities.printMeetingList(fMeetings);//print details of meetings
												break; 								
												
										
										case 3: // Look up Meeting sub-menu: Return to main menu
												break;
																			
									}
									break;
									
									
							
							case 4: // Look up Meeting: Search past meetings by contact
									System.out.println("\n");
									System.out.println("*** Look up a Meeting -- Search Past Meetings by Contact");
									userSubChoice = ContactManagerUtilities.searchByContactOptions();
									
									switch (userSubChoice) {											
										case 1: // Look up Meeting sub-menu: Search by contact ID
												System.out.print("Please enter a contact's ID: ");
												entry = System.console().readLine();
												if (entry.equals("back")) {
													break;//go back to main menu
												}
												id = ContactManagerUtilities.validateNumber(entry);
												contact = contactManager.getContact(id);
												if (contact == null) {
													break;//go back to main menu
												}											
												List<PastMeeting> pMeetings = contactManager.getPastMeetingList(contact);
												if (pMeetings.isEmpty()) {
													System.out.println("No meetings found.");
													break;//go back to main menu
												}
												ContactManagerUtilities.printMeetingList(pMeetings);//print details of meetings
												break;												 
												
												
										case 2: // Look up Meeting sub-menu: Search by contact name
												System.out.print("Please enter a contact's name: ");
												entry = System.console().readLine();
												if (entry.equals("back")) {
													break;
												}
												contacts = contactManager.getContacts(entry);
												if (contacts.isEmpty()) {
													System.out.println("No contacts found.");
													break;
												}
												System.out.println("Contacts matching this name: ");
												for (Contact c : contacts) {
													System.out.println(c.getName() + "\t" + "ID: " + c.getId());
												}
												System.out.print("Enter the ID of the contact you wish to select: ");
												entry = System.console().readLine();
												if (entry.equals("back")) {
													break;
												}
												id = ContactManagerUtilities.validateNumber(entry);
												contact = contactManager.getContact(id);	
												if (contact == null) {
													break;//go back to main menu
												}																		
												pMeetings = contactManager.getPastMeetingList(contact);
												if (pMeetings.isEmpty()) {
													System.out.println("No meetings found.");
													break;//go back to main menu
												}
												ContactManagerUtilities.printMeetingList(pMeetings);//print details of meetings
												break; 													
										
												
										case 3: // Return to main menu
												break;
									}
									break;
									
									
							case 5: // Look up Meeting: Search future meetings by ID
									System.out.println("\n");
									System.out.println("*** Look up a Meeting -- Search Past Meetings by ID");
									System.out.print("Enter meeting ID: ");
									entry = System.console().readLine();
									if (entry.equals("back")) {
										break;
									}
									id = ContactManagerUtilities.validateNumber(entry);
									PastMeeting pastMeeting = contactManager.getPastMeeting(id);
									if (pastMeeting == null) {
										break;
									}
									ContactManagerUtilities.printMeetingDetails(pastMeeting);
									break;
												
									
							case 6: // Look up Meeting: Search past meetings by ID
									System.out.println("\n");
									System.out.println("*** Look up a Meeting -- Search Future Meetings by ID");
									System.out.print("Enter meeting ID: ");
									entry = System.console().readLine();
									if (entry.equals("back")) {
										break;
									}
									id = ContactManagerUtilities.validateNumber(entry);
									FutureMeeting futureMeeting = contactManager.getFutureMeeting(id);
									if (futureMeeting == null) {
										break;
									}
									ContactManagerUtilities.printMeetingDetails(futureMeeting);
									break;							
							
							
							case 7: // Return to main menu
									break;
						
						}
						break;
						
						
						
				case 3: // Main menu: Create record of past meeting
						System.out.println("\n");
						System.out.println("*** Add a New Past Meeting ***");
						attendeeArray = ContactManagerUtilities.selectAttendees(contactManager.getContactList());						
						if (attendeeArray == null) {//occurs if user opts to quit, or if contactList is empty
							break;
						}
						attendees = contactManager.getContacts(attendeeArray);
						date = ContactManagerUtilities.createDateAndTime();
						if (date == null) {
							break;
						}
						System.out.println("Enter meeting notes: ");
						entry = System.console().readLine();
						if (entry.equals("back")) {
							break;
						}
						if (entry.length() == 0) { //ensures that if user enters nothing, null value is passed
							entry = null;
						}
						contactManager.addNewPastMeeting(attendees, date, entry);
						break;						
						
				
				case 4: //Main menu: Add notes to a meeting that has taken place
						System.out.println("\n");	
						System.out.println("*** Add Meeting Notes ***");
						System.out.print("Enter the ID of the meeting: ");
						entry = System.console().readLine();
						if (entry.equals("back")) { //option to quit
							break;
						}						
						id = ContactManagerUtilities.validateNumber(entry);
						System.out.println("Enter meeting notes:");
						entry = System.console().readLine();
						if (entry.equals("back")) { //option to quit
							break;
						}		
						if (entry.length() == 0) {
							entry = null; //so that an error is thrown if user enters nothing
						}
						contactManager.addMeetingNotes(id, entry);
						break;						
		
				
				case 5: //Main menu: Add a new contact
						System.out.println("\n");
						System.out.println("*** Add a New Contact ***");
						System.out.println("Please enter the contact's name:");
						entry = System.console().readLine();
						if (entry.equals("back")) {
							break; //option to quit
						}
						if (entry.length() == 0) {
							entry = null;//so that an error is thrown if field is empty
						}
						System.out.println("Please enter notes about the contact:");
						String notes = System.console().readLine();
						if (notes.equals("back")) {
							break; //option to quit
						}
						if (notes.length() == 0) {
							notes = null; //so that an error is thrown if field is empty
						}
						contactManager.addNewContact(entry, notes);
						break;
						
						
				case 6: //Main menu: Look up contact
						System.out.println("\n");
						System.out.println("*** Look up a Contact ***");
						int userSubChoice = ContactManagerUtilities.lookUpContactOptions();
						switch (userSubChoice) {
						
							case 1: // Look up Contact sub-menu: Look up by name
									System.out.println("Please enter a contact's name:");
									entry = System.console().readLine();
									if (entry.equals("back")) {
										break;
									}
									if (entry.length() == 0) {
										entry = null; //if user enters nothing, assign to null
									}
									contacts = contactManager.getContacts(entry);
									if (contacts.isEmpty()) {
										System.out.println("No contacts found.");
										break;
									}
									System.out.println("Contacts matching this name: ");
									for (Contact c : contacts) {
										System.out.println(c.getName() + "\t" + "ID: " + c.getId());
										System.out.println("Notes: " + c.getNotes() + "\n");
									
									}
									break;
									
							
							case 2: //Look up Contact sub-menu: Look up by ID
									System.out.println("Please enter a contact's ID:");
									entry = System.console().readLine();
									if (entry.equals("back")) {
										break;
									}
									id = ContactManagerUtilities.validateNumber(entry);
									contact = contactManager.getContact(id);
									if (contact == null) {
										break;
									}
									System.out.println(contact.getName() + "\t" + "ID: " + contact.getId());
									System.out.println("Notes: " + contact.getNotes() + "\n");
									break;
									
									
							case 3: //Look up Contact sub-menu: Display contact list
									ContactManagerUtilities.displayContactList(contactManager.getContactList());
									break;
																
							
							case 4: // Return to main menu
									break;
						}
						break;
				
				
						
				case 7: // Main menu: Save
						contactManager.flush();
						break;
						
						
				case 8: //Main menu: Save and quit				
						contactManager.flush();
						finished = true;
						System.out.println("\n" + "Closing...");
						break;
			}
		}
	}
	
}