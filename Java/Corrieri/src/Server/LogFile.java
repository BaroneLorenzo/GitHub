import java.io.File;
import java.io.FileWriter;

public class LogFile {
	
	public static synchronized void writeLOG(String path, String log)throws Exception
	{
		File file=new File(path);
		FileWriter fw=new FileWriter(file, true);
		fw.write(log);
		fw.flush();
		fw.close();
	}
}
