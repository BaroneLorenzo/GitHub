import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class BinaryFileUser {

	public synchronized static <T> void write(T oggetto, String path)throws Exception {
		ObjectOutputStream fbinarioOut = new ObjectOutputStream(new FileOutputStream(path));
        fbinarioOut.writeObject(oggetto);
        fbinarioOut.flush();
        fbinarioOut.close();
	}
	public synchronized static <T> T read(String path)throws Exception {
		ObjectInputStream fin = new ObjectInputStream(new FileInputStream(path));
        return ((T) fin.readObject());
	}

}
