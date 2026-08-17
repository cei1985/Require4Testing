package de.require4testing.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "testlauf")
public class Testlauf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startDatum;

    private LocalDateTime endDatum;

    @ManyToOne
    @JoinColumn(name = "tester_id")
    private Tester tester;

    public Testlauf() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStartDatum() {
        return startDatum;
    }

    public void setStartDatum(LocalDateTime startDatum) {
        this.startDatum = startDatum;
    }

    public LocalDateTime getEndDatum() {
        return endDatum;
    }

    public void setEndDatum(LocalDateTime endDatum) {
        this.endDatum = endDatum;
    }

    public Tester getTester() {
        return tester;
    }

    public void setTester(Tester tester) {
        this.tester = tester;
    }
}