import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import org.json.simple.parser.ParseException;

//import jdk.internal.jshell.tool.resources.l10n;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;


/**
 * Filename:   PackageManager.java
 * Project:    p4
 * Authors:    John Sas
 * 
 * PackageManager is used to process json package dependency files
 * and provide function that make that information available to other users.
 * 
 * Each package that depends upon other packages has its own
 * entry in the json file.  
 * 
 * Package dependencies are important when building software, 
 * as you must install packages in an order such that each package 
 * is installed after all of the packages that it depends on 
 * have been installed.
 * 
 * For example: package A depends upon package B,
 * then package B must be installed before package A.
 * 
 * This program will read package information and 
 * provide information about the packages that must be 
 * installed before any given package can be installed.
 * all of the packages in
 * 
 * You may add a main method, but we will test all methods with
 * our own Test classes.
 */

public class PackageManager {
    
    private Graph graph;
    static ArrayList<Package> packages;
    
    /*
     * Package Manager default no-argument constructor.
     */
    public PackageManager() {
        this.packages = new ArrayList<Package>();
        this.graph = new Graph();
    }
    
    /*
     * constructGraph is really two methods
     * spin off parseJSON to create an ArrayList 
     * of packages, then from that you can create the graph
     */
    
    /**
     * Takes in a file path for a json file and builds the
     * package dependency graph from it. 
     * 
     * @param jsonFilepath the name of json data file with package dependency information
     * @throws FileNotFoundException if file path is incorrect
     * @throws IOException if the give file cannot be read
     * @throws ParseException if the given json cannot be parsed 
     */
    public void constructGraph(String jsonFilepath) throws FileNotFoundException, IOException, ParseException {
    	
    	FileReader json = new FileReader(jsonFilepath);
    	pasrseJSON(json);
    	
    	/*
    	 * I want a private helper method to parse
    	 * the json data for the sake of encapsulation,
    	 * not sure if the exceptions are
    	 * going to chain properly here though...
    	 * (they do!)
    	 */
    		
    	/*
    	 * parse saves all the JSON data as packages
    	 * in the arraylist packages I added to the 
    	 * class fields, now we need to take that data 
    	 * and build the graph from it 
    	 */

    	for (Package p: packages) {
    		graph.addVertex(p.getName());
    	}
    	
    	/*
    	 * vertices is easier,
    	 * we need to translate dependencies 
    	 * to adjacency and that breaks down into
    	 * -adjacency matrix or
    	 * -adjacency list
    	 * 
    	 * I'm thinking list because its directed
    	 * you should be able to save the name as 
    	 * a vertex then the dependencies in the adjacency
    	 * list
    	 */
    	
    	/*
    	 * next step is to get the edges
    	 * take String[] then add dependencies,
    	 * make all the vertices first, then 
    	 * when you go to add something as a 
    	 * dependency somewhere you know it
    	 * exists
    	 */
    	
    	for (Package p: packages) {
    		String[] dependencies = p.getDependencies();
    		if (dependencies == null) {
    			break;
    		}
    		for (int i = 0; i < dependencies.length; i++) {
    			graph.addEdge(p.getName(), dependencies[i]);
    		}
    		
    	}
    	System.out.println();
    } //method
    
    private void pasrseJSON(FileReader json) throws IOException, ParseException{
    	JSONParser parser = new JSONParser();
		JSONObject input = (JSONObject) parser.parse(json);
		
		//prints the whole JSON object
		System.out.println(input); //debug
		
		/*
		 *now that you have the object you have to get data
		 *out its nested and there's multiple parts
		 * loop through packages, then get the name
		 * (should just be a String)
		 * and a list of dependencies
		 * (probably use an array list) 
		 */
		
		JSONArray data = (JSONArray) input.get("packages");
		
		//now you need to save the data into a package obj
		
		/*
		 * this gets kind of weird because dependencies in the 
		 * json are actually a list, 
		 * but the package constructor takes a String[]
		 * pack.get will return an ArrayList
		 * but then you need to loop through
		 * and save every element to the String[]
		 */

		/*
		 * Now this might not be right, data.size = 3
		 * but if we need to create package objects for 
		 * the dependencies we actually need more 
		 * 
		 * easiest solution is use an arraylist then check to see if
		 * there's a package for all dependencies afterwards
		 * 
		 * if packages.contains == false,
		 * packages.add(new Package(name, dependencies) 
		 * 
		 * based on the json, if no dependencies are listed we can assume
		 * its root and String[] dependencies = null;
		 */
		
		for (int i = 0; i < data.size(); i++) {
			JSONObject pack = (JSONObject) data.get(i); 
			String name = (String) pack.get("name");

			ArrayList<String> dependencyList = (ArrayList<String>) pack.get("dependencies");
			String[] dependencies = new String[dependencyList.size()];
			for (int j = 0; j < dependencyList.size(); j++) {
				dependencies[j] = dependencyList.get(j);
			}
			
			packages.add(new Package(name, dependencies));
		}
		
		//add dependencies as packages
		for (int i = 0; i < data.size(); i++) {
			JSONObject pack = (JSONObject) data.get(i); 
			String name = (String) pack.get("name");
			
			ArrayList<String> dependencyList = (ArrayList<String>) pack.get("dependencies");
			String[] dependencies = new String[dependencyList.size()];
			for (int j = 0; j < dependencyList.size(); j++) {
				dependencies[j] = dependencyList.get(j);
				boolean newPackage = true;
				for (Package p: packages) {
					if (p.getName().equals(dependencies[j])){
						newPackage = false;
					}
				}
				if (newPackage) {
					packages.add(new Package(dependencies[j], null));
				}
			}
		}
    }
        
    /**
     * Helper method to get all packages in the graph.
     * 
     * @return Set<String> of all the packages
     */
    public Set<String> getAllPackages() {
    	
    	/*
    	 * you can't actually instantiate set,
    	 * its an interface, 
    	 * needs to be a hashSet, treeSet etc.
    	 * all you have to do is call the getAllVertices method 
    	 * in graph, which might be kind of circular...
    	 */
    	
    	return graph.getAllVertices();
    }
    
    /**
     * Given a package name, returns a list of packages in a
     * valid installation order.  
     * 
     * Valid installation order means that each package is listed 
     * before any packages that depend upon that package.
     * 
     * @return List<String>, order in which the packages have to be installed
     * 
     * @throws CycleException if you encounter a cycle in the graph while finding
     * the installation order for a particular package. Tip: Cycles in some other
     * part of the graph that do not affect the installation order for the 
     * specified package, should not throw this exception.
     * 
     * @throws PackageNotFoundException if the package passed does not exist in the 
     * dependency graph.
     */
    public List<String> getInstallationOrder(String pkg) throws CycleException, PackageNotFoundException {    	
    	if (!getAllPackages().contains(pkg)) {
    		throw new PackageNotFoundException();
    	}
    	
    	return graphDFS(pkg);
    }
    
    /**
     * Given two packages - one to be installed and the other installed, 
     * return a List of the packages that need to be newly installed. 
     * 
     * For example, refer to shared_dependecies.json - toInstall("A","B") 
     * If package A needs to be installed and packageB is already installed, 
     * return the list ["A", "C"] since D will have been installed when 
     * B was previously installed.
     * 
     * @return List<String>, packages that need to be newly installed.
     * 
     * @throws CycleException if you encounter a cycle in the graph while finding
     * the dependencies of the given packages. If there is a cycle in some other
     * part of the graph that doesn't affect the parsing of these dependencies, 
     * cycle exception should not be thrown.
     * 
     * @throws PackageNotFoundException if any of the packages passed 
     * do not exist in the dependency graph.
     */
    public List<String> toInstall(String newPkg, String installedPkg) throws CycleException, PackageNotFoundException {
    	/*
    	 * run a depth first search to get dependencies
    	 * of the new package, repeat for the  installed
    	 * package then subtract out anything already installed
    	 */
    	
    	var order = graphDFS(newPkg);
    	var installed = graphDFS(installedPkg);
    	
    	for (String s: installed) {
    		order.remove(s);
    	}
    	
    	return order;
    }
    
    /**
     * Return a valid global installation order of all the packages in the 
     * dependency graph.
     * 
     * assumes: no package has been installed and you are required to install 
     * all the packages
     * 
     * returns a valid installation order that will not violate any dependencies
     * 
     * @return List<String>, order in which all the packages have to be installed
     * @throws CycleException if you encounter a cycle in the graph
     */
    public List<String> getInstallationOrderForAllPackages() throws CycleException {
    	var all = getAllPackages();
    	var visited = new ArrayList<String>();

    	/*
    	 * idea here is just to nest depth first search 
    	 * make another visited array 
    	 * add the installation for the first node,
    	 * then repeat for all other nodes,
    	 * adding any additional dependencies
    	 */
    	for (String s : all) {
    		var order = graphDFS(s);
    		for(String v : order) {
    			//if statement prevents any duplicates
    			if (!visited.contains(v)) {
    				visited.add(v);
    			}
    		}
    	}
    	
    	return visited;
    }
    
    /**
     * Find and return the name of the package with the maximum number of dependencies.
     * 
     * Tip: it's not just the number of dependencies given in the json file.  
     * The number of dependencies includes the dependencies of its dependencies.  
     * But, if a package is listed in multiple places, it is only counted once.
     * 
     * Example: if A depends on B and C, and B depends on C, and C depends on D.  
     * Then,  A has 3 dependencies - B,C and D.
     * 
     * @return String, name of the package with most dependencies.
     * @throws CycleException if you encounter a cycle in the graph
     */
    public String getPackageWithMaxDependencies() throws CycleException {
    	int max = 0;
    	String vertex = "";
    	
    	/*
    	 * just call depth first search and it returns all 
    	 * dependencies
    	 */
        for (String s: getAllPackages()) {
        	var order = graphDFS(s);
        	if (order.size() > max) {
        		max = order.size();
        		vertex = s;
        	}
        }
        
        return vertex;
    }

    /**
     * graphDFS is just a wrapper for DPS, 
     * instantiates the lists and starts the 
     * recursive call to DFS
     */
    private ArrayList<String> graphDFS(String vertex) throws CycleException {
    	HashSet<String> visited = new HashSet<String>();
    	HashSet<String> cycle = new HashSet<String>();
    	ArrayList<String> order = new ArrayList<String>();

    	return DFS(vertex, visited, cycle, order);
    }
    
    /**
	 * DFS is the actual implementation
	 * this is the meat of the program
	 * most other things are based on this
	 */
    private ArrayList<String> DFS(String v, HashSet<String> visited, HashSet<String> cycle, ArrayList<String> order) throws CycleException {
    
		if (visited.contains(v)) {
			return order;
		}
		if (cycle.contains(v)) {
			throw new CycleException();
		}
		
		cycle.add(v);
		
		var depList = getDependencies(v);
		for (String dep : depList) {
			order = DFS(dep, visited, cycle, order);
		}
		
		order.add(v);
		visited.add(v);
		cycle.remove(v);
    	
    	return order;
    }//method
 
   	/*
	 * pay attention to pointers,
	 * if you remove the first element
	 * (returns dependencies) you modified the graph
	 * you need to make a copy of the list 
	 * then return that
	 * 
	 * remove the vertex from the list of its dependencies
	 * otherwise its always going to be a cycle....
	 * 
	 * Now based on graphADT that's unnecessary
	 * graph.getAdjacentVertices()
	 * already returns dependencies, 
	 * so just call that
	 *
	 */
    private List<String> getDependencies(String vertex){    	
    	for (String s : getAllPackages()) {
    		if (s.equals(vertex)) {
    			return graph.getAdjacentVerticesOf(s);
    		}
    	}
    	
    	return new ArrayList<String>();
    }

    public Graph getGraph() {
    	return graph;
    }
    
    public static void main (String [] args) {
        System.out.println("PackageManager.main()");

    }
    
    //final checks
    //read over the assignment
    //check the interface
    //did you test all methods/exceptions?
    //do you need to check for edge cases?
    //clean up comments, remove any //debug statements
}
