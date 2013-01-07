/**
* A class that handles the user interface of ContactManager, and takes user entries.
*/

public class ContactManagerUtilities {

	public void displayWelcome() {
		System.out.println("*************************************************************");
		System.out.println("*                    CONTACT MANAGER                        *");
		System.out.println("*************************************************************" + "\n");
	}
	
	public int chooseMainMenuOption() {
		System.out.println("                  ***** Main Menu *****");
		System.out.println("1. Add a future meeting            " + "\t" + " 2. Look up meeting");
		System.out.println("3. Create record of a past meeting " + "\t" + " 4. Add notes to a meeting that has taken place");
		System.out.println("5. Add a new contact               " + "\t" + " 6. Look up contact");
		System.out.println("7. Save data to disk               ");
		System.out.print("Select option: ");
		int selection = Integer.parseInt(System.console().readLine());
		return selection;	
	}