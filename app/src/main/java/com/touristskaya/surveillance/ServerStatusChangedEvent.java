package com.touristskaya.surveillance;

import com.communication.surveillance.lib.communication.CommunicationMessage;
import com.communication.surveillance.lib.communication.ServerInformationMessage;
import com.communication.surveillance.lib.interfaces.Event;
import com.communication.surveillance.lib.interfaces.EventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Add a class header comment
 */
public class ServerStatusChangedEvent implements Event {
    private List<EventListener> listeners;
    private ServerInformationMessage messageData;


    public ServerStatusChangedEvent() {
        listeners = new ArrayList<>();
        messageData = new ServerInformationMessage();
    }


    public ServerInformationMessage getEventData() {
        return messageData;
    }

    public void setEventData(ServerInformationMessage message) {
        this.messageData = message;
        fireEvent();
    }


    @Override
    public void addEventListener(EventListener eventListener) {
        listeners.add(eventListener);
    }

    @Override
    public void removeEventListener(EventListener eventListener) {
        listeners.remove(eventListener);
    }

    @Override
    public void fireEvent() {
        for (int i = 0; i < listeners.size(); ++i)
            listeners.get(i).eventFired(this);
    }
}
