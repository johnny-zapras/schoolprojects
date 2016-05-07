import java.util.LinkedList;
import java.util.Iterator;
import java.util.Stack;

public class Graph<O> implements GraphInterface<O> {

	private LinkedList<Vertex<O>> vertexList;
	private int vertexCount;
	private int edgeCount;
	
	private final int inf = 100000000;
	
	public Graph(){
		vertexList = new LinkedList<Vertex<O>>();
		vertexCount = 0;
		edgeCount = 0;
	}
	
	public Vertex<O> insertVertex(O o){
		Vertex<O> insertedVertex = new Vertex<O>(o);
		vertexList.add(insertedVertex);
		vertexCount +=1;
		return insertedVertex;
	}
	
	public int getNumVertices(){
		return vertexCount;
	}
	
	public int getNumEdges(){
		return edgeCount;
	}
	
	public Edge<O> findEdge(Vertex<O> u, Vertex<O> v) throws GraphException{
		if(u==null || v==null){
			throw new GraphException("Error: one or both vertices passed into the function are null.");
		}
		
		Vertex<O> first=null, second=null;
		for(int i=0; vertexList.get(i)!=null; i++){
			if (vertexList.get(i)==u){
				first = vertexList.get(i);
			}
			if (vertexList.get(i)==v){
				second = vertexList.get(i);
			}
			if (vertexList.get(i)==vertexList.getLast()){
				break;
			}
		}
		if(first==null|| second==null){
			throw new GraphException("d");
		}
		if(first.isAdjacent(second)){
			Iterator<Edge<O>> find = u.incidentEdges();
			while(find.hasNext()){
				Edge<O> edge = find.next();
				if((edge.getEndPoint1()==u && edge.getEndPoint2()==v)	||	(edge.getEndPoint1()==v && edge.getEndPoint2()==u)){
					return edge;
				}
			}
		}
		
		return null;
	}
	
	public Vertex<O> giveOpposite(Vertex<O> v, Edge<O> e){
		if(e.getEndPoint1()==v){
			return e.getEndPoint2();
		}
		else{
			return e.getEndPoint1();
		}
	}
	
	public Iterator<Vertex<O>> vertices(){
		Iterator<Vertex<O>> vertexListIterator = vertexList.listIterator();
		return vertexListIterator;
	}
	
	public boolean areAdjacent(Vertex<O> v, Vertex<O> u) throws GraphException{
		if(u==null || v==null){
			throw new GraphException("Error: null pointer passed to function.");
		}
		
		return v.isAdjacent(u);
	}
	
	public void insertEdge(Vertex<O> v, Vertex<O> u, int weight)  throws GraphException{
		Edge<O> insertedEdge = new Edge<O>(v,u,weight);
		
		if(u==null||v==null){
			throw new GraphException("Error: one or both vertices passed into the function are null.");
		}
		
		if(findEdge(v,u)!=null){
			throw new GraphException("Error: edge already exists within graph.");
		}
		
		edgeCount += 1;
		u.addAdjacent(insertedEdge);
		v.addAdjacent(insertedEdge);
	}
	
	
    public void deleteEdge(Edge<O> e)  throws GraphException{
    	Vertex<O> u = e.getEndPoint1();
    	Vertex<O> v = e.getEndPoint2();
    	
    	if(u==null||v==null){
			throw new GraphException("Error: one or both vertices passed into the function are null.");
		}
		
		if(findEdge(v,u)==null){
			throw new GraphException("Error: edge already exists within graph.");
		}
		
		edgeCount -= 1;
		u.removeAdjacent(e);
		v.removeAdjacent(e);
    }
    
	
	public Iterator<Iterator<Vertex<O>>> ConnectedComponents(){
		String unvisited = "UNVISITED";
		String visited = "VISITED";
		
		for(int i=0; vertexList.get(i)!=null; i++){
			vertexList.get(i).changeStatus(unvisited);
			if (vertexList.get(i)==vertexList.getLast()){
				break;
			}
		}
		Stack<Vertex<O>> stack = new Stack<Vertex<O>>();
		Vertex<O> curVertex = vertexList.get(0);
		LinkedList<Iterator<Vertex<O>>> listOfIterators = new LinkedList<Iterator<Vertex<O>>>();
		for(int i=0; vertexList.get(i)!=null; i++){
			if(vertexList.get(i).checkStatus().compareTo(unvisited)==0){
				LinkedList<Vertex<O>> componentList = new LinkedList<Vertex<O>>();
				while (curVertex != null){
						curVertex.changeStatus(visited);
						componentList.add(curVertex);
						Iterator<Edge<O>> edgeList = curVertex.incidentEdges();
						Vertex<O> tempForCheck = curVertex;
						while(edgeList.hasNext()){
							Edge<O> check = edgeList.next();
							Vertex<O> w;
							if (check.getEndPoint1()==curVertex){
								w = check.getEndPoint2();
							}
							else{
								w = check.getEndPoint1();
							}
							if (w.checkStatus().compareTo(unvisited)==0){
								stack.push(curVertex);
								curVertex = w;
								break;
							}
						}	
						if (curVertex==tempForCheck){
							if (!stack.isEmpty()){
								curVertex = stack.pop();
							}
							else {
								curVertex = null;
							}
						}
				}
				listOfIterators.add(componentList.listIterator());
			}
			if (vertexList.get(i)==vertexList.getLast()){
				break;
			}
		
	}
	Iterator<Iterator<Vertex<O>>> iteratorOfIterators = listOfIterators.listIterator();
	return iteratorOfIterators;
}
	
	public Iterator<Edge<O>> MST(){
		
		String unvisited = "UNVISITED";
		String visited = "VISITED";
		
		LinkedList<Edge<O>> largeEdgeList= new LinkedList<Edge<O>>();
		
		IntegerComparator comp = new IntegerComparator();
		HeapPQ<Integer,Vertex<O>> heap = new HeapPQ<Integer,Vertex<O>> (getNumVertices(),comp);
		Vertex<O> check = vertexList.get(0);
		for (int i=1; vertexList.get(i)!=null; i++){
			Vertex<O> v = vertexList.get(i);
			
			if (v==check){
			v.setDistance(0);
			}
			
			else{
			v.setDistance(inf);
			}
			
			v.setParent(null);
			v.changeStatus(unvisited);
			Position l = heap.insert(v.getDistance(),v);
			v.setLocator(l);
			
			if (vertexList.get(i)==vertexList.getLast()){
				break;
			}
		}
		
		while (!heap.isEmpty()){
			Vertex<O> u = heap.removeMin();
			u.changeStatus(visited);
			Iterator<Edge<O>> edgeList = u.incidentEdges();
			while(edgeList.hasNext()){
				Edge<O> e = edgeList.next();
				Vertex<O> z = this.giveOpposite(u, e);
				if(z.checkStatus().compareTo(unvisited)==0){
					int r = e.getWeight();
					if (r < z.getDistance()){
						largeEdgeList.add(e);
						z.setDistance(r);
						z.setParent(u);
						heap.decreaseKey(z.getLocator(),r);
					}
				}
			}	
		}
	return largeEdgeList.listIterator();
	}
	
}
