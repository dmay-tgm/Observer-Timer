package may;

/**
 * Main class that starts the Subject and the Observers.
 *
 * @author Daniel May
 * @version 20160316.1
 */
public class MainClock {

	/**
	 * Starts the the Subject and two displays.
	 *
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		Clock c = new Clock();
		new Display(c);
		new Display(c);
	}
}