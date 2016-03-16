package may;

import java.util.Observable;

/**
 * Simple Timer.
 *
 * @author Daniel May
 * @version 20160316.1
 */
class Clock extends Observable implements Runnable {
	private boolean running = false;

	/**
	 * Constructor for the Subject.
	 */
	public Clock() {
		(new Thread(this)).start();
	}

	/**
	 * Waits a second and notifies the Observers afterwards.
	 */
	private void refresh() {
		while (running) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.err.println("Timer was interrupted: " + e.getMessage());
			}
			setChanged();
			notifyObservers();
			/**
			 * Normalerweise würde man beim Push Prinzip notifyServes mit den zu
			 * uebergebenden Daten als Parameter ausfueheren. Da es hier aber
			 * keine Daten gibt, wurde das Push Prinzip so umgesetzt, dass die
			 * update Methode im Observer direkt den Countdown dekrementiert.
			 * Mit dem Pull Modell wuerde man notifyServers ohne Parameter
			 * aufrufen und in der update Methode des Observers wuerde man nur
			 * die Referenz auf das Subject Objekt abspeichern. Wenn man dan das
			 * Display aktualisieren will, holt man sich die Daten aus der
			 * eigenen Klasse.
			 */
		}
	}

	/**
	 * Overrides the run method of the Runnable interface.
	 */
	@Override
	public void run() {
		running = true;
		refresh();
	}
}