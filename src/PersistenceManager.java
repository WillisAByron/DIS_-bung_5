import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PersistenceManager {

	private static Log log = new Log();
	private static final PersistenceManager instance;
	private static long transactionID = 0;
	private static int lsn = 0;
	private ConcurrentHashMap<Long, Page> buffer;
	private ConcurrentHashMap<Long, Set<Long>> traID_pageID;
	
	static {
			ConcurrentHashMap<Long, Page> pages = Page.getPages();
            Redo r = new Redo(pages);
			instance = new PersistenceManager(r.getLsn(), r.getPages());
		}

	private PersistenceManager(int clsn, ConcurrentHashMap cHM){
        lsn = clsn;
        buffer = cHM;
        traID_pageID = new ConcurrentHashMap<Long, Set<Long>>();
	}
	
	public static PersistenceManager getInstance() {
		return instance;
	}
	
	public long beginTransaction() {
        long tId = transactionID++;
        int clsn = lsn++;
        log.writeLog(Log.BEGIN_OF_TRANSACTION, clsn, tId, null, null);
        traID_pageID.put(tId, new HashSet<Long>());
		return tId;
	}
	
	public void write (long taid, Long pageID, String data) {
		int cLSN = this.lsn++;
        log.writeLog(Log.PAGE, cLSN, taid, pageID, data);
        Set<Long> tmp = traID_pageID.get(taid);
        if (tmp != null){
            tmp.add(pageID);
        }
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
            for (Page p : buffer.values()){
                p.writeToDisk();
            }
        }
    }

    public void commit(long tID){
        log.writeLog(Log.END_OF_TRANSACTION, lsn++, tID, null, null);

        Set<Long> pageIDs = (Set<Long>) traID_pageID.remove(tID);
        if (pageIDs != null){
            for (Long l : pageIDs){
                Page p = Page.getPages().get(l);
                if (p != null){
                    p.setCommitted(true);
                }
            }
        }
    }
}