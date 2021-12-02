import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.LinkedList;

/**
 * Filename:   Graph.java
 * Project:    p4
 * Authors:    John Sas
 * 
 * Directed and unweighted graph implementation
 */

public class Graph implements GraphADT {
	private int size;
	//where size is number of edges 
	private int order;
	//order is number of vertices
	
	//this was arbitrary
	//could be a tree, hash table
	//both Arraylists, both linkedLists, ect
	ArrayList<LinkedList<String>> graphNodes;
		
	/**
	 * Default no-argument constructor
	 */ 
	public Graph() {
		this.size = 0;
		this.order = 0;
		this.graphNodes = new ArrayList<LinkedList<String>>();
	}

	/*
	 * size and order:
	 * Graph.size() is number of edges
	 * graph.size() is the size of the arrayList
	 * i.e. the order of Graph....
	 * 
	 * order should just be graph.size()
	 * but size is slightly more complicated 
	 * 
	 * size should get set whenever we 
	 * modify the number of edges
	 */
	
	/**
     * Add new vertex to the graph.
     *
     * If vertex is null or already exists,
     * method ends without adding a vertex or 
     * throwing an exception.
     * 
     * Valid argument conditions:
     * 1. vertex is non-null
     * 2. vertex is not already in the graph 
     */
	public void addVertex(String vertex) {
		//check that the vertex isn't null &
		//call containsVertex to 
		//check that vertex isn't already in a node
		if (vertex != null && containsVertex(vertex).isEmpty()) {
			var v = new LinkedList<String>();
			v.add(vertex);
			graphNodes.add(v);
			order++;
		}
	} //method

	/**
	 * earlier I mentioned using built in contains functions
	 * I'm not sure if there's anything that only searches on 
	 * the first element of a linked list so I just wrote up 
	 * the function to do it.
	 * @param vertex
	 * @return
	 */
	private LinkedList<String> containsVertex(String vertex) {
		LinkedList<String> contains= new LinkedList<String>();
		if(graphNodes.isEmpty()) {
			return contains;
		}
		
		for (LinkedList<String> v : graphNodes) {
			if (v.getFirst().equals(vertex)){
				return v;
			}
		} //contains loop 
		return contains;
	}
	
	/**
     * Remove a vertex and all associated 
     * edges from the graph.
     * 
     * If vertex is null or does not exist,
     * method ends without removing a vertex, edges, 
     * or throwing an exception.
     * 
     * Valid argument conditions:
     * 1. vertex is non-null
     * 2. vertex is not already in the graph 
     */
	public void removeVertex(String vertex) {
		/*
		 * for list l in graph
		 * if l.getfirst = vertex -> graph.removeL
		 * else if list.contains(vertex) -> list.remove vertex
		 */
		
		if (vertex == null) {
			return;
		}
		
		//this was a refactor,
		//originally I used a for each loop, 
		//created a concurrent modification exception
		//just use contains to get the list
		//then remove it from graph
		LinkedList listWithVertex = containsVertex(vertex);
		if (!listWithVertex.isEmpty()) {
			size -= listWithVertex.size() - 1 ;
			graphNodes.remove(listWithVertex);
			order--;
		}
		//then call remove edges
		for (LinkedList<String> l : graphNodes) {
			String vertex1 = l.peekFirst();
			this.removeEdge(vertex1, vertex);
		}
	}

	/**
     * Add the edge from vertex1 to vertex2
     * to this graph.  (edge is directed and unweighted)
     * If either vertex does not exist,
     * add vertex, and add edge, no exception is thrown.
     * If the edge exists in the graph,
     * no edge is added and no exception is thrown.
     * 
     * Valid argument conditions:
     * 1. neither vertex is null
     * 2. both vertices are in the graph 
     * 3. the edge is not in the graph
	 */
	public void addEdge(String vertex1, String vertex2) {
		if (vertex1 == null || vertex2 == null) {
			return;
		}
				
		if (containsVertex(vertex1).isEmpty()) {
			addVertex(vertex1);
		}
		if (containsVertex(vertex2).isEmpty()) {
			addVertex(vertex2);
		}
		
		LinkedList<String> temp1 = containsVertex(vertex1);
		//should only need temp1
		//LinkedList<String> temp2 = containsVertex(vertex2);
		
		/*
		 * the checks are easy enough to implement
		 * the issue is the naming convention
		 * lets say vertex2 is the dependency
		 * 
		 * so temp1 is based on vertex1
		 * if we find a node based on vertex2 linked to it
		 * we know vertex2 is already a dependency
		 */
		
		if (temp1 != null && !temp1.contains(vertex2)) {
			temp1.add(vertex2);
			size++;
		}

			//if we got past the first loop 
			//the graph shouldn't contain the edge 
			//and we add it to temp1

	} //method
	
	/**
     * Remove the edge from vertex1 to vertex2
     * from this graph.  (edge is directed and unweighted)
     * If either vertex does not exist,
     * or if an edge from vertex1 to vertex2 does not exist,
     * no edge is removed and no exception is thrown.
     * 
     * Valid argument conditions:
     * 1. neither vertex is null
     * 2. both vertices are in the graph 
     * 3. the edge from vertex1 to vertex2 is in the graph
     */
	public void removeEdge(String vertex1, String vertex2) {
		if (vertex1 == null || vertex2 == null) {
			return;
		}
		
		//need to find the list again
		//then just call remove on vertex2
		LinkedList<String> temp = null; 
		for (LinkedList<String> l : graphNodes) {
			if (l.getFirst().equals(vertex1)) {
				temp = l;
			}
		}
		
		if (temp != null) {
			if(temp.remove(vertex2)) {
				size--;
			}
		}
		
	} //method	

	//also we should get working on unit tests
	
	/**
     * Returns a Set that contains all the vertices
     * 
	 */
	public Set<String> getAllVertices() {
		TreeSet<String> vertices = new TreeSet<String>();
		
		/*
		 * best practice, don't return null,
		 * return an empty set/list and check
		 * for that if you return null and something
		 * isn't expecting it it'll throw an exception.
		 */
		if(graphNodes.isEmpty()) {
			return vertices;
		}
		
		else {
			for (LinkedList<String> l : graphNodes) {
				if (!l.isEmpty()) {
					vertices.add(l.getFirst());
				}
				
			}
		}
		
		return vertices;
	}

	/**
     * Get all the neighbor (adjacent) vertices of a vertex
     *
	 */
	public List<String> getAdjacentVerticesOf(String vertex) {
		//should just be able to loop through
		//skip the vertex and return everything else
		//then you have to find if its a dependency for anything
		LinkedList adjacentVertices = new LinkedList();
		LinkedList listWithVertex = containsVertex(vertex);
		for(int i = 1; i < listWithVertex.size();i++) {
			adjacentVertices.add(listWithVertex.get(i));
		}
		
		/*
		 * I commented out the next for loop
		 * this checks if the node is a dependency 
		 * for other nodes. This relates to direction,
		 * if the graph was directionless, it could go 
		 * both ways and you would want precessors and 
		 * sucessors. In this case, look at the ADT and it
		 * should be one or the other, not both.
		 * 
		 * In this case we just want dependencies
		 */
		
		/*
		for (LinkedList l : graphNodes) {
			if (!l.getFirst().equals(vertex) && l.contains(vertex)) {
				adjacentVertices.add(l.getFirst());
			}
		}
		*/
		return adjacentVertices;
	}
	
	/**
     * Returns the number of edges in this graph.
     */
    public int size() {
        return size;
    }

	/**
     * Returns the number of vertices in this graph.
     */
	public int order() {
        return order;
    }

}
