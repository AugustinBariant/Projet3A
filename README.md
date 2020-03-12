# Projet3A
# Augustin Bariant
# Présentation du projet

Ce projet a pour objectif d'augmenter la vitesse de calcul des S-box de l'algorithme Serpent [1], finaliste à la compétition de l'Advanced Encryption Standard (AES) en 1997. Les S-box sont des permutations de quatre bits, que l'on essaie de factoriser en opérations processeur seulement, sur cinq registres. L'implementation est inspirée du papier "Speeding Up Serpent" [2], de Osvik, et est codée en java.

# Bibliographie

1. "Serpent: A Proposal for the Advanced Encryption Standard", Ross Anderson, Eli Biham and Lars Knudsen, 1997, https://www.cl.cam.ac.uk/~rja14/Papers/serpent.pdf
2. "Speeding Up Serpent", Dag Arne Osvik, 2000, https://www.ii.uib.no/~osvik/pub/aes3.pdf

# Structure du projet

Le projet est constitué de 5 classes:

-La classe Optimizer: c'est la classe de base sur laquelle on travaille. Chaque instance correspond à une configuration, la permutation donnée en argument étant statique.

-La classe Instruction: cette classe a pour but d'énumérer les 5 instructions possibles, et de les linker à une id.

-La classe FullInstruction: cette classe a un attribut Instruction, et complète l'instruction avec des attributs correspondants aux colonnes sur laquelle l'instruction doit agir.

-La classe InstructionCycle: cette classe produit des FullInstructions cycliques afin de les appliquer à une configuration (instance de Optimizer).

-La classe WorkspaceComparator: cette classe compare deux configurations pour tester leur équivalence.

-------------------------------------------
La classe Optimizer:

La principale méthode est

	-public boolean ApplyInstruction(FullInstruction f):

On peut appliquer des instructions à une configuration: Cela modifie l'instance, et teste si la configuration résultante doit être gardée ou doit être jetée parce qu'elle ne satisfait pas certaines conditions.

Cest tests sont produits avec la fonction 

	-public boolean Check(FullInstruction f, boolean[] L1, boolean[] L2)

Ce check lance toutes les fonctions qui vérifient chaque condition:

	-CheckPermutation() qui vérifie si deux colonnes ne sont pas égales,
	-UpdateNegateAndCheck(f) qui vérifie si l'instruction est Not et que la colonne affectée est marquée par le marqueur Neg. Si tout va bien, cette fonction update le tableau Neg,
	-CheckCopy(f) qui vérifie si l'instruction est diférente de Mov et que la colonne modifiée est la copie d'une autre colonne,
	-UpdateNumberOfMatch() qui vérifie si le nombre de lignes qui correspondent à la permutation finale augmente, si ce nombre diminue, le check ne passe pas. De plus, cette fonction update le nombre de matchs,
	-UpdateAndCheckRead(f) qui checke si on a mov vers une ligne non déjà lue,
	-CheckTrueFalse(L2) qui checke si une ligne est toute vraie ou toute fausse, et l'invalide le cas échéant.
	-CheckAndUpdateTree() qui checke si une configuration a déjà été trouvée (grace à un attribut statique qui contient un treeSet de WorkspaceComparator);

---------------------------------------------
La fonction main de Optimizer:

On crée une liste avec plein d'instances d'Optimizer. On applique toutes les instructions possibles grâce à InstructionCycle, puis on ajoute toutes les configuration valides dans la liste.
Tant que le nombre de lignes correspondantes à la permutation cible n'est pas égal à 4, on continue.


----------------------------------------------

Les autre classes sont seulement des outils pour notre classe principale.


