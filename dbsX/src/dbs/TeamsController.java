/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this tDbs.emplate file, choose Tools | TDbs.emplates
 * and open the tDbs.emplate in the editor.
 */
package dbs;

import java.io.IOException;
import java.net.URL;
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
public class TeamsController implements Initializable {

    @FXML
    private Button buttonDetail;
    @FXML
    private Button buttonEdit;
    @FXML
    private Button buttonAdd;
    @FXML
    private ListView<String> teamList;
    @FXML
    private Button switchButton;
    @FXML
    private Label label;
    
    @FXML
    private Button buttonDelete;
    @FXML
    private Button buttonMembers;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadTeams();
    }    

    @FXML
    private void handleButtonDetailAction(ActionEvent event) {
        Dialog dialog = new Dialog();
        dialog.setTitle("Person detail");
        dialog.setWidth(350);
        dialog.setHeight(250);
        
        int selectedIndice = teamList.getSelectionModel().getSelectedIndices().get(0);
        createDialogDetailContent(dialog, Dbs.getTeams().get(selectedIndice));
        dialog.showAndWait();
    }
    
    private void createDialogDetailContent(Dialog dialog, Team team) {
        dialog.getDialogPane().getButtonTypes().setAll(ButtonType.OK);

        // Grid
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        // Components
        Label jmenoContentLabel = new Label(team.jmeno);
        Label kodContentLabel = new Label(team.kod);
        Label nazevStadionuContentLabel = new Label(team.nazevStadionu);
        Label kodSoutezeContentLabel = new Label(team.kodSouteze);
        Label webContentLabel = new Label(team.web);
        Label jmenoLabel = new Label("Jméno");
        Label kodLabel = new Label("Kód");
        Label nazevStadionuLabel = new Label("Název stadionu");
        Label kodSoutezeLabel = new Label("Kód soutěže");
        Label webLabel = new Label("Web");

        grid.add(jmenoLabel, 0, 0);
        grid.add(jmenoContentLabel, 1, 0);
        grid.add(kodLabel, 0, 1);
        grid.add(kodContentLabel, 1, 1);
        grid.add(nazevStadionuLabel, 0, 2);
        grid.add(nazevStadionuContentLabel, 1, 2);
        grid.add(kodSoutezeLabel, 0, 3);
        grid.add(kodSoutezeContentLabel, 1, 3);
        grid.add(webLabel, 0, 4);
        grid.add(webContentLabel, 1, 4);

        dialog.getDialogPane().setContent(grid);
}

    @FXML
    private void handleButtonEditAction(ActionEvent event) {
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Edit team");
        dialog.setWidth(350);
        dialog.setHeight(250);
        
        int selectedIndice = teamList.getSelectionModel().getSelectedIndices().get(0);
        if(selectedIndice == -1){
            throw new NullPointerException("Team not selected");
        }
        Team team = Dbs.getTeams().get(selectedIndice);
        
        createDialogEditContent(dialog, team);
        dialog.showAndWait();
        String[] editedTeamAttributes = dialog.getResult();
        
        if (editedTeamAttributes != null){

            Team editingTeam = Dbs.em.find(Team.class, team.id);
            EntityTransaction et = Dbs.em.getTransaction();
            et.begin();
            editingTeam.setAttributes(editedTeamAttributes[0], editedTeamAttributes[1],
                    editedTeamAttributes[2],editedTeamAttributes[3], editedTeamAttributes[4]);
            et.commit();
            
            Dbs.load();
            loadTeams();
        }
    }
    
    private void createDialogEditContent(Dialog<String[]> dialog, Team team) {
        // Setup dialog buttons
        dialog.getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        // Grid
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        // Components
        TextField jmenoTextField = new TextField(team.jmeno);
        TextField kodTextField = new TextField(team.kod);
        TextField nazevStadionuTextField = new TextField(team.nazevStadionu);
        TextField kodSoutezeTextField = new TextField(team.kodSouteze);
        TextField webTextField = new TextField(team.web);
        Label jmenoLabel = new Label("Jméno");
        Label kodLabel = new Label("Kód týmu");
        Label nazevStadionuLabel = new Label("Název stadionu");
        Label kodSoutezeLabel = new Label("Kód soutěžě");
        Label webLabel = new Label("Web");

        grid.add(jmenoLabel, 0, 0);
        grid.add(jmenoTextField, 1, 0);
        grid.add(kodLabel, 0, 1);
        grid.add(kodTextField, 1, 1);
        grid.add(nazevStadionuLabel, 0, 2);
        grid.add(nazevStadionuTextField, 1, 2);
        grid.add(kodSoutezeLabel, 0, 3);
        grid.add(kodSoutezeTextField, 1, 3);
        grid.add(webLabel, 0, 4);
        grid.add(webTextField, 1, 4);

        dialog.setResultConverter(new Callback<ButtonType, String[]>() {
            @Override
            public String[] call(ButtonType param) {
                  if (param == ButtonType.OK) {
                      return new String[]{
                            kodTextField.getText(), 
                            jmenoTextField.getText(), 
                            nazevStadionuTextField.getText(),
                            kodSoutezeTextField.getText(), 
                            webTextField.getText()
                      };
                  }
                return null;
            }
        });

        dialog.getDialogPane().setContent(grid);
}

    @FXML
    private void handleButtonAddAction(ActionEvent event) {
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("New team");
        dialog.setWidth(350);
        dialog.setHeight(250);
        
        createDialogAddContent(dialog);
        dialog.showAndWait();
        String[] newTeamAttributes = dialog.getResult();
        
        if (newTeamAttributes != null){

            EntityTransaction et = Dbs.em.getTransaction();
            et.begin();
            Team newTeam = new Team();
            newTeam.setAttributes(newTeamAttributes[0], newTeamAttributes[1], newTeamAttributes[2], newTeamAttributes[3], newTeamAttributes[4]);
            Dbs.em.persist(newTeam);
            et.commit();
            
            Dbs.load();
            loadTeams();
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
        TextField kodTextField = new TextField();
        TextField nazevStadionuTextField = new TextField();
        TextField kodSoutezeTextField = new TextField();
        TextField webTextField = new TextField();
        Label jmenoLabel = new Label("Jméno");
        Label kodLabel = new Label("Kód týmu");
        Label nazevStadionuLabel = new Label("Název stadionu");
        Label kodSoutezeLabel = new Label("Kód soutěžě");
        Label webLabel = new Label("Web");

        grid.add(jmenoLabel, 0, 0);
        grid.add(jmenoTextField, 1, 0);
        grid.add(kodLabel, 0, 1);
        grid.add(kodTextField, 1, 1);
        grid.add(nazevStadionuLabel, 0, 2);
        grid.add(nazevStadionuTextField, 1, 2);
        grid.add(kodSoutezeLabel, 0, 3);
        grid.add(kodSoutezeTextField, 1, 3);
        grid.add(webLabel, 0, 4);
        grid.add(webTextField, 1, 4);

        dialog.setResultConverter(new Callback<ButtonType, String[]>() {
            @Override
            public String[] call(ButtonType param) {
                if (param == ButtonType.OK) {
                    return new String[]{
                            kodTextField.getText(), 
                            jmenoTextField.getText(), 
                            nazevStadionuTextField.getText(),
                            kodSoutezeTextField.getText(), 
                            webTextField.getText()};
                  }
                return null;
            }
        });

        dialog.getDialogPane().setContent(grid);
}
    
    
    @FXML
    private void handleButtonDeleteAction(ActionEvent event) {
         Dialog dialog = new Dialog();
        dialog.setTitle("Confirm team delete");
        dialog.setWidth(350);
        dialog.setHeight(250);
        
        Team team = getSelectedTeam();
        createDialogDeleteContent(dialog, team);
        dialog.showAndWait();
        
        if (dialog.getResult().equals(true)){

            Team deletingTeam = Dbs.em.find(Team.class, team.id);
            EntityTransaction et = Dbs.em.getTransaction();
            et.begin();
            Dbs.em.remove(deletingTeam);
            et.commit();
            
            Dbs.load();
            loadTeams();
            
        }
    }
    
    private void createDialogDeleteContent(Dialog dialog, Team team) {
        dialog.getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        // Grid
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        // Components
        Label confirmLabel = new Label("Do you really want to delete " + team.jmeno + " ?");
        grid.add(confirmLabel, 0, 0);

        dialog.setResultConverter(new Callback<ButtonType, Boolean>() {
            @Override
            public Boolean call(ButtonType param) {
                return param == ButtonType.OK;
            }
        });
        
        dialog.getDialogPane().setContent(grid);
}
    

    @FXML
    private void switchScenes(ActionEvent event) throws IOException {
        Parent playersView = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        Scene playersScene = new Scene(playersView);
        
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        window.setScene(playersScene);
        window.show();
    }
    
    private void loadTeams(){
        List<Team> loadedTeams = Dbs.getTeams();
        teamList.getItems().clear();
        for (int i = 0; i < loadedTeams.size(); i++) {
            teamList.getItems().add(loadedTeams.get(i).jmeno);
        }  
    }

    @FXML
    private void handleButtonMembersAction(ActionEvent event) throws IOException {
        Dbs.setSelectedTeam(getSelectedTeam());
        Parent membersView = FXMLLoader.load(getClass().getResource("Team.fxml"));
        Scene membersScene = new Scene(membersView);
        
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        window.setScene(membersScene);
        window.show();
        
        }
    
    private Team getSelectedTeam(){
        int selectedIndice = teamList.getSelectionModel().getSelectedIndices().get(0);
        if(selectedIndice == -1){
            throw new NullPointerException("Team not selected");
        }
        Team team = Dbs.getTeams().get(selectedIndice);
        return team;
    }
    
    
}
