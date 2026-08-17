package de.require4testing.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.require4testing.dao.TestdurchfuehrungDAO;
import de.require4testing.dao.TesterDAO;
import de.require4testing.model.Testdurchfuehrung;
import de.require4testing.model.Tester;
import jakarta.annotation.PostConstruct;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named
@ViewScoped
public class TesterBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Tester tester;

    private List<Tester> testerListe;
    private List<Testdurchfuehrung> testdurchfuehrungen;

    private Long ausgewaehlteTesterId;

    private final TesterDAO testerDAO = new TesterDAO();
    private final TestdurchfuehrungDAO testdurchfuehrungDAO =
            new TestdurchfuehrungDAO();

    @PostConstruct
    public void init() {
        tester = new Tester();
        testerListe = testerDAO.findeAlle();
        testdurchfuehrungen = new ArrayList<>();
    }

    public void ladeTestdurchfuehrungen(AjaxBehaviorEvent event) {
        tester = findeAusgewaehltenTester();

        if (tester == null) {
            testdurchfuehrungen = new ArrayList<>();
            return;
        }

        testdurchfuehrungen =
                testdurchfuehrungDAO.findeFuerTester(tester);
    }

    public void ergebnisSpeichern() {
        for (Testdurchfuehrung testdurchfuehrung : testdurchfuehrungen) {
            testdurchfuehrungDAO.aktualisieren(testdurchfuehrung);
        }

        if (tester != null) {
            testdurchfuehrungen =
                    testdurchfuehrungDAO.findeFuerTester(tester);
        }
    }

    private Tester findeAusgewaehltenTester() {
        if (ausgewaehlteTesterId == null) {
            return null;
        }

        for (Tester eintrag : testerListe) {
            if (ausgewaehlteTesterId.equals(eintrag.getId())) {
                return eintrag;
            }
        }

        return null;
    }

    public Tester getTester() {
        return tester;
    }

    public void setTester(Tester tester) {
        this.tester = tester;
    }

    public List<Tester> getTesterListe() {
        return testerListe;
    }

    public void setTesterListe(List<Tester> testerListe) {
        this.testerListe = testerListe;
    }

    public List<Testdurchfuehrung> getTestdurchfuehrungen() {
        return testdurchfuehrungen;
    }

    public void setTestdurchfuehrungen(
            List<Testdurchfuehrung> testdurchfuehrungen) {
        this.testdurchfuehrungen = testdurchfuehrungen;
    }

    public Long getAusgewaehlteTesterId() {
        return ausgewaehlteTesterId;
    }

    public void setAusgewaehlteTesterId(Long ausgewaehlteTesterId) {
        this.ausgewaehlteTesterId = ausgewaehlteTesterId;
    }
}