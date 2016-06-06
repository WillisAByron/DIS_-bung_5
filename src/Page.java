import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

public class Page {

	private Long id;
	private int lsn;
	private String data;
	private static String path = "C:\\Users\\Glenn\\IdeaProjects\\DIS_Übung5\\generatedFiles\\";

	public static ConcurrentHashMap<Long, Page> getPages(){
		ConcurrentHashMap<Long, Page> result = null;
		File[] files = new File(path).listFiles();
		if (files == null){
			files = new File[0];
		}
		ConcurrentHashMap<Long, Page> pages = new ConcurrentHashMap<>(files.length);
		for (int i = 0; i <= files.length; i++){
			File file = files[i];
			FileReader fr = null;
			try {
				fr = new FileReader(file);
			} catch (FileNotFoundException e){
				e.printStackTrace();
				result = new ConcurrentHashMap<Long, Page>();
			}
			StringBuffer sB = new StringBuffer();
			int c = -1;
			try {
				while ((c = fr.read()) != -1){

				}
			} catch (IOException e){
				e.printStackTrace();
				result = new ConcurrentHashMap<Long, Page>();
			}
			Page p = new Page(sB.toString());
			pages.put(p.getId(), p);
		}
		return pages;
	}

	public Page(Long id, int lsn, String data){
		this.id = id;
		this.lsn = lsn;
		this.data = data;
	}

	public Page(String s){
		StringTokenizer sT = new StringTokenizer(s, ",");
		Long id = Long.parseLong(sT.nextToken());
		int lsn = Integer.parseInt(sT.nextToken());
		String date = sT.nextToken();
	}

	public void setLSN(int lsn){
		this.lsn = lsn;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getLsn() {
		return lsn;
	}

	public void setLsn(int lsn) {
		this.lsn = lsn;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	@Override
	public boolean equals(Object o){
		boolean result = false;
		if (this.getId() == ((Page) o).getId()) {
			result = true;
		}
		return result;
		
	}

	public void writeToDisk() {
		File file = new File(path + getId() + ".txt");
		try {
			FileWriter fw = new FileWriter(file);
			fw.write(toString());
			fw.flush();
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	@Override
	public String toString(){
		return id + "," + lsn + "," + data;
	}
}