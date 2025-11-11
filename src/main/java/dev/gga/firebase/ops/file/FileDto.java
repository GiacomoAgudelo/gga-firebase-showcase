package dev.gga.firebase.ops.file;

import com.google.cloud.storage.Blob;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

public record FileDto(
        String id,
        Blob file,
        StreamingResponseBody streamingResponseBody,
        String signedUrl
) {
}
