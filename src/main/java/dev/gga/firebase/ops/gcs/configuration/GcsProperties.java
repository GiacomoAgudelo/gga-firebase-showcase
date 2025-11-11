package dev.gga.firebase.ops.gcs.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;

@ConfigurationProperties(prefix = "app.firebase")
public record GcsProperties(
        String projectId,
        String privateKeyId,
        String privateKey,
        String clientEmail,
        String clientId,
        String authUri,
        String tokenUri,
        String authProviderX509CertUrl,
        String clientX509CertUrl,
        HashSet<String> scopes,
        String dbId,
        String storage
) {}