package com.tetniacezyciem.controlers;

import com.tetniacezyciem.entity.Disease;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RegisterForm extends VerticalLayout {

    private TextField firstName = new TextField();
    private TextField lastName = new TextField();
    private TextField contactName = new TextField();
    private TextField contactNumber = new TextField();
    private Set<CheckBox> diseases = new HashSet<>();

    public RegisterForm(Collection<Disease> allDiseases) {
        setWidth("100%");
        setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

        addField(firstName, "Imie:");
        addField(lastName, "Nazwisko:");
        addField(contactName, "Imie osoby kontaktowej:");
        addField(contactNumber, "Numer telefonu osoby kontaktowej:");

        Label firstNameLabel = new Label("Choroby, ktore posiadasz");
        firstNameLabel.setWidth("100%");
        addComponentsAndExpand(firstNameLabel);

        allDiseases.forEach(d -> {
            CheckBox checkBox = new CheckBox(d.getName());
            diseases.add(checkBox);
            addComponentsAndExpand(checkBox);
        });
    }

    private void addField(TextField textField, String desc) {
        Label firstNameLabel = new Label(desc);
        firstNameLabel.setWidth("100%");
        addComponentsAndExpand(firstNameLabel);
        textField.setValueChangeMode(ValueChangeMode.BLUR);
        textField.setWidth("100%");

        addComponentsAndExpand(textField);
    }

    public TextField getFirstName() {
        return firstName;
    }

    public TextField getLastName() {
        return lastName;
    }

    public TextField getContactName() {
        return contactName;
    }

    public TextField getContactNumber() {
        return contactNumber;
    }

    public Set<CheckBox> getDiseases() {
        return diseases;
    }
}
