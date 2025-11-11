package dev.gga.firebase.ops.gcs.infra.adapter;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.*;
import dev.gga.firebase.ops.gcs.configuration.GcsProperties;
import dev.gga.firebase.ops.gcs.infra.mapper.StorageMapper;
import dev.gga.firebase.ops.gcs.infra.proxy.BucketProxy;
import dev.gga.firebase.ops.gcs.infra.proxy.StorageProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class GcsStorageFacade implements BucketAdmin, FolderOps, ObjectStorage {

    private final BucketProxy bucketProxy;

    private final StorageProxy storageProxy;

    private final StorageMapper storageMapper;

    @Value("${app.firebase.durationUrlSigned}")
    private int durationUrlSigned;

    private static final String HEADER_CONTENT = "response-content-disposition";


    public GcsStorageFacade(final StorageProxy storageProxy, final BucketProxy bucketProxy,
                            final StorageMapper storageMapper) {
        this.bucketProxy = bucketProxy;
        this.storageProxy = storageProxy;
        this.storageMapper = storageMapper;
    }

    @Override
    public HashSet<BlobId> getAllBlobInfo() {
        return bucketProxy.getAllBlobInfo();
    }

    @Override
    public Optional<Blob> getBlobById(final String id) {
        return bucketProxy.getBlobById(id);
    }

    @Override
    public Page<Blob> getBlobByPrefix(final String prefix) {
        return bucketProxy.getBlobByPrefix(prefix);
    }

    @Override
    public Optional<String> getSignUrl(final String id) {

        var optionaBlob = bucketProxy.getBlobById(id);
        if(optionaBlob.isEmpty()){
            return Optional.empty();
        }
        var file =  optionaBlob.get();
        Map<String,String> qp = new HashMap<>();
        qp.put(HEADER_CONTENT, "inline" + "; filename=\"" + id + "\"");

        var signUrlRequest = storageMapper.toSignUrlRequest(file, qp,
                durationUrlSigned,
                java.util.concurrent.TimeUnit.MINUTES);

        var url = storageProxy.getSignUrl(signUrlRequest);

        return Optional.ofNullable(url);
    }
}
