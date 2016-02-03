
public class Edge implements Comparable<Edge>{
	private final int v;
	private final int w;
	private final double distance;

	public Edge(int v, int w, double distance){
		this.v = v;
		this.w = w;
		this.distance = distance;
	}

	public int either(){
		return v;
	}

	public int other(int v){
		if(v == this.v)
			return w;
		return v;
	}
	public double getDistance()
	{
		return distance;
	}
	public int compareTo(Edge that){
		if(this.distance < that.distance)
			return -1;
		if(this.distance > that.distance)
			return 1;
		return 0;
	}
	public String toString(){
		return "|"+v+" "+w+" "+distance+"| ";
	}
}