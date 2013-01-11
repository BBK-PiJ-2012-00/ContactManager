/**
* A class that handles the user interface of ContactManager, and takes user entries.
*/

import java.io.IllegalArgumentException;

public class ContactManagerUtilities {
	private IllegalArgumentException illegalArgEx = new IllegalArgumentException();

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
		System.out.print("Select option: ");
		int selection = validateOption(1, 8);		
		return selection;	
	}
	
	public static int validateOption(int min, int max) {//to validate user choices from numerical menus
		try {
			int selection = Integer.parseInt(System.console().readLine());
			if (selection < min || selection > max) {
		 		throw illegalArgEx;
		 	}
		 	return selection;
		 }		
		 catch (IllegalArgumentException ex) {
		 	System.out.println("Invalid option. Please make a selection from the menu.");
		 	return validateOption(min, max);		 	
		 }
		 catch (NumberFormatException ex) {
		 	System.out.println("Please enter a number that corresponds to an option on the menu."):
		 }
	}
	
	public static void subMenuOne(Set<Contact> contacts) {//takes contactList from ContactManager class for display
	//set of contacts and date
		Set<Contact> attendeeList = new HashSet<Contact>();
		System.out.println("*** Add a future meeting");
		System.out.println("Your contacts: ");
		Iterator<Contact> iterator = contacts.iterator();
		while (iterator.hasNext()) {
			Contact c = iterator.next();
			System.out.println("c.getName() " + "c.getId()");
		}
		System.out.println("Please enter the IDs of the contacts who are attending, separated" +
		" by a comma. Finish by pressing RETURN.");
		String entry = System.console().readLine();
		
	}
	//Instead, perhaps have method to facilitate and check selection of users, and entry of date,
	//so that the checking is done here
	
	public set<Contact> selectAttendees(Set<Contact> contactList) {
		Set<Contact> attendeeList = new HashSet<Contact>();
		System.out.println("Your contact list: ");
		if (contactList.isEmpty()) {
			System.out.println("<Empty> You will need to add contacts to your contact list before creating meetings.");
			return null;//if null is returned, ContactManager will need to take corresponding action
		}
		for (Contact c : contactList) {
			System.out.println("c.getName() " + "c.getId()");
		}
		System.out.println("Please enter the IDs of the contacts who are attending, separated" +
		" by a comma e.g. 1, 4, 5. Finish by pressing RETURN.");
		String entry = System.console().readLine();
		Scanner sc = new Scanner(userEntry);
		
		
		
	
	}
	
	
	public boolean validateCommaString(String userEntry) {
		Pattern pattern = Pattern.compile("([0-9][0-9]*[\\s]?[,][\\s]?)*[0-9][0-9]*");
		Matcher m = pattern.matcher(userEntry);//match given input against pattern
		boolean verified = m.matches();//upshot - matches or doesn't
		if (verified) {
			return true;
		}
		return false;
	}
		
		boolean b = Pattern.matches("a*b", "aaaaab");
		// if userEntry matches this ok, then proceed to make delimiter String and parse
		//in selectAttendees method
		
}
	