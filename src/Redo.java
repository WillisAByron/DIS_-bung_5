import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAccumulator;

/**
 * Created by Glenn on 07.06.2016.
 */
public class Redo {

    private ConcurrentHashMap<Long, Page> pages;
    private int cLsn =  -1;

    public Redo(ConcurrentHashMap<Long, Page> pages){
        this.pages = pages;

        FileReader fR = null;
        try {
            fR = new FileReader(Log.path + "log");
        } catch (IOException e){
            e.printStackTrace();
        }

        StringBuffer sB = new StringBuffer();
        int c = -1;
        try {
            while((c = fR.read()) != -1) {
                sB.append((char) c);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String[] logs = sB.toString().split("\n");
        for (String s : logs){
            StringTokenizer sT = new StringTokenizer(s, ",");
            int lsn = Integer.parseInt(sT.nextToken());
            String type = sT.nextToken();
            int traId = Integer.parseInt(sT.nextToken());
            Long pageID = Long.parseLong(sT.nextToken());
            String data = sT.nextToken();

            if (lsn > cLsn){
                cLsn = lsn;
            }

            Page p = pages.get(pageID);
            if (p == null){
                p = new Page(pageID, lsn, data);
                pages.put(p.getId(), p);
            } else if (p.getLsn() < cLsn){
                p.setLsn(lsn);
                p.setData(data);
            }
        }
    }

    public int getLsn(){
        return this.cLsn;
    }

    public ConcurrentHashMap<Long, Page> getPages(){
        return this.pages;
    }
}