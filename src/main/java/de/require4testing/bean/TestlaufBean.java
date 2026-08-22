package de.require4testing.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.require4testing.dao.TestdurchfuehrungDAO;
import de.require4testing.dao.TesterDAO;
import de.require4testing.dao.TestfallDAO;
import de.require4testing.dao.TestlaufDAO;
import de.require4testing.model.Testdurchfuehrung;
import de.require4testing.model.Tester;
import de.require4testing.model.Testfall;
import de.require4testing.model.Testlauf;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named
@ViewScoped
public class TestlaufBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Testlauf testlauf;

    private List<Testlauf> testlaeufe;
    private List<Testlauf> konfigurierteTestlaeufe;

    private List<Tester> tester;
    private List<Testfall> testfaelle;

    private Long ausgewaehlterTestlaufId;
    private Long ausgewaehlteTesterId;

    private List<Long> ausgewaehlteTestfallIds;

    private Map<Long, String> testerNachTestlauf;
    private Map<Long, String> testfaelleNachTestlauf;

    private final TestlaufDAO testlaufDAO =
            new TestlaufDAO();

    private final TestfallDAO testfallDAO =
            new TestfallDAO();

    private final TesterDAO testerDAO =
            new TesterDAO();

    private final TestdurchfuehrungDAO testdurchfuehrungDAO =
            new TestdurchfuehrungDAO();

    @PostConstruct
    public void init() {

        testlauf = new Testlauf();

        testlaeufe = testlaufDAO.findeAlle();

        tester = testerDAO.findeAlle();

        testfaelle = testfallDAO.findeAlle();

        ausgewaehlteTestfallIds =
                new ArrayList<>();

        testerNachTestlauf =
                new HashMap<>();

        testfaelleNachTestlauf =
                new HashMap<>();

        ladeKonfigurationsuebersicht();
    }

    /**
     * Legt einen neuen Testlauf an.
     */
    public void speichern() {

        testlaufDAO.speichern(testlauf);

        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(
                        FacesMessage.SEVERITY_INFO,
                        "Testlauf erfolgreich gespeichert.",
                        null));

        testlauf = new Testlauf();

        testlaeufe = testlaufDAO.findeAlle();

        ladeKonfigurationsuebersicht();
    }

    /**
     * Lädt die bestehende Konfiguration des ausgewählten Testlaufs.
     */
    public void testlaufGewechselt(
            AjaxBehaviorEvent event) {

        ausgewaehlteTestfallIds =
                new ArrayList<>();

        ausgewaehlteTesterId = null;

        if (ausgewaehlterTestlaufId == null) {
            return;
        }

        Testlauf ausgewaehlterTestlauf =
                findeAusgewaehltenTestlauf();

        if (ausgewaehlterTestlauf == null) {
            return;
        }

        if (ausgewaehlterTestlauf.getTester() != null) {

            ausgewaehlteTesterId =
                    ausgewaehlterTestlauf
                            .getTester()
                            .getId();
        }

        List<Testdurchfuehrung> testdurchfuehrungen =
                testdurchfuehrungDAO.findeFuerTestlauf(
                        ausgewaehlterTestlaufId);

        for (Testdurchfuehrung testdurchfuehrung
                : testdurchfuehrungen) {

            if (testdurchfuehrung.getTestfall() != null) {

                ausgewaehlteTestfallIds.add(
                        testdurchfuehrung
                                .getTestfall()
                                .getId());
            }
        }
    }

    /**
     * Speichert die Zuordnung eines Testlaufs
     * zu einem Tester und den ausgewählten Testfällen.
     */
    public void zuordnen() {

        if (ausgewaehlterTestlaufId == null) {

            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            "Bitte wählen Sie einen Testlauf aus.",
                            null));

            return;
        }

        if (ausgewaehlteTesterId == null) {

            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            "Bitte wählen Sie einen Tester aus.",
                            null));

            return;
        }

        if (ausgewaehlteTestfallIds == null
                || ausgewaehlteTestfallIds.isEmpty()) {

            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            "Bitte wählen Sie mindestens einen Testfall aus.",
                            null));

            return;
        }

        Testlauf ausgewaehlterTestlauf =
                findeAusgewaehltenTestlauf();

        Tester ausgewaehlterTester =
                findeAusgewaehltenTester();

        if (ausgewaehlterTestlauf == null) {

            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            "Der ausgewählte Testlauf konnte nicht gefunden werden.",
                            null));

            return;
        }

        if (ausgewaehlterTester == null) {

            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            "Der ausgewählte Tester konnte nicht gefunden werden.",
                            null));

            return;
        }

        ausgewaehlterTestlauf.setTester(
                ausgewaehlterTester);

        testlaufDAO.aktualisieren(
                ausgewaehlterTestlauf);

        for (Long testfallId
                : ausgewaehlteTestfallIds) {

            Testfall testfall =
                    findeTestfall(testfallId);

            if (testfall != null
                    && !istBereitsZugeordnet(
                            ausgewaehlterTestlauf,
                            testfall)) {

                Testdurchfuehrung testdurchfuehrung =
                        new Testdurchfuehrung();

                testdurchfuehrung.setTestlauf(
                        ausgewaehlterTestlauf);

                testdurchfuehrung.setTestfall(
                        testfall);

                testdurchfuehrung.setErgebnis(
                        "OFFEN");

                testdurchfuehrungDAO.speichern(
                        testdurchfuehrung);
            }
        }

        testlaeufe =
                testlaufDAO.findeAlle();

        ladeKonfigurationsuebersicht();

        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(
                        FacesMessage.SEVERITY_INFO,
                        "Zuordnung erfolgreich gespeichert.",
                        null));
    }

    /**
     * Baut die Übersicht der bereits konfigurierten Testläufe auf.
     */
    private void ladeKonfigurationsuebersicht() {

        konfigurierteTestlaeufe =
                new ArrayList<>();

        testerNachTestlauf =
                new HashMap<>();

        testfaelleNachTestlauf =
                new HashMap<>();

        if (testlaeufe == null) {
            return;
        }

        for (Testlauf testlauf
                : testlaeufe) {

            if (testlauf.getTester() == null
                    || testlauf.getId() == null) {

                continue;
            }

            List<Testdurchfuehrung> testdurchfuehrungen =
                    testdurchfuehrungDAO.findeFuerTestlauf(
                            testlauf.getId());

            if (testdurchfuehrungen.isEmpty()) {
                continue;
            }

            konfigurierteTestlaeufe.add(
                    testlauf);

            testerNachTestlauf.put(
                    testlauf.getId(),
                    testlauf
                            .getTester()
                            .getName());

            StringBuilder testfallNamen =
                    new StringBuilder();

            for (Testdurchfuehrung testdurchfuehrung
                    : testdurchfuehrungen) {

                if (testdurchfuehrung.getTestfall() == null) {
                    continue;
                }

                if (testfallNamen.length() > 0) {
                    testfallNamen.append(", ");
                }

                testfallNamen.append(
                        testdurchfuehrung
                                .getTestfall()
                                .getTitel());
            }

            testfaelleNachTestlauf.put(
                    testlauf.getId(),
                    testfallNamen.toString());
        }
    }

    /**
     * Prüft, ob ein Testfall bereits einem Testlauf
     * zugeordnet wurde.
     */
    private boolean istBereitsZugeordnet(
            Testlauf testlauf,
            Testfall testfall) {

        List<Testdurchfuehrung> testdurchfuehrungen =
                testdurchfuehrungDAO.findeFuerTestlauf(
                        testlauf.getId());

        for (Testdurchfuehrung testdurchfuehrung
                : testdurchfuehrungen) {

            if (testdurchfuehrung.getTestfall() != null
                    && testfall.getId().equals(
                            testdurchfuehrung
                                    .getTestfall()
                                    .getId())) {

                return true;
            }
        }

        return false;
    }

    private Testlauf findeAusgewaehltenTestlauf() {

        if (ausgewaehlterTestlaufId == null
                || testlaeufe == null) {

            return null;
        }

        for (Testlauf eintrag
                : testlaeufe) {

            if (ausgewaehlterTestlaufId.equals(
                    eintrag.getId())) {

                return eintrag;
            }
        }

        return null;
    }

    private Tester findeAusgewaehltenTester() {

        if (ausgewaehlteTesterId == null
                || tester == null) {

            return null;
        }

        for (Tester eintrag
                : tester) {

            if (ausgewaehlteTesterId.equals(
                    eintrag.getId())) {

                return eintrag;
            }
        }

        return null;
    }

    private Testfall findeTestfall(
            Long testfallId) {

        if (testfallId == null
                || testfaelle == null) {

            return null;
        }

        for (Testfall testfall
                : testfaelle) {

            if (testfallId.equals(
                    testfall.getId())) {

                return testfall;
            }
        }

        return null;
    }

    public Testlauf getTestlauf() {
        return testlauf;
    }

    public void setTestlauf(
            Testlauf testlauf) {
        this.testlauf = testlauf;
    }

    public List<Testlauf> getTestlaeufe() {
        return testlaeufe;
    }

    public void setTestlaeufe(
            List<Testlauf> testlaeufe) {
        this.testlaeufe = testlaeufe;
    }

    public List<Testlauf> getKonfigurierteTestlaeufe() {
        return konfigurierteTestlaeufe;
    }

    public List<Tester> getTester() {
        return tester;
    }

    public void setTester(
            List<Tester> tester) {
        this.tester = tester;
    }

    public List<Testfall> getTestfaelle() {
        return testfaelle;
    }

    public void setTestfaelle(
            List<Testfall> testfaelle) {
        this.testfaelle = testfaelle;
    }

    public Long getAusgewaehlterTestlaufId() {
        return ausgewaehlterTestlaufId;
    }

    public void setAusgewaehlterTestlaufId(
            Long ausgewaehlterTestlaufId) {

        this.ausgewaehlterTestlaufId =
                ausgewaehlterTestlaufId;
    }

    public Long getAusgewaehlteTesterId() {
        return ausgewaehlteTesterId;
    }

    public void setAusgewaehlteTesterId(
            Long ausgewaehlteTesterId) {

        this.ausgewaehlteTesterId =
                ausgewaehlteTesterId;
    }

    public List<Long> getAusgewaehlteTestfallIds() {
        return ausgewaehlteTestfallIds;
    }

    public void setAusgewaehlteTestfallIds(
            List<Long> ausgewaehlteTestfallIds) {

        this.ausgewaehlteTestfallIds =
                ausgewaehlteTestfallIds;
    }

    public Map<Long, String> getTesterNachTestlauf() {
        return testerNachTestlauf;
    }

    public Map<Long, String> getTestfaelleNachTestlauf() {
        return testfaelleNachTestlauf;
    }
}