import gui.InterfaceGrafica;

public class Main {

	public static void main(String[] args) {
		Thread thread = new Thread(new InterfaceGrafica());
		thread.start();
	}

}
