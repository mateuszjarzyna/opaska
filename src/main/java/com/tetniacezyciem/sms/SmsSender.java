package com.tetniacezyciem.sms;

import com.tetniacezyciem.entity.Band;
import com.tetniacezyciem.entity.Contact;
import org.springframework.stereotype.Component;
import pl.smsapi.Client;
import pl.smsapi.api.SmsFactory;
import pl.smsapi.api.action.sms.SMSSend;
import pl.smsapi.exception.SmsapiException;

@Component
public class SmsSender {

    public void send(Band band) throws SmsapiException {
        Contact contact = band.getContacts().iterator().next();
        String number = contact.getPhoneNumber();
        String msg = band.getOwnerName() + " jest w tarapatach. Ktos zeskanowal opaske";

        Client client = new Client("mateusz@jarzyna.eu");
        client.setPasswordHash("791dbdca6fa01a520c47bc8804399fc0");

        SmsFactory smsApi = new SmsFactory(client);
        SMSSend action = smsApi.actionSend()
                .setText(msg)
                .setTo(number);
        action.execute();
    }

}