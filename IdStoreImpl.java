import java.io.Serializable;

public class IdStoreImpl implements IdStore, Serializable {
	private int contactIdAssigner = 0;//on initial use of ContactManager, value will be 0
	private int meetingIdAssigner = 0;
	

	public void saveContactIdAssigner(int idAssignerValue) {
		this.contactIdAssigner = idAssignerValue;
	}
		
	
	public void saveMeetingIdAssigner(int idAssignerValue) {
		this.meetingIdAssigner = idAssignerValue;
	}
	

	public int getContactIdAssigner() {
		return contactIdAssigner;
	}
	
	
	public int getMeetingIdAssigner() {
		return meetingIdAssigner;
	}

}