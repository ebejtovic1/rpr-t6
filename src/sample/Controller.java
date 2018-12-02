package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.time.LocalDate;
import java.util.function.Function;

public class Controller {
    ObservableList<String> god= FXCollections.observableArrayList("Prva","Druga","Treća");
    ObservableList<String> ods= FXCollections.observableArrayList("RI","AE","TK","EE");
    ObservableList<String> red= FXCollections.observableArrayList("Redovan","Samofinansirajuci");
    ObservableList<String> bor= FXCollections.observableArrayList("Da","Ne");
    ObservableList<String> cik= FXCollections.observableArrayList("Bachelor","Master","Doktorski studij","Strucni studij");
    ObservableList<String> mj= FXCollections.observableArrayList("Sarajevo","Mostar","Tuzla","Zenica");


    public ComboBox mjestoRodjenjaComboBox;
    public ChoiceBox odsjekChoiceBox;
    public ChoiceBox redovanSamofinansirajuciChoiceBox;
    public ChoiceBox godinaStudijaChoiceBox;
    public ChoiceBox ciklusStudijaChoiceBox;
    public ChoiceBox borilackeChoiceBox;
    public TextField imeField;
    public TextField prezimeField;
    public TextField indeksField;
    public TextField adresaField;
    public TextField telefonField;
    public TextField emailField;
    public TextField JMBGField;
    public DatePicker dateField;
    private ValidationSupport[] support;

    public Controller() {
        support = new ValidationSupport[8];
        for (int i = 0; i < 8; i++)
            support[i] = new ValidationSupport();
    }



    private void godinaStudija(){
        godinaStudijaChoiceBox.setItems(god);
    }
    private void popuniOdsjek(){
        odsjekChoiceBox.setItems(ods);
    }
    private void popuniRedovan(){
        redovanSamofinansirajuciChoiceBox.setItems(red);
    }
    private void popuniBorilacke(){
        borilackeChoiceBox.setItems(bor);
    }
    private void popuniCiklus(){
        ciklusStudijaChoiceBox.setItems(cik);
    }
    private void popuniMjesto(){
        mjestoRodjenjaComboBox.setItems(mj);
    }

    private boolean validanIndeks(String unos) {
        return (unos.length() == 5 && unos.matches("[0-9]+"));
    }
    private boolean validnoImePrezime(String unos) {
        return (unos.length() >= 1 && unos.length() <= 20 && unos.matches("[a-zA-Z]+"));
    }

    private boolean validanJMBG(String unos){
        if (unos.length() != 13 || !unos.matches("[0-9]+"))
            return false;
        int[] duzinaMjeseci = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int dan = getJMBGDan();
        int mjesec = getJMBGMjesec();
        int godina = getJMBGGodina();
        if (mjesec <= 0 || mjesec > 12)
            return false;
        if (godina % 400 == 0 || (godina % 100 != 0 && godina % 4 == 0))
            duzinaMjeseci[1] = 29;
        return  ((dan > 0 && dan <= duzinaMjeseci[mjesec - 1]));
        
    }

    private boolean validanGrad(String unos) {
        return (!mjestoRodjenjaComboBox.getEditor().getText().equals("") && unos.length() >= 1 && unos.length() <= 40 && Character.isUpperCase(unos.charAt(0)));
    }

    private int getJMBGDan() {
        return Integer.parseInt(getJMBG().substring(0, 2));
    }

    private String getJMBG() {
        return JMBGField.getText();
    }

    private int getJMBGMjesec() {
        return Integer.parseInt(getJMBG().substring(2, 4));
    }

    private int getJMBGGodina() {
        int godina = Integer.parseInt(getJMBG().substring(4, 7));
        if (godina >= 900)
            godina += 1000;
        else
            godina += 2000;
        return godina;
    }

    private boolean validanDatumRodjenja() {
        if (dateField.getValue() == null)
            return false;
        int dan = dateField.getValue().getDayOfMonth();
        int mjesec = dateField.getValue().getMonthValue();
        int godina = dateField.getValue().getYear();
        return ((dateField.getValue().compareTo(LocalDate.now()) <= 0) && validanJMBG(getJMBG()) && dan == getJMBGDan() && mjesec == getJMBGMjesec() && godina == getJMBGGodina());
    }

    private boolean validanEmail(String unos) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(unos);
        return m.matches();
    }
    private boolean validanTelefon(String unos) {
        if ((unos.length() != 9 && unos.length() != 10 && unos.length()!=0) || !unos.matches("[0-9]+")) return false;
        else return true;
    }

    private void provjerUnosa(TextField tekstualnoPolje, Function<String, Boolean> validacija) {
        tekstualnoPolje.textProperty().addListener((observableValue, o, n) -> {
            if (validacija.apply(n)) {
                tekstualnoPolje.getStyleClass().removeAll("poljeNijeIspravno");
                tekstualnoPolje.getStyleClass().add("poljeIspravno");
            } else {
                tekstualnoPolje.getStyleClass().removeAll("poljeIspravno");
                tekstualnoPolje.getStyleClass().add("poljeNijeIspravno");
            }
        });
    }

    private void provjeraUnosaNakonFokusa(TextField tekstualnoPolje, ValidationSupport support) {
        tekstualnoPolje.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue || tekstualnoPolje.getText().equals(""))
                support.setErrorDecorationEnabled(false);
            else
                support.setErrorDecorationEnabled(true);
        });
    }

    private void provjerUnosaDatuma(DatePicker datumPolje) {
        datumPolje.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                support[4].setErrorDecorationEnabled(true);
                if (datumPolje.getEditor().getText().equals("")) {
                    datumPolje.getStyleClass().removeAll("poljeNijeIspravno", "poljeIspravno");
                    datumPolje.getStyleClass().add("poljeNeutralno");
                } else if (validanDatumRodjenja()) {
                    datumPolje.getStyleClass().removeAll("poljeNijeIspravno", "poljeNeutralno");
                    datumPolje.getStyleClass().add("poljeIspravno");
                } else {
                    datumPolje.getStyleClass().removeAll("poljeIspravno", "poljeNeutralno");
                    datumPolje.getStyleClass().add("poljeNijeIspravno");
                }
            } else {
                support[4].setErrorDecorationEnabled(false);
            }
        });
    }

    @FXML
    private void initialize() {
        support[0].registerValidator(imeField, false, Validator.createPredicateValidator(o1 -> validnoImePrezime(imeField.getText()), "Ime mora počinjati sa velikim slovom, dozvoljena su isključivo slova engleskog alfabeta i ne smije biti duže od 20 karaktera"));
        support[1].registerValidator(prezimeField, false, Validator.createPredicateValidator(o1 -> validnoImePrezime(prezimeField.getText()), "Prezime mora počinjati sa velikim slovom, dozvoljena su isključivo slova engleskog alfabeta i ne smije biti duže od 20 karaktera"));
        support[2].registerValidator(indeksField, false, Validator.createPredicateValidator(o1 -> validanIndeks(indeksField.getText()), "Indeks mora biti petocifreni broj"));
        support[3].registerValidator(JMBGField, false, Validator.createPredicateValidator(o1 -> validanJMBG(JMBGField.getText()), "Neispravan format JMBG"));
        support[4].registerValidator(dateField, false, Validator.createPredicateValidator(o1 -> validanDatumRodjenja(), "Vaš datum rođenja se mora poklapati sa JMBG, ne smije biti u budućnosti"));
        support[5].registerValidator(mjestoRodjenjaComboBox, false, Validator.createPredicateValidator(o1 -> validanGrad((String) mjestoRodjenjaComboBox.getValue()), "Grad mora počinjati sa velikim slovom, dozvoljena su isključivo slova engleskog alfabeta i ne smije biti duže od 40 karaktera"));
        support[6].registerValidator(telefonField, false, Validator.createPredicateValidator(o1 -> validanTelefon(telefonField.getText()), "Telefon unosite u formatu: 0XXYYYZZZ"));
        support[7].registerValidator(emailField, false, Validator.createPredicateValidator(o1 -> validanEmail(emailField.getText()), "Neispravan email"));
        godinaStudija();
        popuniOdsjek();
        popuniRedovan();
        popuniBorilacke();
        popuniCiklus();
        popuniMjesto();
        provjerUnosa(imeField, this::validnoImePrezime);
        provjerUnosa(prezimeField, this::validnoImePrezime);
        provjerUnosa(indeksField, this::validanIndeks);
        provjerUnosa(telefonField, this::validanTelefon);
        provjerUnosa(emailField, this::validanEmail);
        provjerUnosa(JMBGField, this::validanJMBG);
        provjerUnosaDatuma(dateField);
        provjerUnosa(mjestoRodjenjaComboBox.getEditor(), this::validanGrad);
        provjeraUnosaNakonFokusa(imeField, support[0]);
        provjeraUnosaNakonFokusa(prezimeField, support[1]);
        provjeraUnosaNakonFokusa(indeksField, support[2]);
        provjeraUnosaNakonFokusa(JMBGField, support[3]);
        provjeraUnosaNakonFokusa(mjestoRodjenjaComboBox.getEditor(), support[5]);
        provjeraUnosaNakonFokusa(telefonField, support[6]);
        provjeraUnosaNakonFokusa(emailField, support[7]);

    }

    public void kliknutoDugmeUnos(ActionEvent actionEvent) {

            String ispis = "";
            ispis += "Ime: " + imeField.getText();
            ispis += "\nPrezime: " + prezimeField.getText();
            ispis += "\nBroj indeksa: " + indeksField.getText();
            ispis += "\nJMBG: " + JMBGField.getText();
            ispis += "\nDatum rođenja: " + dateField.getValue();
            ispis += "\nMjesto rođenja: " + mjestoRodjenjaComboBox.getValue();
            ispis += "\nKontakt adresa: " + adresaField.getText();
            ispis += "\nKontakt telefon: " + telefonField.getText();
            ispis += "\nEmail adresa: " + emailField.getText();
            ispis += "\nOdsjek: " + odsjekChoiceBox.getValue();
            ispis += "\nGodina studija: " + godinaStudijaChoiceBox.getValue();
            ispis += "\nCiklus studija: " + ciklusStudijaChoiceBox.getValue();
            System.out.println(ispis);
        }

}
