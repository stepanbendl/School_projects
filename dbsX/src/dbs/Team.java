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
@Table(name = "tym")
public class Team {
    @Id
    @GeneratedValue
    @Column (name = "id")
    public Integer id;
    
    @Column (name = "kod", length = 3, unique = true, nullable = false)
    public String kod;
    
    @Column (name = "jmeno", nullable = false, unique = true)
    public String jmeno;
    
    @Column (name = "nazevStadionu", nullable = false)
    public String nazevStadionu;
    
    @Column (name = "kodSouteze", nullable = false, length = 3)
    public String kodSouteze;
    
    @Column (name = "web")
    public String web;
    
    @ManyToMany (mappedBy = "teams")
    private Collection<Person> members;

    public Team() {
        this.id = null;
        this.kod = null;
        this.jmeno = null;
        this.nazevStadionu = null;
        this.kodSouteze = null;
        this.web = null;
    }
    
    public void setAttributes(String kod, String jmeno, String nazevStadionu, String kodSouteze, String web) {
        this.kod = kod;
        this.jmeno = jmeno;
        this.nazevStadionu = nazevStadionu;
        this.kodSouteze = kodSouteze;
        this.web = web;
    }
    
    public void addMember(Person person){
        this.members.add(person);
    }
    
    public void removeMember(Person member){
        members.remove(member);
    }

    public Collection<Person> getMembers() {
        return members;
    }
    
}
