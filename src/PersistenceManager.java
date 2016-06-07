import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PersistenceManager {

	private static Log log = new Log();
	private static final PersistenceManager instance;
	private static int transactionID = 0;
	private static int lsn = 0;
	private ConcurrentHashMap<Long, Page> buffer;
	
	
	static {
			ConcurrentHashMap<Long, Page> pages = Page.getPages();
            Redo r = new Redo(pages);
			instance = new PersistenceManager(r.getLsn(), r.getPages());
		}

	private PersistenceManager(int clsn, ConcurrentHashMap cHM){
        lsn = clsn;
        buffer = cHM;
	}
	
	public static PersistenceManager getInstance() {
		return instance;
	}
	
	public int beginTransaction() {
        int tId = transactionID++;
        int clsn = lsn++;
        log.writeLog(Log.BEGIN_OF_TRANSACTION, clsn, tId, null, null);
		return tId;
	}
	
	public void write (int taid, Long pageID, String data) {
		int cLSN = this.lsn++;
        log.writeLog(Log.PAGE, cLSN, taid, pageID, data);

        Page p;
        if(buffer.containsKey(pageID)) {
            p = buffer.get(pageID);
            p.setLsn(cLSN);
            p.setData(data);
        } else {
            p = new Page(cLSN, data);
            buffer.put(pageID, p);
        }
        checkBuffer();
	}

    private void checkBuffer() {
        if (buffer.size() >= 5){
            buffer.values().forEach(Page::writeToDisk);
        }
    }

    public void commit(int tID){
        log.writeLog(Log.END_OF_TRANSACTION, lsn++, tID, null, null);
    }
}