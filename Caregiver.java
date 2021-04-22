
public class Caregiver extends User{
	private Patient patient;
	private boolean hasPermission;
	public Caregiver (String uname, String pword, String fname, Patient p, boolean hP) {
		super(uname, pword,fname);
		patient = p;
		hasPermission = hP;
	}
	public void updatePatient(Patient p) {
		patient = p;
	}
	
}
