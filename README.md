# Projet3A
# Augustin Bariant

Le projet est constitu� de 5 classes:

-La classe Optimizer: c'est la classe de base sur laquelle on travaille. Chaque instance correspond � une configuration, la permutation donn�e en argument �tant statique.

-La classe Instruction: cette classe a pour but d'�num�rer les 5 instructions possibles, et de les linker � une id.

-La classe FullInstruction: cette classe a un attribut Instruction, et compl�te l'instruction avec des attributs correspondants aux colonnes sur laquelle l'instruction doit agir.

-La classe InstructionCycle: cette classe produit des FullInstructions cycliques afin de les appliquer � une configuration (instance de Optimizer).

-La classe WorkspaceComparator: cette classe compare deux configurations pour tester leur �quivalence.

-------------------------------------------
La classe Optimizer:

La principale m�thode est

	-public boolean ApplyInstruction(FullInstruction f):

On peut appliquer des instructions � une configuration: Cela modifie l'instance, et teste si la configuration r�sultante doit �tre gard�e ou doit �tre jet�e parce qu'elle ne satisfait pas certaines conditions.

Cest tests sont produits avec la fonction 

	-public boolean Check(FullInstruction f, boolean[] L1, boolean[] L2)

Ce check lance toutes les fonctions qui v�rifient chaque condition:

	-CheckPermutation() qui v�rifie si deux colonnes ne sont pas �gales,
	-UpdateNegateAndCheck(f) qui v�rifie si l'instruction est Not et que la colonne affect�e est marqu�e par le marqueur Neg. Si tout va bien, cette fonction update le tableau Neg,
	-CheckCopy(f) qui v�rifie si l'instruction est dif�rente de Mov et que la colonne modifi�e est la copie d'une autre colonne,
	-UpdateNumberOfMatch() qui v�rifie si le nombre de lignes qui correspondent � la permutation finale augmente, si ce nombre diminue, le check ne passe pas. De plus, cette fonction update le nombre de matchs,
	-UpdateAndCheckRead(f) qui checke si on a mov vers une ligne non d�j� lue,
	-CheckTrueFalse(L2) qui checke si une ligne est toute vraie ou toute fausse, et l'invalide le cas �ch�ant.
	-CheckAndUpdateTree() qui checke si une configuration a d�j� �t� trouv�e (grace � un attribut statique qui contient un treeSet de WorkspaceComparator);

---------------------------------------------
La fonction main de Optimizer:

On cr�e une liste avec plein d'instances d'Optimizer. On applique toutes les instructions possibles gr�ce � InstructionCycle, puis on ajoute toutes les configuration valides dans la liste.
Tant que le nombre de lignes correspondantes � la permutation cible n'est pas �gal � 4, on continue.


----------------------------------------------

Les autre classes sont seulement des outils pour notre classe principale.


