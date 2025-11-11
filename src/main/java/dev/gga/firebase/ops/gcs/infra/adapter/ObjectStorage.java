package dev.gga.firebase.ops.gcs.infra.adapter;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;

import java.util.HashSet;
import java.util.Optional;

public interface ObjectStorage {

    HashSet<BlobId> getAllBlobInfo();

    Optional<Blob> getBlobById(final String id);

    Page<Blob> getBlobByPrefix(final String prefix);
}
