
public class Square {
	
	private int threat;	

	public Square() {
	}
	
	public int getThreat() {
		return threat;
	}
	
	public void increaseThreatTo(int threat) {	// only got one level of threat	
		if (this.threat < threat) {
			this.threat = threat;
		}
	}
	
	public void removeThreat() {
		this.threat = 0;
	}
}
