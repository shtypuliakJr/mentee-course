import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ObjectVersion;

import java.time.Instant;

public class Main {
    public static void main(String[] args) {

        AwsSessionCredentials credentials = AwsSessionCredentials
                .create(ConfigParam.AWS_ACCESS_KEY.get(), ConfigParam.AWS_SECRET_KEY.get(), "");

        S3Client s3Client = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();

        BucketWorker bucketWorker = new BucketWorker(s3Client, ConfigParam.BUCKET_NAME.get());

        ObjectVersion objectVersion = bucketWorker.getInfoAboutObject("file3.txt", Instant.parse("2022-04-25T15:29:29Z"));
        System.out.println("Filename: " + objectVersion.key());
        System.out.println("Version id: " + objectVersion.versionId());
        System.out.println("Last modified: " + objectVersion.lastModified());
        System.out.println("IsLatest: " + objectVersion.isLatest());

        System.out.println("--------------------------------------------------------");

        GetObjectResponse objectByVersion = bucketWorker.getObjectByVersion(objectVersion.key(), objectVersion.versionId());
        System.out.println("Version id: " + objectByVersion.versionId());
        System.out.println("Last modified: " + objectByVersion.lastModified());
    }

}