import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileCSV{
	private String separator;
	private String path;
	FileCSV(String separator, String path)
	{
		this.path=path;this.separator=separator;
	}
	public synchronized void write(ArrayList<String[]> thingsToWrite)
	{
		
	    try (FileWriter writer = new FileWriter(path)){
	    	
	        for (String[] strings : thingsToWrite) {
	            for (int i = 0; i < strings.length; i++) {
	            	
	                writer.append(strings[i]);
	                if(i < (strings.length-1))
	                    writer.append(separator);
	            }
	            writer.append(System.lineSeparator());
	        }
	        writer.flush();
	        writer.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	public synchronized ArrayList<String[]> read(){
	    try (BufferedReader reader = new BufferedReader(new FileReader(path))){
	        ArrayList<String[]> list = new ArrayList<>();
	        String line = "";
	        while((line = reader.readLine()) != null){
	            String[] array = line.split(separator);
	            list.add(array);
	        }
	        reader.close();
	        return list;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }  
	}
}
