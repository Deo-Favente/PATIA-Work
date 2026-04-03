# Sokoban : Interface visuelle
Jeu : [CodinGame](https://www.codingame.com/training/hard/sokoban) \
Testé sur la VM avec maven installé.
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
```
mvn compile -Djava.net.useSystemProxies=true
```
```
java --add-opens java.base/java.lang=ALL-UNNAMED \
      -server -Xms2048m -Xmx2048m \
      -cp "$(mvn dependency:build-classpath -Dmdep.outputFile=/dev/stdout -q):target/test-classes/:target/classes" \
      sokoban.SokobanMain <nom-fichier-json>
```
OU
```
mvn package -Djava.net.useSystemProxies=true
```
```
java --add-opens java.base/java.lang=ALL-UNNAMED \
      -server -Xms2048m -Xmx2048m \
      -cp target/sokoban-1.0-SNAPSHOT-jar-with-dependencies.jar \
      sokoban.SokobanMain <nom-fichier-json>
```
Nettoyage avec 
```
mvn clean -Djava.net.useSystemProxies=true
```
Voir les solutions : http://localhost:4200/test.html

# Note importante
Des limites de 25 secondes sur le temps d'éxecution étant imposées par la lib donnée, il arrive parfois que l'exécution de certains tests ne marchent pas (0/1 étapes). La seule solution trouvée est de réessayer ou de prendre un autre test plus rapide :)
