package de.require4testing.bean;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import de.require4testing.dao.TestlaufDAO;
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

    private final TestlaufDAO testlaufDAO = new TestlaufDAO();

    @PostConstruct
    public void init() {
        testlauf = new Testlauf();
        ladeTestlaeufe();
    }

    public void speichern() {
        testlaufDAO.speichern(testlauf);

        testlauf = new Testlauf();
        ladeTestlaeufe();
    }

    public void ladeTestlaeufe() {
        testlaeufe = testlaufDAO.findeAlle();
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

    public LocalDateTime getStartDatum() {
        return testlauf.getStartDatum();
    }

    public void setStartDatum(LocalDateTime startDatum) {
        testlauf.setStartDatum(startDatum);
    }

    public LocalDateTime getEndDatum() {
        return testlauf.getEndDatum();
    }

    public void setEndDatum(LocalDateTime endDatum) {
        testlauf.setEndDatum(endDatum);
    }
}