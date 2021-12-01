package edu.neu.madcourse.spotme.database.firestore;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class Firestore {

    public static void writeToDB(FirebaseFirestore db, String tableName, String documentName, Object data) {
        db.collection(tableName).document(documentName).set(data);
    }

    public static void mergeToDB(FirebaseFirestore db, String tableName, String documentName, Object data) {
        db.collection(tableName).document(documentName).set(data, SetOptions.merge());
    }
}
