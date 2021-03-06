package seedu.dailyplanner.ui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.dailyplanner.commons.core.Config;
import seedu.dailyplanner.commons.core.GuiSettings;
import seedu.dailyplanner.commons.events.ui.ExitAppRequestEvent;
import seedu.dailyplanner.logic.Logic;
import seedu.dailyplanner.model.UserPrefs;
import seedu.dailyplanner.model.task.ReadOnlyTask;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart {

    private static final String ICON = "/images/address_book_32.png";
    private static final String FXML = "MainWindow.fxml";
    public static final int MIN_HEIGHT = 600;
    public static final int MIN_WIDTH = 450;

    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private BrowserPanel browserPanel;
    private TaskListPanel taskListPanel;
    private PinnedTaskPanel pinnedTaskPanel;
    private ResultDisplay resultDisplay;
    private StatusBarFooter statusBarFooter;
    private CommandBox commandBox;
    private Config config;
    private UserPrefs userPrefs;

    // Handles to elements of this Ui container
    private VBox rootLayout;
    private Scene scene;

    private String addressBookName;

    @FXML
    private AnchorPane browserPlaceholder;

    @FXML
    private AnchorPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private AnchorPane personListPanelPlaceholder;
    
    @FXML
    private AnchorPane pinnedTaskPlaceholder;

    @FXML
    private AnchorPane resultDisplayPlaceholder;

    @FXML
    private AnchorPane statusbarPlaceholder;
    
    @FXML
    private Label forDate;


    public MainWindow() {
        super();
    }

    @Override
    public void setNode(Node node) {
        rootLayout = (VBox) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    public static MainWindow load(Stage primaryStage, Config config, UserPrefs prefs, Logic logic) {

        MainWindow mainWindow = UiPartLoader.loadUiPart(primaryStage, new MainWindow());
        mainWindow.configure(config.getAppTitle(), config.getDailyPlannerName(), config, prefs, logic);
        return mainWindow;
    }

    private void configure(String appTitle, String addressBookName, Config config, UserPrefs prefs,
                           Logic logic) {

        //Set dependencies
        this.logic = logic;
        this.addressBookName = addressBookName;
        this.config = config;
        this.userPrefs = prefs;

        //Configure the UI
        setTitle(appTitle);
        setIcon(ICON);
        setWindowMinSize();
        setWindowDefaultSize(prefs);
        scene = new Scene(rootLayout);
        
        String css = getClass().getResource("/view/DarkTheme.css").toExternalForm();
        scene.getStylesheets().clear();
        scene.getStylesheets().add(css);
        
        primaryStage.setScene(scene);
        
        

        setAccelerators();
    }

    private void setAccelerators() {
        helpMenuItem.setAccelerator(KeyCombination.valueOf("F1"));
    }

    void fillInnerParts() {
        //browserPanel = BrowserPanel.load(browserPlaceholder);
        pinnedTaskPanel = PinnedTaskPanel.load(primaryStage, getPinnedTaskPlaceholder(), logic.getPinnedTaskList());
        taskListPanel = TaskListPanel.load(primaryStage, getPersonListPlaceholder(), logic.getFilteredTaskList(), logic);
        resultDisplay = ResultDisplay.load(primaryStage, getResultDisplayPlaceholder());
        statusBarFooter = StatusBarFooter.load(primaryStage, getStatusbarPlaceholder(), config.getDailyPlannerFilePath());
        commandBox = CommandBox.load(primaryStage, getCommandBoxPlaceholder(), resultDisplay, logic);
        forDate.setText("");
        setForDateEventHandler();
    }

    private void setForDateEventHandler() {
        logic.getLastShowDateProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue observableValue, Object oldValue,
                Object newValue) {
                String newString = (String) newValue;
                if (newString.equals("")) {
                    forDate.setText("");
                } else if (newString.equals("complete") || newString.equals("completed")){
                    forDate.setText(" - " + newString);
                } else if (newString.equals("not complete") || newString.equals("not completed")) {
                    forDate.setText(" - " + newString);
                }
                else {
                    forDate.setText("for " + (String) newValue);
                }
            }
          });
        
    }

    private AnchorPane getCommandBoxPlaceholder() {
        return commandBoxPlaceholder;
    }

    private AnchorPane getStatusbarPlaceholder() {
        return statusbarPlaceholder;
    }

    private AnchorPane getResultDisplayPlaceholder() {
        return resultDisplayPlaceholder;
    }

    public AnchorPane getPersonListPlaceholder() {
        return personListPanelPlaceholder;
    }
    
    public AnchorPane getPinnedTaskPlaceholder() {
        return pinnedTaskPlaceholder;
    }

    public void hide() {
        primaryStage.hide();
    }

    private void setTitle(String appTitle) {
        primaryStage.setTitle(appTitle);
    }

    /**
     * Sets the default size based on user preferences.
     */
    protected void setWindowDefaultSize(UserPrefs prefs) {
        primaryStage.setHeight(prefs.getGuiSettings().getWindowHeight());
        primaryStage.setWidth(prefs.getGuiSettings().getWindowWidth());
        if (prefs.getGuiSettings().getWindowCoordinates() != null) {
            primaryStage.setX(prefs.getGuiSettings().getWindowCoordinates().getX());
            primaryStage.setY(prefs.getGuiSettings().getWindowCoordinates().getY());
        }
    }

    private void setWindowMinSize() {
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setMinWidth(MIN_WIDTH);
    }

    /**
     * Returns the current size and the position of the main Window.
     */
    public GuiSettings getCurrentGuiSetting() {
        return new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
    }

    @FXML
    public void handleHelp() {
        HelpWindow helpWindow = HelpWindow.load(primaryStage);
        helpWindow.show();
    }

    public void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        raise(new ExitAppRequestEvent());
    }

    public TaskListPanel getPersonListPanel() {
        return this.taskListPanel;
    }

    public void loadPersonPage(ReadOnlyTask person) {
        browserPanel.loadPersonPage(person);
    }

    public void releaseResources() {
        browserPanel.freeResources();
    }
}
