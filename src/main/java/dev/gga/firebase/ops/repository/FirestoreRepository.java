package dev.gga.firebase.ops.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.WriteResult;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Repository
public class FirestoreRepository {

    private final Firestore db;

    public FirestoreRepository(final Firestore db) {
        this.db = db;
    }

    public String create(final String collection, final Map<String, Object> data) throws ExecutionException, InterruptedException {
        String id = UUID.randomUUID().toString();
        ApiFuture<WriteResult> wr = db.collection(collection).document(id).create(data);
        wr.get();
        return id;
    }

    public DocumentSnapshot get(final String collection, final String id) throws ExecutionException, InterruptedException {
        DocumentSnapshot snap = db.collection(collection).document(id).get().get();
        if (!snap.exists()) {
            throw new NoSuchElementException("Note not found: " + id);
        }
        return snap;
    }

    public String update(final String collection, final String id, final Map<String, Object> data) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> wr = db.collection(collection).document(id).set(data);
        wr.get();
        return id;
    }

    public String modify(final String collection, final String id, final Map<String, Object> data) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> wr = db.collection(collection).document(id).set(data, SetOptions.merge());
        wr.get();
        return id;
    }

    public void delete(final String collection, final String id) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> wr = db.collection(collection).document(id).delete();
        wr.get();
    }
}
