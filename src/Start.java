import java.util.ArrayList;
import java.util.List;

public class Start {
	
	public static PersistenceManager pM;
	public static Log log = new Log();

	public static void main(String[] args) {
		pM = PersistenceManager.getInstance();
		List<Client> clients = new ArrayList<Client>();
        for (int i = 0; i <= 4; i++){
            clients.add(new Client(pM, i));
        }
        for (int i = 0; i <= 4; i++){
            Thread t = new Thread(clients.get(i));
            t.start();
        }
	}

}
