import java.io.Serializable;

public class IdStoreImpl implements IdStore, Serializable {
	private int contactIdAssigner;
	private int meetingIdAssigner;

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