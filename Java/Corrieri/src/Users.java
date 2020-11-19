import java.io.Serializable;
import java.util.ArrayList;

public class Users implements Serializable {
	private ArrayList<User> users;
	
	public Users() {
		
		try{load();}catch(Exception e) {users=new ArrayList<User>();}
	}
	
	private int search(String cap) 
	{
		for (int i=0; i<users.size(); i++)
		{
			if (users.get(i).getCap().equals(cap))
				return i;
		}
		return -1;
	}
	
	public void login(String cap, String code)throws Exception
	{
		int index=search(cap);
		if (index<0)
			throw new Exception("Error CAP doesn't exist");
		users.get(index).login(cap, code);
	}
	
	private void load()throws Exception
	{
		users=BinaryFile.read("users.dat");
	}
	
	private void download()throws Exception
	{
		BinaryFile.write(users, "users.dat");
	}
	
	
	
	private class User{
		private String cap;
		private String code;
		
		User(String cap, String code)
		{
			this.cap=cap;this.code=code;
		}
		
		public String getCap() {return cap;}
		
		public void login(String cap, String code)throws Exception
		{
			if (!this.cap.equals(cap) || !this.code.equals(code))
			{
				throw new Exception("Login Error");
			}
		}
	}

}
