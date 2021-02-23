package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Site Checker");
        GridPane gp = new GridPane();

        TextField AddressBar = new TextField();
        GridPane.setConstraints(AddressBar, 0,0);

        Label result = new Label();
        GridPane.setConstraints(result, 1,2);


        Line line = new Line(1,2,98,99);

        Button setCheckForDown = new Button("Check Site");
        GridPane.setConstraints(setCheckForDown, 0,1);
        setCheckForDown.setOnAction(event -> {
            try {
                String address = AddressBar.getText();
                InetAddress inetAddress = InetAddress.getByName(address);
                if (inetAddress.isReachable(500)) {
                    String msg = "Server is reachable. An Email will be sent when it is down.";

                    updateLabel(msg, result);
                    boolean reachable = true;
                    while(reachable){
                        if(!inetAddress.isReachable(5000)){
                            reachable = false;
                            try {
                                Email email = new SimpleEmail();
                                email.setFrom("alekanandy@hotmail.com");
                                ArrayList<InternetAddress> mail = new ArrayList<>();
                                mail.add(new InternetAddress("alekanandy@hotmail.com"));
                                email.addTo("alekanandy@hotmail.com");
                                email.setHostName(" smtp.office365.com");
                                email.setSmtpPort(587);
                                email.setAuthenticator(new DefaultAuthenticator("alekanadny@hotmail.com", "Andrei82@"));
                                String emailMsg = "The website " + address + " has gone down";
                                email.setStartTLSEnabled(true);
                                email.setSubject(  "Website is down");
                                email.setMsg(emailMsg);
                                email.send();
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                }else{
                    String msg = "Website is currently unreachable.";
                    updateLabel(msg, result);

                }
            }catch(IOException e){
                e.printStackTrace();
            }
        });
        gp.getChildren().addAll(result, setCheckForDown, AddressBar);
        primaryStage.setScene(new Scene(gp, 300, 275));
        primaryStage.show();
    }

    public void updateLabel(String msg, Label result){
        /*new Thread(new Runnable() {
            @Override public void run() {

                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            result.setText("msg");
                        }
                    });
                }
            }).start();*/

           Platform.runLater(
                   () -> {
                result.setText(msg);
            }
);


        //timer.schedule(timerTask, 100);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
