
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
		boolean isEmpty = false; //these booleans facilitate display of pertinent error messages
		boolean falseContact = false;
		boolean falseDate = false;
		String unknownContacts = "The following contacts do not exist in your contact list: ";//for multiple unknowns
		Meeting futureMeeting = null;
		int meetingID = 0; //ID assigner in Meeting begins at 1, so this figure is just a default
		
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
			
			Calendar now = Calendar.getInstance(); 			
			if (date.before(now)) { //If user-specified date is in the past, throw exception
				falseDate = true;
			}
			if (isEmpty || falseContact || falseDate) {
				throw illegalArgEx;
			}
			
			futureMeeting = new FutureMeetingImpl(contacts, date);
			futureMeetings.add(futureMeeting);
			meetingID = futureMeeting.getId();
			System.out.println("Future meeting added.");
			
			return meetingID;				
		}
				
		catch (IllegalArgumentException illegalArgEx) {
			if (isEmpty == true) {
				System.out.println("Error: No contacts have been specified.");
			}
			if (falseContact == true) {
				System.out.println("Error: " + unknownContacts);
			} 
			if (falseDate == true) {
				System.out.println("Error: Invalid date. Please ensure the date and time are in the future.");
			}			 
		}
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
	
	
	/**
	* Sorts a list into chronological order
	*/
	public List<Meeting> sort(List<Meeting> list) {
		//Temporary variables used for re-ordering
		Meeting tempMeeting1 = null;
		Meeting tempMeeting2 = null;
		boolean sorted = true;
		
		for (int j = 0; j < list.size() - 1; j++) {
			tempMeeting1 = list.get(j);
			tempMeeting2 = list.get(j + 1);
			
			if (tempMeeting1.getDate().after(tempMeeting2.getDate())) {
				//Swaps elements over if first element has later date than second
				list.set(j, tempMeeting2);
				list.set(j + 1, tempMeeting1);
			}
		}
		
		for (int i = 0; i < list.size() - 1; i++) {//Loop that checks whether list is sorted
			if (list.get(i).getDate().after(list.get(i + 1).getDate())) {
				sorted = false;
			}			
		}
		
		if (!sorted) { //Recursively calls this method until the list is sorted
			list = sort(list);
		}
		
		return list;
	}
		
	
	
	
	public List<Meeting> getMeetingList(Calendar date) {
		List<Meeting> meetingList = new ArrayList<Meeting>();		
		
		for (Meeting m : pastMeetings) {
			Calendar meetingDate = m.getDate();
			if (meetingDate.get(Calendar.YEAR) == date.get(Calendar.YEAR)) {
				if (meetingDate.get(Calendar.MONTH) == date.get(Calendar.MONTH)) {
					if (meetingDate.get(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH)) {
						meetingList.add(m);// if there's a match, add meeting to the list
					}
				}
			}			
		}	
		
		for (Meeting m : futureMeetings) { //go through futureMeetings
			Calendar meetingDate = m.getDate();
			if (meetingDate.get(Calendar.YEAR) == date.get(Calendar.YEAR)) {
				if (meetingDate.get(Calendar.MONTH) == date.get(Calendar.MONTH)) {
					if (meetingDate.get(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH)) {
						meetingList.add(m);// if there's a match, add meeting to the list
					}
				}
			}			
		}
		
		meetingList = sort(meetingList);
		return meetingList;	//may be empty if no matches found
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
			
			for (int i = 0; i < meetingList.size(); i++) {//convert List<Meeting> to List<PastMeeting>
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
			
			Calendar now = Calendar.getInstance();			
			if (date.after(now)) { //If user-specified date is in the future, throw exception
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
			System.out.println("Past meeting added.");
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
					pastMeetingFound = true;
					if (text == null) { //throws exception before anything else can be executed
						throw nullPointerEx;
					}
					PastMeeting pm = (PastMeeting) m; //cast so that addNotes() can be called
					pm.addNotes(text); 				
					System.out.println("Notes for meeting ID No. " + id + " updated successfully.");
					return;
				} 
			}		
		}
		
		catch (NullPointerException ex) {
			System.out.println("Error: No notes have been specified!");
		}					
		
		if (!pastMeetingFound) { //Means future meeting is being converted			
			boolean containsMeeting = false;
			boolean futureDate = false;
			Calendar now = Calendar.getInstance();
			Meeting meeting = null;//Allows the meeting matching the id to be used throughout the method
			
			try {				
				Iterator<Meeting> iterator = futureMeetings.iterator(); 
				while (iterator.hasNext()) {
					meeting = iterator.next();
					if (meeting.getId() == id) {
						containsMeeting = true;
						break;
					}
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
				System.out.println("Meeting lists updated.");		
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
	
	
	
	
	public void addNewContact(String name, String notes) {
		try {
			if (name == null || notes == null) {
				throw nullPointerEx;
			}
			
			Contact contact = new ContactImpl(name);
			contact.addNotes(notes);
			contactList.add(contact);
			System.out.println("Contact added.");
		}
		
		catch (NullPointerException nex) {
			System.out.println("Error: Please ensure that BOTH the NAME and NOTES fields are filled in.");			
		}
	}
	
	
	
	
	public Set<Contact> getContacts(int... ids) {
		Set<Contact> idMatches = new HashSet<Contact>();
		int id = 0;
		String idString = "";//to facilitate an error message that lists all invalid IDs
		boolean found;
		
		try { 
			for (int i = 0; i < ids.length; i++) {//boolean needs to be reset to false for each iteration 
			//otherwise it will stay true after one id is matched!
				found = false;
				id = ids[i];
				for (Contact c : contactList) {
					if (c.getId() == id) {
						idMatches.add(c);
						found = true;
					}
				}				
				
				if (found == false) {
					idString = idString + id + "\n";//add unknown ID to idString for display later
				}		
			}
			
			if (idString.length() > 0) { //if the idString contains something
					throw illegalArgEx;	
			}
									
			return idMatches;
		}
		
		catch (IllegalArgumentException ex) {
			System.out.println("Note: The following IDs were not found and haven't " +
				"been added to the attendee list:" + "\n" + idString);
		}
		
		return idMatches;
	}
	
	
	
	public Contact getContact(int id) {
		for (Contact c : contactList) {//matches against list of contacts
			if (c.getId() == id) {
				return c;
			}
		}
		
		System.out.println("ID not found!");
		return null; //returns null if ID isn't found
	}
		
	
	
	public Set<Contact> getContacts(String name) {
		Set<Contact> contactSet =  new HashSet<Contact>();
		Contact contact = null;
		
		try {
			if (name == null) {
				throw nullPointerEx;
			}
				
			for (Contact c : contactList) {
				if (c.getName().equals(name)) {
					contactSet.add(c);
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
      		
      		for (Contact c : contactList) { //write contacts to file
      			oos.writeObject(c);
      		}
      		
      		for (Meeting m : pastMeetings) { //write past meetings to file
      			oos.writeObject(m);
      		}
      		
      		for (Meeting m : futureMeetings) { //write future meetings to file
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
      		ex.printStackTrace();
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
					IdStore ids = (IdStore) obj;
					ContactImpl.restoreIdAssigner(ids.getContactIdAssigner());//restores idAssigner to previous state
					MeetingImpl.restoreIdAssigner(ids.getMeetingIdAssigner());
				}
				
				if (obj instanceof Contact) {
					Contact contact = (Contact) obj;
					contactList.add(contact);
				}
				
				if (obj instanceof FutureMeeting) {
					Meeting meeting = (FutureMeeting) obj;
					futureMeetings.add(meeting);
				}
				
				if (obj instanceof PastMeeting) {
					Meeting meeting = (PastMeeting) obj;
					pastMeetings.add(meeting);
				}
			}
			
			ois.close();
		}
		
		catch (EOFException ex) { //Exception thrown when end of file reached
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
	
	
	
	public Set<Contact> getContactList() {//required by ContactManagerRunner
		return this.contactList;
	}	
	
}


