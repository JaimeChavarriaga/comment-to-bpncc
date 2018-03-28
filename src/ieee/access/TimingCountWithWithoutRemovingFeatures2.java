package ieee.access;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.IFeatureStructure;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelManager;
import ieee.access.utils.Stopwatch;

/**
 * Obtains processing time of counting products in a feature models without textual or cross-tree 
 * constraints using the traditional algorithm with and without removing mandatory leaves.
 * 
 * Usage:
 *    TimingCountWithWithoutRemovingFeatures2 <feature-ide-model.xml> <iterations>
 * Example:
 *    TimingCountWithWithoutRemovingFeatures2 models/example-IoT-fm-2.xml 10
 * 
 * @author Jaime Chavarriaga
 */
public class TimingCountWithWithoutRemovingFeatures2 {

	public static void main(String[] args) {

		// when no arguments are provided
		if (args.length == 0) {
			System.out.println("Counting products with and without removing mandatory leaves");
			System.out.println("Usage: " + System.getProperty("sun.java.command") + " <feature-ide-model.xml> <iterations>");
			System.exit(0);
		}
		
		// when an argument is provided
		
		BigInteger normal_count 	= BigInteger.ZERO;
		long normal_count_time		= 0L;
		BigInteger removed_count	= BigInteger.ZERO;
		long removing_leaves_time	= 0L;
		long removed_count_time		= 0L;
		
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
			
			System.err.println("Calculating number of products without removing mandatory leaves");
			Stopwatch sw = new Stopwatch();
			normal_count = countConfigurations(root);			
			normal_count_time = sw.elapsedTime();
			
			System.err.println("Removing mandatory leaves");
			sw = new Stopwatch();
			removeMandatoryLeaves(root);
			removing_leaves_time = sw.elapsedTime();

			System.err.println("Calculating number of products after removing mandatory leaves");
			sw = new Stopwatch();
			removed_count = countConfigurations(root);			
			removed_count_time = sw.elapsedTime();
			
			System.out.println(
					normal_count 
					+ ","
					+ normal_count_time
					+ ","
					+ removed_count
					+ ","
					+ removing_leaves_time
					+ ","
					+ removed_count_time);
			
		}
				
	}
	
	/**
	 * Removes the mandatory leaves of the feature model
	 * 
	 * @param	f	parent feature of the tree where the mandatory leaves will be removed
	 * @return  true if the feature f is a mandatory leaf that must be removed
	 */
	public static boolean removeMandatoryLeaves(IFeatureStructure f) {
	
		if (!f.hasChildren()) {
			return f.isMandatory();
			
		} else if (!f.isAnd()) {
			for (IFeatureStructure fs : f.getChildren()) {
				removeMandatoryLeaves(fs);
			}
			
		} else {
			// You cannot remove elements when you are iterating the child features
			// It is necessary to store which features must be removed and later
			// remove them
			List<IFeatureStructure> fs_toRemove = new ArrayList<>();
			for (IFeatureStructure fs : f.getChildren()) {
				if (removeMandatoryLeaves(fs) == true) {
					fs_toRemove.add(fs);
				}
			}
			for(IFeatureStructure fs : fs_toRemove ) {
				// System.out.println("remove > " + fs.getFeature().getName());
				f.removeChild(fs);
			}
		}
		return false;
	}
	
	/**
	 * Counts the valid configurations in a feature model without textual or cross-tree constraints
	 * 
	 * @param 	f 	parent feature of the tree to count the valid configurations
	 * @return  number of valid configurations in the subtree
	 */
	public static BigInteger countConfigurations(IFeatureStructure f) {

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
		
		BigInteger count = BigInteger.ZERO;

		// default value for a leaf
		if (!f.hasChildren()) {
			count = BigInteger.ONE;
			
		// if the feature is an AND-group
		} else if (f.isAnd()) {
			count = BigInteger.ONE;
			for (IFeatureStructure fs : f.getChildren()) {
				count = count.multiply(countConfigurations(fs));
			}
			
		// if the feature is an alternative-group
		} else if (f.isAlternative()) {
			for (IFeatureStructure fs : f.getChildren()) {
				count = count.add(countConfigurations(fs));
			}
			
		// if the feature is an or-group
		} else if (f.isOr()) {
			count = BigInteger.ONE;
			for (IFeatureStructure fs : f.getChildren()) {
				count = count.multiply(countConfigurations(fs).add(BigInteger.ONE));
			}
			count = count.subtract(BigInteger.ONE);
			
		}
		
		// if the feature is optional, a one must be added to the count 
		if (!f.isMandatory()) {
			count = count.add(BigInteger.ONE);
		}
			
		// return the count
		return count;		
	}
	
}
