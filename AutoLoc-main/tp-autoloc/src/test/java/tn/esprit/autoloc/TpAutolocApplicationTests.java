package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.*;
import tn.esprit.autoloc.domain.enums.CategorieVehicule;
import tn.esprit.autoloc.domain.enums.RoleEmploye;
import tn.esprit.autoloc.domain.enums.StatutVehicule;
import tn.esprit.autoloc.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class TpAutolocApplicationTests {

    @Autowired
    private IAgenceRepository agenceRepository;
    @Autowired
    private IClientRepository clientRepository;
    @Autowired
    private IVehiculeRepository vehiculeRepository;
    @Autowired
    private IEmployeRepository employeRepository;
    @Autowired
    private IEquipementRepository equipementRepository;
    @Autowired
    private IMaintenanceRepository maintenanceRepository;
    @Autowired
    private IReservationRepository reservationRepository;
    @Autowired
    private IContratRepository contratRepository;
    @Autowired
    private IPaiementRepository paiementRepository;

    // ---------- Partie 1 : vérification des repositories ----------
    @Test
    void testRepositoriesExistent() {
        assertNotNull(agenceRepository);
        assertNotNull(employeRepository);
        assertNotNull(vehiculeRepository);
        assertNotNull(clientRepository);
        assertNotNull(reservationRepository);
        assertNotNull(contratRepository);
        assertNotNull(paiementRepository);
        assertNotNull(maintenanceRepository);
        assertNotNull(equipementRepository);
        System.out.println("Les 9 repositories sont bien injectés.");
    }

    // ---------- Partie 2 : AgenceRepository ----------
    @Test
    void testAgenceRepository() {
        Agence agence = new Agence();
        agence.setNom("AutoLoc Tunis");
        agence.setVille("Tunis");
        agence.setAdresse("Avenue Habib Bourguiba");
        agence.setTelephone("71000000");

        Agence saved = agenceRepository.save(agence);
        System.out.println("Agence enregistrée : id=" + saved.getId()
                + ", nom=" + saved.getNom() + ", ville=" + saved.getVille());
        assertNotNull(saved.getId());

        List<Agence> agences = agenceRepository.findAll();
        System.out.println("Nombre d'agences dans findAll : " + agences.size());
        assertFalse(agences.isEmpty());

        Optional<Agence> trouvee = agenceRepository.findById(saved.getId());
        assertTrue(trouvee.isPresent());
        System.out.println("findById : " + trouvee.get().getNom());

        assertTrue(agenceRepository.existsById(saved.getId()));
        System.out.println("count = " + agenceRepository.count());

        agenceRepository.deleteById(saved.getId());
        assertFalse(agenceRepository.existsById(saved.getId()));
        assertNull(agenceRepository.findById(saved.getId()).orElse(null));
        System.out.println("Agence supprimée.");
    }

    // ---------- Partie 3 : ClientRepository ----------
    @Test
    void testClientRepository() {
        Client c1 = new Client();
        c1.setNom("Ben Ali");
        c1.setPrenom("Sami");
        c1.setEmail("sami.benali@mail.com");
        c1.setTelephone("20111222");
        c1.setNumPermis("P12345");
        c1.setDateInscription(LocalDate.now());

        Client c2 = new Client();
        c2.setNom("Trabelsi");
        c2.setPrenom("Amel");
        c2.setEmail("amel.trabelsi@mail.com");
        c2.setTelephone("22333444");
        c2.setNumPermis("P67890");
        c2.setDateInscription(LocalDate.now());

        Client s1 = clientRepository.save(c1);
        Client s2 = clientRepository.save(c2);
        assertNotNull(s1.getId());
        assertNotNull(s2.getId());

        List<Client> clients = clientRepository.findAll();
        clients.forEach(c -> System.out.println("Client : " + c.getId()
                + " - " + c.getNom() + " " + c.getPrenom()));

        Optional<Client> trouve = clientRepository.findById(s1.getId());
        assertTrue(trouve.isPresent());
        System.out.println("findById : " + trouve.get().getEmail());

        assertTrue(clientRepository.existsById(s1.getId()));
        assertFalse(clientRepository.existsById(999999L));

        System.out.println("count = " + clientRepository.count());
        assertTrue(clientRepository.count() >= 2);

        // Nettoyage
        clientRepository.deleteById(s1.getId());
        clientRepository.deleteById(s2.getId());
    }

    // ---------- Partie 4 : VehiculeRepository ----------
    @Test
    void testVehiculeRepository() {
        Vehicule v = new Vehicule();
        v.setImmatriculation("123 TUN 4567");
        v.setMarque("Renault");
        v.setModele("Clio");
        v.setCategorie(CategorieVehicule.CITADINE);
        v.setTarifJournalier(new BigDecimal("80.00"));
        v.setStatut(StatutVehicule.DISPONIBLE);

        Vehicule saved = vehiculeRepository.save(v);
        assertNotNull(saved.getIdVehicule());

        vehiculeRepository.findAll().forEach(x -> System.out.println("Véhicule : "
                + x.getIdVehicule() + " - " + x.getMarque() + " " + x.getModele()));
        System.out.println("count = " + vehiculeRepository.count());

        // Modification puis save()
        StatutVehicule ancienStatut = saved.getStatut();
        saved.setTarifJournalier(new BigDecimal("95.00"));
        saved.setStatut(StatutVehicule.LOUE);
        Vehicule modifie = vehiculeRepository.save(saved);
        assertNotEquals(ancienStatut, modifie.getStatut());
        assertEquals(0, new BigDecimal("95.00").compareTo(modifie.getTarifJournalier()));
        assertEquals(StatutVehicule.LOUE, modifie.getStatut());
        System.out.println("Véhicule modifié : tarif=" + modifie.getTarifJournalier()
                + ", statut=" + modifie.getStatut());

        vehiculeRepository.deleteById(saved.getIdVehicule());
        assertFalse(vehiculeRepository.existsById(saved.getIdVehicule()));
    }

    // ---------- Partie 5 : exercice autonome ----------
    // 1) Employe
    @Test
    void testEmployeRepository() {
        Employe e = new Employe();
        e.setNom("Gharbi");
        e.setPrenom("Karim");
        e.setRole(RoleEmploye.AGENT);

        Employe saved = employeRepository.save(e);
        assertNotNull(saved.getId());
        System.out.println("Employé : " + saved.getNom() + " " + saved.getPrenom()
                + " (" + saved.getRole() + ")");

        assertFalse(employeRepository.findAll().isEmpty());
        assertTrue(employeRepository.existsById(saved.getId()));

        employeRepository.deleteById(saved.getId());
        assertFalse(employeRepository.existsById(saved.getId()));
    }

    // 2) Equipement
    @Test
    void testEquipementRepository() {
        Equipement eq = new Equipement();
        eq.setLibelle("GPS");

        Equipement saved = equipementRepository.save(eq);
        assertNotNull(saved.getId());
        System.out.println("Équipement : " + saved.getLibelle());

        Optional<Equipement> trouve = equipementRepository.findById(saved.getId());
        assertTrue(trouve.isPresent());
        assertEquals("GPS", trouve.get().getLibelle());
        assertTrue(equipementRepository.existsById(saved.getId()));

        equipementRepository.deleteById(saved.getId());
        assertFalse(equipementRepository.existsById(saved.getId()));
    }

    // 3) Maintenance
    @Test
    void testMaintenanceRepository() {
        Maintenance m = new Maintenance();
        m.setDateDebut(LocalDate.now());
        m.setDateFin(LocalDate.now().plusDays(2));
        m.setDescription("Vidange et contrôle des freins");

        Maintenance saved = maintenanceRepository.save(m);
        assertNotNull(saved.getId());
        System.out.println("Maintenance : " + saved.getDescription()
                + " du " + saved.getDateDebut() + " au " + saved.getDateFin());

        assertFalse(maintenanceRepository.findAll().isEmpty());
        assertTrue(maintenanceRepository.existsById(saved.getId()));

        maintenanceRepository.deleteById(saved.getId());
        assertFalse(maintenanceRepository.existsById(saved.getId()));
    }
}


