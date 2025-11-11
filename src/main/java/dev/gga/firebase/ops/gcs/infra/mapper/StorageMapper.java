package dev.gga.firebase.ops.gcs.infra.mapper;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import dev.gga.firebase.ops.gcs.infra.dto.SignUrlRequest;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class StorageMapper {

    public SignUrlRequest toSignUrlRequest(final Blob file, final Map<String, String> queryParam,
                                   final int durationUrlSigned, final TimeUnit timeUnit) {
        BlobInfo info = BlobInfo.newBuilder(file.getBlobId()).build();
        List<Storage.SignUrlOption> options =  List.of(Storage.SignUrlOption.httpMethod(com.google.cloud.storage.HttpMethod.GET),
                Storage.SignUrlOption.withV4Signature(),
                Storage.SignUrlOption.withQueryParams(queryParam));

        Storage.SignUrlOption[] opts = (options != null)
                ? options.toArray(Storage.SignUrlOption[]::new)   // Java 11+
                : new Storage.SignUrlOption[0];

        return new SignUrlRequest(info, opts, durationUrlSigned, timeUnit);
    }
}
