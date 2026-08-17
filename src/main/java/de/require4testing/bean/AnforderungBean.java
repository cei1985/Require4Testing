package de.require4testing.bean;

import java.io.Serializable;
import java.util.List;

import de.require4testing.dao.AnforderungDAO;
import de.require4testing.model.Anforderung;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named
@ViewScoped
public class AnforderungBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Anforderung anforderung;
    private List<Anforderung> anforderungen;

    private final AnforderungDAO anforderungDAO = new AnforderungDAO();

    @PostConstruct
    public void init() {
        anforderung = new Anforderung();
        anforderungen = anforderungDAO.findeAlle();
    }

    public void speichern() {
        anforderungDAO.speichern(anforderung);

        anforderung = new Anforderung();
        anforderungen = anforderungDAO.findeAlle();
    }

    public Anforderung getAnforderung() {
        return anforderung;
    }

    public void setAnforderung(Anforderung anforderung) {
        this.anforderung = anforderung;
    }

    public List<Anforderung> getAnforderungen() {
        return anforderungen;
    }

    public void setAnforderungen(List<Anforderung> anforderungen) {
        this.anforderungen = anforderungen;
    }
}