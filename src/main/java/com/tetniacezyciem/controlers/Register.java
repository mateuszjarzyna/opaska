package com.tetniacezyciem.controlers;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.tetniacezyciem.entity.Band;
import com.tetniacezyciem.entity.Contact;
import com.tetniacezyciem.entity.Disease;
import com.tetniacezyciem.repository.BandRepository;
import com.tetniacezyciem.repository.ContactRepository;
import com.tetniacezyciem.repository.DiseaseRepository;
import com.tetniacezyciem.sms.SmsSender;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.transaction.annotation.Transactional;
import pl.smsapi.exception.SmsapiException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@SpringUI
@Theme("valo")
@Transactional
public class Register extends UI {

    private final DiseaseRepository diseaseRepository;
    private final BandRepository bandRepository;
    private final ContactRepository contactRepository;
    private final SmsSender smsSender;


    private VerticalLayout layout;

    public Register(DiseaseRepository diseaseRepository, BandRepository bandRepository,
                    ContactRepository contactRepository, SmsSender smsSender) {
        this.diseaseRepository = diseaseRepository;
        this.bandRepository = bandRepository;
        this.contactRepository = contactRepository;
        this.smsSender = smsSender;
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        String bandName = vaadinRequest.getParameter("b");
        Band band = bandRepository.findByBandShortId(bandName);

        layout = new VerticalLayout();
        layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        setContent(layout);

        if (band == null) {
            registerNewForm(bandName);
        } else {
            askForPassword(band);
        }
    }

    private void askForPassword(Band band) {
        Label header = new Label("Wpisz haslo (na odwrocie opaski)");
        header.addStyleName(ValoTheme.LABEL_H1);
        layout.addComponent(header);
        Label wrongPassword = new Label("<font color=\"red\"> zle haslo</font>", ContentMode.HTML);
        wrongPassword.setStyleName(ValoTheme.LABEL_HUGE);
        layout.addComponent(wrongPassword);
        wrongPassword.setVisible(false);
        TextField password = new TextField();
        password.setValueChangeMode(ValueChangeMode.BLUR);
        password.setWidth("100%");
        layout.addComponent(password);
        Button ok = new Button("Ok");
        ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
        ok.setDescription("Ok");
        layout.addComponent(ok);

        ok.addClickListener(clickEvent -> {
            if (!password.getValue().equals("123")) {
                wrongPassword.setVisible(true);
                return;
            }

            layout = new VerticalLayout();
            Label ownerName = new Label(band.getOwnerName() + " potrzebuje pomocy!");
            ownerName.addStyleName(ValoTheme.LABEL_H1);
            layout.addComponent(ownerName);

            layout.addComponent(new Label(band.getOwnerName() + " cierpi na kilka chorob. Ponizej ich spis i opis."));
            Contact firstContact = band.getContacts().iterator().next();
            layout.addComponent(new Label("Powiadom 112 oraz <a href=\"tel:" + firstContact.getPhoneNumber() + "\">" + firstContact.getName() + "</a>", ContentMode.HTML));
            band.getDiseases().forEach(d -> {
                layout.addComponent(new Label(d.getName() + " - " + d.getHowToHelp()));
            });

            setContent(layout);

            try {
                smsSender.send(band);
            } catch (SmsapiException e) {
                e.printStackTrace();
            }
        });
    }

    private void registerNewForm(String bandName) {
        Label header = new Label("Rejestracja nowej opaski");
        header.addStyleName(ValoTheme.LABEL_H1);
        layout.addComponent(header);

        Button registerButton = new Button("Zarejestruj");

        RegisterForm registerForm = new RegisterForm(Lists.newArrayList(diseaseRepository.findAll()));
        layout.addComponent(registerForm);
        layout.addComponent(registerButton);

        registerButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        registerButton.setDescription("Zarejestruj");

        registerButton.addClickListener(click -> {
            addNewBand(registerForm, bandName);
            layout = new VerticalLayout();
            layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
            setContent(layout);
            Label registered = new Label("Zarejestrowano");
            registered.addStyleName(ValoTheme.LABEL_H1);
            layout.addComponent(registered);
        });
    }

    private void addNewBand(RegisterForm registerForm, String bandName) {
        Contact contact = Contact.builder()
                .name(registerForm.getContactName().getValue())
                .phoneNumber(registerForm.getContactNumber().getValue())
                .build();
        contact = contactRepository.save(contact);

        Band band = Band.builder()
                .bandShortId(bandName)
                .ownerName(registerForm.getFirstName().getValue() + " " + registerForm.getLastName().getValue())
                .phoneNumber("")
                .diseases(getDiseases(registerForm))
                .contacts(Sets.newHashSet(contact))
                .build();
        bandRepository.save(band);
    }

    private Set<Disease> getDiseases(RegisterForm registerForm) {
        List<String> names = registerForm.getDiseases().stream()
                .filter(CheckBox::getValue)
                .map(CheckBox::getCaption)
                .collect(Collectors.toList());
        return diseaseRepository.findByNameIn(names);
    }
}
