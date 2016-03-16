package may;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Simple GUI in order to display the timer.
 *
 * @author Daniel May
 * @version 20160316.1
 */
public class Display extends JFrame implements Observer, ActionListener {

	private static final long serialVersionUID = -6134691523829644298L;

	private LocalTime lt;

	private JButton pause, stop, start, incH, decH, incM, decM, incS, decS;
	private JLabel hours;
	private JLabel minutes;
	private JLabel seconds;

	private boolean running;

	/**
	 * Constructor that registers the observer.
	 *
	 * @param clock
	 *            Subject that initiates a decrement every second
	 */
	public Display(Observable clock) {
		running = false;
		clock.addObserver(this);
		lt = LocalTime.of(0, 0, 0);
		setupDisplay();
		display();
	}

	/**
	 * This method gets called, if the subject notifies the observers that a
	 * change occurred.
	 *
	 * @param clock
	 *            the subject
	 * @param arg1
	 *            object that could be passed by the subject
	 */
	@Override
	public void update(Observable clock, Object arg1) {
		if (running) {
			lt = lt.minusSeconds(1);
			display();
			if (isZero()) {
				running = false;
				setButtonsEnabled(true);
				/*
				 * Lambda expressions for overriding the run method. The dialog
				 * is a thread because, otherwise it would block the rest of the
				 * application.
				 */
				Thread t = new Thread(() -> {
					JOptionPane.showMessageDialog(null, "The countdown has reached zero!", "Finished!",
							JOptionPane.INFORMATION_MESSAGE);
				});
				t.start();
			}
		}
	}

	/**
	 * Displays the time.
	 */
	private void display() {
		seconds.setText(toClockView(lt.getSecond()));
		minutes.setText(toClockView(lt.getMinute()));
		hours.setText(toClockView(lt.getHour()));
	}

	/**
	 * Turns a single digit integer into a two digit integer, by adding a
	 * leading 0 and converting it to String.
	 *
	 * @param value
	 *            the integer value to be converted
	 * @return converted String representation
	 */
	private static String toClockView(int value) {
		return value < 10 ? "0" + value : value + "";
	}

	/**
	 * Sets up the display
	 */
	private void setupDisplay() {

		Font bigFont = new Font(Font.MONOSPACED, Font.BOLD, 60);

		this.setLayout(new BorderLayout());

		JPanel clock = new JPanel();

		JPanel hPanel = new JPanel();
		hPanel.setLayout(new BoxLayout(hPanel, BoxLayout.Y_AXIS));
		incH = new JButton("inc");
		incH.addActionListener(this);
		hPanel.add(incH);
		hours = new JLabel("", SwingConstants.CENTER);
		hours.setFont(bigFont);
		hPanel.add(hours);
		decH = new JButton("dec");
		decH.addActionListener(this);
		hPanel.add(decH);
		clock.add(hPanel);

		JLabel split = new JLabel(":");
		split.setFont(bigFont);
		clock.add(split);

		JPanel mPanel = new JPanel();
		mPanel.setLayout(new BoxLayout(mPanel, BoxLayout.Y_AXIS));
		incM = new JButton("inc");
		incM.addActionListener(this);
		mPanel.add(incM);
		minutes = new JLabel("", SwingConstants.CENTER);
		minutes.setFont(bigFont);
		mPanel.add(minutes);
		decM = new JButton("dec");
		decM.addActionListener(this);
		mPanel.add(decM);
		clock.add(mPanel);

		JLabel split2 = new JLabel(":");
		split2.setFont(bigFont);
		clock.add(split2);

		JPanel sPanel = new JPanel();
		sPanel.setLayout(new BoxLayout(sPanel, BoxLayout.Y_AXIS));
		incS = new JButton("inc");
		incS.addActionListener(this);
		sPanel.add(incS);
		seconds = new JLabel("", SwingConstants.CENTER);
		seconds.setFont(bigFont);
		sPanel.add(seconds);
		decS = new JButton("dec");
		decS.addActionListener(this);
		sPanel.add(decS);
		clock.add(sPanel);

		this.add(clock);
		display();

		JPanel buttons = new JPanel();
		start = new JButton("Start");
		start.addActionListener(this);
		start.setEnabled(false);
		buttons.add(start);
		pause = new JButton("Pause");
		pause.addActionListener(this);
		pause.setEnabled(false);
		buttons.add(pause);
		stop = new JButton("Stop");
		stop.addActionListener(this);
		stop.setEnabled(false);
		buttons.add(stop);
		this.add(buttons, BorderLayout.SOUTH);

		this.setTitle("Timer");
		this.pack();
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	/**
	 * Overrides the actionPerformed method.
	 *
	 * @param e
	 *            the action event that triggered the action
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Start")) {
			running = true;
			setButtonsEnabled(false);
			start.setEnabled(false);
			pause.setEnabled(true);
			stop.setEnabled(true);
		} else if (e.getActionCommand().equals("Pause")) {
			running = false;
			pause.setEnabled(false);
			start.setEnabled(true);
		} else if (e.getActionCommand().equals("Stop")) {
			running = false;
			lt = LocalTime.of(0, 0, 0);
			display();
			setButtonsEnabled(true);
		} else {
			if (e.getSource().equals(incH)) {
				lt = lt.plusHours(1);
			} else if (e.getSource().equals(decH)) {
				lt = lt.minusHours(1);
			} else if (e.getSource().equals(incM)) {
				lt = lt.plusMinutes(1);
			} else if (e.getSource().equals(decM)) {
				lt = lt.minusMinutes(1);
			} else if (e.getSource().equals(incS)) {
				lt = lt.plusSeconds(1);
			} else if (e.getSource().equals(decS)) {
				lt = lt.minusSeconds(1);
			}
			display();
			if (isZero()) {
				start.setEnabled(false);
			} else {
				start.setEnabled(true);
			}
		}
	}

	/**
	 * Checks if the the time is zero.
	 * 
	 * @return if the time is zero
	 */
	private boolean isZero() {
		return lt.getHour() == 0 && lt.getMinute() == 0 && lt.getSecond() == 0;
	}

	/**
	 * Enables or disables all increment and decrement buttons.
	 *
	 * @param isEnabled
	 *            if the buttons should be enabled or disable
	 */
	private void setButtonsEnabled(boolean isEnabled) {
		incH.setEnabled(isEnabled);
		decH.setEnabled(isEnabled);
		incM.setEnabled(isEnabled);
		decM.setEnabled(isEnabled);
		incS.setEnabled(isEnabled);
		decS.setEnabled(isEnabled);
		start.setEnabled(!isEnabled);
		stop.setEnabled(!isEnabled);
		pause.setEnabled(!isEnabled);
	}
}