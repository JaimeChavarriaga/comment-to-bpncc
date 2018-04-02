# Comment to BPNCC paper

Comparison of the algorithm to count valid configurations of a feature model without constraints with and without the removing mandatory leave features. 
This pre-processing, suggested in the [BPNCC paper](http://ieeexplore.ieee.org/document/7875079/) increases the total processing time, instead of reducing it, for that type of feature models.

## Evaluation

If you are interested, you may check some results of the evaluation in the [docs/comment.md file](https://github.com/JaimeChavarriaga/comment-to-bpncc/blob/master/docs/comment.md). 
In addition, you can use the included software to perform further evaluations and comparisons.

## Installation

This software is an Eclipse project. It can be compiled using Eclipse or an ant build file.

1. To compile the software:

    - Import the project into an Eclipse IDE. The project will be compiled automatically.
    - or run the ant build file by executing `ant`
   
2. To run the software,

    - In Windows, execute the `all-tests.bat` batch file.
    - In Linux, execute the `all-tests.sh` shell file. 
    
---

## License
This software uses the [FeatureIDE library](https://featureide.github.io/). 
It includes libraries from [SAT4J](http://www.sat4j.org/) and [Antlr](http://www.antlr.org/) among others.
FeatureIDE and this code are released under [L-GPL version 3](https://www.gnu.org/licenses/lgpl-3.0.en.html).
