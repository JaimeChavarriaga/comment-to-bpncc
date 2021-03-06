package ieee.access;

import java.io.File;

import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.IFeatureStructure;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelManager;
import ieee.access.utils.Stopwatch;

/**
 * Obtains processing time of counting products in a feature models without textual or cross-tree 
 * constraints using the traditional algorithm.
 * 
 * Usage:
 *    TimingCountExistingAlgorithm <feature-ide-model.xml> <iterations>
 * Example:
 *    TimingCountExistingAlgorithm models/example-IoT-fm-2.xml 10
 *    
 * @author Jaime Chavarriaga
 */
public class TimingCountExistingAlgorithm {

	public static void main(String[] args) {

		// when no arguments are provided
		if (args.length == 0) {
			System.out.println("Counting products with and without removing mandatory leaves");
			System.out.println("Usage: " + System.getProperty("sun.java.command") + " <feature-ide-model.xml> <iterations>");
			System.exit(0);
		}
		
		// when an argument is provided
		
		long normal_count 			= 0L;
		long normal_count_time		= 0L;
		
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
			
			System.err.println("Obtaining the root feature");
			IFeatureStructure root = fm.getStructure().getRoot();
			
			System.err.println("Calculating number of products");
			Stopwatch sw = new Stopwatch();
			normal_count = countConfigurations(root);			
			normal_count_time = sw.elapsedTime();

			System.out.println(
					normal_count 
					+ ","
					+ normal_count_time);
		}			
			
	}
	
	/**
	 * Counts the valid configurations in a feature model without textual or cross-tree constraints
	 * 
	 * @param 	f 	parent feature of the tree to count the valid configurations
	 * @return  number of valid configurations in the subtree
	 */
	public static long countConfigurations(IFeatureStructure f) {

		/*
		The algorithm implements the following rules:
			
			count root(c)  = count(c)
			count mandatory(c)  = count(c)
		    count optional(c) = count(c) + 1
		    count and(c1, . . . , cn)  = count(c1) ∗ . . . ∗ count(cn)
		    count alternative(c1, . . . , cn)  = count(c1) + . . . + count(cn)
		    count or(c1, . . . , cn)  = (count(c1) + 1) ∗ . . . ∗ (count(cn) + 1) − 1
		    count leaf  = 1
		*/
		
		long count = 0;

		// default value for a leaf
		if (!f.hasChildren()) {
			count = 1;
			
		// if the feature is an AND-group
		} else if (f.isAnd()) {
			count = 1;
			for (IFeatureStructure fs : f.getChildren()) {
				count *= countConfigurations(fs);
			}
			
		// if the feature is an alternative-group
		} else if (f.isAlternative()) {
			for (IFeatureStructure fs : f.getChildren()) {
				count += countConfigurations(fs);
			}
			
		// if the feature is an or-group
		} else if (f.isOr()) {
			count = 1;
			for (IFeatureStructure fs : f.getChildren()) {
				count *= (countConfigurations(fs)+1);
			}
			count--;
			
		}
		
		// if the feature is optional, a one must be added to the count 
		if (!f.isMandatory()) {
			count++;
		}
			
		// return the count
		return count;		
	}
	
}

