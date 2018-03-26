package ieee.access;

import java.io.File;

import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.configuration.Configuration;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelManager;

/**
 * Obtains processing time of counting products in a feature models without textual or cross-tree 
 * constraints using SAT solvers.
 * 
 * Usage:
 *    Test1 <feature-ide-model.xml> <iterations>
 * Example:
 *    Test1 models/example-IoT-fm-2.xml 10
 *    
 * @author Jaime Chavarriaga
 */
public class Test1 {

	public static void main(String[] args) {

		// when no arguments are provided
		if (args.length == 0) {
			System.out.println("Counting products with and without removing mandatory leaves");
			System.out.println("Usage: Test1 <feature-ide-model.xml>");
			System.exit(0);
		}
		
		// when an argument is provided
		
		long sat_count 			= 0L;
		long sat_count_time		= 0L;
		
		System.err.println("Checking file " + args[0]);
		File file = null;
		try {
			file = new File(args[0]);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
		}

		System.err.println("Checking the number of iterations");
		int iterations = (args.length == 2) ? Integer.parseInt(args[1]) : 1;
		
		System.err.println("Running " + iterations + " iterations");
		for (int i=0; i<iterations; i++) {
			System.err.println("Loading feature model");
			IFeatureModel fm = null;
			fm = FeatureModelManager.load(file.toPath()).getObject();
			if (fm == null) {
				System.out.println("Error loading feature model");
			}		
			
			System.err.println("Obtaining configurations using a SAT solver");
			Configuration configs = new Configuration(fm);		
			Stopwatch sw = new Stopwatch();
			sat_count = configs.number(20000);
			sat_count_time = sw.elapsedTime();
			
			System.out.println(
					sat_count 
					+ ","
					+ sat_count_time);
		}			
	}

}
