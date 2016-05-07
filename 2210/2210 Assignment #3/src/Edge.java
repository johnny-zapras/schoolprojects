
public class Edge<O> {
	
	private Vertex<O> firstVertex;
	private Vertex<O> secondVertex;
	private int weight;
	
	public Edge(Vertex<O> inputFirst, Vertex<O> inputSecond, int inputWeight){
		this.firstVertex = inputFirst;
		this.secondVertex = inputSecond;
		this.weight = inputWeight;
	}
	
	public Vertex<O> getEndPoint1(){
		return this.firstVertex;
	}
	
	public Vertex<O> getEndPoint2(){
		return this.secondVertex;
	}

	public int getWeight(){
		return this.weight;
	}
}
