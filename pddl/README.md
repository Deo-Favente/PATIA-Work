# Langage PDDL : Tours de hanoi
## Structuration du répertoire :

- `hanoi/` : Le fichier `domain.pddl` des tours de hanoi ainsi qu'un problème 3 piquets 3 disques (`hanoi3.pddl`) et 3 piquets 4 disques (`hanoi4.pddl`) demandé dans le rendu.
- `taquin/` : Le fichier `domain.pddl` du problème du taquin ainsi que des fichiers problèmes (3x3 et 4x4). On trouve également un répertoire `scripts/` contenant des scripts pythons qui nous ont assisté pour générer automatiquement des fichiers problèmes.
- `sokoban/` : Le fichier `domain.pddl` du problème de Sokoban ainsi que des fichiers problèmes qui nous ont servi pour l'interface graphique.
## Exécution
Simplement utiliser le script dédié :
```
./pddl4j.sh
```
Scripts pour tester rapidement :
```
./pddl4j-hanoi3x3.sh # Tours de Hanoï 3 piquets 3 disques
./pddl4j-hanoi4x4.sh # Tours de Hanoï 3 piquets 4 disques
./pddl4j-taquin3x3.sh # Taquin 3x3
./pddl4j-taquin4x4.sh # Taquin 4x4
```