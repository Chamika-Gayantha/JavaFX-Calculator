import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class FXCalculator extends Application {

    // UI Components for displaying input and calculation history
    private TextField display = new TextField();
    private Label lastLabel = new Label("Last: ");
    private Label currentLabel = new Label("Current: ");

    // Variables to store the  current calculation
    private double num1 = 0;
    private String operator = "";

    @Override
    public void start(Stage stage) {

        // ===== Top Display Section =====
        
        VBox topBox = new VBox(5);
        topBox.setPadding(new Insets(10));
        topBox.setStyle("-fx-background-color: #111;"); // Dark background for the display area

        lastLabel.setStyle("-fx-text-fill: #00ffff;"); // Cyan text for history
        currentLabel.setStyle("-fx-text-fill: #00ffff;");

        // Main input field styling
        display.setFont(Font.font(28));
        display.setAlignment(Pos.CENTER_RIGHT); 
        display.setEditable(false); // Prevent manual keyboard typing for better control
        display.setStyle(
                 "-fx-background-color: black;" +
                 "-fx-text-fill: white;" +
                 "-fx-border-color: white;" +
                 "-fx-border-width: 2;"
        );

        topBox.getChildren().addAll(lastLabel, display, currentLabel);

        // ===== Buttons Grid =====

        // GridPane allows placing buttons in a row/column coordinate system
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(8); // Horizontal gap between buttons
        grid.setVgap(8); // Vertical gap between buttons

        // Button labels in order of appearance (4 columns wide)
        String[] buttons = {
                "7","8","9","/",
                "4","5","6","*",
                "1","2","3","-",
                "0","C","=","+"
        };

        int row = 0, col = 0;
        for (String text : buttons) {
            Button btn = createStyledButton(text);

            // Attach the click event to our logic handler
            btn.setOnAction(e -> handleInput(text));

            grid.add(btn, col, row);

            col++;
            if (col == 4) { // Wrap to next row after 4 columns
                col = 0;
                row++;
            }
        }

        // ===== Root Layout =====


        BorderPane root = new BorderPane();
        root.setTop(topBox);      // Display at the top
        root.setCenter(grid);     // Buttons in the middle
        root.setStyle("-fx-background-color: #000;");

        Scene scene = new Scene(root, 300, 420);

        stage.setTitle("JavaFX Retro Calculator");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Creates a button with  consistent retro theme and hover effects.
     */
    private Button createStyledButton(String text) {
        Button btn = new Button(text);
        btn.setPrefSize(65, 65);
        btn.setFont(Font.font(16));

        // Default style: Black background with white borders
        String defaultStyle = "-fx-background-color: black; -fx-text-fill: white; " +
                             "-fx-border-color: white; -fx-border-width: 2; -fx-font-weight: bold;";
        
        String hoverStyle = "-fx-background-color: #222; -fx-text-fill: cyan; " +
                           "-fx-border-color: white; -fx-border-width: 2;";

        btn.setStyle(defaultStyle);

        // UI Feedback: Change style when mouse enters/exits button area
        btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
        btn.setOnMouseExited(e -> btn.setStyle(defaultStyle));

        return btn;
    }

    
      //Central logic for handling all button clicks.
     
    private void handleInput(String value) {
        
        // 1. Number Input: Append value into the current display

        if (value.matches("[0-9]")) {
            display.setText(display.getText() + value);
        }

        // 2. Clear (C)
        else if (value.equals("C")) {
            display.clear();
            num1 = 0;
            operator = "";
            lastLabel.setText("Last: ");
            currentLabel.setText("Current: ");
        }

        // 3. Calculation (=)
        else if (value.equals("=")) {
            if (display.getText().isEmpty() || operator.isEmpty()) return;

            double num2 = Double.parseDouble(display.getText());
            double result = 0;

            switch (operator) {
                case "+": result = num1 + num2; break;
                case "-": result = num1 - num2; break;
                case "*": result = num1 * num2; break;
                case "/":
                    if (num2 != 0) result = num1 / num2;
                    else {
                        display.setText("Error"); // Handle division by zero
                        return;
                    }
                    break;
            }

            display.setText(String.valueOf(result));
            lastLabel.setText("Last: " + num1 + " " + operator + " " + num2);
            currentLabel.setText("Current: Result");
            operator = ""; // Clear operator after equals
        }

        // 4. Operator (+, -, *, /): Store first number and prepare for second input
        else {
            if (display.getText().isEmpty()) return;
            
            num1 = Double.parseDouble(display.getText());
            operator = value;
            currentLabel.setText("Current: " + num1 + " " + operator);
            display.clear();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}