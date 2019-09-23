package cz.cvut.fel.pjv;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import static cz.cvut.fel.pjv.Utils.*;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Čumák
 */
public class Main extends Application {

    final double ASPECT_RATIO = INIT_APP_HEIGHT / INIT_APP_WIDTH;

    private GameCanvas gameCanvas;
    private UserActionHandler userActionHandler;
    private GameLoopHandler gameLoopHandler;
    private SpriteManager spriteManager;
    private MapGenerator mapGenerator;

    @Override
    public void init() throws Exception {
        spriteManager = new SpriteManager();
        mapGenerator = new MapGenerator(spriteManager);
        mapGenerator.generateMap();
        gameCanvas = new GameCanvas(spriteManager);
        gameLoopHandler = new GameLoopHandler(spriteManager, gameCanvas);
        userActionHandler = new UserActionHandler(gameLoopHandler);
    }

    @Override
    public void start(Stage stage) throws Exception {
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(gameCanvas);
        gameCanvas.setWidth(INIT_APP_WIDTH);
        gameCanvas.setHeight(INIT_APP_HEIGHT);
        
        Scene scene = new Scene(stackPane, INIT_APP_WIDTH, INIT_APP_HEIGHT);
        stage.setScene(scene);
        stage.setTitle("Mario");
        stage.setOnCloseRequest(event -> System.exit(0));
        stage.setResizable(false);
        
        
        
        scene.setOnKeyPressed(userActionHandler);
        scene.setOnKeyReleased(userActionHandler);
        

        //show the stage
        stage.show();
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
