import java.util.Vector;
import java.io.*;
public class ImageSegment {
	
	public static void main(String[] args){

	if (args.length!=3){
		System.out.println("Error: incorrect number of arguments entered.\nPlease recheck arguments.");
		System.exit(-1);
	}
	
	Graph<Pixel> mainGraph = new Graph<Pixel>();
	String pictureInput = args[0];
	String pictureOutput = args[1];
	int numOfSegments = Integer.parseInt(args[2]);
	
	try{
	ImageReader picReader = new ImageReader(pictureInput);
	Vector<Vertex<Pixel>> lookupVector = new Vector<Vertex<Pixel>>(picReader.getHeight() * picReader.getWidth());
	
	
	for(int i=0; i<picReader.getWidth(); i++){
		for(int j=0; j<picReader.getHeight(); j++){
			Pixel insertedPixel = new Pixel(i, j, picReader.getRed(i, j), picReader.getGreen(i, j), picReader.getBlue(i, j));
			lookupVector.setElementAt(mainGraph.insertVertex(insertedPixel), i+j*picReader.getWidth());
		}
	}
	
	/////add vertical edges
	for(int i=0; i<picReader.getWidth(); i++){
		for(int j=0; j<picReader.getHeight(); j++){
			
			Vertex<Pixel> backVertex;
			Vertex<Pixel> currentVertex = lookupVector.get(i+j*picReader.getWidth());
			Vertex<Pixel> nextVertex;
			
			
			if (j==0){
				nextVertex = lookupVector.get(i+(j+1)*picReader.getWidth());
				int weightForEdge = Math.abs(currentVertex.getObject().getRed() - nextVertex.getObject().getRed()) + Math.abs(currentVertex.getObject().getGreen() - nextVertex.getObject().getGreen()) + Math.abs(currentVertex.getObject().getBlue() - nextVertex.getObject().getBlue());
				Edge<Pixel> forwardEdge = new Edge<Pixel>(currentVertex, nextVertex, weightForEdge);
				currentVertex.addAdjacent(forwardEdge);
			}
			
			else if (j==picReader.getHeight()-1){
				backVertex = lookupVector.get(i+(j-1)*picReader.getWidth());
				int weightForEdge = Math.abs(currentVertex.getObject().getRed() - backVertex.getObject().getRed()) + Math.abs(currentVertex.getObject().getGreen() - backVertex.getObject().getGreen()) + Math.abs(currentVertex.getObject().getBlue() - backVertex.getObject().getBlue());
				Edge<Pixel> backEdge = new Edge<Pixel>(currentVertex, backVertex, weightForEdge);
				currentVertex.addAdjacent(backEdge);
			}
			
			else{
				
				nextVertex = lookupVector.get(i+(j+1)*picReader.getWidth());
				int weightForEdge1 = Math.abs(currentVertex.getObject().getRed() - nextVertex.getObject().getRed()) + Math.abs(currentVertex.getObject().getGreen() - nextVertex.getObject().getGreen()) + Math.abs(currentVertex.getObject().getBlue() - nextVertex.getObject().getBlue());
				Edge<Pixel> forwardEdge = new Edge<Pixel>(currentVertex, nextVertex, weightForEdge1);
				currentVertex.addAdjacent(forwardEdge);
				
				backVertex = lookupVector.get(i+(j-1)*picReader.getWidth());
				int weightForEdge2 = Math.abs(currentVertex.getObject().getRed() - backVertex.getObject().getRed()) + Math.abs(currentVertex.getObject().getGreen() - backVertex.getObject().getGreen()) + Math.abs(currentVertex.getObject().getBlue() - backVertex.getObject().getBlue());
				Edge<Pixel> backEdge = new Edge<Pixel>(currentVertex, backVertex, weightForEdge2);
				currentVertex.addAdjacent(backEdge);
				
			}
			
		}
	}

	//add horizontal edges
	for(int i=0; i<picReader.getWidth(); i++){
		for(int j=0; j<picReader.getHeight(); j++){
			
			Vertex<Pixel> backVertex;
			Vertex<Pixel> currentVertex = lookupVector.get(i+j*picReader.getWidth());
			Vertex<Pixel> nextVertex;
			
			
			if (i==0){
				nextVertex = lookupVector.get((i+1)+j*picReader.getWidth());
				int weightForEdge = Math.abs(currentVertex.getObject().getRed() - nextVertex.getObject().getRed()) + Math.abs(currentVertex.getObject().getGreen() - nextVertex.getObject().getGreen()) + Math.abs(currentVertex.getObject().getBlue() - nextVertex.getObject().getBlue());
				Edge<Pixel> forwardEdge = new Edge<Pixel>(currentVertex, nextVertex, weightForEdge);
				currentVertex.addAdjacent(forwardEdge);
			}
			
			else if (i==picReader.getWidth()-1){
				backVertex = lookupVector.get((i-1)+j*picReader.getWidth());
				int weightForEdge = Math.abs(currentVertex.getObject().getRed() - backVertex.getObject().getRed()) + Math.abs(currentVertex.getObject().getGreen() - backVertex.getObject().getGreen()) + Math.abs(currentVertex.getObject().getBlue() - backVertex.getObject().getBlue());
				Edge<Pixel> backEdge = new Edge<Pixel>(currentVertex, backVertex, weightForEdge);
				currentVertex.addAdjacent(backEdge);
			}
			
			else{
				
				nextVertex = lookupVector.get(i+1)+j*picReader.getWidth());
				int weightForEdge1 = Math.abs(currentVertex.getObject().getRed() - nextVertex.getObject().getRed()) + Math.abs(currentVertex.getObject().getGreen() - nextVertex.getObject().getGreen()) + Math.abs(currentVertex.getObject().getBlue() - nextVertex.getObject().getBlue());
				Edge<Pixel> forwardEdge = new Edge<Pixel>(currentVertex, nextVertex, weightForEdge1);
				currentVertex.addAdjacent(forwardEdge);
				
				backVertex = lookupVector.get((i-1)+j*picReader.getWidth());
				int weightForEdge2 = Math.abs(currentVertex.getObject().getRed() - backVertex.getObject().getRed()) + Math.abs(currentVertex.getObject().getGreen() - backVertex.getObject().getGreen()) + Math.abs(currentVertex.getObject().getBlue() - backVertex.getObject().getBlue());
				Edge<Pixel> backEdge = new Edge<Pixel>(currentVertex, backVertex, weightForEdge2);
				currentVertex.addAdjacent(backEdge);
				
			}
			
		}
	}
	
	
	}
	
	catch(Exception io){
		System.out.println("Error: can't open picture.");
	}
	}
}