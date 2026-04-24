# Langage PDDL : Tours de hanoi
## Structuration du répertoire :

- `hanoi/` : Le fichier `domain.pddl` des tours de hanoi ainsi qu'un problème 3x3 (`hanoi3.pddl`) et 4x4 (`hanoi4.pddl`) demandé dans le rendu.
- `taquin/` : Le fichier `domain.pddl` du problème du taquin ainsi que des fichiers problèmes (3x3 et 4x4). On trouve également un répertoire `scripts/` contenant des scripts pythons qui nous ont assisté pour générer automatiquement des fichiers problèmes.
- `sokoban/` : Le fichier `domain.pddl` du problème de Sokoban ainsi que des fichiers problèmes qui nous ont servi pour l'interface graphique.
- `scripts/` : Des scripts pour tester rapidement le rendu
## Exécution
Simplement utiliser le script dédié :
```
./pddl4j.sh
```
Scripts pour tester rapidement :
```
./pddl4j-hanoi3x3.sh # Tours de Hanoï 3x3
./pddl4j-hanoi4x4.sh # Tours de Hanoï 4x4
```