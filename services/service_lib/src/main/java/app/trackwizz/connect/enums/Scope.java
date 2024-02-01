package app.trackwizz.connect.enums;

public enum Scope {

    /**
     * Code : 10
     * Name : kyc-extraction
     * Internal Code: A104
     */
    KYC_EXTRACTION(10,
            "kyc-extraction",
            "A104"),
    /**
     * Code : 11
     * Name : kyc-match
     * Internal Code: A108
     */
    KYC_MATCH(11,
            "kyc-match",
            "A108"),
    /**
     * Code : 12
     * Name : photo-match
     * Internal Code: A108
     */
    FACE_MATCH(12,
            "face-match",
            "A108")
    ;
    private final Integer id;
    private final String name;
    private final String internalCode;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInternalCode() {
        return internalCode;
    }

    Scope(Integer id, String name, String internalCode) {
        this.id = id;
        this.name = name;
        this.internalCode = internalCode;
    }
}
