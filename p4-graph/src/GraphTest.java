
import org.junit.After;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * 
 * JUnit Tests for the Graph Class
 *
 */
public class GraphTest {
	Graph graphNodes;
	//ArrayList<LinkedList<String>> graphNodes;
	
	/*
	 * without pakageManager this is sort 
	 * of a pain,
	 * just look at what construct graph is doing then 
	 * replicate that, have to make an arrayList, then add 
	 * all the dependency linked lists to it.... 
	 */
	
	 /** Create the graph based on valid.json for use in any test */
    @BeforeEach
    public void setUp() throws Exception{
    	//graphNodes = new ArrayList<LinkedList<String>>(); 
    	graphNodes = new Graph();

    	
    	graphNodes.addEdge("A","B");
    	graphNodes.addEdge("E","B");
    	graphNodes.addEdge("B","C");
    	graphNodes.addEdge("B","D");
    	
    	/*
    	var b = new LinkedList<String>();
    	b.add("B");
    	b.add("C");
    	b.add("D");
    	graphNodes.add(b);
    	
    	var e = new LinkedList<String>();
    	e.add("E");
    	e.add("B");
    	graphNodes.add(e);
    	
    	var c = new LinkedList<String>();
    	c.add("C");
    	graphNodes.add(c);
    	
       	var d = new LinkedList<String>();
    	d.add("D");
    	graphNodes.add(d);
    	*/	
    }
    
    @AfterEach
    public void tearDown() throws Exception {
        graphNodes = null;
    }
	
    @Test
    public void test000_Initalization_check_size_and_order() {
    	 assertEquals(4, graphNodes.size());
    	 assertEquals(5, graphNodes.order());
    }
    
    /**
     * Adding a valid vertex increments order, 
     * but not size
     */
    @Test
    public void test001_addVertex_check_order() {
    	graphNodes.addVertex("G"); 
    	assertEquals(4, graphNodes.size());
    	assertEquals(6, graphNodes.order());
    }
    
    /**
     * Should be able to handle a duplicate vertex
     * without adding it or changing order
     */
    @Test
    public void test002_addVertex_duplicate_exception_not_thrown() {
    	graphNodes.addVertex("A"); 
    	assertEquals(4, graphNodes.size());
    	assertEquals(5, graphNodes.order());
    }
    
    /**
     * Should be able to handle a null vertex
     * without adding it or changing order
     */
    @Test
    public void test003_addVertex_null_exception_not_thrown() {
    	graphNodes.addVertex(null); 
    	assertEquals(4, graphNodes.size());
    	assertEquals(5, graphNodes.order());
    }
    
    /**
     * removing a vertex should decrement order
     * and remove any edges it was included in,
     * which should also decrease size
     */
    @Test
    public void test004_removeVertex_check_order_and_size() {
    	graphNodes.removeVertex("A");
    	assertEquals(3, graphNodes.size());
    	assertEquals(4, graphNodes.order());
    }
    
    /**
     * trying to remove a null vertex shouldn't
     * throw an exception or change size/order
     */
    @Test
    public void test005_removeVertex_null_exception_not_thrown() {
    	graphNodes.removeVertex(null);
    	assertEquals(4, graphNodes.size());
    	assertEquals(5, graphNodes.order());
    }
    
    /**
     * trying to remove a vertex that's not in 
     * the graph also shouldn't
     * throw an exception or change size/order
     * 
     * This actually found something, I was returning 
     * an empty list for contains, but I didn't check
     * whether it was empty and it always decrementing
     * leading to the wrong size/order counts 
     */
    @Test
    public void test006_removeVertex_vertex_not_found() {
    	graphNodes.removeVertex("Z");
    	assertEquals(4, graphNodes.size());
    	assertEquals(5, graphNodes.order());
    }
    
    /**
     * Add an edge where the vertex is already
     * in the graph, size should increment 
     * 
     * Note: for setup/test000 we already checked 
     * addEdge where the vertex wasn't 
     * in the graph yet
     */
    @Test
    public void test007_addEdge_vertex_exists() {
    	graphNodes.addVertex("G");
    	graphNodes.addEdge("A", "G");
    	assertEquals(5, graphNodes.size());
    	assertEquals(6, graphNodes.order());
    }
    
    /**
     * Remove an edge, decrement size
     */
    @Test
    public void test008_removeEdge() {
     	graphNodes.removeEdge("A", "B");
    	assertEquals(3, graphNodes.size());
    	assertEquals(5, graphNodes.order());
    }
    
    /**
     * Vertex not found, shouldn't do 
     * anything, size and order unchanged
     */
    @Test
    public void test009_removeEdge_vertex_not_in_graph() {
    	graphNodes.removeEdge("A", "H");
    	assertEquals(4, graphNodes.size());
    	assertEquals(5, graphNodes.order());
    }
    
    /**
     * Should return all vertices from the setup
     */
    @Test
    public void test010_getAllVertices() {
    	var allVert = graphNodes.getAllVertices(); 
    	var expected = new TreeSet<String>();
    	expected.add("A");
    	expected.add("B");
    	expected.add("C");
    	expected.add("D");
    	expected.add("E");
    	assertEquals(allVert, expected);
    }
    
    /**
     * Should return the dependencies of 
     * given vertex, in this case A has a dependency of B
     * Note: this doesn't cascade, B has its own dependencies
     * and package manager handles that with depth first search, 
     * Adjacent Verticies is just returning direct dependencies/
     */
    @Test
    public void test011_getAdjacentVertices() {
    	var dependencies = graphNodes.getAdjacentVerticesOf("A"); 
    	List<String> expected = new ArrayList<String>();
    	expected.add("B");
    	assertEquals(dependencies, expected);
    }
    
}
