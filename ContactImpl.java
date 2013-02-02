import java.io.Serializable;
import java.lang.Comparable;



public class ContactImpl implements Contact, Serializable, Comparable<Contact> { 
	private int id;
	private String name;
	private String notes = "";
	private static int idAssigner = 0; //to facilitate assignment of an id

	
	public ContactImpl(String name) {
		this.name = name;
		id = idAssigner + 1; //first contact will have id 1, second 2 etc
		incrementIdAssigner();
	}
	
	
	private static void incrementIdAssigner() {
		idAssigner++;
	}
	
	
	//to be used when data is loaded from file to restore value of idAssigner
	public static void restoreIdAssigner(int idValue) {
		idAssigner = idValue;
	}
	
	
	public static int getIdAssigner() {
		return idAssigner;
	}
	
	
	public int getId() {
		return id;
	}
	

	public String getName() {
		return name;
	}


	public String getNotes() {
		return notes;
	}


	public void addNotes(String note) {
		notes = notes + note + "\n"; //so that notes String is not overwritten if addNotes is called more than once
	}
	
	
	
	@Override
	public int compareTo(Contact contact) {
		Integer id = (Integer) this.id;
		Integer otherId = (Integer) contact.getId();		
		return id.compareTo(otherId);
	}
	
	

}