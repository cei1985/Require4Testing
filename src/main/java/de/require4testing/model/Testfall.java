package de.require4testing.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "testfall")
public class Testfall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titel;

    @Column(nullable = false, length = 1000)
    private String beschreibung;

    @ManyToOne(optional = false)
    @JoinColumn(name = "anforderung_id", nullable = false)
    private Anforderung anforderung;

    public Testfall() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public Anforderung getAnforderung() {
        return anforderung;
    }

    public void setAnforderung(Anforderung anforderung) {
        this.anforderung = anforderung;
    }
}