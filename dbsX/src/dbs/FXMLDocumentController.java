/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this tDbs.emplate file, choose Tools | TDbs.emplates
 * and open the tDbs.emplate in the editor.
 */
package dbs;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
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
 *
 * @author Čumák
 */

public class FXMLDocumentController implements Initializable {

    @FXML
    private Button buttonEdit;
    @FXML
    private Label label;
    @FXML
    private ListView<String> playerList;
    @FXML
    private Button buttonDetail;
    @FXML
    private Button buttonAdd;
    
    //private List<Person> players;
    @FXML
    private Button switchButton;
    @FXML
    private Button buttonDelete;
    
    
    @FXML
    private void handleButtonAddAction(ActionEvent event) {
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Přidat osobu");
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
        
        createDialogAddContent(dialog);
        dialog.showAndWait();
        String[] newPersonAttributes = dialog.getResult();
        
        if (newPersonAttributes != null){
            
            if(!validateInput(newPersonAttributes)){
                return;
            }
            
            Team teamToAssign = findTeamByName(newPersonAttributes[5]);
            Team team = Dbs.em.find(Team.class, teamToAssign.id);

            EntityTransaction et = Dbs.em.getTransaction();
            et.begin();
            Person newPerson = new Person();
            newPerson.setAttributes(newPersonAttributes[0], newPersonAttributes[1], newPersonAttributes[2], newPersonAttributes[3], newPersonAttributes[4]);

            Dbs.em.persist(newPerson);
            et.commit();

            Dbs.load();
            loadPlayers();
        }
        
    }
    
    @FXML
    private void handleButtonDetailAction(ActionEvent event) {
        Dialog dialog = new Dialog();
        dialog.setTitle("Detail osoby");
        dialog.setWidth(350);
        dialog.setHeight(250);
        
        int selectedIndice = playerList.getSelectionModel().getSelectedIndices().get(0);
        createDialogDetailContent(dialog, Dbs.getPlayers().get(selectedIndice));
        dialog.showAndWait();
        
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
        
        
        int selectedIndice = playerList.getSelectionModel().getSelectedIndices().get(0);
        if(selectedIndice == -1){
            throw new NullPointerException("Person not selected");
        }
        Person person = Dbs.getPlayers().get(selectedIndice);
        createDialogEditContent(dialog, person);
        dialog.showAndWait();
        String[] editedPersonAttributes = dialog.getResult();
        
        if (editedPersonAttributes != null){
            
            if(!validateInput(editedPersonAttributes)){
                return;
            }
            
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
            loadPlayers();
        }
        
    }
    
    @FXML
    private void handleButtonDeleteAction(ActionEvent event) {
        Dialog dialog = new Dialog();
        dialog.setTitle("Potvrdit odstranění");
        dialog.setWidth(350);
        dialog.setHeight(250);
        
        int selectedIndice = playerList.getSelectionModel().getSelectedIndices().get(0);
        if(selectedIndice == -1){
            throw new NullPointerException("Person not selected");
        }
        Person person = Dbs.getPlayers().get(selectedIndice);
        createDialogDeleteContent(dialog, person);
        dialog.showAndWait();
        
        if (dialog.getResult().equals(true)){

            Person editingPerson = Dbs.em.find(Person.class, person.id);
            EntityTransaction et = Dbs.em.getTransaction();
            et.begin();
            Dbs.em.remove(editingPerson);
            et.commit();
            
            Dbs.load();
            loadPlayers();
        }
    }
    
    private void createDialogAddContent(Dialog<String[]> dialog) {
        // Setup dialog buttons
        dialog.getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        // Grid
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        // Components
        TextField jmenoTextField = new TextField();
        TextField rodneCisloTextField = new TextField();
        TextField prijmeniTextField = new TextField();
        TextField cisloRegistraceTextField = new TextField();
        TextField emailTextField = new TextField();
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
    private void createDialogDetailContent(Dialog dialog, Person person) {
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
    
    private void createDialogEditContent(Dialog<String[]> dialog, Person person) {
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
    
    
    private void createDialogDeleteContent(Dialog dialog, Person person) {
        dialog.getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        // Grid
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        // Components
        Label confirmLabel = new Label("Opravdu chcete odstranit " + 
                person.jmeno + " " + person.prijmeni + " z databáze ?");
        grid.add(confirmLabel, 0, 0);

        dialog.setResultConverter(new Callback<ButtonType, Boolean>() {
            @Override
            public Boolean call(ButtonType param) {
                return param == ButtonType.OK;
            }
        });
        
        dialog.getDialogPane().setContent(grid);
}
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadPlayers();
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
    
    private void loadPlayers(){
        List<Person> loadedPlayers = Dbs.getPlayers();
        playerList.getItems().clear();
        for (int i = 0; i < loadedPlayers.size(); i++) {
        playerList.getItems().add(loadedPlayers.get(i).jmeno + " " + loadedPlayers.get(i).prijmeni);
        }
    }
    
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
    
    private Team[] findTeam(Person person){
        Collection<Team> teams = person.getTeams();
        Team[] teamsArray = new Team[teams.size()];
        return teams.toArray(teamsArray);
    }
    
    private Team findTeamByName(String name){
        Team toRet = null;
        for (Team team : Dbs.getTeams()){
            if(team.jmeno.equals(name))
                return team;
        }
        return toRet;
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
}
