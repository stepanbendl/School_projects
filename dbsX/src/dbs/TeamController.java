/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbs;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.persistence.EntityTransaction;

/**
 * FXML Controller class
 *
 * @author Čumák
 */
public class TeamController implements Initializable {

    @FXML
    private Button buttonDelete;
    @FXML
    private Label teamNameLabel;
    @FXML
    private Button buttonAdd;
    @FXML
    private Button buttonEdit;
    @FXML
    private Button buttonDetail;
    @FXML
    private ListView<String> membersList;
    @FXML
    private Button backButton;
    
    private Team team;
    
    private ArrayList<Person> members = new ArrayList();

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.team = Dbs.getSelectedTeam();
        teamNameLabel.setText(team.jmeno);
        loadTeamMembers();
    }   

    private void loadTeamMembers(){
        members.clear();
        membersList.getItems().clear();
        Collection<Person> teamMembersCollection = team.getMembers();
        members.addAll(teamMembersCollection);
        for (Person member : members){
            membersList.getItems().add(member.jmeno + " " + member.prijmeni);
        }
    }
    
    @FXML
    private void handleButtonDeleteAction(ActionEvent event) {
        Dialog dialog = new Dialog();
        dialog.setTitle("Confirm member removal");
        dialog.setWidth(350);
        dialog.setHeight(250);
        
        int selectedIndice = membersList.getSelectionModel().getSelectedIndices().get(0);
        if(selectedIndice == -1){
            throw new NullPointerException("Member not selected");
        }
        
        Person person = members.get(selectedIndice);
        createDialogDeleteContent(dialog, person);
        dialog.showAndWait();
        
        if (dialog.getResult().equals(true)){
            Person dbPerson = Dbs.em.find(Person.class, person.id);
            Team dbTeam = Dbs.em.find(Team.class, team.id);
            
            EntityTransaction et = Dbs.em.getTransaction();
            et.begin();
            dbTeam.removeMember(person);
            dbPerson.removeTeam(team);
            Dbs.em.merge(dbPerson);
            Dbs.em.merge(dbTeam);
            et.commit();
            Dbs.load();
            loadTeamMembers();
        }
    }
    
    private void createDialogDeleteContent(Dialog dialog, Person person){
        dialog.getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        // Grid
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        // Components
        Label confirmLabel = new Label("Do you really want to remove "
                + person.jmeno + " " + person.prijmeni + " from " + team.jmeno + " ?");
        grid.add(confirmLabel, 0, 0);

        dialog.setResultConverter(new Callback<ButtonType, Boolean>() {
            @Override
            public Boolean call(ButtonType param) {
                return param == ButtonType.OK;
            }
        });
        
        dialog.getDialogPane().setContent(grid);
        
        if (dialog.getResult() != null){
            Person dbPerson = Dbs.em.find(Person.class, person.id);
            Team dbTeam = Dbs.em.find(Team.class, team.id);
            
            EntityTransaction et = Dbs.em.getTransaction();
            et.begin();
            dbTeam.removeMember(person);
            dbPerson.removeTeam(team);
            Dbs.em.merge(dbPerson);
            Dbs.em.merge(dbTeam);
            et.commit();
            Dbs.load();
            loadTeamMembers();
        }
        
    }

    @FXML
    private void handleButtonAddAction(ActionEvent event) {
        Dialog<Person> dialog = new Dialog();
        dialog.setTitle("Přidat člena");
        dialog.setWidth(350);
        dialog.setHeight(250);
        
        createDialogAddContent(dialog);
        dialog.showAndWait();
        
        Person person = dialog.getResult();
        
        if (person != null){
            Person dbPerson = Dbs.em.find(Person.class, person.id);
            Team dbTeam = Dbs.em.find(Team.class, team.id);
            
            EntityTransaction et = Dbs.em.getTransaction();
            et.begin();
            dbTeam.addMember(person);
            dbPerson.addTeam(team);
            Dbs.em.merge(dbPerson);
            Dbs.em.merge(dbTeam);
            et.commit();
            Dbs.load();
            loadTeamMembers();
        }
    }
    
    private void createDialogErrorContent(Dialog dialog){
        dialog.getDialogPane().getButtonTypes().setAll(ButtonType.OK);

        // Grid
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        // Components
        Label confirmLabel = new Label("Chybně zadaná data, prosím zkontrolujte údaje");
        
        grid.add(confirmLabel, 0, 0);
        dialog.getDialogPane().setContent(grid);
    }
    
    private void createDialogAddContent(Dialog<Person> dialog){
        dialog.getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        // Grid
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        // Components
        Label confirmLabel = new Label("Hráč");
        ComboBox playerComboBox = new ComboBox();
        
        // Fill ComboBox
        ArrayList<Person> teamPlayers = new ArrayList();
        for (Person person : Dbs.getPlayers()){
            if (team.getMembers().contains(person)){
                continue;
            }
            teamPlayers.add(person);
        }
        for (Person player : teamPlayers) {
            playerComboBox.getItems().add(player.jmeno + " " + player.prijmeni
                    + " ,rodné číslo " + player.rodne_cislo);
        }
                
        grid.add(confirmLabel, 0, 0);
        grid.add(playerComboBox, 0, 1);

        dialog.setResultConverter(new Callback<ButtonType, Person>() {
            @Override
            public Person call(ButtonType param) {
                if (param == ButtonType.OK){
                    return teamPlayers.get(playerComboBox.getSelectionModel().getSelectedIndex());
                }
                return null;
            }
        });
        
        dialog.getDialogPane().setContent(grid);
        
    }

    @FXML
    private void handleButtonEditAction(ActionEvent event) {
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Upravit osobu");
        dialog.setWidth(350);
        dialog.setHeight(250);
        dialog.setOnCloseRequest(e -> {
            if(!validateInput(dialog.getResult())){
                e.consume();
                Dialog errorDialog = new Dialog();
                errorDialog.setTitle("Chybné údaje");
                createDialogErrorContent(errorDialog);
                errorDialog.showAndWait();
            }});
        
        int selectedIndice = membersList.getSelectionModel().getSelectedIndices().get(0);
        if(selectedIndice == -1){
            throw new NullPointerException("Person not selected");
        }
        Person person = members.get(selectedIndice);
        createDialogEditContent(dialog, person);
        dialog.showAndWait();
        String[] editedPersonAttributes = dialog.getResult();
        
        if (editedPersonAttributes != null){
            
            Person editingPerson = Dbs.em.find(Person.class, person.id);
            Team teamToAdd = findTeamByName(editedPersonAttributes[5]);
            EntityTransaction et = Dbs.em.getTransaction();
            et.begin();
            editingPerson.setAttributes(editedPersonAttributes[0], editedPersonAttributes[1],
                    editedPersonAttributes[2],editedPersonAttributes[3], editedPersonAttributes[4]);
            if(!editingPerson.getTeams().contains(teamToAdd)){
                editingPerson.addTeam(teamToAdd);
            }
            et.commit();
            
            Dbs.load();
            loadTeamMembers();
        }
    }
    //0 - birth number id
    //1 - name
    //2 - surname
    //3 - registration number
    //4 - email
    //5 - team
    private boolean validateInput(String[] editedPersonAttributes){
        char[] emailChars = editedPersonAttributes[4].toCharArray();
        boolean emailAT = false;
        boolean emailDOT = false;
        
        for (char x : emailChars){
            if (x == '@'){
                emailAT = true;
            }
            if (x == '.'){
                emailDOT = true;
            }
        }
        return !(editedPersonAttributes[0].length() != 11 || 
                editedPersonAttributes[3].length() != 10 || !emailAT || !emailDOT);
    }
    
    private void createDialogEditContent(Dialog<String[]> dialog, Person person){
        // Setup dialog buttons
        dialog.getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        // Grid
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        // Components
        TextField jmenoTextField = new TextField(person.jmeno);
        TextField prijmeniTextField = new TextField(person.prijmeni);
        TextField rodneCisloTextField = new TextField(person.rodne_cislo);
        TextField cisloRegistraceTextField = new TextField(person.cislo_registrace);
        TextField emailTextField = new TextField(person.email);
        ComboBox teamComboBox = new ComboBox();
        Label jmenoLabel = new Label("Jméno");
        Label prijmeniLabel = new Label("Příjmení");
        Label rodneCisloLabel = new Label("Rodné číslo");
        Label cisloRegistraceLabel = new Label("Číslo registrace");
        Label emailLabel = new Label("Email");
        Label tymLabel = new Label("Tým");
        
        //Filling combobox
        Collection<Team> teams = Dbs.getTeams();
        Team[] teamsArray = new Team[teams.size()];
        teamsArray = teams.toArray(teamsArray);
        String[] teamNames = new String[teams.size()];
        for (int i = 0; i < teamsArray.length; i++) {
            teamNames[i] = teamsArray[i].jmeno;
        }
        teamComboBox.getItems().addAll(Arrays.asList(teamNames));
        teamComboBox.getSelectionModel().selectFirst();

        grid.add(jmenoLabel, 0, 0);
        grid.add(jmenoTextField, 1, 0);
        grid.add(prijmeniLabel, 0, 1);
        grid.add(prijmeniTextField, 1, 1);
        grid.add(rodneCisloLabel, 0, 2);
        grid.add(rodneCisloTextField, 1, 2);
        grid.add(cisloRegistraceLabel, 0, 3);
        grid.add(cisloRegistraceTextField, 1, 3);
        grid.add(emailLabel, 0, 4);
        grid.add(emailTextField, 1, 4);
        grid.add(tymLabel, 0, 5);
        grid.add(teamComboBox, 1, 5);

        dialog.setResultConverter(new Callback<ButtonType, String[]>() {
            @Override
            public String[] call(ButtonType param) {
                  if (param == ButtonType.OK) {
                      return new String[]{
                            rodneCisloTextField.getText(), 
                            jmenoTextField.getText(), 
                            prijmeniTextField.getText(),
                            cisloRegistraceTextField.getText(), 
                            emailTextField.getText(),
                            teamComboBox.getValue().toString()
                      };
                  }
                return null;
            }
        });

        dialog.getDialogPane().setContent(grid);
    }

    @FXML
    private void handleButtonDetailAction(ActionEvent event) {
        Dialog dialog = new Dialog();
        dialog.setTitle("Person detail");
        dialog.setWidth(350);
        dialog.setHeight(250);
        
        int selectedIndice = membersList.getSelectionModel().getSelectedIndices().get(0);
        createDialogDetailContent(dialog, members.get(selectedIndice));
        dialog.showAndWait();
    }
    
        
    private void createDialogDetailContent(Dialog dialog, Person person){
        dialog.getDialogPane().getButtonTypes().setAll(ButtonType.OK);
        
        // Grid
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        // Components
        Label jmenoContentLabel = new Label(person.jmeno);
        Label prijmeniContentLabel = new Label(person.prijmeni);
        Label rodneCisloContentLabel = new Label(person.rodne_cislo);
        Label cisloRegistraceContentLabel = new Label(person.cislo_registrace);
        Label emailContentLabel = new Label(person.email);
        Label jmenoLabel = new Label("Jméno");
        Label prijmeniLabel = new Label("Příjmení");
        Label rodneCisloLabel = new Label("Rodné číslo");
        Label cisloRegistraceLabel = new Label("Číslo registrace");
        Label emailLabel = new Label("Email");

        grid.add(jmenoLabel, 0, 0);
        grid.add(jmenoContentLabel, 1, 0);
        grid.add(prijmeniLabel, 0, 1);
        grid.add(prijmeniContentLabel, 1, 1);
        grid.add(rodneCisloLabel, 0, 2);
        grid.add(rodneCisloContentLabel, 1, 2);
        grid.add(cisloRegistraceLabel, 0, 3);
        grid.add(cisloRegistraceContentLabel, 1, 3);
        grid.add(emailLabel, 0, 4);
        grid.add(emailContentLabel, 1, 4);
        
        Collection<Team> playerTeamsColl = person.getTeams();
        Team[] playerTeamsArr = new Team[playerTeamsColl.size()];
        playerTeamsColl.toArray(playerTeamsArr);
        for (int i = 5; i < 5 + playerTeamsArr.length; i++) {
            grid.add(new Label("Tým " + (i - 4)), 0, i);
            grid.add(new Label(playerTeamsArr[i-5].jmeno), 1, i);
        }
        dialog.getDialogPane().setContent(grid);
    }
    
    /**
     * Switches scene to teams scene
     * @param event 
     */
    @FXML
    private void switchScenes(ActionEvent event) throws IOException {
        Parent teamsView = FXMLLoader.load(getClass().getResource("Teams.fxml"));
        Scene teamsScene = new Scene(teamsView);
        
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        window.setScene(teamsScene);
        window.show();
        
    }
    
    private Team findTeamByName(String name){
        Team toRet = null;
        for (Team searchedTeam : Dbs.getTeams()){
            if(searchedTeam.jmeno.equals(name))
                return searchedTeam;
        }
        return toRet;
    }
}
