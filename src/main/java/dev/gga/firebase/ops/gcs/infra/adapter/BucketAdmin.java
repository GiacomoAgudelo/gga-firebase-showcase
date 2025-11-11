package dev.gga.firebase.ops.gcs.infra.adapter;

import com.google.cloud.storage.BlobId;

import java.util.HashSet;

public interface BucketAdmin {

    HashSet<BlobId> getAllBlobInfo();
}
