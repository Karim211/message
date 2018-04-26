package pac;

public class MainServer {
	public static void main(String arg[]) {
		Serveur s = new Serveur(50020,"Users.txt");
		s.run();
		//String str = "";
		/*while (!str.equals("exit")) {
			System.out.println("");
		}*/
	}
}
