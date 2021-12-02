
import org.json.simple.parser.ParseException;
import org.junit.After;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
 * JUnit Tests for the PackageManager Class
 * 
 * For some reason cycle detection is off
 * its related to junit,
 * if I instantiate PackageManager with
 * a different class and test shared_dependencies
 * it doesn't throw the cycle exception and cyclic.json
 * causes it to get thrown as expected
 * 
 * Solution: it wasn't detecting a cycle because
 * the graph was based on valid, so I moved package
 * initialization from the setup to tests themselves
 */
public class PackageManagerTest {
	public static final String VALID = "valid.json";
	public static final String CYCLIC = "cyclic.json";
	public static final String SHAREDDEPENDENCIES = "shared_dependencies.json";

	static PackageManager pValid;
	static PackageManager pCyclic;
	static PackageManager pShared;
	
	
	@BeforeEach
	 public void setUp() throws Exception {
		pValid = new PackageManager();
		pCyclic = new PackageManager();
		pShared = new PackageManager();
		
		//pValid.constructGraph(VALID);
		//pCyclic.constructGraph(CYCLIC);
		//pShared.constructGraph(SHAREDDEPENDENCIES);
		
    }
	
    @AfterEach
    public void tearDown() throws Exception {
    	pValid = null;
    	pCyclic = null;
    	pShared = null;
    }
    
    /**
     * just do this for one of them 
     */
    @Test
    public void test000_get_all_packages() {
    	try {
			pShared.constructGraph(SHAREDDEPENDENCIES);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	var getPackages = pShared.getAllPackages();
    	String[] allPackages = {"A", "B", "C", "D"};
    	
    	for (int i = 0; i < allPackages.length; i++) {
    		if (!getPackages.contains(allPackages[i])) {
    			fail("expected package not found");
    		}
    	}

    }
    
    /**
     * now the order matters and you have to account for that
     * getInstallationOrder returns a list
     * probably add everything to a new list
     * then just do a .equals
     */
    @Test
    public void test001_getInstallationOrder() {
    	try {
			pValid.constructGraph(VALID);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	//there's two possibilites for Valid 
    	//D, C, B, A
    	//or C, D, B, A
    	List one = new ArrayList<String>();
    	one.add("D");
    	one.add("C");
    	one.add("B");
    	one.add("A");
    	
    	List two = new ArrayList<String>();
    	two.add("C");
    	two.add("D");
    	two.add("B");
    	two.add("A");
    	
    	try {
			var install = pValid.getInstallationOrder("A");
			if (!(install.equals(one) ||install.equals(two))) {
				fail ("incorrect installation order");
			}
		} catch (CycleException e) {
			fail ("unexpected cycle exception");
			
		} catch (PackageNotFoundException e) {
			fail ("unexpected package not found exception");
		}
    	
    }
    
    
    /*
     * for some reason we have issues with cycle detection...
     * test 2 is throwing it for shared
     * but test 6 isn't throwing it for cyclic...
     */
    @Test
    public void test002_getInstallationOrderForAllPackages() {
    	try {
			pShared.constructGraph(SHAREDDEPENDENCIES);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	//there's also two possibilites for Shared 
    	//D, B, C, A
    	//or D, C, B, A
    	List one = new ArrayList<String>();
    	one.add("D");
    	one.add("B");
    	one.add("C");
    	one.add("A");
    	
    	List two = new ArrayList<String>();
    	two.add("D");
    	two.add("C");
    	two.add("B");
    	two.add("A");
    	
    	try {
			var install = pShared.getInstallationOrderForAllPackages();
			if (!(install.equals(one) ||install.equals(two))) {
				fail ("incorrect installation order");
			}
		} catch (CycleException e) {
			fail ("unexpected cycle exception");
			
		} 
    }
    
    @Test
    public void test003_getPackageWithMaxDependencies() {
    	try {
			pValid.constructGraph(VALID);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	try {
			var max = pValid.getPackageWithMaxDependencies();
			if (!(max.equals("A") || max.equals("E"))) {
				fail ("package with max dependencies not returned");
			}
		} catch (CycleException e) {
			fail ("unexpected cycle exception");
			
		} 
    }
    
    @Test
    public void test004_toInstall_check_order() {
    	try {
			pValid.constructGraph(VALID);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	//Valid, install E
    	// where B is already installed
    	//so it should just return E
    	
    	List order = new ArrayList<String>();
    	order.add("E");
    	
    	try {
			var install = pValid.toInstall("E","B");
			if (!install.equals(order)) {
				fail ("incorrect installation order");
			}
		} catch (CycleException e) {
			fail ("unexpected cycle exception");
			
		} catch (PackageNotFoundException e) {
			fail ("unexpected package not found exception");
		}
    }
    
    @Test
    public void test005_toInstall_PackageNotFoundException() {
    	try {
			pValid.constructGraph(VALID);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	try {
			var install = pValid.getInstallationOrder("G");
			fail ("package not found exception not thrown");
			
			
		} catch (CycleException e) {
			fail ("unexpected cycle exception");
			
		} catch (PackageNotFoundException e) {
			//pass
		}
    }
    
    @Test
    public void test006_getInstallationOrder_CycleException() {
    	try {
    		pCyclic.constructGraph(CYCLIC);
    		var install = pCyclic.getInstallationOrderForAllPackages();
			
			fail ("cycle not detected");
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (CycleException e) {
			//pass
			
		}
    }
    
}

