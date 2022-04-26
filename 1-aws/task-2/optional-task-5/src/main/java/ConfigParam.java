public enum ConfigParam {

    AWS_ACCESS_KEY(""),
    AWS_SECRET_KEY(""),
    BUCKET_NAME("mentee-artur-shtypuliak-task2");

    private final String value;

    ConfigParam(String value) {
        this.value = value;
    }

    public String get() {
        return value;
    }
}
