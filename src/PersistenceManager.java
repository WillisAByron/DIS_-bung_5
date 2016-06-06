import java.util.ArrayList;
import java.util.List;

public class PersistenceManager {

	private static Log log = new Log();
	private static final PersistenceManager instance;
	private static int transactionID=0;
	private static int lsn = 0;
	private List<Page> buffer = new ArrayList<>();
	
	
	static {
		try {
			instance = new PersistenceManager();
		}
		catch (Throwable e) {
		throw new RuntimeException(e.getMessage());
			}
		}

	private PersistenceManager(){
	}
	
	public static PersistenceManager getInstance() {
		return instance;
	}
	
	public int beginTransaction() {
		return transactionID++;
	}
	
	public void commit(int taid){
		
	}
	
	public void write (int taid, Page page) {
		int cLSN = this.lsn++;
		page.setLSN(cLSN);
		log.log(taid, page);
		
		if (buffer.contains(page)) {
			buffer.remove(page);
		}
		
		buffer.add(page);
		if (buffer.size() >= 5) {
			for (Page p : buffer) {
				p.writeToDisk();
			}
			
			buffer = new ArrayList<>();
		}
	}
}