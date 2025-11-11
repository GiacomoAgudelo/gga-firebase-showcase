package dev.gga.firebase.ops.gcs.infra.proxy;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import dev.gga.firebase.ops.gcs.infra.dto.SignUrlRequest;
import io.grpc.Context;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class StorageProxy {

    private final Storage storage;

    public StorageProxy(final Storage storage) {
        this.storage = storage;
    }

    public String getSignUrl(final SignUrlRequest signUrlRequest) {
        return storage.signUrl(
                signUrlRequest.info(),
                signUrlRequest.durationUrlSigned(),
                signUrlRequest.timeUnit(),
                signUrlRequest.opts()).toString();

    }
}
