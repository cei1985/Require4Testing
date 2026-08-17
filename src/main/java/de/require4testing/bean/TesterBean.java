package de.require4testing.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.require4testing.dao.TestdurchfuehrungDAO;
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
    private List<Testdurchfuehrung> testdurchfuehrungen;

    private final TestdurchfuehrungDAO testdurchfuehrungDAO =
            new TestdurchfuehrungDAO();

    @PostConstruct
    public void init() {
        tester = new Tester();
        testdurchfuehrungen = new ArrayList<>();
    }

    public void ladeTestdurchfuehrungen(AjaxBehaviorEvent event) {
        if (tester.getId() == null) {
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

        if (tester.getId() != null) {
            testdurchfuehrungen =
                    testdurchfuehrungDAO.findeFuerTester(tester);
        }
    }

    public Tester getTester() {
        return tester;
    }

    public void setTester(Tester tester) {
        this.tester = tester;
    }

    public List<Testdurchfuehrung> getTestdurchfuehrungen() {
        return testdurchfuehrungen;
    }

    public void setTestdurchfuehrungen(
            List<Testdurchfuehrung> testdurchfuehrungen) {
        this.testdurchfuehrungen = testdurchfuehrungen;
    }
}