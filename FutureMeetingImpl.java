
import java.util.Calendar;
import java.util.Set;

public class FutureMeetingImpl extends MeetingImpl implements FutureMeeting {

	public FutureMeetingImpl(Set<Contact> contacts, Calendar date) {
		super(contacts, date);
	}

}