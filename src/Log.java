import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;


public class Log {

	public static int BEGIN_OF_TRANSACTION = 0;
	public static int END_OF_TRANSACTION = 1;
	public static int PAGE = 2;

	private List<String> lines = new ArrayList<String>();
	String path = "C:\\Users\\Glenn\\IdeaProjects\\DIS_Übung5\\generatedFiles\\log\\";
	private FileWriter fW;
	
	public Log(){
		File file = new File(path + "log");
		try {
			fW = new FileWriter(file, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeLog(int logType, int lsn, int traId, Long pageID, String data){
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
			fW.write(lsn + "," + type + "," + traId + "," + pageID + "," + data);
		} catch (IOException e){
			e.printStackTrace();
		}
	}
}