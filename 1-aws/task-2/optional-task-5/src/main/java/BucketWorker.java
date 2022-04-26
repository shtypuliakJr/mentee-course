import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectVersionsRequest;
import software.amazon.awssdk.services.s3.model.ObjectVersion;
import software.amazon.awssdk.services.s3.paginators.ListObjectVersionsIterable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

public class BucketWorker {

    private final S3Client s3;
    private final String bucket;

    public BucketWorker(S3Client s3, String bucket) {
        this.s3 = s3;
        this.bucket = bucket;
    }

    public ObjectVersion getInfoAboutObject(String objectName, Instant date) {

        ListObjectVersionsRequest listObjectVersionsRequest = ListObjectVersionsRequest.builder()
                .bucket(bucket)
                .prefix(objectName)
                .build();

        ListObjectVersionsIterable response = s3.listObjectVersionsPaginator(listObjectVersionsRequest);

        return response.versions().stream()
                .filter(objectVersion -> objectVersion.key().equals(objectName))
                .filter(objectVersion -> objectVersion.lastModified().isBefore(date))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public GetObjectResponse getObjectByVersion(String key, String version) {
        GetObjectRequest request = GetObjectRequest.builder().bucket(bucket).key(key).versionId(version).build();
        String pathFolder = "src/main/resources/";
        Path pathForFile = Path.of(pathFolder + key);
        try {
            Files.deleteIfExists(pathForFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s3.getObject(request, pathForFile);
    }
}