/**
* An auxiliary class that handles much of the user interface of ContactManager, 
* taking and validating user entries.
*/

import java.io.*;
import java.util.List;
import java.util.Set;
import java.util.Calendar;


public interface Utilities {
	
	/**
	* Displays welcome banner on screen.
	*/
	public static void displayWelcome();
		
	

	/**
	* Displays main menu of numerical choices. Makes use of
	* validateOption() method to ensure only choices
	* within menu range can be selected.
	*
	* @return an integer for use in switch statement
	* of ContactManager class.
	*/
	public static int chooseMainMenuOption();	
	
	
	
	/**
	* A method that validates an integer within
	* a range of integers, for use with numerical
	* option menus. For example, if a menu interface offers
	* a range of choices from 1 - 8, this method can be
	* used to ensure numbers outside of the range are
	* caught as exceptions.
	*
	* @param min the minimum value acceptable.
	* @param max the maximum value acceptable.
	* @return a user-entered number representing a numerical option.
	* @throws NumberFormatException if a non-integer is entered.
	* @throws IllegalArgumentException if a number outside of the specified
	* range is entered.
	*/
	public static int validateOption(int min, int max);	
		
	
	
	/**
	* A method that allows the user to select multiple contacts by ID number. These
	* numbers are then added to an array, which is returned to the ContactManager class
	* for validation with the getContacts(int... ids) method. 
	* Prints contactList Set for user's ease of reference.
	*
	* @param contactList set, passed from ContactManager.
	* @return array of integers representing user-selected contact IDs.
	* @throws IllegalArgumentException if the IDs aren't entered in a comma-separated
	* String (although there is some leniency with regard to comma spacing).
	*/
	public static int[] selectAttendees(Set<Contact> contactList);
	
	
	
	/**
	* A method to validate a user-entered String of digits separated by commas and
	* spaces. Some degree of leniency with regard to spacing to ensure user-friendliness.
	* I.e. 1,2,3 , 4, 5,6, 7 is considered valid.
	* Utilised by selectAttendees() method.
	*
	* @param userEntry a user-entered String passed from selectAttendees().
	* @return true or false (String is valid or invalid respectively).
	*/	
	public static boolean validateCommaString(String userEntry);	
	
	
	
	/**
	* Creates a Calendar date and time from user input. Rules out invalid dates such as
	* 29.02 outside of a leap year, and ensures that months with only 30 days cannot
	* be assigned a date of 31. Rules out invalid times such as 25:79.
	*
	* @return Calendar object with user-determined date and time.
	* @throws IllegalArgumentException if the date or time is invalid, in terms of format
	* and value (i.e. time not in 24-hour hh:mm format, or outside of valid range).	
	*/
	public static Calendar createDateAndTime();	
	
	
	
	/**
	* Creates a Calendar object without a user-specified time. Is identical to 
	* createDateAndTime() method other than that it doesn't request a time from the
	* user. Is useful for looking up meetings by date only (without regard to time).
	*
	* @return a user-determined date.
	* @throws IllegalArgumentException if the date is invalid (i.e. 29th Feb outside of
	* a leap year), and also if the format is invalid (not in dd.mm.yyyy format, although
	* there is some leniency i.e. d.m.yyyy where possible).
	*/
	public static Calendar createDate();
	
	
	
	/**
	* Validates user-entered dates, for use with createDateAndTime(), and createDate()
	* methods. Accepts dates in dd.mm.yyyy format, with leniency for d.m.yyyy where
	* possible i.e. 1.2.2013. Also validates that day values do not exceed 31, and that
	* month values do not exceed 12.
	*
	* @param userEntry a user-entered String, passed from other methods or classes for validation.
	* @return true or false depending on whether the String is considered valid or not.
	*/
	public static boolean validateDateEntry(String userEntry);	
	
	
	
	/**
	* Validates user-entered times.  Checks time format, allowing hh:mm / h:mm / hh.mm / 
	* h.mm. Rules out invalid times such as 25:99. 
	* 
	* @param userEntry a user-entered String, passed from other methods for validation.
	* @return true or false depending on whether the String is considered valid or not.
	*/
	public static boolean validateTimeEntry(String userEntry);		
		
	
	
	/**
	* A sub-menu providing the user with numerical options as to how they would like to
	* go about searching for a meeting. Prints the options on the screen and takes
	* a user-entered int to pass back to ContactManager. Makes use of validateOption().
	*
	* @return an int for use in switch statement of ContactManager.
	*/
	public static int lookUpMeetingOptions();
		
	
	
	/**
	* A sub-menu providing the user with numerical options as to how they would like to
	* go about searching for a meeting use contact details (contact name or ID).
	* Prints the options on screen and takes a user-entered int to pass back to
	* ContactManager. Makes use of validateOption().
	*
	* @return an int for use in switch statement of ContactManager.
	*/
	public static int searchByContactOptions();		
	
	
	
	/**
	* A method that prints the details (date, time, ID, attendees) of a meeting. If
	* the meeting is an instance of PastMeeting, it also prints the meeting notes.
	*
	* @param meeting the meeting about which details are to be printed.
	*/
	public static void printMeetingDetails(Meeting meeting);
	
	
	
	/**
	* A method to validate that user input can be interpreted as an int. Used in
	* ContactManager whenever a user is required to enter a number (i.e. to specify
	* a meeting or contact ID).
	*
	* @param entry a String entered by the user, pending conversion to int.
	* @return the user-entered String parsed to an int.
	* @throws NumberFormatException if the parameter String consists of anything other
	* than a number.
	*/
	public static int validateNumber(String entry);	
	
	
	
	/**
	* A method to print lists containing Meetings or subtypes of Meeting.
	*
	* @param list a list of type Meeting (or subtype).
	*/
	public static void printMeetingList(List<? extends Meeting> list);
	
	
	
	/**
	* A sub-menu providing the user with numerical options as to how they would like to
	* go about searching for a contact. Prints the options on screen and takes
	* a user-entered int to pass back to ContactManager. Makes use of validateOption().
	*
	* @return an int for use in switch statement of ContactManager.
	*/
	public static int lookUpContactOptions();
	
	
	
	/**
	* Displays contact list on screen.
	*
	* @param contactList contact set from ContactManager.
	*/
	public static void displayContactList(Set<Contact> contactList);
		
}

