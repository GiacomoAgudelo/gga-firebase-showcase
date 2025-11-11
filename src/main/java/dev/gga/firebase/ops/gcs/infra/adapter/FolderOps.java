package dev.gga.firebase.ops.gcs.infra.adapter;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import dev.gga.firebase.ops.gcs.configuration.GcsProperties;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public interface FolderOps {
    Optional<String> getSignUrl(final String id);
}
