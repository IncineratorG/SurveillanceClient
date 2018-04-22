package com.touristskaya.surveillance;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.communication.surveillance.lib.communication.CommunicationMessage;
import com.communication.surveillance.lib.communication.EmptyCommand;
import com.communication.surveillance.lib.communication.MessagePayload;
import com.communication.surveillance.lib.communication.PayloadType;
import com.communication.surveillance.lib.communication.ServerInformationMessage;
import com.communication.surveillance.lib.communication.SnapshotRequestCommand;
import com.communication.surveillance.lib.communication.StartSurveillanceCommand;
import com.communication.surveillance.lib.communication.StopSurveillanceCommand;
import com.communication.surveillance.lib.interfaces.CommunicationCommand;
import com.communication.surveillance.lib.interfaces.Event;
import com.communication.surveillance.lib.interfaces.EventListener;

import java.util.ServiceConfigurationError;


public class MainActivity extends AppCompatActivity {
    private static final String CLIENT_COMMUNICATION_MESSAGE_FIELD = "client_communication_message";
    private ImageView imageView;
    private Button toggleObservationButton;
    private Button requestSnapshotButton;

    private ServerInformationMessage currentServerInformation = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.image_view);

        toggleObservationButton = (Button) findViewById(R.id.toggle_observation_button);
        toggleObservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onToggleObservationButtonClicked();
            }
        });
        requestSnapshotButton = (Button) findViewById(R.id.request_snapshot_button);
        requestSnapshotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRequestSnapshotButtonClicked();
            }
        });

        FirebaseManager.getInstance().addServerStatusChangedEventListener(new EventListener() {
            @Override
            public void eventFired(Event event) {
                ServerStatusChangedEvent serverStatusChangedEvent = (ServerStatusChangedEvent) event;
                if (serverStatusChangedEvent == null)
                    return;

                currentServerInformation = serverStatusChangedEvent.getEventData();
                if (currentServerInformation == null)
                    return;

                if (currentServerInformation.getSurveillanceStatus() == ServerInformationMessage.SurveillanceStatus.ACTIVE)
                    toggleObservationButton.setText("STOP");
                else if (currentServerInformation.getSurveillanceStatus() == ServerInformationMessage.SurveillanceStatus.NOT_ACTIVE)
                    toggleObservationButton.setText("START");
            }
        });

        FirebaseManager.getInstance().addIncomingCommunicationMessageEventListener(new EventListener() {
            @Override
            public void eventFired(Event event) {
                IncomingCommunicationMessageEvent incomingCommunicationMessageEvent = (IncomingCommunicationMessageEvent) event;
                CommunicationMessage communicationMessage = incomingCommunicationMessageEvent.getEventData();

                if (communicationMessage == null)
                    return;
                if (communicationMessage.getPayload() == null)
                    return;

                MessagePayload payload = communicationMessage.getPayload();
                if (payload == null)
                    return;

                if (payload.getType() == PayloadType.IMAGE) {
                    String imageData = payload.getPayloadData();
                    Bitmap imageBitmap = SerializationHelper.bitmapFromString(imageData);

                    imageView.setImageBitmap(imageBitmap);
                }

                CommunicationMessage emptyMessage = new CommunicationMessage();
                FirebaseManager.getInstance().sendMessage(emptyMessage);
            }
        });
    }


    private void onRequestSnapshotButtonClicked() {
        if (currentServerInformation == null)
            return;

        if (currentServerInformation.getSurveillanceStatus() == ServerInformationMessage.SurveillanceStatus.ACTIVE) {
            SnapshotRequestCommand snapshotRequestCommand = new SnapshotRequestCommand();

            CommunicationMessage communicationMessage = new CommunicationMessage();
            communicationMessage.setCommand(snapshotRequestCommand);

            FirebaseManager.getInstance().sendMessage(communicationMessage);
        }
    }

    private void onToggleObservationButtonClicked() {
        if (currentServerInformation == null)
            return;
        if (currentServerInformation.getServerStatus() == ServerInformationMessage.ServerStatus.OFFLINE)
            return;

        CommunicationCommand command = null;
        if (currentServerInformation.getSurveillanceStatus() == ServerInformationMessage.SurveillanceStatus.ACTIVE) {
            command = new StopSurveillanceCommand();
        } else if (currentServerInformation.getSurveillanceStatus() == ServerInformationMessage.SurveillanceStatus.NOT_ACTIVE) {
            command = new StartSurveillanceCommand();
        }

        CommunicationMessage communicationMessage = new CommunicationMessage();
        communicationMessage.setCommand(command);

        FirebaseManager.getInstance().sendMessage(communicationMessage);
    }









    public void setImage() {
//        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.test_image);
//        System.out.println(icon.getByteCount());


        //        firebaseReference.child("a").child("d").child("communication_message").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot == null) {
//                    System.out.println("getSnapshotData()->DATA_SNAPSHOT_IS_NULL");
//                    return;
//                }
//                if (dataSnapshot.getValue() == null) {
//                    System.out.println("getSnapshotData()->DATA_SNAPSHOT_VALUE_IS_NULL");
//                    return;
//                }
//
//                // Получаем снимок от сервера.
//                String imageData = dataSnapshot.getValue().toString();
//                // Устанавливаем снимок в 'snapshotImageView'.
//                byte[] imageByteArray = Base64.decode(imageData, Base64.DEFAULT);
//                Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
//
//                imageView.setImageBitmap(imageBitmap);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });






//        firebaseReference.child("a").child("d").child(COMMUNICATION_MESSAGE_FIELD).setValue(testCommand_v2);


//        firebaseReference.child("a").child("d").child("a1").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot == null)
//                    return;
//                if (dataSnapshot.getValue() == null)
//                    return;
//
//                System.out.println(dataSnapshot.getValue().toString());
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }
}
