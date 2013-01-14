

import java.util.*;
import java.io.*;


public class ContactManagerImpl implements ContactManager { 
	private IllegalArgumentException illegalArgEx = new IllegalArgumentException();
	private NullPointerException nullPointerEx = new NullPointerException();
	private IllegalStateException illegalStateEx = new IllegalStateException();
	private Set<Contact> contactList = new HashSet<Contact>(); //contacts added to this via addContact()	
	private Set<Meeting> pastMeetings = new HashSet<Meeting>();//list of past meetings
	private Set<Meeting> futureMeetings = new HashSet<Meeting>();//list of future meetings
	

	
	public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
		boolean isEmpty = false; //these booleans facilitate display of pertinent error message
		boolean falseContact = false;
		boolean falseDate = false;
		String unknownContacts = "The following contacts do not exist in your contact list: ";//for multiple unknowns
		Meeting futureMeeting = null;
		
		try {
			if (contacts.isEmpty()) {
				isEmpty = true;
			}
			//check that contacts are known/existent against contactList set
			for (Contact c : contacts) {
				if (!contactList.contains(c)) {
					falseContact = true;
					unknownContacts = unknownContacts + "\n" + c.getName();
				}
			}
			
			Calendar now = Calendar.getInstance(); //Test this works with time			
			if (date.before(now)) { //If user-specified date is in the past, throw exception
				falseDate = true;
			}
			if (isEmpty || falseContact || falseDate) {
				throw illegalArgEx;
			}				
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
		futureMeetings.add(futureMeeting);
		int meetingID = futureMeeting.getId();
		return meetingID;
	}
	
	
	
	
	public PastMeeting getPastMeeting(int id) {
		try {
			for (Meeting m : futureMeetings) {
				if (m.getId() == id) {
					throw illegalArgEx;//if specified id matches id of future meeting
				}
			}			
		}
		
		catch (IllegalArgumentException ex) {
			System.out.print("Error: The meeting with this ID has not taken place yet!");
			return null;
			//confirm returning null is best course of action - currently booted back to main
		}
		
		for (Meeting m : pastMeetings) {
			if (m.getId() == id) {
				PastMeeting pastMeeting = (PastMeeting) m;
				return pastMeeting;
			}
		}		
		return null; //returns null if no meeting is found
	}
	
	
	
	
	public FutureMeeting getFutureMeeting(int id) {
		try {
			for (Meeting m : pastMeetings) { //checks id against past meetings
				if (m.getId() == id) {
					throw illegalArgEx; //throws exception if specified id matches past meeting
				}
			}			
		}
		
		catch (IllegalArgumentException ex) {
			System.out.print("Error: The meeting with this ID has already taken place!");
			return null;
			//what action to take? - safest is to go back to main menu
		}
		
		for (Meeting m : futureMeetings) {
			if (m.getId() == id) {
				FutureMeeting futureMeeting = (FutureMeeting) m;
				return futureMeeting;
			}
		}		
		return null;//if no matching meeting is found
	}
	
	
	
	
	public Meeting getMeeting(int id) {
		for (Meeting m : pastMeetings) {// searches pastMeetings for match
			if (m.getId() == id) {
				return m;
			}
		}
		
		for (Meeting m : futureMeetings) { //searches futureMeetings for match
			if (m.getId() == id) {
				return m;
			}
		}
		return null; //if no matches are found		
	}
	
	
	
	
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
			
			list = sort(list);
			return list;
		}
		
		catch (IllegalArgumentException ex) {
			System.out.println("Error: The specified contact doesn't exist!");
		}
		
	return list; //may be empty if no meetings found
	//won't be any duplicates because a meeting cannot belong to pastMeetings AND futureMeetings
	}
	
	
	
	//ADAPT FOR INCLUSION OF TIME!!!!!
	/**
	* Sorts a list into chronological order
	*/
	public List<Meeting> sort(List<Meeting> list) {
		//temporary variables used for re-ordering
		Meeting tempMeeting1 = null;
		Meeting tempMeeting2 = null;
		boolean sorted = true;
		
		for (int j = 0; j < list.size() - 1; j++) {
			tempMeeting1 = list.get(j);
			tempMeeting2 = list.get(j + 1);
			
			if (tempMeeting1.getDate().after(tempMeeting2.getDate())) {
				//swaps elements over if first element has later date than second
				list.set(j, tempMeeting2); //set() prevents list growing when rearranging elements
				list.set(j + 1, tempMeeting1);
			}
		}
		
		for (int i = 0; i < list.size() - 1; i++) { //loop that checks whether list is sorted
			if (list.get(i).getDate().after(list.get(i + 1).getDate())) {
				sorted = false;
			}			
		}
		
		if (!sorted) { //recursively calls this method until the list is sorted
			list = sort(list);
		}
		
		return list;
	}
		
	
	
	
	public List<Meeting> getMeetingList(Calendar date) {
		List<Meeting> meetingList = new ArrayList<Meeting>();		
		
		for (Meeting m : pastMeetings) { //go through pastMeetings
			if (m.getDate().equals(date)) {
				meetingList.add(m); //if there's a match, add it to the list
			}
		}	
		
		for (Meeting m : futureMeetings) { //go through futureMeetings
			if (m.getDate().equals(date)) {
				meetingList.add(m);
			}
		}
		
		meetingList = sort(meetingList);
		return meetingList;	//may be empty if no matches found
		//won't be any duplicates because a meeting cannot belong to pastMeetings AND futureMeetings
	}
	
	
	
	
	public List<PastMeeting> getPastMeetingList(Contact contact) {
		List<Meeting> meetingList = new ArrayList<Meeting>();//2 lists to deal with casting (return type List<PastMeeting>)
		List<PastMeeting> pastMeetingList = new ArrayList<PastMeeting>();
		
		try {
			if (!contactList.contains(contact)) {
				throw illegalArgEx;
			}
			
			for (Meeting m : pastMeetings) {
				if (m.getContacts().contains(contact)) {
					meetingList.add(m);
				}
			}
						
			meetingList = sort(meetingList);	
			
			for (int i = 0; i < meetingList.size(); i++) { //convert List<Meeting> to List<PastMeeting>
				Meeting m = meetingList.get(i);
				PastMeeting pm = (PastMeeting) m;
				pastMeetingList.add(pm);
			}
				
			return pastMeetingList;				
		}
		
		catch (IllegalArgumentException ex) {
			System.out.println("Error: The specified contact doesn't exist.");
		}
		
		return pastMeetingList;//may be empty if no matches found
	}
	
	
	
	
	//TIME!!!	
	public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {
		boolean emptyContacts = false;//to allow simultaneous error correction for user
		boolean nullContacts = false;
		boolean nullDate = false;
		boolean nullText = false;
		boolean falseContact = false;	
		boolean falseDate = false;
		String unknownContacts = "The following contacts are not on your contact list: ";	
		
		try {
			if (contacts.isEmpty()) {
				emptyContacts = true;
			}
			
			Calendar now = Calendar.getInstance(); //Test this works with time			
			if (date.after(now)) { //If user-specified date is in the past, throw exception
				falseDate = true;
			}
			
			for (Contact c : contacts) { //throws exception if contact doesn't exist
				if (!contactList.contains(c)) {
					falseContact = true;
					unknownContacts += "\n" + c.getName();
				}
			}			
			
			if (contacts == null) {
				nullContacts = true;
			}
			
			if (date == null) {
				nullDate = true;
			}
			
			if (text == null) {
				nullText = true;
			} 
			
			if (emptyContacts || falseContact || falseDate) {
				throw illegalArgEx;
			}
			
			if (nullContacts || nullDate || nullText) {
				throw nullPointerEx;
			}
			
			Meeting pastMeeting = new PastMeetingImpl(contacts, date, text);
			pastMeetings.add(pastMeeting);
		}
		
		catch (IllegalArgumentException ex) {
			if (falseDate) {
				System.out.println("Error: Ensure the date is not in the future!");
			}
			
			if (emptyContacts) {
				System.out.println("Error: No contacts specified!");
			}
			
			if (falseContact) {
				System.out.println("Error: " + unknownContacts);
			}
		}
		
		catch (NullPointerException nex) {
			if (nullText) {
				System.out.println("Error: No meeting notes specified!");
			}
			
			if (nullContacts) {
				System.out.println("Error: No contacts specified!");
			}
			
			if (nullDate) {
				System.out.println("Error: No date specified!");
			}
		}
	}
		
	
	
	
	public void addMeetingNotes(int id, String text) {
		boolean pastMeetingFound = false;//to decide if program should look through futureMeetings if no matching meeting
		//is found in pastMeetings.
		
		try {	
			for (Meeting m : pastMeetings) {
				if (m.getId() == id) {
					pastMeetingFound = true; //prevents unnecessary conversion of past meeting to past meeting
					if (text == null) { //throws exception before anything else can be executed
						throw nullPointerEx;
					}
					PastMeeting pm = (PastMeeting) m; //cast so that addNotes() can be called
					pm.addNotes(text); 				
					System.out.println("Notes for meeting ID No. " + id + " updated successfully.");
				}
				break; 
			}			
		}
		
		catch (NullPointerException ex) {
			System.out.println("Error: No notes have been specified!");
		}					
		
		if (!pastMeetingFound) { //means future meeting is being converted			
			boolean containsMeeting = false;
			boolean futureDate = false;
			Calendar now = Calendar.getInstance();
			Meeting meeting = null;//to allow the meeting matching the id to be used throughout the method
			
			try {				
				Iterator<Meeting> iterator = futureMeetings.iterator(); 
				while (iterator.hasNext()) {
					meeting = iterator.next();
					if (meeting.getId() == id) {
						containsMeeting = true;
					}
					break;
				}				
				System.out.println("Meeting ID: " + meeting.getId() + " is being updated...");
				
				if (meeting.getDate().after(now)) {
					throw illegalStateEx;
				}			
				if (text == null) {
					throw nullPointerEx;
				}
				if (!containsMeeting) {
					throw illegalArgEx;
				}				
				
				Meeting pastMeeting = new PastMeetingImpl(meeting.getContacts(), meeting.getDate(), text, meeting.getId());
				pastMeetings.add(pastMeeting);
				futureMeetings.remove(meeting);			
			}
			
			catch (IllegalArgumentException aEx) {
				System.out.println("Error: No meeting with that ID exists!");				
			}
			
			catch (IllegalStateException sEx) {
				System.out.println("Error: The meeting with this ID has not taken place yet!");
			}
			
			catch (NullPointerException pEx) {
				System.out.println("Error: No notes have been specified!");
			}
		}
	}
	
	/** 
	* Create a new contact with the specified name and notes. 
	*
	* @param name the name of the contact. 
	* @param notes notes to be added about the contact.
	* @throws NullPointerException if the name or the notes are null
	*/ 
	public void addNewContact(String name, String notes) {
		try {
			if (name == null || notes == null) {
				throw nullPointerEx;
			}
			Contact contact = new ContactImpl(name);
			contact.addNotes(notes);
			contactList.add(contact);
		}
		catch (NullPointerException nex) {
			System.out.println("Error: Please ensure that BOTH the NAME and NOTES fields are filled in.");
			//option to reenter a good idea
			//As it stands, user is thrown back to main menu
			//works here, as return is void -- need to see what happens in other methods
		}
	}
	
	/** 
	* Returns a list containing the contacts that correspond to the IDs
	* 
	* @param ids an arbitrary number of contact IDs 
	* @return a list containing the contacts that correspond to the IDs. 
	* @throws IllegalArgumentException if any of the IDs does not correspond to a real contact 
	*/
	public Set<Contact> getContacts(int... ids) {
		Set<Contact> idMatches = new HashSet<Contact>();
		int id = 0;
		String idString = "";//to facilitate an error message that lists all invalid IDs
		boolean found;
		try { 
			for (int i = 0; i < ids.length; i++) {//boolean needs to be reset to false here for each iteration 
			//otherwise it will stay true after one id is matched!
				found = false;
				id = ids[i];
				Contact contact = null;
				Iterator<Contact> iterator = contactList.iterator();
				while (iterator.hasNext()) {
					contact = iterator.next();
					if (contact.getId() == id) {
						idMatches.add(contact);
						found = true;
					}				
				}
				if (found == false) {
					idString = idString + id + "\n";
					//throw illegalArgEx;
				}		
			}
			if (idString.length() > 0) {
					throw illegalArgEx;	
			}						
			return idMatches;
		}
		catch (IllegalArgumentException ex) {
			System.out.println("Note: The following IDs were not found and haven't " +
				"been added to the attendee list:" + "\n" + idString);
			//user's next option? Return to main?
		}
		return idMatches;
	}
	
	/*
	* Returns a single contact matching an ID.
	*/
	public Contact getContact(int id) {
		for (Contact c : contactList) {//matches against list of contacts
			if (c.getId() == id) {
				return c;
			}
		}
		System.out.println("ID not found!");
		return null;
	}
		
	
	/** 
	* Returns a list with the contacts whose name contains that string. 
	* 
	* @param name the string to search for 
	* @return a list with the contacts whose name contains that string. 
	* @throws NullPointerException if the parameter is null 
	*/
	public Set<Contact> getContacts(String name) {
		Set<Contact> contactSet =  new HashSet<Contact>();
		Contact contact = null;
		try {
			if (name == null) {
				throw nullPointerEx;
			}
			Iterator<Contact> iterator = contactList.iterator();
			while (iterator.hasNext()) {
				contact = iterator.next();
				if (contact.getName().equals(name)) {
					contactSet.add(contact);
				}
			}	
		}
		catch (NullPointerException nex) {
			System.out.println("Error: Please ensure that you enter a name.");
			System.out.println("Contact name: ");
			String name2 = System.console().readLine();
			if (name2.equals("back")) {
				return null;//allow user to exit rather than get stuck
			}
			return getContacts(name2);
		}
		return contactSet;
	}

	
	
	
	public void flush() {
		IdStore ids = new IdStoreImpl();
		ids.saveContactIdAssigner(ContactImpl.getIdAssigner());
		ids.saveMeetingIdAssigner(MeetingImpl.getIdAssigner());		
		try {
 			FileOutputStream fos = new FileOutputStream("contacts.txt");
 			System.out.println("Saving data...");
      		ObjectOutputStream oos = new ObjectOutputStream(fos);      	
      		oos.writeObject(ids);//saves IdStore object containing idAssigners
      		Iterator<Contact> contactIterator = contactList.iterator();
      		while (contactIterator.hasNext()) {//write contents of contactList to file
      			Contact c = contactIterator.next();
      			oos.writeObject(c);
      		}
      		Iterator<Meeting> iteratorPM = pastMeetings.iterator();
      		while (iteratorPM.hasNext()) {//writes contents of pastMeetings to file
      			Meeting m = iteratorPM.next();
      			oos.writeObject(m);
      		}
      		Iterator<Meeting> iteratorFM = futureMeetings.iterator();
      		while (iteratorFM.hasNext()) {//writes contents of futureMeetings to file
      			Meeting m = iteratorFM.next();
      			oos.writeObject(m);
      		}
      		oos.close();
      		System.out.println("Saved.");
      	}
      	catch (FileNotFoundException ex) {
      		System.out.println("Creating contacts.txt file for data storage...");
      		File contactsTxt = new File("./contacts.txt");
      		flush();
      	}
      	catch (IOException ex) {
      		ex.printStackTrace();//need to be more explicit?
      	} 	
	}
	
	
	
	//Loads data from file upon opening program
	public void loadData() {
		System.out.println("Loading data from file...");
		try {
			File contactsFile = new File("./contacts.txt");
			if (contactsFile.length() == 0) {
				System.out.println("No saved data found.");
				return;
			}
			FileInputStream fis = new FileInputStream("contacts.txt");
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object obj = null;
			while ((obj = ois.readObject()) != null) { //read to end of file
				if (obj instanceof IdStore) {
					IdStore ids = (IdStoreImpl) obj;
					ContactImpl.restoreIdAssigner(ids.getContactIdAssigner());
					MeetingImpl.restoreIdAssigner(ids.getMeetingIdAssigner());
				}
				if (obj instanceof Contact) {
					Contact contact = (ContactImpl) obj;
					contactList.add(contact);
				}
				if (obj instanceof FutureMeeting) {
					Meeting meeting = (FutureMeetingImpl) obj;
					futureMeetings.add(meeting);
				}
				if (obj instanceof PastMeeting) {
					Meeting meeting = (PastMeetingImpl) obj;
					pastMeetings.add(meeting);
				}
			}
			ois.close();
		}
		catch (EOFException ex) {
			System.out.println("Data from previous session loaded.");
		}
		catch (FileNotFoundException ex) {
      		System.out.println("File not found! Please ensure contacts.txt is in the same directory.");
      	}
		catch (IOException ex) {
			ex.printStackTrace();
		}
		catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}		
	}				
		
		
	
	
	public static void main(String[] args) {
	
		ContactManagerImpl cm = new ContactManagerImpl();
		cm.launch();
		
	}
	
	private void launch() {
	
		ContactManagerUtilities.displayWelcome();
		loadData(); //load data from previous sessions (if there is any)	
		
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
						System.out.println("*** ADD A FUTURE MEETING");
						attendeeArray = ContactManagerUtilities.selectAttendees(contactList);						
						if (attendeeArray == null) {//occurs if user opts to quit, or if contactList is empty
							break;//return to main menu
						}
						attendees = getContacts(attendeeArray);
						date = ContactManagerUtilities.createDate();
						if (date == null) {
							break;//return to main menu
						}
						this.addFutureMeeting(attendees, date);
						break;//return to main menu 
						
						
				case 2: // Main menu: Look up meeting
						System.out.println("\n");
						System.out.println("*** LOOK UP A MEETING");
						int userChoice = ContactManagerUtilities.lookUpMeetingOptions();
						switch (userChoice) {									
									
							case 1: // Look up Meeting: Search meetings by date
									System.out.println("\n");
									System.out.println("*** LOOK UP MEETING -- Search by Date");
									date = ContactManagerUtilities.createDate();
									if (date == null) {
										break;//return to main menu
									}
									List<Meeting> foundMeetings = getMeetingList(date);
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
									System.out.println("*** LOOK UP MEETING -- Search by Meeting ID");
									System.out.println("Please enter a meeting ID: ");
									entry = System.console().readLine();
									if (entry.equals("back")) {
										break;
									}
									id = ContactManagerUtilities.validateNumber(entry);								
									Meeting meeting = getMeeting(id);
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
									System.out.println("*** LOOK UP MEETING -- Search Future Meetings by Contact");
									int userSubChoice = ContactManagerUtilities.searchByContactOptions();
									
									switch (userSubChoice) {								
																					
										case 1: // Look up Meeting sub-menu: search by contact ID
												System.out.print("Please enter a contact's ID: ");
												entry = System.console().readLine();
												if (entry.equals("back")) {
													break;
												}
												id = ContactManagerUtilities.validateNumber(entry);
												contact = getContact(id);
												if (contact == null) {
													break;
												}											
												List<Meeting> fMeetings = getFutureMeetingList(contact);
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
												contacts = getContacts(entry);
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
												contact = getContact(id);
												if (contact == null) {
													break;
												}																			
												fMeetings = getFutureMeetingList(contact);
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
									System.out.println("*** LOOK UP MEETING -- Search Past Meetings by Contact");
									userSubChoice = ContactManagerUtilities.searchByContactOptions();
									
									switch (userSubChoice) {											
										case 1: // Look up Meeting sub-menu: Search by contact ID
												System.out.print("Please enter a contact's ID: ");
												entry = System.console().readLine();
												if (entry.equals("back")) {
													break;//go back to main menu
												}
												id = ContactManagerUtilities.validateNumber(entry);
												contact = getContact(id);
												if (contact == null) {
													break;//go back to main menu
												}											
												List<PastMeeting> pMeetings = getPastMeetingList(contact);
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
												contacts = getContacts(entry);
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
												contact = getContact(id);	
												if (contact == null) {
													break;//go back to main menu
												}																		
												pMeetings = getPastMeetingList(contact);
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
									System.out.println("*** LOOK UP MEETING -- Search Past Meetings by ID");
									System.out.print("Enter meeting ID: ");
									entry = System.console().readLine();
									if (entry.equals("back")) {
										break;
									}
								    id = ContactManagerUtilities.validateNumber(entry);
								    PastMeeting pastMeeting = this.getPastMeeting(id);
								    if (pastMeeting == null) {
								    	break;
								    }
								    ContactManagerUtilities.printMeetingDetails(pastMeeting);
								    break;
												
									
							case 6: // Look up Meeting: Search past meetings by ID
									System.out.println("\n");
									System.out.println("*** LOOK UP MEETING -- Search Future Meetings by ID");
									System.out.print("Enter meeting ID: ");
									entry = System.console().readLine();
									if (entry.equals("back")) {
										break;
									}
								    id = ContactManagerUtilities.validateNumber(entry);
									FutureMeeting futureMeeting = getFutureMeeting(id);
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
						System.out.println("*** ADD NEW PAST MEETING");
						attendeeArray = ContactManagerUtilities.selectAttendees(contactList);						
						if (attendeeArray == null) {//occurs if user opts to quit, or if contactList is empty
							break;
						}
						attendees = getContacts(attendeeArray);
						date = ContactManagerUtilities.createDate();
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
						this.addNewPastMeeting(attendees, date, entry);
						break;						
						
				
				case 4: //Main menu: Add notes to a meeting that has taken place
						System.out.println("\n");	
						System.out.println("*** ADD MEETING NOTES");
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
						this.addMeetingNotes(id, entry);
						break;						

				
				case 5: //Main menu: Add a new contact
						System.out.println("\n");
						System.out.println("*** ADD NEW CONTACT");
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
						this.addNewContact(entry, notes);
						break;
						
						
				case 6: //Main menu: Look up contact
						System.out.println("\n");
						System.out.println("*** LOOK UP CONTACT");
						int userSubChoice = ContactManagerUtilities.lookUpContactOptions();
						switch (userSubChoice) {
						
							case 1: // Look up Contact sub-menu: Look up by name
									System.out.println("Please enter a contact's name:");
									entry = System.console().readLine();
									if (entry.equals("back") {
										break;
									}
									if (entry.length() == 0) {
										entry = null; //if user enters nothing, assign to null
									}
									contacts = getContacts(entry);
									if (contacts.isEmpty()) {
										System.out.println("No contacts found.");
										break;
									}
									System.out.println("Contacts matching this name: ");
									for (Contact c : contacts) {
										System.out.println(c.getName() + "\t" + "ID: " + c.getId());
									
									}
									break;
									
							
							case 2: //Look up Contact sub-menu: Look up by ID
									System.out.println("Please enter a contact's ID:");
									entry = System.console().readLine();
									if (entry.equals("back")) {
										break;
									}
									id = ContactManagerUtilities.validateNumber(entry);
									contact = getContact(id);
									if (contact == null) {
										break;
									}
									System.out.println(contact.getName() + "\t" + "ID: " + contact.getId());
									System.out.println("Notes: " + contact.getNotes() + "\n");
									break;					
							
							
							case 3: // Return to main menu
									break;
						}
						break;
				
				
						
				case 7: // Main menu: Save
						flush();
						break;
						
						
				case 8: //Main menu: Save and quit				
						flush();
						finished = true;
						System.out.println("\n" + "Closing...");
						break;
			}
		}
	}
}

//WHERE POSSIBLE, SIMPLIFY ITERATOR TO FOR EACH LOOP

//CORRECT DATES: allow any year to be entered

//MEETING TIMES

//ADD "CREATED" NOTE to end of each addNew method, if successful
		
//ADD COMMENTS TO EACH CASE FOR CLARITY	

//DISPLAY CONTACT LIST? - in submenu of look up contact

//TIDY UP LINE SPACING

//DEAL WITH FLOW AFTER EXCEPTIONS IN METHODS OF THIS CLASS

//MAKE CODE MORE LEGIBLE - SPACE

//REORGANIZE UTILITY CLASS

//UTILITY INTERFACE - UTILITIES

//JAVA DOCS
	
		
		
		
		
		
	
		
	
	
	
	
	








//ask user for dates in specific format, which can then be converted to create a new Calendar
//make sure that if wrong format is entered, you throw an exception.

//update dates to include time?



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

	
	//if returned list is empty, write empty? in main: if list.isEmpty(), print <empty> for
	//user clarity
	
	//when users specify a date, should they also include a time?
			
//how does before/after affect dates which are the same?
//contains -- may have to do this in more detail with an iterator, as the naming of 
//Contact variables may not allow for contactList.contains(contact); new contacts will probably
//be just called contact each time they are created, with their name and id the only things to 
//identify them by.

//initialise notes variable as null in launch so that if user enters nothing, relevant
//method is still found




//when saved, contents of file will be overwritten - alert user of this
















