import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.IllegalArgumentException;
import java.lang.NumberFormatException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application{

    Database db = new Database();
    Person user;

    static String readFile(String path, Charset encoding) throws IOException{
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

	public static void main(String[] args){
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
        Gson gson = new Gson();

        String content = "";
        try {
            content = readFile("database.json", StandardCharsets.UTF_8);
        }
        catch(IOException e){e.printStackTrace();}

        db = gson.fromJson(content, db.getClass());
        db.puteveryoneinallpeople();
        Collections.sort(db.getAppointments());

        primaryStage.setTitle("MEDICODE");
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);

        Scene primaryScene;

/*******************************************************************************
*                                                                              *
*                                                                              *
*                             Common Elements                                  *
*                                                                              *
*                                                                              *
*******************************************************************************/

        Button logout_btn           = new Button("Log Out");
        Button viewappointments_btn = new Button("View Appointments");
        Button viewmessages_btn     = new Button("View Messages");
        Button viewprofile_btn      = new Button("View Profile");
        Button viewnurses_btn       = new Button("View Nurses");


/*******************************************************************************
*                                                                              *
*                                                                              *
*                               Screens                                        *
*                                                                              *
*                                                                              *
*******************************************************************************/
        /***********************************
        *            Login Screen          *
        ***********************************/
        GridPane logingrid = new GridPane();
        logingrid.setAlignment(Pos.CENTER);
        logingrid.setHgap(10);
        logingrid.setVgap(10);
        logingrid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        logingrid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("User Name:");
        logingrid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        logingrid.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        logingrid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        logingrid.add(pwBox, 1, 2);

        Button login_btn = new Button("Sign in");
		Button logincreateaccount_btn = new Button("Create account");
        VBox vbBtn = new VBox(10);
        vbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        vbBtn.getChildren().add(login_btn);
		vbBtn.getChildren().add(logincreateaccount_btn);
        logingrid.add(vbBtn, 1, 4);

        Text loginFailText = new Text();
        logingrid.add(loginFailText, 1, 6);
        loginFailText.setFill(Color.FIREBRICK);

        primaryScene = new Scene(logingrid, 300, 275);
        primaryStage.setScene(primaryScene);
        primaryStage.show();

        /***********************************
        *        Create Account Screen     *
        ***********************************/
        GridPane createaccountgrid = new GridPane();
        createaccountgrid.setAlignment(Pos.CENTER);
        createaccountgrid.setHgap(10);
        createaccountgrid.setVgap(10);
        createaccountgrid.setPadding(new Insets(25, 25, 25, 25));

        Button createaccountback_btn = new Button("Back");
        createaccountgrid.add(createaccountback_btn, 0, 0);

        Text createaccount_txt = new Text("Create Account");
        createaccount_txt.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        createaccountgrid.add(createaccount_txt, 1, 0, 2, 1);

        createaccountgrid.add(new Label("First Name:"), 0, 1);
        TextField firstnametf = new TextField();
        createaccountgrid.add(firstnametf, 1, 1);

        createaccountgrid.add(new Label("Last Name:"), 0, 2);
        TextField lastnametf = new TextField();
        createaccountgrid.add(lastnametf, 1, 2);

        createaccountgrid.add(new Label("Date of Birth:"), 0, 3);
        TextField dobtf = new TextField();
        createaccountgrid.add(dobtf, 1, 3);

        createaccountgrid.add(new Label("Password:"), 0, 4);
        TextField passwordtf = new TextField();
        createaccountgrid.add(passwordtf, 1, 4);

        Text fillallfields = new Text();
        fillallfields.setFill(Color.FIREBRICK);
        createaccountgrid.add(fillallfields, 0, 6);

        Button createaccountcreate_btn = new Button("Create Account");
        createaccountgrid.add(createaccountcreate_btn, 1, 5);

/*******************************************************************************
*                                                                              *
*                                                                              *
*                            User Screen                                       *
*                                                                              *
*                                                                              *
*******************************************************************************/
        GridPane userviewgrid = new GridPane();
        GridPane usergrid = new GridPane();
        //usergrid.setAlignment(Pos.CENTER);
        usergrid.setHgap(10);
        usergrid.setVgap(10);
        usergrid.setPadding(new Insets(25, 25, 25, 25));
        usergrid.add(userviewgrid, 1, 0);

        /***********************************
        *          Patient Elements        *
        ***********************************/
        Button viewprescriptions_btn = new Button("View Prescriptions");
        Button viewdoctor_btn        = new Button("View Doctor");
        Button changedoctor_btn      = new Button("Change Doctor");

        VBox pbtnvbox = new VBox();
        pbtnvbox.setPrefWidth(150);

        /***********************************
        *           Doctor Elements        *
        ***********************************/
        Button createpatient_btn   = new Button("Create Patient");
        Button logappointment_btn  = new Button("Log Appointment");
        Button patientsearch_btn   = new Button("Patient Search");
        Button viewpatients_btn    = new Button("View Patients");
        Button prescribemed_btn    = new Button("Prescribe Medication");
        Button makeappointment_btn = new Button("Make Appointment");

        VBox dbtnvbox = new VBox();
        dbtnvbox.setPrefWidth(150);


/*******************************************************************************
*                                                                              *
*                                                                              *
*                          Logged in Screen                                    *
*                                                                              *
*                                                                              *
*******************************************************************************/

        /***********************************
        *    Appointment Table Columns     *
        ***********************************/
        TableColumn<Appointments, String> datecol = new TableColumn<>("Date");
        datecol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Appointments, String> timecol = new TableColumn<>("Time");
        timecol.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn<Appointments, String> concernscol = new TableColumn<>("Concerns");
        concernscol.setCellValueFactory(new PropertyValueFactory<>("concerns"));

        TableColumn<Appointments, String> concerncol = new TableColumn<>("Concern");
        concerncol.setCellValueFactory(new PropertyValueFactory<>("concern"));

        TableColumn<Appointments, String> patientcol = new TableColumn<>("Patient");
        patientcol.setCellValueFactory(new PropertyValueFactory<>("patientUsername"));

        /***********************************
        *            Appointments          *
        ***********************************/
        GridPane appointmentgrid = new GridPane();
        appointmentgrid.setAlignment(Pos.CENTER);
        appointmentgrid.setHgap(10);
        appointmentgrid.setVgap(10);
        appointmentgrid.setPadding(new Insets(25, 25, 25, 25));

        TableView<Appointments> uappointments_tv = new TableView<>();

        uappointments_tv.getColumns().add(datecol);
        uappointments_tv.getColumns().add(timecol);
        uappointments_tv.getColumns().add(concerncol);

        TableView<Appointments> pappointments_tv = new TableView<>();

        TableColumn<Appointments, String> pdatecol = new TableColumn<>("Date");
        pdatecol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Appointments, String> ptimecol = new TableColumn<>("Time");
        ptimecol.setCellValueFactory(new PropertyValueFactory<>("time"));
        
        TableColumn<Appointments, String> ppatientcol = new TableColumn<>("Patient");
        ppatientcol.setCellValueFactory(new PropertyValueFactory<>("patientUsername"));
        
        pappointments_tv.getColumns().add(pdatecol);
        pappointments_tv.getColumns().add(ptimecol);
        pappointments_tv.getColumns().add(concernscol);

        appointmentgrid.add(new Label("Your Upcoming Appointments:"), 0, 0);
        appointmentgrid.add(uappointments_tv                        , 0, 1);
        appointmentgrid.add(new Label("Your Past Appointments:")    , 0, 2);
        appointmentgrid.add(pappointments_tv                        , 0, 3);

        /***********************************
        *  Previous Appointment Sub Window *
        ***********************************/
        GridPane subappointmentgrid = new GridPane();
        subappointmentgrid.setAlignment(Pos.CENTER);
        subappointmentgrid.setHgap(10);
        subappointmentgrid.setVgap(10);
        subappointmentgrid.setPadding(new Insets(25, 25, 25, 25));

        TextField subappp_tf         = new TextField("");
        TextField subappd_tf         = new TextField("");
        TextField subappconcerns_tf  = new TextField("");
        TextField subappbp_tf        = new TextField("");
        TextField subapptemp_tf      = new TextField("");
        TextField subappheight_tf    = new TextField("");
        TextField subappallergies_tf = new TextField("");

        Button sublog_btn            = new Button("Log");

        subappointmentgrid.add(new Label("Patient Username: "), 0, 0);
        subappointmentgrid.add(new Label("Doctor Username: " ), 0, 1);
        subappointmentgrid.add(new Label("Concerns: "        ), 0, 2);
        subappointmentgrid.add(new Label("Blood Pressure: "  ), 0, 3);
        subappointmentgrid.add(new Label("Temperature: "     ), 0, 4);
        subappointmentgrid.add(new Label("Height: "          ), 0, 5);
        subappointmentgrid.add(new Label("Allergies: "       ), 0, 6);

        subappointmentgrid.add(subappp_tf                     , 1, 0);
        subappointmentgrid.add(subappd_tf                     , 1, 1);
        subappointmentgrid.add(subappconcerns_tf              , 1, 2);
        subappointmentgrid.add(subappbp_tf                    , 1, 3);
        subappointmentgrid.add(subapptemp_tf                  , 1, 4);
        subappointmentgrid.add(subappheight_tf                , 1, 5);
        subappointmentgrid.add(subappallergies_tf             , 1, 6);

        subappointmentgrid.add(sublog_btn                     , 1, 7);

        Stage subappointmentwindow = new Stage();
        subappointmentwindow.setScene(new Scene(subappointmentgrid));

        /***********************************
        *         Log Appointments         *
        ***********************************/
        GridPane logappointmentgrid = new GridPane();
        logappointmentgrid.setAlignment(Pos.CENTER);
        logappointmentgrid.setHgap(10);
        logappointmentgrid.setVgap(10);
        logappointmentgrid.setPadding(new Insets(25, 25, 25, 25));

        TableView<Appointments> loguappointments_tv = new TableView<>();
        
        TableColumn<Appointments, String> ldatecol = new TableColumn<>("Date");
        ldatecol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Appointments, String> ltimecol = new TableColumn<>("Time");
        ltimecol.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn<Appointments, String> lconcerncol = new TableColumn<>("Concern");
        lconcerncol.setCellValueFactory(new PropertyValueFactory<>("concern"));

        TableColumn<Appointments, String> lpatientcol = new TableColumn<>("Patient");
        lpatientcol.setCellValueFactory(new PropertyValueFactory<>("patientUsername"));

        loguappointments_tv.getColumns().add(ldatecol);
        loguappointments_tv.getColumns().add(ltimecol);
        loguappointments_tv.getColumns().add(lpatientcol);
        loguappointments_tv.getColumns().add(lconcerncol);

        logappointmentgrid.add(loguappointments_tv, 0, 0);

        /***********************************
        *         Make Appointments        *
        ***********************************/
        GridPane makeappointmentgrid = new GridPane();
        makeappointmentgrid.setAlignment(Pos.CENTER);
        makeappointmentgrid.setHgap(10);
        makeappointmentgrid.setVgap(10);
        makeappointmentgrid.setPadding(new Insets(25, 25, 25, 25));

        TextField dappointuser_tf    = new TextField();
        TextField dappointdate_tf    = new TextField();
        TextField dappointtime_tf    = new TextField();
        TextField dappointconcern_tf = new TextField();

        Button addappointment_btn = new Button("Add Appointment");

        makeappointmentgrid.add(new Label("Patient Username: "), 0, 0);
        makeappointmentgrid.add(dappointuser_tf                , 1, 0);
        makeappointmentgrid.add(new Label("Date: ")            , 0, 1);
        makeappointmentgrid.add(dappointdate_tf                , 1, 1);
        makeappointmentgrid.add(new Label("Time: ")            , 0, 2);
        makeappointmentgrid.add(dappointtime_tf                , 1, 2);
        makeappointmentgrid.add(new Label("Concern: ")         , 0, 3);
        makeappointmentgrid.add(dappointconcern_tf             , 1, 3);
        makeappointmentgrid.add(addappointment_btn             , 1, 4);

        /***********************************
        *        View Prescriptions        *
        ***********************************/
        GridPane vprescriptiongrid = new GridPane();
        vprescriptiongrid.setAlignment(Pos.CENTER);
        vprescriptiongrid.setHgap(10);
        vprescriptiongrid.setVgap(10);
        vprescriptiongrid.setPadding(new Insets(25, 25, 25, 25));

        TableView<Prescription> prescriptions_tv = new TableView<>();

        TableColumn<Prescription, String> prescnamecol = new TableColumn<>("Medication");
        prescnamecol.setCellValueFactory(new PropertyValueFactory<>("prescName"));

        TableColumn<Prescription, String> expdatecol = new TableColumn<>("Expiration Date");
        expdatecol.setCellValueFactory(new PropertyValueFactory<>("expDate"));

        TableColumn<Prescription, Integer> qtycol = new TableColumn<>("Qty.");
        qtycol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        prescriptions_tv.getColumns().add(prescnamecol);
        prescriptions_tv.getColumns().add(expdatecol);
        prescriptions_tv.getColumns().add(qtycol);

        vprescriptiongrid.add(new Label("Your Prescriptions:"), 0, 0);
        vprescriptiongrid.add(prescriptions_tv                , 0, 1);

        /***********************************
        *        Prescribe Medication      *
        ***********************************/
        GridPane dprescribegrid = new GridPane();
        dprescribegrid.setAlignment(Pos.CENTER);
        dprescribegrid.setHgap(10);
        dprescribegrid.setVgap(10);
        dprescribegrid.setPadding(new Insets(25, 25, 25, 25));

        TextField dprecuser_tf = new TextField();
        TextField dprecmeds_tf = new TextField();
        TextField dprecexp_tf  = new TextField();
        TextField dprecqty_tf  = new TextField();

        Button prescribe_btn = new Button("Prescribe");

        dprescribegrid.add(new Label("Patient Username: "), 0, 0);
        dprescribegrid.add(dprecuser_tf                   , 1, 0);
        dprescribegrid.add(new Label("Medication: ")      , 0, 1);
        dprescribegrid.add(dprecmeds_tf                   , 1, 1);
        dprescribegrid.add(new Label("Expiration Date: ") , 0, 2);
        dprescribegrid.add(dprecexp_tf                    , 1, 2);
        dprescribegrid.add(new Label("Quantity: ")        , 0, 3);
        dprescribegrid.add(dprecqty_tf                    , 1, 3);
        dprescribegrid.add(prescribe_btn                  , 1, 4);


        /***********************************
        *             View Doctor          *
        ***********************************/
        GridPane vdoctorgrid = new GridPane();
        vdoctorgrid.setAlignment(Pos.CENTER);
        vdoctorgrid.setHgap(10);
        vdoctorgrid.setVgap(10);
        vdoctorgrid.setPadding(new Insets(25, 25, 25, 25));

        Label docusername = new Label("");
        Label docfullname = new Label("");
        Label docphone    = new Label("");

        vdoctorgrid.add(docusername, 0, 0);
        vdoctorgrid.add(docfullname, 0, 1);
        vdoctorgrid.add(docphone   , 0, 2);

        /***********************************
        *            View Profile          *
        ***********************************/
        GridPane vprofilegrid = new GridPane();
        vprofilegrid.setAlignment(Pos.CENTER);
        vprofilegrid.setHgap(10);
        vprofilegrid.setVgap(10);
        vprofilegrid.setPadding(new Insets(25, 25, 25, 25));

        Label username_l = new Label("");
        Label fullname_l = new Label("");
        Label phone_l    = new Label("");

        vprofilegrid.add(username_l, 0, 0);
        vprofilegrid.add(fullname_l, 0, 1);
        vprofilegrid.add(phone_l   , 0, 2);

        /***********************************
        *           Change Doctor          *
        ***********************************/
        GridPane changedocgrid = new GridPane();
        changedocgrid.setAlignment(Pos.CENTER);
        changedocgrid.setHgap(10);
        changedocgrid.setVgap(10);
        changedocgrid.setPadding(new Insets(25, 25, 25, 25));

        TableView<Doctor> docs_tv = new TableView<>();

        TableColumn<Doctor, String> docusername_tc = new TableColumn<>("Username");
        docusername_tc.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<Doctor, String> docfirstname_tc = new TableColumn<>("First Name");
        docfirstname_tc.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Doctor, String> doclastname_tc = new TableColumn<>("Last Name");
        doclastname_tc.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        docs_tv.getColumns().add(docusername_tc);
        docs_tv.getColumns().add(docfirstname_tc);
        docs_tv.getColumns().add(doclastname_tc);

        Button changedocchange_btn = new Button("Change");

        changedocgrid.add(new Label("Doctors:"), 0, 0);
        changedocgrid.add(changedocchange_btn  , 1, 0);
        changedocgrid.add(docs_tv              , 0, 1);

        /***********************************
        *             View Nurses          *
        ***********************************/
        GridPane vnursegrid = new GridPane();
        vnursegrid.setAlignment(Pos.CENTER);
        vnursegrid.setHgap(10);
        vnursegrid.setVgap(10);
        vnursegrid.setPadding(new Insets(25, 25, 25, 25));

        TableView<Nurse> nurses_tv = new TableView<>();

        TableColumn<Nurse, String> nurseusername_tc = new TableColumn<>("Username");
        nurseusername_tc.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<Nurse, String> nursefirstname_tc = new TableColumn<>("First Name");
        nursefirstname_tc.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Nurse, String> nurselastname_tc = new TableColumn<>("Last Name");
        nurselastname_tc.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Nurse, String> nursephonenum_tc = new TableColumn<>("Phone Number");
        nursephonenum_tc.setCellValueFactory(new PropertyValueFactory<>("phone"));

        nurses_tv.getColumns().add(nurseusername_tc);
        nurses_tv.getColumns().add(nursefirstname_tc);
        nurses_tv.getColumns().add(nurselastname_tc);
        nurses_tv.getColumns().add(nursephonenum_tc);

        nurses_tv.setPrefWidth(600);

        vnursegrid.add(new Label("Nurses:"), 0, 0);
        vnursegrid.add(nurses_tv           , 0, 1);

        /***********************************
        *            View Patients         *
        ***********************************/
        GridPane vpatientgrid = new GridPane();
        vpatientgrid.setAlignment(Pos.CENTER);
        vpatientgrid.setHgap(10);
        vpatientgrid.setVgap(10);
        vpatientgrid.setPadding(new Insets(25, 25, 25, 25));

        TableView<Patient> patients_tv = new TableView<>();

        TableColumn<Patient, String> patientusername_tc = new TableColumn<>("Username");
        patientusername_tc.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<Patient, String> patientfirstname_tc = new TableColumn<>("First Name");
        patientfirstname_tc.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Patient, String> patientlastname_tc = new TableColumn<>("Last Name");
        patientlastname_tc.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Patient, String> patientphonenum_tc = new TableColumn<>("Phone Number");
        patientphonenum_tc.setCellValueFactory(new PropertyValueFactory<>("phone"));

        patients_tv.getColumns().add(patientusername_tc);
        patients_tv.getColumns().add(patientfirstname_tc);
        patients_tv.getColumns().add(patientlastname_tc);
        patients_tv.getColumns().add(patientphonenum_tc);

        patients_tv.setPrefWidth(600);

        vpatientgrid.add(new Label("Patients:"), 0, 0);
        vpatientgrid.add(patients_tv           , 0, 1);

        /***********************************
        *              Messages            *
        ***********************************/
        GridPane messagegrid = new GridPane();
        messagegrid.setAlignment(Pos.CENTER);
        messagegrid.setHgap(10);
        messagegrid.setVgap(10);
        messagegrid.setPadding(new Insets(25, 25, 25, 25));

        TableView<Message> messages_tv = new TableView<>();

        TableColumn<Message, String> sendercol = new TableColumn<>("Sender");
        sendercol.setCellValueFactory(new PropertyValueFactory<>("sender"));

        TableColumn<Message, String> subjectcol = new TableColumn<>("Subject");
        subjectcol.setCellValueFactory(new PropertyValueFactory<>("subject"));

        TableColumn<Message, String> messagecol = new TableColumn<>("Message");
        messagecol.setCellValueFactory(new PropertyValueFactory<>("message"));

        messages_tv.getColumns().add(sendercol);
        messages_tv.getColumns().add(subjectcol);
        messages_tv.getColumns().add(messagecol);

        Button sendmessage_btn = new Button("Send Message");

        messagegrid.add(new Label("Your Messages:"), 0, 0);
        messagegrid.add(sendmessage_btn            , 1, 0);
        messagegrid.add(messages_tv                , 0, 1);

        /***********************************
        *        Messages Sub window       *
        ***********************************/
        GridPane submessagegrid = new GridPane();
        submessagegrid.setAlignment(Pos.CENTER);
        submessagegrid.setHgap(10);
        submessagegrid.setVgap(10);
        submessagegrid.setPadding(new Insets(25, 25, 25, 25));

        TextField sender_tf  = new TextField("");
        TextField subject_tf = new TextField("");

        TextArea  message_ta = new TextArea();
        message_ta.setWrapText(true);

        submessagegrid.add(sender_tf , 0, 0);
        submessagegrid.add(subject_tf, 0, 1);
        submessagegrid.add(message_ta, 0, 2);

        Button subsend_btn = new Button("Send");

        Stage submessagewindow = new Stage();
        submessagewindow.setScene(new Scene(submessagegrid));


/*******************************************************************************
*                                                                              *
*                                                                              *
*                               Event Listeners                                *
*                                                                              *
*                                                                              *
*******************************************************************************/

        logout_btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                userviewgrid.getChildren().clear();
                primaryScene.setRoot(logingrid);
            }
        });

        login_btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                // use instanceof later to control which screen is displayed
                for (Person p : db.allpeople){
                    if(!userTextField.getText().equals(p.getUsername())){
                        continue;
                    }

                    if (!pwBox.getText().equals(p.getPassword())){
                        loginFailText.setText("Incorrect Username or Password");
                        return;
                    }

                    user = p;
                    primaryScene.setRoot(usergrid);
                    userTextField.setText("");
                    pwBox.setText("");

                    if (p instanceof Patient){
                        usergrid.add(pbtnvbox, 0, 0);

                        pbtnvbox.getChildren().addAll(
                        logout_btn, viewappointments_btn, viewprescriptions_btn,
                        viewdoctor_btn, changedoctor_btn, viewnurses_btn,
                        viewmessages_btn, viewprofile_btn
                        );

                        for (Node b : pbtnvbox.getChildren())
                            ((Button) b).prefWidthProperty().bind(pbtnvbox.widthProperty());

                    }

                    else if (p instanceof Doctor){
                        usergrid.add(dbtnvbox, 0, 0);
                        dbtnvbox.getChildren().addAll(
                        logout_btn, viewappointments_btn, createpatient_btn,
                        logappointment_btn, patientsearch_btn, viewnurses_btn,
                        viewpatients_btn, prescribemed_btn, makeappointment_btn,
                        viewmessages_btn, viewprofile_btn
                        );

                        for (Node b : dbtnvbox.getChildren())
                            ((Button) b).prefWidthProperty().bind(dbtnvbox.widthProperty());
                    }

                    return;
                }
                loginFailText.setText("Incorrect Username or Password");
            }
        });

        createaccountback_btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                primaryScene.setRoot(logingrid);
            }
        });

        createaccountcreate_btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                if (
                    firstnametf.getText().length()  == 0 ||
                    lastnametf.getText().length()   == 0 ||
                    dobtf.getText().length()        == 0 ||
                    passwordtf.getText().length()   == 0
                ){
                    fillallfields.setText("Please fill all fields!");
                    return;
                }
            }
        });

        logincreateaccount_btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                createaccountback_btn.setVisible(true);
                primaryScene.setRoot(createaccountgrid);
            }
        });

        viewappointments_btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                userviewgrid.getChildren().clear();
                ArrayList<Appointments> upcoming = new ArrayList<>();
                ArrayList<Appointments> previous = new ArrayList<>();

                for (Appointments a : db.getAppointments()){
                    if (!(user.getUsername().equals(a.getPatientUsername()) ||
                          user.getUsername().equals(a.getDoctorUsername())))
                        continue;
                    if (a.getUpcoming())
                        upcoming.add(a);
                    else
                        previous.add(a);
                }
                uappointments_tv.setItems(FXCollections.observableList(upcoming));
                pappointments_tv.setItems(FXCollections.observableList(previous));
                userviewgrid.add(appointmentgrid, 0, 0);
            }
        });

        viewprescriptions_btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                userviewgrid.getChildren().clear();
                if (((Patient)user).getPrescriptionList() != null)
                    prescriptions_tv.setItems(FXCollections.observableList(((Patient)user).getPrescriptionList()));
                userviewgrid.add(prescriptions_tv, 0, 0);
            }
        });

        prescribemed_btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                userviewgrid.getChildren().clear();
                userviewgrid.add(dprescribegrid, 0, 0);
                for (Patient p : db.getPatientList()){
                    if (!p.getUsername().equals(dprecuser_tf.getText()))
                        continue;
                    try{
                            Prescription presc = new Prescription(
                                dprecmeds_tf.getText(), dprecexp_tf.getText(),
                                Integer.parseInt(dprecqty_tf.getText())
                            );
                            p.getPrescriptionList().add(presc);
                        }
                        catch(NumberFormatException ex){ex.printStackTrace();}
                }
            }
        });

        makeappointment_btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                userviewgrid.getChildren().clear();
                userviewgrid.add(makeappointmentgrid, 0, 0);
            }
        });

        createpatient_btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                userviewgrid.getChildren().clear();
                createaccountback_btn.setVisible(false);
                userviewgrid.add(createaccountgrid, 0, 0);
            }
        });

        viewdoctor_btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                userviewgrid.getChildren().clear();
                for (Doctor d : db.getDoctorsList()){
                    if (!d.getUsername().equals(((Patient)user).getDoctor()))
                        continue;

                    docusername.setText("Username: " + d.getUsername());
                    docfullname.setText("Name: " + d.getFirstName() + " " + d.getLastName());
                    docphone.setText("Phone Number:" + d.getPhone());
                    userviewgrid.add(vdoctorgrid, 0, 0);
                    return;
                }
            }
        });

        viewprofile_btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                userviewgrid.getChildren().clear();

                username_l.setText("Username: " + user.getUsername());
                fullname_l.setText("Name: " + user.getFirstName() + " " + user.getLastName());
                phone_l.setText("Phone Number: " + user.getPhone());
                userviewgrid.add(vprofilegrid, 0, 0);
                return;
            }
        });

        changedoctor_btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                userviewgrid.getChildren().clear();
                docs_tv.setItems(FXCollections.observableList(db.getDoctorsList()));
                userviewgrid.add(changedocgrid, 0, 0);
            }
        });

        changedoctor_btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                userviewgrid.getChildren().clear();
                docs_tv.setItems(FXCollections.observableList(db.getDoctorsList()));
                userviewgrid.add(changedocgrid, 0, 0);
            }
        });

        changedocchange_btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                Doctor d = docs_tv.getSelectionModel().getSelectedItem();
                if (d == null)
                    return;
                System.out.println(d);
            }
        });

        viewnurses_btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                userviewgrid.getChildren().clear();
                ArrayList<Nurse> n_al = new ArrayList<>();
                for (Nurse n : db.getNursesList()){
                    if (user instanceof Doctor){
                        if (n.getDoctor().equals(user.getUsername()))
                            n_al.add(n);
                    }
                    for (String p : n.getPatients()){
                        if (user.getUsername().equals(p))
                            n_al.add(n);
                    }
                }
                nurses_tv.setItems(FXCollections.observableList(n_al));
                userviewgrid.add(vnursegrid, 0, 0);
            }
        });

        viewpatients_btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                userviewgrid.getChildren().clear();
                ArrayList<Patient> p_al = new ArrayList<>();
                for (Patient p : db.getPatientList()){
                    for (String d_p : ((Doctor) user).getPatients()){
                        if (p.getUsername().equals(d_p))
                            p_al.add(p);
                    }
                }
                patients_tv.setItems(FXCollections.observableList(p_al));
                userviewgrid.add(vpatientgrid, 0, 0);
            }
        });

        viewmessages_btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                userviewgrid.getChildren().clear();
                if (user.getMessages() != null)
                    messages_tv.setItems(FXCollections.observableList(user.getMessages()));
                userviewgrid.add(messagegrid, 0, 0);
            }
        });

        sendmessage_btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                message_ta.setText("");
                sender_tf.setText("");
                subject_tf.setText("");

                message_ta.setPromptText("Message");
                sender_tf.setPromptText("Recipient");
                subject_tf.setPromptText("Subject");

                message_ta.setEditable(true);
                sender_tf.setEditable(true);
                subject_tf.setEditable(true);

                try{
                    submessagegrid.add(subsend_btn, 0, 3);
                }
                catch (IllegalArgumentException ex){}

                submessagewindow.show();
            }
        });

        subsend_btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                for (Person p : db.allpeople){
                    if (!p.getUsername().equals(sender_tf.getText()))
                        continue;
                    Message m = new Message();
                    m.setSender(user.getUsername());
                    m.setRecipient(p.getUsername());
                    m.setSubject(subject_tf.getText());
                    m.setMessage(message_ta.getText());
                    p.getMessages().add(m);
                }
                submessagewindow.hide();
            }
        });

        messages_tv.setRowFactory( tv -> {
            TableRow<Message> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())){
                    Message m = row.getItem();
                    sender_tf.setText(m.getSender());
                    subject_tf.setText(m.getSubject());
                    message_ta.setText(m.getMessage());

                    message_ta.setEditable(false);
                    sender_tf.setEditable(false);
                    subject_tf.setEditable(false);

                    submessagegrid.getChildren().remove(subsend_btn);

                    submessagewindow.show();
                }
            });
            return row;
        });

        pappointments_tv.setRowFactory( tv -> {
            TableRow<Appointments> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())){
                    Appointments a = row.getItem();
                    sublog_btn.setVisible(false);

                    subappp_tf.setText(a.getPatientUsername());
                    subappd_tf.setText(a.getDoctorUsername());
                    subappconcerns_tf.setText(a.getConcerns());
                    subappbp_tf.setText(a.getBloodPressure());
                    subapptemp_tf.setText(String.valueOf(a.getTemperature()));
                    subappheight_tf.setText(a.getHeight());
                    subappallergies_tf.setText(a.getAllergies());

                    subappp_tf.setEditable(false);
                    subappd_tf.setEditable(false);
                    subappconcerns_tf.setEditable(false);
                    subappbp_tf.setEditable(false);
                    subapptemp_tf.setEditable(false);
                    subappheight_tf.setEditable(false);
                    subappallergies_tf.setEditable(false);

                    subappointmentwindow.show();
                }
            });
            return row;
        });
        
        logappointment_btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                userviewgrid.getChildren().clear();
                
                ArrayList<Appointments> upcoming = new ArrayList<>();
                
                for (Appointments a : db.getAppointments()){
                    if (!user.getUsername().equals(a.getDoctorUsername()))
                        continue;
                    if (a.getUpcoming())
                        upcoming.add(a);
                }
                
                loguappointments_tv.setItems(FXCollections.observableList(upcoming));
                userviewgrid.add(logappointmentgrid, 0, 0);
            }
        });

        loguappointments_tv.setRowFactory( tv -> {
            TableRow<Appointments> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())){
                    Appointments a = row.getItem();
                    sublog_btn.setVisible(true);

                    subappp_tf.setText(a.getPatientUsername());
                    subappd_tf.setText(a.getDoctorUsername());
                    subappconcerns_tf.setText("");
                    subappbp_tf.setText("");
                    subapptemp_tf.setText("");
                    subappheight_tf.setText("");
                    subappallergies_tf.setText("");

                    subappp_tf.setEditable(false);
                    subappd_tf.setEditable(false);
                    subappconcerns_tf.setEditable(true);
                    subappbp_tf.setEditable(true);
                    subapptemp_tf.setEditable(true);
                    subappheight_tf.setEditable(true);
                    subappallergies_tf.setEditable(true);

                    subappointmentwindow.show();
                }
            });
            return row;
        });
    }

    @Override
    public void stop(){
        db.updateJSON();
    }
}