import java.io.Serializable;

public class User implements Serializable{
	private String cap;
	private String password;
	User(String cap, String password)
	{
		this.cap=cap;this.password=password;
	}
	public String getPassword() {
		return password;
	}
	public String getCap()
	{
		return cap;
	}
}
