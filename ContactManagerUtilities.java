
import java.util.regex.*;
import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.HashSet;
import java.util.Set;
import java.util.Calendar;
import java.util.GregorianCalendar;


/**
* An auxiliary class that handles much of the user interface of ContactManager, 
* taking and validating user entries.
*/
public class ContactManagerUtilities {
	private static IllegalArgumentException illegalArgEx = new IllegalArgumentException();
	
	private ContactManagerUtilities() {
		//Disallows instantiation
	}
	

	/**
	* Displays welcome banner on screen.
	*/
	public static void displayWelcome() {
		System.out.println("\n");
		System.out.println("*************************************************************");
		System.out.println("*                    CONTACT MANAGER                        *");
		System.out.println("*************************************************************" + "\n");
	}
	
	
	
	/**
	* Displays main menu of numerical choices. 
	*
	* @return an integer for use in switch statement of ContactManagerRunner class.
	*/
	public static int chooseMainMenuOption() {
		System.out.println();
		System.out.println();
		System.out.println("                  ***** Main Menu *****");
		System.out.println("1. Add a future meeting            " + "\t" + " 2. Look up meeting");
		System.out.println("3. Create record of a past meeting " + "\t" + " 4. Add notes to a meeting that has taken place");
		System.out.println("5. Add a new contact               " + "\t" + " 6. Look up contact");
		System.out.println("7. Save data to disk               " + "\t" + " 8. Save and exit.");
		System.out.println("(Exit back to this menu at any point by entering \"back\")");
		System.out.print("Select option: ");
		
		int selection = validateOption(1, 8);		
		return selection;	
	}
	
	
	
	/**
	* A method that validates whether an integer is within a range of acceptable values
	* for use with numerical option menus. I.e. if the range of menu choices is 1-8, this
	* method ensures values outside of this range are caught as exceptions.
	*
	* @param min the minimum value acceptable.
	* @param max the maximum value acceptable.
	* @return a user-entered number representing a numerical option.
	* @throws NumberFormatException if a non-integer is entered.
	* @throws IllegalArgumentException if a number outside of the specified
	* range is entered.
	*/
	public static int validateOption(int min, int max) {
		int selection;
		
		try {
			selection = Integer.parseInt(System.console().readLine());
			if (selection < min || selection > max) {
		 		throw illegalArgEx;
		 	}		 	
		 }	
		 
		 catch (NumberFormatException ex) {
		 	System.out.println("Please enter a number that corresponds to an option on the menu.");
		 	return validateOption(min, max);
		 }	
		 
		 catch (IllegalArgumentException ex) {
		 	System.out.println("Invalid option. Please make a selection from the menu.");
		 	return validateOption(min, max);		 	
		 }
		 
		 return selection;
	}
	
		

	/**
	* A method that allows the user to select multiple contacts by ID number. These
	* numbers are added to an array, which is returned to the ContactManagerRunner class
	* for use with ContactManager's getContacts(int... ids) method. 
	*
	* @param contactList set, passed from ContactManager.
	* @return array of integers representing user-selected contact IDs.
	* @throws IllegalArgumentException if the IDs aren't entered in a comma-separated
	* String (although there is some leniency with regard to comma spacing).
	*/
	public static int[] selectAttendees(Set<Contact> contactList) {
		
		List<Integer> attendeeList = new ArrayList<Integer>();
		int[] attendees;
		
		if (contactList.isEmpty()) {
			System.out.println("<Empty> You will need to add contacts to your contact list before creating meetings.");
			return null;//Sends the user back to the main menu via a break statement in ContactManagerRunner
		}		
		
		displayContactList(contactList);
		
		System.out.println("Please enter the IDs of the attendees, separated" +
		" by a comma e.g. 1, 4, 5. Finish by pressing RETURN.");
		
		try{
			String userEntry = System.console().readLine();
			if (userEntry.equals("back")) {
				return null;//Allows user to cancel and return to main menu
			}
			
			boolean verified = validateCommaString(userEntry);//Determines whether input is valid
			
			if (!verified) {
				throw illegalArgEx;
			}
			
			Scanner sc = new Scanner(userEntry);//If input is valid, creates an arrayList with the user input numbers
			Pattern delimiterPattern = Pattern.compile("[\\s]?[,][\\s]?");
			sc.useDelimiter(delimiterPattern);
			
			do {
				int selection = sc.nextInt();
				attendeeList.add(selection);
			} while (sc.hasNextInt());
			
			attendees = new int[attendeeList.size()];//Converts to array for use in ContactManager's getContacts()
			for (int i = 0; i < attendeeList.size(); i++) {
				attendees[i] = attendeeList.get(i);
			}
		}
		
		catch (IllegalArgumentException ex) {
			System.out.println("Error! Please enter the contact ID numbers separated by a comma " +
			"i.e. 1, 2, 3 or 1,2,3 and finish by pressing RETURN.");
			return selectAttendees(contactList);
		}
		
		return attendees;	
	}
	
	
	
	/**
	* A method to validate a user-entered String of digits separated by commas and
	* spaces. Some degree of leniency with regard to spacing to ensure user-friendliness.
	* I.e. 1,2,3 , 4, 5,6, 7 is considered valid.
	*
	* @param userEntry a user-entered String passed from selectAttendees().
	* @return true or false (String is valid or invalid respectively).
	*/	
	public static boolean validateCommaString(String userEntry) {
		Pattern pattern = Pattern.compile("([0-9][0-9]*[\\s]?[,][\\s]?)*[0-9][0-9]*");
		Matcher m = pattern.matcher(userEntry);//Match given input against pattern
		boolean verified = m.matches();//Returns true if input matches, false otherwise
		
		if (verified) {
			return true;
		}
		
		return false;
	}
	
	
	
	/**
	* Creates a Calendar date and time from user input. Rules out invalid dates such as
	* 29.02 outside of a leap year, and ensures that months with only 30 days cannot
	* be assigned a date of 31. Rules out invalid times such as 25:79.
	*
	* @return Calendar object with user-determined date and time.
	* @throws IllegalArgumentException if the date or time is invalid, in terms of format
	* and value (i.e. time not in 24-hour hh:mm format, or outside of valid range).	
	*/
	public static Calendar createDateAndTime() {
		Calendar date = null;		
		int day;
		int month;
		int year;
		int hour;
		int minutes;
		
		int[] dateArray = new int[3];//Stores values representing date
		boolean febOverflow = false;//Booleans to facilitate error messages below
		boolean monthOverflow = false;
		boolean badDateFormat = false;		
		
		int[] timeArray = new int[2];//To store values representing time
		boolean badTimeFormat = false;		
		
		try {
			System.out.println("Please enter the date of the meeting in dd.mm.yyyy format: ");
			String userEntry = System.console().readLine();
			
			if (userEntry.equals("back")) {
				return null;//Allows user to cancel and return to main menu 
			}
			
			boolean dateVerified = validateDateEntry(userEntry);//Determines whether input is valid
			if (!dateVerified) {
				badDateFormat = true;
				throw illegalArgEx;
			}
			
			Scanner sc = new Scanner(userEntry);//If input is valid, creates a date
			Pattern delimiterPattern = Pattern.compile("[\\.]");
			sc.useDelimiter(delimiterPattern);
			
			for (int i = 0; i < 3; i++) {
				dateArray[i] = sc.nextInt();
			}
			
			day = dateArray[0];
			month = dateArray[1] - 1;//Calendar interprets January as 0
			year = dateArray[2];			
			
			if (month == 1 && day > 28) {
				if ((year % 4 == 0) && (year % 100 != 0)) {
					//this is ok - it's a leap year
				}
				if ((year % 4 == 0) && (year % 100 == 0) && (year % 400 == 0)) {
					//this is ok - it's a leap year
				}
				else {
					febOverflow = true;
					throw illegalArgEx;//Feb only has 29 days in leap year
				}
			}
			if (month % 2 == 1) {//Disallows day having value of 31 when month has only 30 days
				if (day > 30) {
					monthOverflow = true;
					throw illegalArgEx;
				}
			}
			
			//Time verification
			System.out.println("Please enter the time of the meeting in 24-hour format (hh:mm) : ");
			userEntry = System.console().readLine();			
			
			if (userEntry.equals("back")) {
				return null;//Allows user to cancel and return to main menu 
			}			
			
			boolean timeVerified = validateTimeEntry(userEntry);//Determines whether input is valid
			if (!timeVerified) {
				badTimeFormat = true;
				throw illegalArgEx;
			}			
			
			Scanner scTime = new Scanner(userEntry);//If input is valid, creates a time
			delimiterPattern = Pattern.compile("([\\:]|[\\.])");
			scTime.useDelimiter(delimiterPattern);	
			
			for (int i = 0; i < 2; i++) {
				timeArray[i] = scTime.nextInt();
			}			
		
			hour = timeArray[0];
			minutes = timeArray[1];							
				
			date = new GregorianCalendar(year, month, day, hour, minutes);				
		}
		
		catch (IllegalArgumentException ex) {
			if (badDateFormat) {
				System.out.println("Error! Please enter the date in dd.mm.yyyy format " +
				"i.e. 01.11.2013 and finish by pressing RETURN.");
				return createDate();
			}
			
			if (badTimeFormat) {
				System.out.println("Error! Please enter the time in 24-hour format " +
				"i.e. 13:01 and finished by pressing RETURN.");
				return createDateAndTime();
			}
			
			if (febOverflow) {
				System.out.println("Invalid date! February has 28 days outside of a leap year.");
				return createDateAndTime();
			}
			if (monthOverflow) {
				System.out.println("Error! The month you specified has 30 days.");
				return createDateAndTime();
			} 
		}
		return date;	
	}
	
	
	
	/**
	* Creates a Calendar object without a user-specified time. Useful for looking up 
	* meetings by date only (without regard to time).
	*
	* @return a user-determined date.
	* @throws IllegalArgumentException if the date is invalid (i.e. 29th Feb outside of
	* a leap year), and also if the format is invalid (not in dd.mm.yyyy format, although
	* there is some leniency i.e. d.m.yyyy where possible).
	*/
	public static Calendar createDate() {
		Calendar date = null;		
		int day;
		int month;
		int year;		
		
		int[] dateArray = new int[3]; //To store values representing date
		boolean febOverflow = false;//Booleans to facilitate error messages below
		boolean monthOverflow = false;
		boolean badDateFormat = false;		
		
		try {
			System.out.println("Please enter the date of the meeting in dd.mm.yyyy format: ");
			String userEntry = System.console().readLine();
			
			if (userEntry.equals("back")) {
				return null;//Allows user to cancel and return to main menu 
			}
			
			boolean dateVerified = validateDateEntry(userEntry);//Determines whether input is valid
			if (!dateVerified) {
				badDateFormat = true;
				throw illegalArgEx;
			}
			
			Scanner sc = new Scanner(userEntry);//If input is valid, creates a date
			Pattern delimiterPattern = Pattern.compile("[\\.]");
			sc.useDelimiter(delimiterPattern);
			
			for (int i = 0; i < 3; i++) {
				dateArray[i] = sc.nextInt();
			}
			
			day = dateArray[0];
			month = dateArray[1] - 1;//Calendar interprets January as 0, February as 1 etc
			year = dateArray[2];			
			
			if (month == 1 && day > 28) {
				if ((year % 4 == 0) && (year % 100 != 0)) {
					//this is ok - it's a leap year
				}
				if ((year % 4 == 0) && (year % 100 == 0) && (year % 400 == 0)) {
					//this is ok - it's a leap year
				}
				else {
					febOverflow = true;
					throw illegalArgEx;//Feb only has 29 days in leap year
				}
			}
			if (month % 2 == 1) {//Disallows day having value of 31 when month has only 30 days
				if (day > 30) {
					monthOverflow = true;
					throw illegalArgEx;
				}
			}			
				
			date = new GregorianCalendar(year, month, day); 				
		}
		
		catch (IllegalArgumentException ex) {
			if (badDateFormat) {
				System.out.println("Error! Please enter the date in dd.mm.yyyy format " +
				"i.e. 01.11.2013 and finish by pressing RETURN.");
				return createDate();
			}		
			
			if (febOverflow) {
				System.out.println("Invalid date! February has 28 days outside of a leap year.");
				return createDate();
			}
			if (monthOverflow) {
				System.out.println("Error! The month you specified has 30 days.");
				return createDate();
			} 
		}
		return date;	
	}
	
	
	
	/**
	* Validates user-entered dates. Accepts dates in dd.mm.yyyy format, with leniency
	* for d.m.yyyy where possible i.e. 1.2.2013. Also validates that day values do not 
	* exceed 31, and that month values do not exceed 12.
	*
	* @param userEntry a user-entered String, passed from other methods or classes for validation.
	* @return true or false depending on whether the String is considered valid or not.
	*/
	public static boolean validateDateEntry(String userEntry) {
		Pattern pattern = Pattern.compile("(([0]?[1-9])|([1-2][0-9])|([3][0-1]))[\\.](([0]?[1-9])|([1][0-2]))[\\.][1-2][0-9][0-9][0-9]");
		Matcher m = pattern.matcher(userEntry);//Match given input against pattern
		boolean verified = m.matches(); //True if matched, false otherwise
		
		return verified;		
	}
	
	
	
	/**
	* Validates user-entered times.  Checks time format, allowing hh:mm / h:mm / hh.mm / 
	* h.mm. Rules out invalid times such as 25:99. 
	* 
	* @param userEntry a user-entered String, passed from other methods for validation.
	* @return true or false depending on whether the String is considered valid or not.
	*/
	public static boolean validateTimeEntry(String userEntry) {			
		Pattern pattern = Pattern.compile("(([0]?[0-9])|([1][0-9])|([2][0-3]))([\\:]|[\\.])(([0-5][0-9]))");
		Matcher m = pattern.matcher(userEntry);//Match given input against pattern
		boolean verified = m.matches(); //True if matched, false otherwise
		
		return verified;
	}	
	
	
	
	/**
	* A sub-menu with numerical options for selecting how to search for
	* a meeting. 
	*
	* @return an int for use in switch statement of ContactManager.
	*/	
	public static int lookUpMeetingOptions() {
		System.out.println("1. Search by date                    " + "\t" + "2. Search by meeting ID");
		System.out.println("3. Search future meetings by contact " + "\t" + "4. Search past meetings by contact");
		System.out.println("5. Search past meetings by ID        " + "\t" + "6. Search future meetings by ID");
		System.out.println("7. Return to main menu");
		System.out.print("Select option: ");		
		
		int selection = validateOption(1, 7);		
		return selection;	
	}
	
	
	
	/**
	* A sub-menu with numerical options for selecting how to search 
	* for a meeting by Contact (ID or name).
	*
	* @return an int for use in switch statement of ContactManagerRunner.
	*/
	public static int searchByContactOptions() {
		System.out.println("1. Search by contact ID");
		System.out.println("2. Search by contact name");
		System.out.println("3. Return to main menu");
		System.out.println("Select option: ");
		
		int selection = validateOption(1, 3);
		return selection;
	}
	
	
	
	/**
	* Prints the details (date, time, ID, attendees) of a meeting. If
	* the meeting is an instance of PastMeeting, it also prints the meeting notes.
	*
	* @param meeting the meeting about which details are to be printed.
	*/
	public static void printMeetingDetails(Meeting meeting) {
		Calendar date = meeting.getDate();		
		
		String timeString = renderTime(date);//Renders date/time into user-friendly format
		String dateString = renderDate(date);
		
		System.out.println("Meeting details: ");
		System.out.println("ID:   " + meeting.getId());
		
		System.out.println("Date: " + dateString);
		System.out.println("Time: " + timeString);
			
		System.out.println("Attendees: ");
		for (Contact c : meeting.getContacts()) {
			System.out.println("ID: " + c.getId() + "\t" + c.getName());
		}
		
		if (meeting instanceof PastMeeting) {//Print meeting notes if it's a past meeting
			PastMeeting pMeeting = (PastMeeting) meeting;
			System.out.println("Notes: ");
			System.out.println(pMeeting.getNotes());
		}
	}
	
	
	
	/**
	* A method to validate that user input can be interpreted as an int. 
	*
	* @param entry a String entered by the user, pending conversion to int.
	* @return the user-entered String parsed to an int.
	* @throws NumberFormatException if the parameter String consists of anything other
	* than numbers.
	*/
	public static int validateNumber(String entry) {
		int num;
		
		try {
			num = Integer.parseInt(entry);
		}
		
		catch (NumberFormatException ex) {
			System.out.println("Error: Please enter a number!");
			entry = System.console().readLine();
			return validateNumber(entry);
		}
		
		return num;
	}
	
	
	
	/**
	* A method to print lists containing Meetings (and subtypes).
	*
	* @param list a list of type Meeting (or subtype).
	*/
	public static void printMeetingList(List<? extends Meeting> list) {
		System.out.println("Meeting list: ");
		
		for (Meeting m : list) {
			Calendar date = m.getDate();
			String dateString = renderDate(date);
			String timeString = renderTime(date);
			System.out.println("ID: " + m.getId() + "\t" + dateString + " " + timeString);
		}
	}
	
	
	
	/**
	* A sub-menu with numerical options for selecting how to search for a contact.
	*
	* @return an int for use in switch statement of ContactManagerRunner.
	*/
	public static int lookUpContactOptions() {
		System.out.println("1. Search by name       " + "\t" + "2. Search by ID");
		System.out.println("3. Display contact list " + "\t" + "4. Return to main menu ");
		System.out.print("Select option: ");		
		
		int selection = validateOption(1, 4);		
		return selection;	
	}
	
	
	
	/**
	* Displays contact list on screen.
	*
	* @param contactList contact set from ContactManager.
	*/
	public static void displayContactList(Set<Contact> contactList) {
		Set<Contact> sortedContactList = new TreeSet<Contact>();
		System.out.println("Contact List: ");
		
		if (contactList.isEmpty()) {
			System.out.println("<Empty>");
		}
		
		for (Contact c : contactList) {
			sortedContactList.add(c);
		}
		for (Contact c: sortedContactList) {
			System.out.println("ID: " + c.getId() + "\t" + c.getName());
		}
	}
	
	
	
	/**
	* Renders time fit for display on screen.
	*
	* @param date the Calendar object whose value is to be printed on screen.
	* @return a String, showing the time in a user-friendly format.
	*/
	public static String renderTime(Calendar date) {
		String timeString = "";
		
		if (date.get(Calendar.HOUR_OF_DAY) < 10) {
			timeString += "0" + date.get(Calendar.HOUR_OF_DAY);
		}
		else {
			timeString += date.get(Calendar.HOUR_OF_DAY);
		}
		
		timeString += ":";
		
		if (date.get(Calendar.MINUTE) < 10) {//Ensures such as 12:00 display as such, not as 12:0				
				timeString += "0" + date.get(Calendar.MINUTE);
		}
		else {
			timeString += date.get(Calendar.MINUTE);
		}
					
		return timeString;
			
	}	
	
	
	
	/**
	* Renders date fit for display on screen.
	*
	* @param date the Calendar object whose value is to be printed on screen.
	* @return a String, showing the date in a user-friendly format.
	*/
	public static String renderDate(Calendar date) {
		String dateString = "";
		
		if (date.get(Calendar.DAY_OF_MONTH) < 10) {
			dateString += "0" + date.get(Calendar.DAY_OF_MONTH) + ".";
		}
		else {
			dateString += date.get(Calendar.DAY_OF_MONTH) + ".";
		}
		
		if (date.get(Calendar.MONTH) < 9) {
			dateString += "0" + (date.get(Calendar.MONTH) + 1) + ".";//+1 to print month in meaningful sense to user
		}
		else {
			dateString += (date.get(Calendar.MONTH) +1 ) + ".";
		} 
		
		dateString += date.get(Calendar.YEAR);		  
	
		return dateString;
	}		
		
}

