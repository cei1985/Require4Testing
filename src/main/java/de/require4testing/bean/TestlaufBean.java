package de.require4testing.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.require4testing.dao.TestdurchfuehrungDAO;
import de.require4testing.dao.TesterDAO;
import de.require4testing.dao.TestfallDAO;
import de.require4testing.dao.TestlaufDAO;
import de.require4testing.model.Testdurchfuehrung;
import de.require4testing.model.Tester;
import de.require4testing.model.Testfall;
import de.require4testing.model.Testlauf;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named
@ViewScoped
public class TestlaufBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Testlauf testlauf;

    private List<Testlauf> testlaeufe;
    private List<Tester> tester;
    private List<Testfall> testfaelle;

    private Long ausgewaehlterTestlaufId;
    private Long ausgewaehlteTesterId;
    private List<Long> ausgewaehlteTestfallIds;

    private final TestlaufDAO testlaufDAO = new TestlaufDAO();
    private final TestfallDAO testfallDAO = new TestfallDAO();
    private final TesterDAO testerDAO = new TesterDAO();
    private final TestdurchfuehrungDAO testdurchfuehrungDAO =
            new TestdurchfuehrungDAO();

    @PostConstruct
    public void init() {
        testlauf = new Testlauf();

        testlaeufe = testlaufDAO.findeAlle();
        tester = testerDAO.findeAlle();
        testfaelle = testfallDAO.findeAlle();

        ausgewaehlteTestfallIds = new ArrayList<>();
    }

    public void speichern() {

        if (ausgewaehlterTestlaufId == null) {
            throw new IllegalStateException(
                    "Kein Testlauf ausgewählt.");
        }

        if (ausgewaehlteTesterId == null) {
            throw new IllegalStateException(
                    "Kein Tester ausgewählt.");
        }

        Testlauf ausgewaehlterTestlauf =
                findeAusgewaehltenTestlauf();

        Tester ausgewaehlterTester =
                findeAusgewaehltenTester();

        if (ausgewaehlterTestlauf == null) {
            throw new IllegalStateException(
                    "Kein gültiger Testlauf ausgewählt.");
        }

        if (ausgewaehlterTester == null) {
            throw new IllegalStateException(
                    "Kein gültiger Tester ausgewählt.");
        }

        ausgewaehlterTestlauf.setTester(ausgewaehlterTester);

        testlauf = ausgewaehlterTestlauf;

        for (Long testfallId : ausgewaehlteTestfallIds) {

            Testfall testfall = findeTestfall(testfallId);

            if (testfall != null) {

                Testdurchfuehrung testdurchfuehrung =
                        new Testdurchfuehrung();

                testdurchfuehrung.setTestlauf(ausgewaehlterTestlauf);
                testdurchfuehrung.setTestfall(testfall);
                testdurchfuehrung.setErgebnis("OFFEN");

                testdurchfuehrungDAO.speichern(
                        testdurchfuehrung);
            }
        }

        testlauf = new Testlauf();
        ausgewaehlterTestlaufId = null;
        ausgewaehlteTesterId = null;
        ausgewaehlteTestfallIds = new ArrayList<>();

        testlaeufe = testlaufDAO.findeAlle();
    }

    private Testlauf findeAusgewaehltenTestlauf() {

        for (Testlauf eintrag : testlaeufe) {

            if (ausgewaehlterTestlaufId.equals(
                    eintrag.getId())) {

                return eintrag;
            }
        }

        return null;
    }

    private Tester findeAusgewaehltenTester() {

        for (Tester eintrag : tester) {

            if (ausgewaehlteTesterId.equals(
                    eintrag.getId())) {

                return eintrag;
            }
        }

        return null;
    }

    private Testfall findeTestfall(Long testfallId) {

        for (Testfall testfall : testfaelle) {

            if (testfallId.equals(testfall.getId())) {
                return testfall;
            }
        }

        return null;
    }

    public Testlauf getTestlauf() {
        return testlauf;
    }

    public void setTestlauf(Testlauf testlauf) {
        this.testlauf = testlauf;
    }

    public List<Testlauf> getTestlaeufe() {
        return testlaeufe;
    }

    public void setTestlaeufe(List<Testlauf> testlaeufe) {
        this.testlaeufe = testlaeufe;
    }

    public List<Tester> getTester() {
        return tester;
    }

    public void setTester(List<Tester> tester) {
        this.tester = tester;
    }

    public List<Testfall> getTestfaelle() {
        return testfaelle;
    }

    public void setTestfaelle(List<Testfall> testfaelle) {
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
}