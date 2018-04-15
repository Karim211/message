package application;

public class MainServer {
	public static void main(String arg[]) {
		Serveur s = new Serveur(3200);
		s.run();
	}
}
