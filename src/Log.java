import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Log {

	public static int BEGIN_OF_TRANSACTION = 0;
	public static int END_OF_TRANSACTION = 1;
	public static int PAGE = 2;

	private List<String> lines = new ArrayList<String>();
	public static String path = "C:\\Users\\Glenn\\IdeaProjects\\DIS_-bung_5\\generatedFiles\\log\\";
	private FileWriter fW;
	
	public Log(){
		File file = new File(path + "log.txt");
		try {
			fW = new FileWriter(file, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeLog(int logType, int lsn, long traId, Long pageID, String data){
		String type = "";
		try {
			switch (logType){
				case 0:
					type = "TB";
					break;
				case 1:
					type = "TE";
					break;
				case 2:
					type = "P";
					break;
			}
			if (pageID != null && data != null){
				fW.write(lsn + "," + type + "," + traId + "," + pageID + "," + data + "," + "\r\n");
			} else {
				fW.write(lsn + "," + type + "," + traId + "," + "," + "," + "\r\n");
			}
			fW.flush();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
}