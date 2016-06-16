public class Edge implements Comparable<Edge> {

	private int source, destination;
	private double distance;

	public Edge(int source, int destination, double distance) {
		this.source = source;
		this.destination = destination;
		this.distance = distance;
	}

	public int source() {
		return source;
	}

	public int destination() {
		return destination;
	}

	public double distance() {
		return distance;
	}

	public int compareTo(Edge that) {
		if(this.distance < that.distance)
			return -1;
		if(this.distance > that.distance)
			return 1;
		return 0;
	}

	public String toString() {
		return "| "+source+"---"+destination+" "+distance+" |";
	}

	public static void main(String[] args) {
		Edge e = new Edge(1, 2, 3.0);
		Edge a = new Edge(2, 1, 4.0);
		System.out.println(e);
		System.out.println(a);
	}
}