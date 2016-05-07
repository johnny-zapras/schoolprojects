import java.util.LinkedList;
import java.util.Iterator;

public class Vertex<O> {
	
	private LinkedList<Edge<O>> adjacencyList;
	private O object;
	private String vertexStatus;
	private int distance;
	private Vertex<O> parent;
	private Position locator;
	
	
	
	public Vertex(O objectIn){
		object = objectIn;
		adjacencyList = new LinkedList<Edge<O>>();
		vertexStatus = "";
	}

	public O getObject(){
		return object;
	}
	
	public void addAdjacent(Edge<O> e) throws GraphException{
		for(int i=0; !adjacencyList.isEmpty() && adjacencyList.get(i)!=null; i++){
			Edge<O> check = adjacencyList.get(i);
			if ((check.getEndPoint1()==e.getEndPoint1()	&& check.getEndPoint2()==e.getEndPoint2())	||	(check.getEndPoint1()==e.getEndPoint2()	&& check.getEndPoint2()==e.getEndPoint1())){
				throw new GraphException("Error: Edge already in list.");
			}
			if (adjacencyList.get(i)==adjacencyList.getLast()){
				break;
			}
		}
		adjacencyList.add(e);
	}
	
	public Iterator<Edge<O>> incidentEdges(){
		return adjacencyList.listIterator();
	}
	
	public boolean isAdjacent(Vertex<O> v){
		for(int i=0;  !adjacencyList.isEmpty() && adjacencyList.get(i)!=null  ; i++){
			Edge<O> check = adjacencyList.get(i);
			if ((check.getEndPoint1()==v	&& check.getEndPoint2()==this)	||	(check.getEndPoint1()==this	&& check.getEndPoint2()==v)){
				return true;
			}
			if (adjacencyList.get(i)==adjacencyList.getLast()){
				break;
			}
		}
		return false;
	}
	
	public void removeAdjacent(Edge<O> e) throws GraphException{
		for(int i=0; !adjacencyList.isEmpty() && adjacencyList.get(i)!=null; i++){
			Edge<O> check = adjacencyList.get(i);
			if ((check.getEndPoint1()==e.getEndPoint1()	&& check.getEndPoint2()==e.getEndPoint2())	||	(check.getEndPoint1()==e.getEndPoint2()	&& check.getEndPoint2()==e.getEndPoint1())){
				adjacencyList.remove(i);
				return;
			}
		}
		throw new GraphException("Error: Edge does not exist in list.");
	}
	
	public void changeStatus(String inputStatus){
		this.vertexStatus = inputStatus;
	}
	
	public String checkStatus(){
		return vertexStatus;
	}
	
	public int getDistance(){
		return distance;
	}
	
	public void setDistance(int dist){
		distance = dist;
	}
	
	public void setParent(Vertex<O> par){
		parent = par;
	}
	
	public Vertex<O> getParent(){
		return parent;
	}
	
	public void setLocator(Position loc){
		locator = loc;
	}
	
	public Position getLocator(){
		return locator;
	}
}
