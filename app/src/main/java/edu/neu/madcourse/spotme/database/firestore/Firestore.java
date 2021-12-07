package edu.neu.madcourse.spotme.database.firestore;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class Firestore {

    public static void writeToDB(FirebaseFirestore db, String tableName, String documentName, Object data) {
        db.collection(tableName).document(documentName).set(data);
    }

    public static void mergeToDB(FirebaseFirestore db, String tableName, String documentName, Object data) {
        db.collection(tableName).document(documentName).set(data, SetOptions.merge());
    }

    public static DocumentReference readFromDBCollection(FirebaseFirestore db, String collection, String documentName) {
        return db.collection(collection).document(documentName);
    }

    public static void writeToDBSubCollection(FirebaseFirestore db, String collection, String documentName, String subCollection, String subDocument, Object data) {
        db.collection(collection).document(documentName).collection(subCollection).document(subDocument).set(data);
    }

    public static DocumentReference readFromDBSubCollection(FirebaseFirestore db, String collection, String documentName, String subCollection, String subDocument) {
        return db.collection(collection).document(documentName).collection(subCollection).document(subDocument);
    }

}
