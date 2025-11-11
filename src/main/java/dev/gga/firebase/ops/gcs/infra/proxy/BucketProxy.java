package dev.gga.firebase.ops.gcs.infra.proxy;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class BucketProxy {

    private final Bucket bucket;


    public BucketProxy(final Bucket bucket) {
        this.bucket = bucket;
    }


    public HashSet<BlobId> getAllBlobInfo() {
        return StreamSupport.stream(bucket.list().iterateAll().spliterator(), false)
                .map(Blob::getBlobId)
                .collect(Collectors.toCollection(HashSet::new));
    }

    public Optional<Blob> getBlobById(final String id) {
            return Optional.ofNullable(bucket.get(id));
    }

    public Page<Blob> getBlobByPrefix(final String prefix) {
        return bucket.list(
                Storage.BlobListOption.prefix(prefix),
                Storage.BlobListOption.currentDirectory()
        );
    }
}
