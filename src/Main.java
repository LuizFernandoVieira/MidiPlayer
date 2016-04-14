import gui.TocadorMidi;

public class Main {

	public static void main(String[] args) {
		Thread thread = new Thread(new TocadorMidi());
		thread.start();
	}

}
