
//standard java library imports
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//then this is from java.simple,
//needed to be included as a library for the prj
//like junit 
import org.json.simple.parser.ParseException;

/**
 * PackageProgram:
 * test class to actually run
 * the program 
 * 
 * I don't really like calling
 * it test here because this essentially 
 * is the program
 * 
 * This is what runs, 
 * everything else is scaffolding...
 */
public class PackageProgram {

	public static void main(String[] args) {
		
		/*
		 * first goal is to get the program to run
		 * we may or may not need a graph
		 * Definately need a PackageManager and 
		 * a file path to our json
		 */
		
		//Graph g = new Graph();
		PackageManager p1 = new PackageManager();
		String cyclic = "cyclic.json";
		String valid = "valid.json";
		String shared = "shared_dependencies.json";
		//valid seems like the easiest starting point here...
		
		//then you call construct to spin up the graph object
		//lay down the try catch for the exceptions
		
		/*
		 * now just to get this working
		 * we had to implement constructGraph
		 * and add a toString method to Package
		 * to check the json parsing
		 * 
		 * That being said, getAllPackages should be easy
		 */
		
		try {
			String vertex = "A";
			String vertex2 = "G";
        	p1.constructGraph(valid);
        	
        	
        	Graph g = p1.getGraph();
        	printGraph(g);
        	
        	var dep = g.getAdjacentVerticesOf("A");
        	
        	
        	var order = p1.getInstallationOrder(vertex);
        	
        	//System.out.println();
        	var install = p1.toInstall("E","B");
        	System.out.println(p1.getPackageWithMaxDependencies());
        	
        	//g.removeVertex(vertex);
        	
        	/*
        	printGraph(g);
        	
        	g.addVertex(vertex2);
        	g.addEdge(vertex2, "E");
        	printGraph(g);
        	List av = g.getAdjacentVerticesOf(vertex2);
        	
        	g.addVertex("z");
        	*/
        	
        }
        catch (FileNotFoundException e){
        	e.printStackTrace();
        }
        catch (IOException e){
        	e.printStackTrace();
        }
        catch (ParseException e){
        	
        } catch (CycleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PackageNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} //method
	
	public static void printGraph(Graph g) {
		System.out.println("size " + g.size());
    	System.out.println("order " + g.order());
    	
		for (int i = 0; i < g.graphNodes.size(); i++) {
    		LinkedList temp = g.graphNodes.get(i);
    		for (int j = 0; j < temp.size(); j++) {
    			System.out.print(temp.get(j)+",");
    		}
    		System.out.println();
    	}
	}

}
