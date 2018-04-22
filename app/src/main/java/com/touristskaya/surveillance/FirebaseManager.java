package com.touristskaya.surveillance;

import android.util.Base64;

import com.communication.surveillance.lib.communication.CommunicationMessage;
import com.communication.surveillance.lib.communication.ServerInformationMessage;
import com.communication.surveillance.lib.interfaces.CommunicationManager;
import com.communication.surveillance.lib.interfaces.EventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * TODO: Add a class header comment
 */
public class FirebaseManager implements CommunicationManager {
    private volatile static FirebaseManager instance;
    private static DatabaseReference firebaseReference;
    private static final String COMMUNICATION_MESSAGE_FIELD = "communication_message";
    private static final String SERVER_STATUS_FIELD = "server_status";
    private IncomingCommunicationMessageEvent incomingCommunicationMessageEvent;
    private ServerStatusChangedEvent serverStatusChangedEvent;
    private ValueEventListener communicationMessageValueEventListener;
    private ValueEventListener serverStatusEventListener;


    private FirebaseManager() {
        firebaseReference = FirebaseDatabase.getInstance().getReference();

        incomingCommunicationMessageEvent = new IncomingCommunicationMessageEvent();
        serverStatusChangedEvent = new ServerStatusChangedEvent();

        communicationMessageValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot == null || dataSnapshot.getValue() == null)
                    return;

                CommunicationMessage message = SerializationHelper.communicationMessageFromString(dataSnapshot.getValue().toString());
                if (message == null)
                    return;
                if (message.getType() == CommunicationMessage.MessageType.REQUEST)
                    return;

                incomingCommunicationMessageEvent.setEventData(message);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        serverStatusEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot == null || dataSnapshot.getValue() == null)
                    return;

                ServerInformationMessage serverInformationMessage = SerializationHelper.serverInformationMessageFromString(
                                                                        dataSnapshot.getValue().toString()
                                                                    );
                serverStatusChangedEvent.setEventData(serverInformationMessage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


        firebaseReference.child("a")
                .child("d")
                .child(COMMUNICATION_MESSAGE_FIELD)
                .addValueEventListener(communicationMessageValueEventListener);

        firebaseReference.child("a")
                .child("d")
                .child(SERVER_STATUS_FIELD)
                .addValueEventListener(serverStatusEventListener);
    }

    public static FirebaseManager getInstance() {
        if (instance == null) {
            synchronized (FirebaseManager.class) {
                if (instance == null)
                    instance = new FirebaseManager();
            }
        }

        return instance;
    }

    @Override
    public void sendMessage(CommunicationMessage communicationMessage) {
        String dataString = SerializationHelper.communicationMessageToString(communicationMessage);
        firebaseReference.child("a").child("d").child(COMMUNICATION_MESSAGE_FIELD).setValue(dataString);
    }


    public ServerInformationMessage getServerInformation() {
        firebaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot == null || dataSnapshot.getValue() == null)
                    return;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return null;
    }


    public void addIncomingCommunicationMessageEventListener(EventListener listener) {
        incomingCommunicationMessageEvent.addEventListener(listener);
    }
    public void removeIncomingCommunicationMessageEventListener(EventListener listener) {
        incomingCommunicationMessageEvent.removeEventListener(listener);
    }

    public void addServerStatusChangedEventListener(EventListener listener) {
        serverStatusChangedEvent.addEventListener(listener);
    }
    public void removeServerStatusChangedEventListener(EventListener listener) {
        serverStatusChangedEvent.removeEventListener(listener);
    }



    //    @Override
//    public void sendMessage(CommunicationMessage message) {
////        Account currentAccount = AccountManager.getInstance().getCurrentAccount();
////        if (currentAccount.isIncomplete())
////            return;
//
//        // ===
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ObjectOutputStream oos = null;
//        try {
//            oos = new ObjectOutputStream(baos);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            oos.writeObject(message);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            oos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
////        String dataString = Base64.getEncoder().encodeToString(baos.toByteArray());
//        String dataString = android.util.Base64.encodeToString(baos.toByteArray(), Base64.NO_WRAP);
//
//        firebaseReference.child("a").child("d").child(COMMUNICATION_MESSAGE_FIELD).setValue(dataString);
//        // ===
//
////        firebaseDatabase.child(currentAccount.getName()).child(currentAccount.getPassword()).child(COMMUNICATION_MESSAGE_FIELD).setValue(dataString);
//    }
}
