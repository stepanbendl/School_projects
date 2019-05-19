/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbs;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.*;

/**
 *
 * @author Čumák
 */

@Entity 
@Table(name = "osoba")
public class Person {
    @Id
    @GeneratedValue
    @Column (name = "id")
    public Integer id;
    
    @Column (name = "rodne_cislo", length = 11, unique = true, nullable = false)
    public String rodne_cislo;
    
    @Column(name = "jmeno", nullable = false)
    public String jmeno;
    
    @Column (name = "prijmeni", nullable = false)
    public String prijmeni;
    
    @Column (name = "cislo_registrace", unique = true, nullable = false, length = 10)
    public String cislo_registrace;
    
    @Column (name = "email", unique = true)
    public String email;
    
    @ManyToMany (fetch=FetchType.EAGER)
    @JoinTable(
        name = "clen",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "kod"))
    private Collection<Team> teams;

    public Person() {
        this.id = null;
        this.rodne_cislo = null;
        this.jmeno = null;
        this.prijmeni = null;
        this.cislo_registrace = null;
        this.email = null;
    }
    
    /**
     *
     * Set attributes for entity
     * 
     * @param rodne_cislo
     * @param jmeno
     * @param prijmeni
     * @param cislo_registrace
     * @param email
     */
    public void setAttributes(String rodne_cislo, String jmeno, String prijmeni, String cislo_registrace, String email){
        this.rodne_cislo = rodne_cislo;
        this.jmeno = jmeno;
        this.prijmeni = prijmeni;
        this.cislo_registrace = cislo_registrace;
        this.email = email;
    }
    
    public void addTeam(Team team){
        teams.add(team);
    }

    public Collection<Team> getTeams() {
        return teams;
    }
    
    public void removeTeam(Team team){
        teams.remove(team);
    }
}
