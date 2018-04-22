package com.touristskaya.surveillance;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.communication.surveillance.lib.communication.CommunicationMessage;
import com.communication.surveillance.lib.communication.ServerInformationMessage;

import java.io.*;

/**
 * Created by Alexander on 16.04.2018.
 */
public class SerializationHelper {
    public static CommunicationMessage communicationMessageFromString(String dataString) {
        CommunicationMessage message = null;
        byte[] dataBytes;
        try {
//            dataBytes = Base64.getDecoder().decode(dataString);
            dataBytes = android.util.Base64.decode(dataString, android.util.Base64.NO_WRAP);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }

        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(dataBytes));
            message = (CommunicationMessage) ois.readObject();
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (message == null) {
            System.out.println("SerializationHelper->MESSAGE_IS_NULL");
            return null;
        }

        return message;
    }

    public static ServerInformationMessage serverInformationMessageFromString(String dataString) {
        ServerInformationMessage serverInformationMessage = null;
        byte[] dataBytes;
        try {
//            dataBytes = Base64.getDecoder().decode(dataString);
            dataBytes = android.util.Base64.decode(dataString, android.util.Base64.NO_WRAP);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }

        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(dataBytes));
            serverInformationMessage = (ServerInformationMessage) ois.readObject();
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (serverInformationMessage == null) {
            System.out.println("SerializationHelper->MESSAGE_IS_NULL");
            return null;
        }

        return serverInformationMessage;
    }

    public static String communicationMessageToString(CommunicationMessage message) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (oos == null)
            return null;

        try {
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return android.util.Base64.encodeToString(baos.toByteArray(), android.util.Base64.NO_WRAP);
    }

    public static Bitmap bitmapFromString(String dataString) {
        byte[] imageByteArray = Base64.decode(dataString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
    }

    public static String imageToString() {
        return null;
    }


    //    public void setSurveillanceData(SurveillanceData data) {
//        Account currentAccount = AccountManager.getInstance().getCurrentAccount();
//        if (currentAccount.isIncomplete())
//            return;
//
//
//        System.out.println("HERE");

//        BufferedImage bufferedImage = null;
//        try {
//            bufferedImage = ImageIO.read(new File("C:\\Empty files\\image_4.jpg"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        if (bufferedImage == null) {
//            System.out.println("BUFFERED_IMAGE_IS_NULL");
//            return;
//        }
//
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        try {
//            ImageIO.write(bufferedImage, "jpg", bos);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String imageDataString = Base64.getEncoder().encodeToString(bos.toByteArray());
//
//
//        firebaseDatabase.child(currentAccount.getName()).child(currentAccount.getPassword()).child(COMMUNICATION_MESSAGE_FIELD).setValue(imageDataString);
//    }
}
