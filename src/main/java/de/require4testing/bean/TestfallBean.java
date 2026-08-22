package de.require4testing.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.require4testing.dao.AnforderungDAO;
import de.require4testing.dao.TestfallDAO;
import de.require4testing.model.Anforderung;
import de.require4testing.model.Testfall;
import jakarta.annotation.PostConstruct;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named
@ViewScoped
public class TestfallBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Testfall testfall;
    private Long ausgewaehlteAnforderungsId;

    private List<Anforderung> anforderungen;
    private List<Testfall> testfaelle;

    private final AnforderungDAO anforderungDAO =
            new AnforderungDAO();

    private final TestfallDAO testfallDAO =
            new TestfallDAO();

    @PostConstruct
    public void init() {

        testfall = new Testfall();

        anforderungen = anforderungDAO.findeAlle();

        testfaelle = new ArrayList<>();

        if (!anforderungen.isEmpty()) {

            ausgewaehlteAnforderungsId =
                    anforderungen.get(0).getId();

            ladeTestfaelle();
        }
    }

    /**
     * Speichert einen neuen Testfall für die ausgewählte Anforderung.
     */
    public void speichern() {

        Anforderung anforderung =
                findeAusgewaehlteAnforderung();

        if (anforderung == null) {
            throw new IllegalStateException(
                    "Keine gültige Anforderung ausgewählt.");
        }

        testfall.setAnforderung(anforderung);

        testfallDAO.speichern(testfall);

        testfall = new Testfall();

        ladeTestfaelle();
    }

    /**
     * Wird aufgerufen, wenn eine andere Anforderung
     * im Auswahlfeld ausgewählt wird.
     */
    public void anforderungGewechselt(
            AjaxBehaviorEvent event) {

        ladeTestfaelle();
    }

    /**
     * Lädt ausschließlich die Testfälle,
     * die der aktuell ausgewählten Anforderung zugeordnet sind.
     */
    private void ladeTestfaelle() {

        testfaelle = new ArrayList<>();

        if (ausgewaehlteAnforderungsId == null) {
            return;
        }

        List<Testfall> alleTestfaelle =
                testfallDAO.findeAlle();

        for (Testfall eintrag : alleTestfaelle) {

            if (eintrag.getAnforderung() != null
                    && ausgewaehlteAnforderungsId.equals(
                            eintrag.getAnforderung().getId())) {

                testfaelle.add(eintrag);
            }
        }
    }

    /**
     * Ermittelt die aktuell ausgewählte Anforderung.
     */
    private Anforderung findeAusgewaehlteAnforderung() {

        if (ausgewaehlteAnforderungsId == null) {
            return null;
        }

        for (Anforderung anforderung : anforderungen) {

            if (ausgewaehlteAnforderungsId.equals(
                    anforderung.getId())) {

                return anforderung;
            }
        }

        return null;
    }

    public Testfall getTestfall() {
        return testfall;
    }

    public void setTestfall(Testfall testfall) {
        this.testfall = testfall;
    }

    public Long getAusgewaehlteAnforderungsId() {
        return ausgewaehlteAnforderungsId;
    }

    public void setAusgewaehlteAnforderungsId(
            Long ausgewaehlteAnforderungsId) {

        this.ausgewaehlteAnforderungsId =
                ausgewaehlteAnforderungsId;
    }

    public List<Anforderung> getAnforderungen() {
        return anforderungen;
    }

    public void setAnforderungen(
            List<Anforderung> anforderungen) {

        this.anforderungen = anforderungen;
    }

    public List<Testfall> getTestfaelle() {
        return testfaelle;
    }

    public void setTestfaelle(
            List<Testfall> testfaelle) {

        this.testfaelle = testfaelle;
    }
}