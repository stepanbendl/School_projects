/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbs;

import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;


/**
 *
 * @author Čumák
 */
public class Dbs extends Application { 
    
    private static List<Team> teams;
    private static List<Person> players;
    public static EntityManagerFactory emf = Persistence.createEntityManagerFactory("dbsPU");
    public static EntityManager em = emf.createEntityManager();
    private static Team selectedTeam = null;
    private static Person selectedPlayer = null;
        
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(event -> em.close());
        stage.setOnCloseRequest(event -> emf.close());
    }  

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {       
        load();
        launch(args);

    }
    
    public static void load(){
        TypedQuery<Team> getTeams = em.createQuery("SELECT t FROM Team AS t", Team.class);

        teams = getTeams.getResultList();
        
        TypedQuery<Person> getPlayers = em.createQuery("SELECT p FROM Person AS p", Person.class);
        
        players = getPlayers.getResultList();
    }

    public static List<Team> getTeams() {
        return teams;
    }

    public static List<Person> getPlayers() {
        return players;
    }

    public static Team getSelectedTeam() {
        return selectedTeam;
    }

    public static void setSelectedTeam(Team selectedTeam) {
        Dbs.selectedTeam = selectedTeam;
    }

    public static Person getSelectedPlayer() {
        return selectedPlayer;
    }

    public static void setSelectedPlayer(Person selectedPlayer) {
        Dbs.selectedPlayer = selectedPlayer;
    }
    
    
}
