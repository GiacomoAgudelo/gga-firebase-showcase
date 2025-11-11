package dev.gga.firebase.ops.gcs.configuration;


import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.cloud.StorageClient;
import dev.gga.firebase.ops.gcs.infra.adapter.BucketAdmin;
import dev.gga.firebase.ops.gcs.infra.adapter.FolderOps;
import dev.gga.firebase.ops.gcs.infra.adapter.GcsStorageFacade;
import dev.gga.firebase.ops.gcs.infra.adapter.ObjectStorage;
import dev.gga.firebase.ops.gcs.infra.mapper.StorageMapper;
import dev.gga.firebase.ops.gcs.infra.proxy.BucketProxy;
import dev.gga.firebase.ops.gcs.infra.proxy.StorageProxy;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
@EnableConfigurationProperties(GcsProperties.class)
public class GcsConfiguration {

    private static String normalizePk(final String pk) {
        return pk == null ? null : pk.replace("\\n", "\n");
    }

    @Bean
    public FirebaseApp firebaseApp(final GcsProperties p) throws Exception {

        var creds = ServiceAccountCredentials.fromPkcs8(
                        p.clientId(),
                        p.clientEmail(),
                        normalizePk(p.privateKey()),
                        p.privateKeyId(),
                        p.scopes()
                ).toBuilder()
                .setTokenServerUri(URI.create(p.tokenUri()))
                .build();

        var options = FirebaseOptions.builder()
                .setCredentials(creds)
                .setProjectId(p.projectId())
                .build();

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    public Firestore firestore(final FirebaseApp app, final GcsProperties p) {
        return FirestoreClient.getFirestore(app, p.dbId());
    }

    @Bean
    public Bucket bucket(final FirebaseApp app, final GcsProperties p) {
        return StorageClient.getInstance(app).bucket(p.storage());
    }

    @Bean
    public Storage storage(final Bucket bucket) {
        return bucket.getStorage();
    }

    @Bean
    public BucketProxy bucketProxy(final Bucket bucket){
        return new BucketProxy(bucket);
    }

    @Bean
    public StorageProxy storageProxy(final Storage storage){
        return new StorageProxy(storage);
    }

    @Bean
    public StorageMapper storageMapper(final Storage storage){
        return new StorageMapper();
    }

    @Bean
    public GcsStorageFacade gcsStorageAdapter(final StorageProxy storageProxy, final BucketProxy bucketProxy, final StorageMapper storageMapper) { return new GcsStorageFacade(storageProxy, bucketProxy, storageMapper); }

    @Bean
    public ObjectStorage objectStorage(final GcsStorageFacade gcsStorageFacade) { return gcsStorageFacade; }

    @Bean
    public FolderOps folderOps(final GcsStorageFacade gcsStorageFacade) { return gcsStorageFacade; }

    @Bean
    public BucketAdmin bucketAdmin(final GcsStorageFacade gcsStorageFacade) { return gcsStorageFacade; }

}