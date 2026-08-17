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
@Table(name = "testdurchfuehrung")
public class Testdurchfuehrung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String ergebnis;

    @ManyToOne(optional = false)
    @JoinColumn(name = "testfall_id", nullable = false)
    private Testfall testfall;

    @ManyToOne(optional = false)
    @JoinColumn(name = "testlauf_id", nullable = false)
    private Testlauf testlauf;

    public Testdurchfuehrung() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getErgebnis() {
        return ergebnis;
    }

    public void setErgebnis(String ergebnis) {
        this.ergebnis = ergebnis;
    }

    public Testfall getTestfall() {
        return testfall;
    }

    public void setTestfall(Testfall testfall) {
        this.testfall = testfall;
    }

    public Testlauf getTestlauf() {
        return testlauf;
    }

    public void setTestlauf(Testlauf testlauf) {
        this.testlauf = testlauf;
    }
}