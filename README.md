# ClusterSourceFileRetriver
## What is this repository
This app helps retrieve the source code that corresponds to each cluster. As we know, each cluster contains as series of methods. In this app, we will create a txt file named after each cluster to contain all the source code and comments for the methods in that cluster.
## How?
This tool will go through all the chars and count `//`, `/*` and `*/` pairs, and `{` and `}` pairs to seperate comments and code as well as different methods. Then, it will parse the clustering results and match each method to those seperated texts.
## Input
The jar files of the two target systems, Tomcat 6.0 and 8.5, are in "tomcat60" and "tomcat85" respectively.
## Ouput
the output files are the txt files that start with "call_graph".
## How to Run?
in the root directory, do:
```
mvn install
// to generate call graph with method parameters populated 
java -jar target/javacg-0.1-SNAPSHOT-static.jar -n {input_dir} > {output_file_name} 
// to generate call graph with method parameters not populated, this is used for our project 
java -jar target/javacg-0.1-SNAPSHOT-static.jar -dn {input_dir} > {output_file_name}
```
