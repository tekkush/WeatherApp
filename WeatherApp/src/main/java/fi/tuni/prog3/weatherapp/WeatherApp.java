package fi.tuni.prog3.weatherapp;

import static javafx.application.Application.launch;
import java.util.Map;
import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * This class implements the GUI for the weather forecast application.
 * 
 * @author andre
 */
public class WeatherApp extends Application {
    private Weather weather = new Weather();
    
    private final ReadAndWriteToFile histAndFav = new ReadAndWriteToFile();
    
    private Scene scene;
    
    private final VBox mainBox = new VBox();
    
    private final TextField searchBar = new TextField();
    
    private final Button searchButton = new Button("Search");
    private final Button favouriteButton = new Button();
    private final Button toggleUnitButton = new Button("Unit");
    
    private ComboBox<String> favouriteBox = new ComboBox<>();
    private ComboBox<String> historyBox = new ComboBox<>();
    
    private Character unit;
    
    private String input;
    private String unitStr;
    private String[] currentLoc;
    
    private final Label header = new Label();
    private final Label place = new Label();
    private final Label creditBox = new Label();
    
    private final HBox searchBox = new HBox();
    private final VBox contentBox = new VBox();
    private final HBox buttonBox = new HBox();

    private final Button leftMainButton = new Button("24-hour forecast");
    private final Button centreMainButton = new Button("Current forecast");
    private final Button rightMainButton = new Button("5-day forecast");
    
    private Boolean atLeft;
    private Boolean atCentre;
    private Boolean atRight;
    
    private Boolean favSentMe;
    private Boolean hisSentMe;
    
    private final Label temp = new Label();
    private final Label feel = new Label();
    private final Label tempMin = new Label();
    private final Label tempMax = new Label();
    private final Label pressure = new Label();
    private final Label humidity = new Label();
    private final Label windSpeed = new Label();
    
    /**
     * This function initiates the application's GUI.
     * @param stage: the item representing the primary and only GUI window.
     */
    @Override
    public void start(Stage stage) {
        stage.setTitle("WeatherApp");
        stage.setResizable(false);
        
        if(this.histAndFav.getLast().equals("")) {
            this.histAndFav.addToHistory("Tampere, fi");
        }
            
        String startLoc = this.histAndFav.getLast();
        String[] startLocArr = startLoc.split(", ");
        
        this.currentLoc = new String[]{startLocArr[0], startLocArr[1]};
        
        Boolean inFav = false;
        for(String town : this.histAndFav.getFav()) {
            if(town.toLowerCase().equals((this.currentLoc[0] + ", " + this.currentLoc[1]).toLowerCase())) {
                this.favouriteButton.setText("★");
                inFav = true;
            }
        }
        if(!inFav) {
            this.favouriteButton.setText("☆");
        }
        
        this.weather.getWeatherHourly(this.currentLoc[0], this.currentLoc[1],'C');
        this.weather.getWeather(this.currentLoc[0], this.currentLoc[1]);
        this.weather.getWeatherDaily(this.currentLoc[0], this.currentLoc[1],'C');
        
        this.unit = 'C';
        this.unitStr = " °C";
        
        this.leftMainButton.setPrefWidth(150);
        this.centreMainButton.setPrefWidth(150);
        this.rightMainButton.setPrefWidth(150);
        
        this.buttonBox.getChildren().addAll(this.leftMainButton, 
                                            this.centreMainButton, 
                                            this.rightMainButton);      
        
        this.favouriteBox.setPromptText("Favourites");
        for(String loc : this.histAndFav.getFav()) {
            this.favouriteBox.getItems().add(loc);
        }
        
        this.historyBox.setPromptText("History");
        for(String loc : this.histAndFav.getHis()) {
            this.historyBox.getItems().add(loc);
        }
        
        this.searchBar.setPromptText("e.g. Tampere, fi");
        
        this.searchButton.setPrefWidth(52);
        this.searchBar.setPrefWidth(155);
        this.favouriteButton.setPrefWidth(28);
        this.favouriteBox.setPrefWidth(97);
        this.historyBox.setPrefWidth(80);
        this.toggleUnitButton.setPrefWidth(38);
        this.searchBox.getChildren().addAll(this.searchButton, 
                                            this.historyBox, 
                                            this.searchBar,
                                            this.favouriteButton,
                                            this.favouriteBox,
                                            this.toggleUnitButton);

        this.header.setAlignment(Pos.CENTER);
        this.header.setPrefWidth(450);
        this.header.setFont(new Font(19));

        this.place.setAlignment(Pos.CENTER);
        this.place.setPrefWidth(450);
        this.place.setFont(new Font(18));
        this.place.setText(this.currentLoc[0] + ", " + this.currentLoc[1].toUpperCase());

        this.creditBox.setText(" Icons by Alessio Atzeni, \n" + 
                               " https://www.alessioatzeni.com/ (Free for commercial use)");
        
        this.creditBox.setPrefWidth(450);
        this.creditBox.setMinHeight(40);
        this.creditBox.setStyle("-fx-border-width: 1;" +
                                "-fx-border-color: gray;");
        
        this.mainBox.getChildren().addAll(this.searchBox, 
                                          this.header,     // What gets modified between pages.
                                          this.place,      // What gets modified between searches.
                                          this.contentBox, // What gets modified between pages.
                                          this.buttonBox, 
                                          this.creditBox);

        this.temp.setPrefWidth(225);
        this.temp.setFont(new Font(30));
        
        this.feel.setAlignment(Pos.CENTER);
        this.feel.setPrefWidth(450);
        this.feel.setFont(new Font(18));

        this.tempMin.setPrefWidth(80);
        this.tempMin.setFont(new Font(16));

        this.tempMax.setPrefWidth(80);
        this.tempMax.setFont(new Font(16));

        this.pressure.setPrefWidth(80);
        this.pressure.setFont(new Font(16));

        this.humidity.setPrefWidth(80);
        this.humidity.setFont(new Font(16));

        this.windSpeed.setPrefWidth(80);
        this.windSpeed.setFont(new Font(16));

        this.atLeft = false;
        this.atCentre = false;
        this.atRight = false;
        
        this.favSentMe = false;
        this.hisSentMe = false;
        
        setCentreScreen();
        
        this.scene = new Scene(this.mainBox, 450, 474);
        stage.setScene(this.scene);
        stage.show();
                
// Main screen button(s) handlers !!!
        this.leftMainButton.setOnAction((ActionEvent event) -> {
            setLeftScreen();
        });
        
        this.centreMainButton.setOnAction((ActionEvent event) -> {
            setCentreScreen();
        });
                
        this.rightMainButton.setOnAction((ActionEvent event) -> {
            setRightScreen();
        });
        
// Search button handler !!!
        this.searchButton.setOnAction((ActionEvent event) -> {
            search();
        });
        
// Favourites button handler !!!
        this.favouriteButton.setOnAction((ActionEvent event) -> {
            String favCandidate = this.currentLoc[0] + ", " + this.currentLoc[1];

            if(this.favouriteButton.getText().equals("★")) {
                this.histAndFav.removeFromFavorites(favCandidate);
                this.favouriteButton.setText("☆");
                this.refreshComboBoxes();
                return;
            }
                        
            for(String town : this.histAndFav.getFav()) {
                if(town.equals(favCandidate)) {
                    return;
                }
            }
            
            this.histAndFav.addToFavorites(favCandidate);
            this.favouriteButton.setText("★");
            this.refreshComboBoxes();
        });

// Unit button handler !!!
        this.toggleUnitButton.setOnAction((ActionEvent event) -> {
            if(this.unit.equals('C')) {
                this.unit = 'F';
                this.unitStr = " °F";
                
                this.weather.getWeatherHourly(this.currentLoc[0], this.currentLoc[1],'F');
                this.weather.getWeatherDaily(this.currentLoc[0], this.currentLoc[1],'F');
            }
            else if(this.unit.equals('F')) {
                this.unit = 'C';
                this.unitStr = " °C";
                
                this.weather.getWeatherHourly(this.currentLoc[0], this.currentLoc[1],'C');
                this.weather.getWeatherDaily(this.currentLoc[0], this.currentLoc[1],'C');
            }
            
            if(this.atLeft.equals(true)) {
                this.atLeft = false;
                setLeftScreen();
            }
            else if(this.atCentre.equals(true)) {
                this.atCentre = false;
                setCentreScreen();
            }   
            else if(this.atRight.equals(true)) {
                this.atRight = false;
                setRightScreen();
            }
        });
        
// Favourites handler !!!
        this.favouriteBox.setOnAction((ActionEvent event) -> {
            this.favSentMe = true;
            this.input = this.favouriteBox.getValue().substring(0);
            search();
        });

// History handler !!!
        this.historyBox.setOnAction((ActionEvent event) -> {
            this.hisSentMe = true;
            this.input = this.historyBox.getValue().substring(0);
            search();
        });
    }
    
    /**
     * This function is used to refresh the state of the "History" and 
     * "Favourites" drop-down boxes whenever they are to be updated.
     */
    private void refreshComboBoxes() {
        this.searchBox.getChildren().remove(this.favouriteBox);
        this.favouriteBox = new ComboBox<>();
        for(String loc : this.histAndFav.getFav()) {
            this.favouriteBox.getItems().add(loc);
        }
        this.favouriteBox.setPromptText("Favourites");

        this.searchBox.getChildren().remove(this.historyBox);
        this.historyBox = new ComboBox<>();
        for(String loc : this.histAndFav.getHis()) {
            this.historyBox.getItems().add(loc);
        }
        this.historyBox.setPromptText("History");
        
        this.favouriteBox.setPrefWidth(97);
        this.historyBox.setPrefWidth(80);
        
        this.searchBox.getChildren().add(3, this.favouriteBox);
        this.searchBox.getChildren().add(1, this.historyBox);
        
        this.favouriteBox.setOnAction((ActionEvent event) -> {
            this.favSentMe = true;
            this.input = this.favouriteBox.getValue().substring(0);
            search();
        }); 
        
        this.historyBox.setOnAction((ActionEvent event) -> {
            this.hisSentMe = true;
            this.input = this.historyBox.getValue().substring(0);
            search();
        });
    }
    
    /**
     * This function implements the search for locations whenever the 
     * "search" button is pressed.
     */
    private void search() {
        if(this.favSentMe) {
            this.input = this.favouriteBox.getValue();
            this.favSentMe = false;
        }
        else if(this.hisSentMe) {
            this.input = this.historyBox.getValue();
            this.hisSentMe = false;
        }
        else {
            this.input = this.searchBar.getText();
        }
             
        if(this.input.isEmpty() || this.input == null) {
            return;
        }
        
        String[] newLoc = {};
        
        newLoc = input.split(", ");
       
        this.searchBar.clear();
        
        Weather newWeather = new Weather();
        
        if(newLoc.length != 2) {}
        else if(newWeather.getWeather(newLoc[0], newLoc[1]) == null) {}
        else {
            this.place.setText(newLoc[0] + ", " + newLoc[1].toUpperCase());
            this.weather = newWeather;
            this.currentLoc = newLoc;

            this.weather.getWeatherHourly(this.currentLoc[0], this.currentLoc[1],'C');
            this.weather.getWeatherDaily(this.currentLoc[0], this.currentLoc[1],'C');

            this.atCentre = false; 
            
            this.histAndFav.addToHistory(newLoc[0] + ", " + newLoc[1]);
            setCentreScreen();
        }
        
        this.favouriteButton.setText("☆");
        
        for(String town : this.histAndFav.getFav()) {
            if(town.equals((this.currentLoc[0] + ", " + this.currentLoc[1]))) {
                this.favouriteButton.setText("★");
            }
        }
        this.refreshComboBoxes();
    }
    
    /**
     * This function sets the GUI to the leftmost screen, which depicts 
     * the 24-hour weather forecast.
     */
    private void setLeftScreen() {
        if(this.atLeft) {
            return;
        }
        this.atLeft = true;
        this.atCentre = false;
        this.atRight = false;
        
        this.contentBox.getChildren().clear();
        
        this.header.setText("24-hour forecast");

        VBox hourlyColumn = new VBox();
        
        for(Map<String, String> nthHour : this.weather.getHourlyWeather()) {
            String iconStr  = nthHour.get("icon");
            iconStr = iconStr + ".png";
            InputStream nthImageStream = getClass().getClassLoader().getResourceAsStream(iconStr);   
            Image nthImage = new Image(nthImageStream);
            ImageView nthImageSlot = new ImageView(nthImage);
            HBox imageBox = new HBox(nthImageSlot);
            imageBox.setPrefWidth(185);
            
            Label nthDayTemp = new Label("\n" + nthHour.get("temp") + this.unitStr);
            
            nthDayTemp.setFont(new Font(15));
            nthDayTemp.setPrefWidth(108);
                        
            Label timeLabel = new Label("\n" + nthHour.get("dt_txt"));
            timeLabel.setPrefWidth(140);
            
            HBox nthHourBox = new HBox(timeLabel, imageBox, nthDayTemp);
            nthHourBox.setStyle("-fx-border-width: 1;" +
                                "-fx-border-color: gray;" +
                                "-fx-background-color: white;");
            
            hourlyColumn.getChildren().add(nthHourBox);
            try{nthImageStream.close();} 
            catch(IOException e) {}
        }
        
        ScrollPane scrollHourlyColumn = new ScrollPane(hourlyColumn);
        scrollHourlyColumn.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        this.contentBox.getChildren().addAll(scrollHourlyColumn);
    }
    
    /**
     * This function sets the GUI to the centre screen, which depicts 
     * the current weather forecast.
     */
    private void setCentreScreen() {
        if(this.atCentre) {
            return;
        }
        this.atLeft = false;
        this.atCentre = true;
        this.atRight = false;
                
        this.contentBox.getChildren().clear();
        
        this.header.setText("Current forecast");
        
        this.temp.setText(this.weather.getTemp(this.unit));
        this.feel.setText(this.weather.getDesc() + ", feels like " + this.weather.getTempFL(this.unit));
               
        Label tempMaxLabel = new Label("Max temp:");
        tempMaxLabel.setPrefWidth(100);
        tempMaxLabel.setFont(new Font(16));
        this.tempMax.setText(this.weather.getTempMax(this.unit));
        HBox tempMaxBox = new HBox(tempMaxLabel, this.tempMax);
        tempMaxBox.setAlignment(Pos.CENTER);  
        
        Label tempMinLabel = new Label("Min temp:");
        tempMinLabel.setPrefWidth(100);
        tempMinLabel.setFont(new Font(16));
        this.tempMin.setText(this.weather.getTempMin(this.unit));
        HBox tempMinBox = new HBox(tempMinLabel, this.tempMin);
        tempMinBox.setAlignment(Pos.CENTER);      
        
        Label pressureLabel = new Label("Pressure:");
        pressureLabel.setPrefWidth(100);
        pressureLabel.setFont(new Font(16));
        this.pressure.setText(this.weather.getPressure() + " hPa");
        HBox pressureBox = new HBox(pressureLabel, this.pressure);
        pressureBox.setAlignment(Pos.CENTER);    
        
        Label humidityLabel = new Label("Humidity:");
        humidityLabel.setPrefWidth(100);
        humidityLabel.setFont(new Font(16));
        this.humidity.setText(this.weather.getHumidity() + "%");
        HBox humidityBox = new HBox(humidityLabel, this.humidity);
        humidityBox.setAlignment(Pos.CENTER);  
        
        Label windSpeedLabel = new Label("Wind speed:");
        windSpeedLabel.setPrefWidth(100);
        windSpeedLabel.setFont(new Font(16));
        this.windSpeed.setText(this.weather.getWindSpeed(this.unit) + this.weather.getWindDeg());
        HBox windSpeedBox = new HBox(windSpeedLabel, this.windSpeed);
        windSpeedBox.setAlignment(Pos.CENTER);  

        VBox detailBox = new VBox(this.feel, 
                                  tempMaxBox, 
                                  tempMinBox,
                                  pressureBox,  
                                  humidityBox, 
                                  windSpeedBox);
        
        detailBox.setStyle("-fx-border-width: 1;" +
                           "-fx-border-color: gray;" +
                           "-fx-background-color: white;");
        
        detailBox.setAlignment(Pos.CENTER);
        
        InputStream imageStream = getClass().getClassLoader().getResourceAsStream(this.weather.getIcon() + ".png");   
        Image image = new Image(imageStream);
        ImageView imageSlot = new ImageView(image);
        HBox imageBox = new HBox(imageSlot);
        imageBox.setAlignment(Pos.CENTER_RIGHT);
        imageBox.setPrefWidth(225);
        
        this.contentBox.getChildren().addAll(new HBox(imageBox, this.temp), 
                                             this.feel,
                                             detailBox);
    }
    
    /**
     * This function sets the GUI to the rightmost screen, which depicts 
     * the 5-day weather forecast.
     */
    private void setRightScreen() {
        if(this.atRight) {
            return;
        }
        this.atLeft = false;
        this.atCentre = false;
        this.atRight = true;
        
        this.contentBox.getChildren().clear();
        
        this.header.setText("5-day forecast");
        
        VBox dailyColumn = new VBox();
        
        for(Map<String, String> nthDay : this.weather.getDailyWeather()) {
            String iconStr  = nthDay.get("icon");
            iconStr = iconStr + ".png";
            InputStream nthImageStream = getClass().getClassLoader().getResourceAsStream(iconStr);
            Image nthImage = new Image(nthImageStream);
            ImageView nthImageSlot = new ImageView(nthImage);
            HBox imageBox = new HBox(nthImageSlot);
            imageBox.setPrefWidth(185);
            
            Label nthDayTemp = new Label("Max: " + nthDay.get("temp_max") + this.unitStr + "\n" + 
                                         "Min: " + nthDay.get("temp_min") + this.unitStr);

            nthDayTemp.setFont(new Font(15));
            nthDayTemp.setPrefWidth(100);
                        
            Label timeLabel = new Label("\n" + nthDay.get("date"));
            timeLabel.setPrefWidth(140);
            
            HBox nthDayBox = new HBox(timeLabel, imageBox, nthDayTemp);
            nthDayBox.setStyle("-fx-border-width: 1;" +
                               "-fx-border-color: gray;" +
                               "-fx-background-color: white;");
            
            dailyColumn.getChildren().add(nthDayBox);
            try{nthImageStream.close();}
            catch(IOException e) {}
        }

        this.contentBox.getChildren().addAll(dailyColumn);
    }
    
    /**
     * The main functin of the class.
     * @param args: the arguments for the main function.
     */
    public static void main(String[] args) {
        launch();
    }
}