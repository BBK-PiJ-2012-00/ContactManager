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
	* A method that allows the user to select contacts by ID no.
	* to be added to a set, which is usually a required parameter 
	* for creating a new meeting. Prints contactList Set for 
	* ease of reference.
	*/
	public static Integer[] selectAttendees(Set<Contact> contactList) {
		//Set<Contact> attendeeList = new HashSet<Contact>();
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
		
		
		
}

/**
int selection = sc.nextInt(); //repeating getContacts(int... ids).... THINK! Do this in other class?
				for (Contact c : contactList) {
					if (c.getId() == selection) {//match the int with contact ID in contactList
						attendeeList.add(c);
					}
				}
			} while (sc.hasNextInt());			
*/
	