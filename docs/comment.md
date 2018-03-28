# Comment to the BPNCC paper</h1>

**Abstract:**
The "Binary Pattern for Nested Cardinality Constraints for Software Product Line of 
IoT-Based Feature Models" paper presents as its main contribution an algorithm that 
calculates the number of configurations that satisfy the constraints of a feature 
model without textual or cross-tree constraints.
However, the well-known method for counting valid configurations in these models 
apparently outperforms the BPNCC proposal.
As part of the solution, BPNCC pre-processes the feature mdel to remove the mandatory 
features at the leaves of the tree to improve the processing time.
Apparently, this model transformation increases but not decreases the time.
Regretfully, the BPNCC paper does not compare that solution with the existing algorithm.
This page describes an experimental implementation of the existing algorithm, and 
compares its execution time with and without the proposed pre-processing.

Author: [Jaime Chavarriaga](https://soft.vub.ac.be/soft/users/jchavarr). 

---

## Introduction

The ["Binary Pattern for Nested Cardinality Constraints for Software Product Line of IoT-Based Feature Models"](http://ieeexplore.ieee.org/document/7875079/?reload=true) paper, on the one hand, does not 
compare their proposal to the existing algorithm to count the number of valid 
configurations for a feature model without textual or cross-tree constraints. 
And, on the other hand, it does not include an evaluation of the proposed
pre-processing step that removes the mandatory features 
at the leaves of the feature tree.

This page reports an experimental implementation of the existing algorithm that
shows the effect of pre-processing the feature model. The source code is available 
in a [Github project](https://github.com/JaimeChavarriaga/comment-to-bpncc). 

## Existing algorithm to count product configurations

Many authors have proposed techniques for counting the valid configurations in a 
feature model [2-4].
There is a well-known algorithm that applies to feature trees, i.e., feature models 
without textual constraints [5-10].
This algorithm has been explained using the following description [10]: 

	count root(c)  = count(c)
	count mandatory(c)  = count(c)
	count optional(c) = count(c) + 1
	count and(c1, . . . , cn)  = count(c1) ∗ . . . ∗ count(cn)
	count alternative(c1, . . . , cn)  = count(c1) + . . . + count(cn)
	count or(c1, . . . , cn)  = (count(c1) + 1) ∗ . . . ∗ (count(cn) + 1) − 1
	count leaf  = 1
		    
The following is a method that implements that algorithm:

	public static long countConfigurations(IFeatureStructure f) {
		
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

 
## BPNCC proposal

The BPNCC approach is not well explained in the paper. 
It comprises three modules that, apparently, performs many combinatorics.
For instance, the third module (figure  6) has a line that “Generate all possible 2ˆL-1 combinations”. 

Although there is not a proper description of the algorithm, it is possible to perform a simple comparison.
The Figure 1 in the BPNCC paper shows a simple feature model. 
The tables 1 to 3 of that paper describes the solution using the proposed algorithm.
According to that, the number of products can be calculated by (1) calculating some values for each combination of features, (2) summing that values for each group and (3) multiplying these sums. 
According to the tables, it is necessary to calculate 7 values for the group F1, 4 values for the groups F2 and F3, and three values for the group F4 in the BPNCC algorithm. 
In contrast, the existing algorithm calculates just one value for each group.

Note that, while the existing technique may be performed easily by hand, the proposed BPNCC method looks too complex to be performed by hand.
Regretfully, the paper does not include a comparison nor discusses the complexity or scalability of the proposed solution.


## Preprocessing proposed in BPNCC

As a part of the BPNCC proposal, it pre-processes the feature model by removing the mandatory features at the leaves of the tree.
Removing that features does not affect the total number of valid configurations.
It is performed to reduce the number of features in the model and, possibly, reduce the processing time of the counting process.

There are many proposals to simplify feature models [11,12]. 
They can be used to pre-process the models. 
However, they may reduce the processing time when the feature models have textual constraints and the algorithm require to use solvers or when the operations require arbitrary-precision operations.
If the counting algorithm is performed using data types such as integers or long integers, it may increase the processing time instead of reducing it.


## Evaluation

The following table compares the processing time required to count the valid configurations in the feature models included in the BPNCC paper.
In addition, it shows processing time for the feature models of the Linux 2.6.33.3 and Busybox 1.18.0 that require arbitrary-precision operations.
It shows the average time of counting the products in 10 tests where each one performs the count 10 times.
We executed the procedure multiple times in the same program because the the JIT compiler optimizes some operations, and therefore reduces the processing time, during the execution.
Note that the pre-processing increases the processing time instead of reducing it.

**Using long integers:**

|                 | # configs   | only counting (ns)  | # configs   | removing (ns)  | and counting (ns)  | total (ns) | diff   |
|-----------------|------------:|---------------:|------------:|---------------:|-------------------:|-----------:|-------:|
| IoT FM 1        | 18          | 32806.40       | 18          | 27874.08       | 13030.37           | 40904.45   | 24.68% |
| IoT FM 2        | 6188        | 46301.90       | 6188        | 40678.43       | 28351.98           | 69030.41   | 49.09% |

**Using BigInteger:**

|                 | # configs   | only counting (ns)  | # configs   | removing (ns)  | and counting (ns)  | total (ns) | diff   |
|-----------------|------------:|---------------:|------------:|---------------:|-------------------:|-----------:|-------:|
| IoT FM 1        | 18          | 64580.38       | 18          | 31863.53       | 34692.24           | 66555.77   | 3.06%  |
| IoT FM 2        | 6188        | 101179.79      | 6188        | 39603.18       | 69499.81           | 109102.99  | 7.83%  |
| Busybox 1.18.0  | 7.7333E+212 | 1083772.74     | 7.7333E+212 | 902456.14      | 813551.67          | 1716007.81 | 58.34% |
| Linux 2.66.33.3 | 3.901E+1672 | 2920557.12     | 3.901E+1672 | 1997061.87     | 2702654.08         | 4699715.95 | 60.92% |


## Conclusions

The “Binary Pattern for Nested Cardinality Constraints forSoftware Product Line of IoT-Based Feature Models” paper introduces an  algorithm  that  is  apparently  more  complex and harder to implement  than  the  existing  algorithms. In addition, it includes a pre-processing step that may increase the processing time when the existing algorithm is used.
Finally, the paper does not describe clearly the algorithm neither compares their solution with existing solutions.  
        
## References

1. A. Abbas, I. F. Siddiqui, S. U. J. Lee, and A. K. Bashir, “Binary pattern fornested cardinality constraints for software product line of iot-based featuremodels,” IEEE Access, vol. 5, pp. 3971–3980, 2017.
2. D.  Benavides,  S.  Segura,  and  A.  Ruiz-Cortés,  “Automated  analysis of feature models 20 years later: A literature review,” Information Systems,vol. 35, no. 6, pp. 615–636, 2010.
3. D. Benavides, A. R. Cortés, P. Trinidad, and S. Segura, “A survey on the automated analyses of feature models.” in XV Jornadas de Ingeniería delSoftware y Bases de Datos (JISBD 2006), 2006, pp. 367–376.
4. R. Heradio, D. Fernandez-Amoros, J. A. Cerrada, and I. Abad, “A literature review on feature diagram product counting and its usage in software product line economic models,” International Journal of Software Engineering and Knowledge Engineering, vol. 23, no. 08, pp. 1177–1204, 2013.
5. A. Van Deursen and P. Klint, “Domain-specific language design requires feature descriptions,” Journal of Computing and Information Technology, vol. 10, no. 1, pp. 1–17, 2002.
6. T. von der Maßen and H. Lichter, “Determining the variation degree of feature models,” in 10th International Conference on Software Product Lines (SPLC 2005). Springer, 2005, pp. 82–88.</li>
7. P. Van Den Broek and I. Galvão, “Analysis of feature models using generalised feature trees.” in Third International Workshop on Variability Modelling of Software-Intensive Systems (VaMoS 2009), 2009, pp. 29–35.
8. D. Fernandez-Amoros, R. H. Gil, and J. C. Somolinos, “Inferring information from feature diagrams to product line economic models,” in 13th International Software Product Line Conference (SPLC 2009). Carnegie Mellon University, 2009, pp. 41–50.</li>
9. D. Fernandez-Amoros, R. Heradio, J. A. Cerrada, and C. Cerrada, “A scalable approach to exact model and commonality counting for extended feature  models,” IEEE Transactions on Software Engineering, vol. 40, no. 9, pp. 895–910, 2014.
10. S.  Apel,  D.  Batory,  C.  Kästner,  and  G.  Saake,  Software  Product  Lines. Berlin, Heidelberg: Springer Berlin Heidelberg, 2013, pp. 3–15.
11. H. Yan,  W. Zhang,  H. Zhao, and  H. Mei,  “An  optimization strategy to feature models’ verification by eliminating verification-irrelevant featuresand constraints,” in Formal Foundations of Reuse and Domain Engineering. Springer Berlin Heidelberg, 2009, pp. 65–75.
12. S. Segura, “Automated analysis of feature models using atomic sets,” in 12th International Conference of Software Product Lines (SPLC 2008),Second Volume (Workshops), 2008, pp. 201–207.

---
© Jaime Chavarriaga et al. 2018
