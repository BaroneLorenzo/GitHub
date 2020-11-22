import java.sql.Timestamp;
import java.util.ArrayList;

public class Tracking {
	private ArrayList<Pacco> pacchi;
	
	Tracking() {
		pacchi=new ArrayList<Pacco>();	
	}
	
	public int size() {return pacchi.size();}
	
	public void add(String code, String capSrc, String capDest)throws Exception
	{
		if (search(code)!=-1)
			throw new Exception("Duplicate");
		pacchi.add(new Pacco(code, capSrc, capDest));
	}
	
	public Pacco get(int index)throws Exception
	{
		if (pacchi.size()<index)
			throw new Exception ("Index out of bound");
		return pacchi.get(index);
	}
	
	public int search(String code)
	{
		for (int i=0;i<pacchi.size();i++)
		{
			if (pacchi.get(i).getCode().equals(code))
				return i;
		}
		return -1;
	}
	public class Pacco{
		private TrackPack traccia;
		private String code;
		private String capSrc;
		private String capDest;
		private Timestamp start;
		private Timestamp end;
		
		
		Pacco(String code, String capSrc, String capDest)
		{
			this.code=code; this.capSrc=capSrc; this.capDest=capDest;traccia=new TrackPack();start=null;end=null;
		}
		
		public String getStatus()
		{
			String status;
			String[] singleStatus;
			if (end!=null)
				status="Package n. "+code+" is arrived to destination%";
			else
				status="Package n. "+code+" is in transit.%";
			
			status+="Package shipped from "+capSrc+" to "+capDest+"%";
			status+="Shipping update%";
			status+="Source Cap\tDate/Time\tDestination Cap\tDate/Time\t%";
			for (int i=0;i<traccia.size();i++)
			{
				singleStatus=traccia.get(i).info();
				status+=singleStatus[1]+"\t"+singleStatus[3]+"\t"+singleStatus[2]+"\t"+singleStatus[4]+"\t%";
			}
			return status;
		}
		
		public void addIssue(String src, String dest, Timestamp start, Timestamp end)
		{
			traccia.addExist(src, dest, start, end);
		}
		
		public String getCode() {return code;}
		
		public TrackPack get() {return traccia;}
		
		public String[] getInfo()
		{
			String st, en;
			if (start==null) {st="null";}else {st=start.toString();}
			if (end==null) {en="null";}else {en=end.toString();}
			String[] info= {code, capSrc, capDest, st, en};
			return info;
		}
		
		public void setStart(Timestamp start) {this.start=start;}
		
		public void setEnd(Timestamp end) {this.end=end;}
		
		public String getSrc() {return capSrc;}
		
		public String getDest() {return capDest;}
		
		public void sendTo(String src, String dest)
		{
			
			Timestamp time=new Timestamp(System.currentTimeMillis());
			if (src.equals(capSrc))
				start=time;
			traccia.add(src, dest, time);
		}
		
		public void received(String dest)
		{
			Timestamp time=new Timestamp(System.currentTimeMillis());
			if (dest.equals(capDest))
				end=time;
			traccia.get(traccia.size()-1).arrived(time);
		}	
	}
	
	public class TrackPack{
		private ArrayList<Travel> viaggio;
		
		TrackPack(){viaggio=new ArrayList<Travel>();}
		
		public Travel get(int index) {return viaggio.get(index);}
		
		public int size() {return viaggio.size();}
		
		private void add(String capStart, String capEnd, Timestamp time)
		{
			viaggio.add(new Travel(capStart, capEnd, time)); 
		}
		private void addExist(String capStart, String capEnd, Timestamp start, Timestamp end)
		{
			viaggio.add(new Travel(capStart, capEnd, start, end)); 
		}
		
	}
	
	public class Travel{
		private String capStart;
		private String capEnd;
		private Timestamp start;
		private Timestamp end;
		
		Travel(String capStart, String capEnd, Timestamp start, Timestamp end)
		{
			this.capStart=capStart;this.capEnd=capEnd;this.start=start; this.end=end;
		}
		
		Travel(String capStart, String capEnd, Timestamp time)
		{
			this.capStart=capStart;this.capEnd=capEnd;start=time; end=null;
		}
		
		public String[] info() 
		{
			String st, en;
			if (start==null) {st="null";}else {st=start.toString();}
			if (end==null) {en="null";}else {en=end.toString();}
			String[] info= {"null", capStart, capEnd, st, end.toString()};
			return info;
		}
		
		private void arrived(Timestamp time)
		{
			end=time;
		}
		
		private boolean isSend()
		{
			if (start!=null) return true;
			else return false;
		}
		
		private boolean isArrived()
		{
			if (end!=null) return true;
			else return false;
		}
		
		private String getCapStart() {return capStart;}
		private String getCapEnd() {return capEnd;}
		private Timestamp getTimeStart() {return start;}
		private Timestamp getTimeEnd() {return end;}
	}
}
