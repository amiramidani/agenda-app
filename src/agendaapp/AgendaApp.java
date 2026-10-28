package agendaapp;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class AgendaApp extends Application {

    private TableView<Evenement> table;
    private TextField txtTitre, txtDate, txtHeure, txtDescription;
    private ComboBox<String> comboType;
    private CheckBox chkImportant;
    private TextField txtRecherche;
    private ComboBox<String> comboRecherche;
    private EvenementDAO dao = new EvenementDAO();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("📅 Agenda Personnel");

        // === HEADER ===
        Label header = new Label("📅 Mon Agenda Personnel");
        header.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        header.setStyle("-fx-text-fill: white;");
        HBox headerBox = new HBox(header);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(15));
        headerBox.setStyle("-fx-background-color: linear-gradient(to right, #2c3e50, #3498db); -fx-background-radius: 8;");

        // === FORMULAIRE ===
        Label lblFormTitle = new Label("Nouvel Événement");
        lblFormTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        lblFormTitle.setStyle("-fx-text-fill: #2c3e50;");

        txtTitre = createStyledTextField("Titre de l'événement");
        txtDate = createStyledTextField("2026-05-10");
        txtHeure = createStyledTextField("14:00");
        txtDescription = createStyledTextField("Description de l'événement");

        comboType = new ComboBox<>();
        comboType.getItems().addAll("Réunion", "Personnel", "Travail", "Médical", "Autre");
        comboType.setValue("Réunion");
        comboType.setStyle("-fx-font-size: 13px; -fx-pref-width: 250;");

        chkImportant = new CheckBox("Événement important ⭐");
        chkImportant.setStyle("-fx-font-size: 13px;");

        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(12);
        form.add(createLabel("Titre"), 0, 0);           form.add(txtTitre, 1, 0);
        form.add(createLabel("Date (YYYY-MM-DD)"), 0, 1); form.add(txtDate, 1, 1);
        form.add(createLabel("Heure (HH:MM)"), 0, 2);   form.add(txtHeure, 1, 2);
        form.add(createLabel("Description"), 0, 3);      form.add(txtDescription, 1, 3);
        form.add(createLabel("Type"), 0, 4);             form.add(comboType, 1, 4);
        form.add(createLabel("Important"), 0, 5);        form.add(chkImportant, 1, 5);

        VBox formBox = new VBox(10, lblFormTitle, form);
        formBox.setPadding(new Insets(15));
        formBox.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        // === BOUTONS CRUD ===
        Button btnAjouter = createStyledButton("➕ Ajouter", "#27ae60");
        Button btnModifier = createStyledButton("✏️ Modifier", "#f39c12");
        Button btnSupprimer = createStyledButton("🗑️ Supprimer", "#e74c3c");
        Button btnEffacer = createStyledButton("🔄 Effacer", "#95a5a6");
        Button btnExporter = createStyledButton("📄 Exporter", "#2980b9");

        btnAjouter.setOnAction(e -> ajouterEvenement());
        btnModifier.setOnAction(e -> modifierEvenement());
        btnSupprimer.setOnAction(e -> supprimerEvenement());
        btnEffacer.setOnAction(e -> effacerFormulaire());
        btnExporter.setOnAction(e -> exporterEvenements());

        HBox boutons = new HBox(10, btnAjouter, btnModifier, btnSupprimer, btnEffacer, btnExporter);
        boutons.setAlignment(Pos.CENTER);
        boutons.setPadding(new Insets(10));

        // === RECHERCHE ===
        Label lblRechTitle = new Label("🔍 Recherche");
        lblRechTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        lblRechTitle.setStyle("-fx-text-fill: #2c3e50;");

        txtRecherche = createStyledTextField("Mot clé...");
        comboRecherche = new ComboBox<>();
        comboRecherche.getItems().addAll("Titre", "Date", "Type");
        comboRecherche.setValue("Titre");
        comboRecherche.setStyle("-fx-font-size: 13px;");

        Button btnRechercher = createStyledButton("🔍 Rechercher", "#3498db");
        Button btnAfficherTous = createStyledButton("📋 Afficher Tous", "#8e44ad");
        Button btnImportants = createStyledButton("⭐ Importants", "#e67e22");

        btnRechercher.setOnAction(e -> rechercher());
        btnAfficherTous.setOnAction(e -> chargerTous());
        btnImportants.setOnAction(e -> afficherImportants());

        HBox rechercheRow = new HBox(10, txtRecherche, comboRecherche, btnRechercher, btnAfficherTous, btnImportants);
        rechercheRow.setAlignment(Pos.CENTER_LEFT);

        VBox rechercheBox = new VBox(10, lblRechTitle, rechercheRow);
        rechercheBox.setPadding(new Insets(15));
        rechercheBox.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        // === TABLEAU ===
        table = new TableView<>();
        table.setStyle("-fx-font-size: 13px;");

        TableColumn<Evenement, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(50);
        colId.setStyle("-fx-alignment: CENTER;");

        TableColumn<Evenement, String> colTitre = new TableColumn<>("Titre");
        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colTitre.setPrefWidth(130);

        TableColumn<Evenement, String> colDate = new TableColumn<>("Date");
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateEvenement"));
        colDate.setPrefWidth(100);
        colDate.setStyle("-fx-alignment: CENTER;");

        TableColumn<Evenement, String> colHeure = new TableColumn<>("Heure");
        colHeure.setCellValueFactory(new PropertyValueFactory<>("heure"));
        colHeure.setPrefWidth(60);
        colHeure.setStyle("-fx-alignment: CENTER;");

        TableColumn<Evenement, String> colDesc = new TableColumn<>("Description");
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDesc.setPrefWidth(170);

        TableColumn<Evenement, String> colType = new TableColumn<>("Type");
        colType.setCellValueFactory(new PropertyValueFactory<>("typeEvenement"));
        colType.setPrefWidth(100);
        colType.setCellFactory(column -> new TableCell<Evenement, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-alignment: CENTER; -fx-font-weight: bold;");
                    switch (item) {
                        case "Réunion":
                            setStyle("-fx-alignment: CENTER; -fx-font-weight: bold; -fx-background-color: #AED6F1; -fx-text-fill: #1B4F72;");
                            break;
                        case "Personnel":
                            setStyle("-fx-alignment: CENTER; -fx-font-weight: bold; -fx-background-color: #A9DFBF; -fx-text-fill: #196F3D;");
                            break;
                        case "Travail":
                            setStyle("-fx-alignment: CENTER; -fx-font-weight: bold; -fx-background-color: #F9E79F; -fx-text-fill: #7D6608;");
                            break;
                        case "Médical":
                            setStyle("-fx-alignment: CENTER; -fx-font-weight: bold; -fx-background-color: #F5B7B1; -fx-text-fill: #922B21;");
                            break;
                        default:
                            setStyle("-fx-alignment: CENTER; -fx-font-weight: bold; -fx-background-color: #D7BDE2; -fx-text-fill: #6C3483;");
                            break;
                    }
                }
            }
        });

        TableColumn<Evenement, String> colImportant = new TableColumn<>("Important");
        colImportant.setCellValueFactory(new PropertyValueFactory<>("importantText"));
        colImportant.setPrefWidth(80);
        colImportant.setStyle("-fx-alignment: CENTER;");

        table.getColumns().addAll(colId, colTitre, colDate, colHeure, colDesc, colType, colImportant);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(350);
        table.setRowFactory(tv -> new TableRow<Evenement>() {
            @Override
            protected void updateItem(Evenement item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else if (item.isImportant()) {
                    setStyle("-fx-background-color: #FFD6E0;"); 
                } else {
                    setStyle("");
                }
            }
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtTitre.setText(newVal.getTitre());
                txtDate.setText(newVal.getDateEvenement());
                txtHeure.setText(newVal.getHeure());
                txtDescription.setText(newVal.getDescription());
                comboType.setValue(newVal.getTypeEvenement());
                chkImportant.setSelected(newVal.isImportant());
            }
        });

        VBox tableBox = new VBox(table);
        tableBox.setPadding(new Insets(5));
        tableBox.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        VBox.setVgrow(table, Priority.ALWAYS);

        // === FOOTER ===
        Label footer = new Label("Agenda Personnel — JavaFX + Oracle DB");
        footer.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 11px;");
        HBox footerBox = new HBox(footer);
        footerBox.setAlignment(Pos.CENTER);
        footerBox.setPadding(new Insets(5));

        // === LAYOUT FINAL ===
        VBox root = new VBox(12, headerBox, formBox, boutons, rechercheBox, tableBox, footerBox);
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #ecf0f1;");
        VBox.setVgrow(tableBox, Priority.ALWAYS);

        Scene scene = new Scene(root, 950, 780);
        primaryStage.setScene(scene);
        primaryStage.show();

        chargerTous();
    }

    // === HELPERS UI ===

    private TextField createStyledTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle("-fx-font-size: 13px; -fx-pref-width: 250; -fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #bdc3c7; -fx-padding: 6;");
        return tf;
    }

    private Label createLabel(String text) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));
        lbl.setStyle("-fx-text-fill: #34495e;");
        return lbl;
    }

    private Button createStyledButton(String text, String color) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 5; -fx-cursor: hand;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: derive(" + color + ", -15%); -fx-text-fill: white; -fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 5; -fx-cursor: hand;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 5; -fx-cursor: hand;"));
        return btn;
    }

    // === VALIDATIONS ===

    private boolean validerFormulaire() {
        String titre = txtTitre.getText().trim();
        String date = txtDate.getText().trim();
        String heure = txtHeure.getText().trim();
        String description = txtDescription.getText().trim();

        txtTitre.setText(titre);
        txtDate.setText(date);
        txtHeure.setText(heure);
        txtDescription.setText(description);

        if (titre.isEmpty() || date.isEmpty() || heure.isEmpty()) {
            alerte(Alert.AlertType.WARNING, "Remplissez au moins le titre, la date et l'heure.");
            return false;
        }
        if (titre.length() < 3) {
            alerte(Alert.AlertType.WARNING, "Le titre doit contenir au moins 3 caractères.");
            return false;
        }
        if (titre.length() > 90) {
            alerte(Alert.AlertType.WARNING, "Le titre ne doit pas dépasser 90 caractères.\nActuellement : " + titre.length() + " caractères.");
            return false;
        }
        if (!titre.matches("[a-zA-ZÀ-ÿ0-9 '\\-]+")) {
            alerte(Alert.AlertType.WARNING, "Le titre contient des caractères non autorisés.\nUtilisez uniquement des lettres, chiffres, espaces, apostrophes et tirets.");
            return false;
        }
        if (description.length() > 500) {
            alerte(Alert.AlertType.WARNING, "La description ne doit pas dépasser 500 caractères.\nActuellement : " + description.length() + " caractères.");
            return false;
        }
        if (!validerDate(date)) {
            alerte(Alert.AlertType.WARNING, "Format de date invalide. Utilisez YYYY-MM-DD (ex: 2026-05-10).");
            return false;
        }
        if (!validerHeure(heure)) {
            alerte(Alert.AlertType.WARNING, "Format d'heure invalide. Utilisez HH:MM avec deux chiffres (ex: 09:00, 14:30).\nHeures: 00-23, Minutes: 00-59.");
            return false;
        }
        LocalDate dateEvent = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if (dateEvent.isBefore(LocalDate.now())) {
            alerte(Alert.AlertType.WARNING, "Impossible d'ajouter un événement dans le passé !");
            return false;
        }
        return true;
    }

    private boolean validerDate(String date) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean validerHeure(String heure) {
        if (!heure.matches("\\d{2}:\\d{2}")) return false;
        String[] parts = heure.split(":");
        int h = Integer.parseInt(parts[0]);
        int m = Integer.parseInt(parts[1]);
        return h >= 0 && h <= 23 && m >= 0 && m <= 59;
    }

    // === CRUD ===

    private void ajouterEvenement() {
        if (!validerFormulaire()) return;
        try {
            if (dao.existeDoublon(txtTitre.getText(), txtDate.getText())) {
                alerte(Alert.AlertType.WARNING, "Cet événement existe déjà (même titre et même date) !");
                return;
            }
            if (dao.existeConflit(txtDate.getText(), txtHeure.getText())) {
                alerte(Alert.AlertType.WARNING, "Un événement existe déjà à cette date et heure !");
                return;
            }
            Evenement e = new Evenement(
                txtTitre.getText(), txtDate.getText(), txtHeure.getText(),
                txtDescription.getText(), comboType.getValue(), chkImportant.isSelected()
            );
            dao.ajouter(e);
            chargerTous();
            effacerFormulaire();
            alerte(Alert.AlertType.INFORMATION, "Événement ajouté avec succès !");
        } catch (SQLException ex) {
            alerte(Alert.AlertType.ERROR, "Erreur: " + ex.getMessage());
        }
    }

    private void modifierEvenement() {
        Evenement selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            alerte(Alert.AlertType.WARNING, "Sélectionnez un événement à modifier.");
            return;
        }
        if (!validerFormulaire()) return;
        try {
            if (dao.existeConflitModification(txtDate.getText(), txtHeure.getText(), selected.getId())) {
                alerte(Alert.AlertType.WARNING, "Un autre événement existe déjà à cette date et heure !");
                return;
            }
            selected.setTitre(txtTitre.getText());
            selected.setDateEvenement(txtDate.getText());
            selected.setHeure(txtHeure.getText());
            selected.setDescription(txtDescription.getText());
            selected.setTypeEvenement(comboType.getValue());
            selected.setImportant(chkImportant.isSelected());
            dao.modifier(selected);
            chargerTous();
            effacerFormulaire();
            alerte(Alert.AlertType.INFORMATION, "Événement modifié avec succès !");
        } catch (SQLException ex) {
            alerte(Alert.AlertType.ERROR, "Erreur: " + ex.getMessage());
        }
    }

    private void supprimerEvenement() {
        Evenement selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            alerte(Alert.AlertType.WARNING, "Sélectionnez un événement à supprimer.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer cet événement ?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    dao.supprimer(selected.getId());
                    chargerTous();
                    effacerFormulaire();
                    alerte(Alert.AlertType.INFORMATION, "Événement supprimé !");
                } catch (SQLException ex) {
                    alerte(Alert.AlertType.ERROR, "Erreur: " + ex.getMessage());
                }
            }
        });
    }

    private void exporterEvenements() {
        try {
            List<Evenement> liste = dao.afficherTous();
            if (liste.isEmpty()) {
                alerte(Alert.AlertType.WARNING, "Aucun événement à exporter !");
                return;
            }
            String nomFichier = "src/agendaapp/agenda_export.txt";
            dao.exporterFichier(nomFichier, liste);
            alerte(Alert.AlertType.INFORMATION, "Export réussi !\nFichier : " + nomFichier);
        } catch (Exception ex) {
            alerte(Alert.AlertType.ERROR, "Erreur d'export : " + ex.getMessage());
        }
    }

    private void afficherImportants() {
        try {
            List<Evenement> list = dao.afficherImportants();
            if (list.isEmpty()) {
                alerte(Alert.AlertType.INFORMATION, "Aucun événement important trouvé.");
            }
            table.setItems(FXCollections.observableArrayList(list));
        } catch (SQLException ex) {
            alerte(Alert.AlertType.ERROR, "Erreur: " + ex.getMessage());
        }
    }

    private void rechercher() {
        String motCle = txtRecherche.getText().trim();
        if (motCle.isEmpty()) {
            alerte(Alert.AlertType.WARNING, "Entrez un mot clé pour rechercher.");
            return;
        }
        try {
            List<Evenement> resultats;
            switch (comboRecherche.getValue()) {
                case "Date":
                    if (!validerDate(motCle)) {
                        alerte(Alert.AlertType.WARNING, "Format de date invalide. Utilisez YYYY-MM-DD.");
                        return;
                    }
                    resultats = dao.rechercherParDate(motCle);
                    break;
                case "Type":
                    resultats = dao.rechercherParType(motCle);
                    break;
                default:
                    resultats = dao.rechercherParTitre(motCle);
            }
            if (resultats.isEmpty()) {
                alerte(Alert.AlertType.INFORMATION, "Aucun événement trouvé.");
            }
            table.setItems(FXCollections.observableArrayList(resultats));
        } catch (SQLException ex) {
            alerte(Alert.AlertType.ERROR, "Erreur: " + ex.getMessage());
        }
    }

    private void chargerTous() {
        try {
            List<Evenement> list = dao.afficherTous();
            table.setItems(FXCollections.observableArrayList(list));
        } catch (SQLException ex) {
            alerte(Alert.AlertType.ERROR, "Erreur: " + ex.getMessage());
        }
    }

    private void effacerFormulaire() {
        txtTitre.clear();
        txtDate.clear();
        txtHeure.clear();
        txtDescription.clear();
        comboType.setValue("Réunion");
        chkImportant.setSelected(false);
        table.getSelectionModel().clearSelection();
    }

    private void alerte(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}