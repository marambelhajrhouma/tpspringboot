# Notes - Couche Repository (Atelier 3)

## Choix d'interface

Les neuf repositories du package `tn.esprit.autoloc.repository` étendent `JpaRepository<Entité, Long>`.
`JpaRepository` cumule CRUD, listes, tri, pagination et méthodes liées au contexte de persistance (`flush`, `saveAndFlush`, `getReferenceById`...).

| Interface | Étend | Justification |
|---|---|---|
| IContratRepository | JpaRepository<Contrat, Long> | CRUD complet, `findAll` renvoie une List, `saveAndFlush` disponible. |
| IAgenceRepository | JpaRepository<Agence, Long> | Interface complète pour JPA (CRUD, listes, tri, pagination, flush). |
| IEmployeRepository | JpaRepository<Employe, Long> | Même justification. |
| IVehiculeRepository | JpaRepository<Vehicule, Long> | Même justification. |
| IEquipementRepository | JpaRepository<Equipement, Long> | Même justification. |
| IClientRepository | JpaRepository<Client, Long> | Même justification. |
| IReservationRepository | JpaRepository<Reservation, Long> | Même justification. |
| IPaiementRepository | JpaRepository<Paiement, Long> | Créée pour lire les paiements. Dans la composition Contrat-Paiement, le code métier passe par le Contrat pour créer ou retirer un paiement. |
| IMaintenanceRepository | JpaRepository<Maintenance, Long> | Même justification. |

## Points d'attention

- **Iterable vs List** : `CrudRepository.findAll()` renvoie un `Iterable`, alors que `JpaRepository.findAll()` renvoie une `List` (plus pratique : `size()`, `get(i)`, `stream()`).
- **Suppressions en lot** : `deleteAllInBatch` et `deleteAllByIdInBatch` contournent le contexte de persistance. La cascade et l'`orphanRemoval` ne s'appliquent pas : supprimer ainsi des contrats laisserait des paiements orphelins ou ferait échouer la requête sur la contrainte de clé étrangère.
- **Pas de `@Repository`** : Spring Data génère le proxy au démarrage (log `Found 9 JPA repository interfaces`).

## Anomalies SonarQube for IDE

| Anomalie | Règle / explication | Correction apportée |
|---|---|---|
| Import dupliqué (`jakarta.persistence.*` importé deux fois) dans `Vehicule.java` | `java:S1128` : les imports en double ou inutiles doivent être supprimés | Suppression de la ligne en double |
| Import dupliqué (`jakarta.persistence.*` importé deux fois) dans `Equipement.java` | `java:S1128` : les imports en double ou inutiles doivent être supprimés | Suppression de la ligne en double |
| Imports inutilisés (`BigDecimal`, `LocalDate`, `enums.*`) dans `Equipement.java` | Imports non utilisés = code mort | Suppression via Optimize Imports (Ctrl+Alt+O) |

Après correction, l'analyse affiche **No Issues** et `./mvnw test` donne `BUILD SUCCESS` (7 tests, 0 échec).
