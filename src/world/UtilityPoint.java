package world;

public class UtilityPoint {
	public double x;
	public double y;
	public double Utility;
	public boolean isAgent;
	public String name;
	
	public UtilityPoint()
	{}
	
	public UtilityPoint(double x, double y, double utility, boolean isAgent, String name)
	{
		this.x = x;
		this.y = y;
		this.Utility = utility;
		this.isAgent = isAgent;
		this.name = name;
	}
	
	public double GetDistanceTo(double x, double y)
	{
		return Math.sqrt( Math.pow((x-this.x),2) + Math.pow((y-this.y),2) );
	}
}
