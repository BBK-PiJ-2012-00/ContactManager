/**
* A class that handles some of the user interface of ContactManager, and takes/validates user entries.
*/

import java.util.regex.*;
import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ContactManagerUtilities {
	private static IllegalArgumentException illegalArgEx = new IllegalArgumentException();

	public static void displayWelcome() {
		System.out.println("*************************************************************");
		System.out.println("*                    CONTACT MANAGER                        *");
		System.out.println("*************************************************************" + "\n");
	}
	
	//an option to go back to main in case of wrong selection?
	public static int chooseMainMenuOption() {
		System.out.println("                  ***** Main Menu *****");
		System.out.println("1. Add a future meeting            " + "\t" + " 2. Look up meeting");
		System.out.println("3. Create record of a past meeting " + "\t" + " 4. Add notes to a meeting that has taken place");
		System.out.println("5. Add a new contact               " + "\t" + " 6. Look up contact");
		System.out.println("7. Save data to disk               " + "\t" + " 8. Save and exit.");
		System.out.println("(Exit back to this menu at any point by entering \"back\"");
		System.out.print("Select option: ");
		int selection = validateOption(1, 8);		
		return selection;	
	}
	
	public static int validateOption(int min, int max) {//to validate user choices from numerical menus
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
	* A method that allows the user to select contacts by ID number. These numbers
	* are then added to an array, which is returned to the ContactManager class
	* for validation with the getContacts(int... ids) method. 
	* Prints contactList Set for ease of reference.
	*/
	public static Integer[] selectAttendees(Set<Contact> contactList) {
		
		List<Integer> attendeeList = new ArrayList<Integer>();
		Integer[] attendees;
		System.out.println("Your contact list: ");
		if (contactList.isEmpty()) {
			System.out.println("<Empty> You will need to add contacts to your contact list before creating meetings.");
			return null;//This sends the user back to the main menu via a break statement in ContactManager
		}
		for (Contact c : contactList) {//Prints contactList for ease of reference
			System.out.println("c.getName() " + "c.getId()");
		}
		System.out.println("Please enter the IDs of the contacts who are attending, separated" +
		" by a comma e.g. 1, 4, 5. Finish by pressing RETURN.");
		
		try{
			String userEntry = System.console().readLine();
			if (userEntry.equals("back")) {
				return null;//allows user to cancel and return to main menu - ensure main menu handles null appropriately
			}
			boolean verified = validateCommaString(userEntry);//determines whether input is valid
			if (!verified) {
				throw illegalArgEx;
			}
			Scanner sc = new Scanner(userEntry);//if input is valid, creates an arrayList with the user input numbers
			Pattern delimiterPattern = Pattern.compile("[\\s]?[,][\\s]?");
			sc.useDelimiter(delimiterPattern);
			do {
				int selection = sc.nextInt();
				attendeeList.add(selection);
			} while (sc.hasNextInt());
			attendees = attendeeList.toArray(new Integer[0]);//converts to array for use in ContactManager getContacts() method
			//so that it can be ascertained whether the user has selected contacts who exist						
		}
		catch (IllegalArgumentException ex) {
			System.out.println("Error! Please enter the contact ID numbers separated by a comma " +
			"i.e. 1, 2, 3 or 1,2,3 and finish by pressing RETURN.");
			attendees = selectAttendees(contactList);
		}
		return attendees;	
	}
	
	/*
	* A method to validate a user-entered String of
	* digits separated by commas and spaces.
	* Is reasonably lenient to ensure user friendliness:
	* 1,2,3 , 4, 5, 6 is considered valid.
	*/
	public static boolean validateCommaString(String userEntry) {
		Pattern pattern = Pattern.compile("([0-9][0-9]*[\\s]?[,][\\s]?)*[0-9][0-9]*");
		Matcher m = pattern.matcher(userEntry);//match given input against pattern
		boolean verified = m.matches();//upshot - matches or doesn't
		if (verified) {
			return true;
		}
		return false;
	}
	
	
	public static Calendar createDate() {
		Calendar date;
		int day;
		int month;
		int year;
		int[] dateArray = new int[3];
		try {
			System.out.println("Please enter the date of the meeting in dd.mm.yyyy format: ");
			String userEntry = System.console().readLine();
			if (userEntry.equals("back")) {
				return null;//allows user to cancel and return to main menu 
			}
			boolean verified = validateDateEntry(userEntry);//determines whether input is valid
			if (!verified) {
				throw illegalArgEx;
			}
			Scanner sc = new Scanner(userEntry);//if input is valid, creates a date
			Pattern delimiterPattern = Pattern.compile("[\\.]");
			sc.useDelimiter(delimiterPattern);
			for (int i = 0; i < 3; i++) {
				dateArray[i] = sc.nextInt();
			}
			day = dateArray[0];
			month = dateArray[1] - 1;//Calendar interprets January as 0, February as 1 etc
			year = dateArray[2];
			if (month == 1 && day == 29) {
				if ((year % 4 0= 0) && (year % 100 != 0)) {
					//this is ok - it's a leap year
				}
				if ((year % 4 == 0) && (year % 100 == 0) && (year % 400 == 0)) {
					//this is ok - it's a leap year
				}
				else {
					throw illegalDateException;//Feb only has 29 days in leap year
				}
			}
			
			
			
			//A year will be a leap year if it is divisible by 4 but not by 100. 
		//If a year is divisible by 4 and by 100, it is not a leap year unless it is also divisible by 400
								
		}
		catch (IllegalArgumentException ex) {
			System.out.println("Error! Please enter the date in dd.mm.yyyy format " +
			"i.e. 01.11.2013 and finish by pressing RETURN.");
			date = createDate();
		}
		return date;	
	}
	
	
	public boolean validateDateEntry(String userEntry) {
		//checks date format, allowing d.m.yyyy or dd.mm.yyyy. Rules out invalid days and months i.e. 59.40.2001
		Pattern pattern = Pattern.compile("(([0]?[1-9])|([1-2][0-9])|([3][0-1]))[\\.](([0]?[1-9])|([1][0-2]))[\\.][2][0][0-9][0-9]");
		Matcher m = pattern.matcher(userEntry);//match given input against pattern
		boolean verified = m.matches();//upshot - matches or doesn't
		if (verified) {
			return true;
		}
		return false;
	}
		//MUST DEAL WITH MONTHS THAT HAVE 30/31 DAYS!!!
		.
	
		
		
		
		
}

