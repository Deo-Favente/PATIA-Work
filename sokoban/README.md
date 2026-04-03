# Play the game on [CodinGame](https://www.codingame.com/training/hard/sokoban)

Maven is needed.

# Installation
Dans ce dossier sokoban :
```
mvn install:install-file \
   -Dfile=pddl4j-4.0.0.jar \
   -DgroupId=fr.uga \
   -DartifactId=pddl4j \
   -Dversion=4.0.0 \
   -Dpackaging=jar \
   -DgeneratePom=true \
   -Djava.net.useSystemProxies=true
 ```  
# Utilisation
````
mvn clean -Djava.net.useSystemProxies=true
mvn compile -Djava.net.useSystemProxies=true
````

````
java --add-opens java.base/java.lang=ALL-UNNAMED \
      -server -Xms2048m -Xmx2048m \
      -cp "$(mvn dependency:build-classpath -Dmdep.outputFile=/dev/stdout -q):target/test-classes/:target/classes" \
      sokoban.SokobanMain <nom-fichier-json>
````
OU
````
mvn package 
````
```
java --add-opens java.base/java.lang=ALL-UNNAMED \
      -server -Xms2048m -Xmx2048m \
      -cp target/sokoban-1.0-SNAPSHOT-jar-with-dependencies.jar \
      sokoban.SokobanMain <nom-fichier-json>
```
Sorry ```mvn exec:java``` has still an open issue ("Directory src/main/resources/view/assets not found.")

See planning solutions at http://localhost:4200/test.html
