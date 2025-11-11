package dev.gga.firebase.ops.gcs.infra.dto;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

import java.util.Map;

public record SignUrlRequest(
        BlobInfo info,
        Storage.SignUrlOption[] opts,
        int durationUrlSigned,
        java.util.concurrent.TimeUnit timeUnit) {
}
