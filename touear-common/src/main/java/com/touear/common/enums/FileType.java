package com.touear.common.enums;

public enum FileType {
    GIF("gif", "image/gif"),
    JPG("jpg", "image/jpg"),
    JPEG("jpeg", "image/jpeg"),
    PNG("png", "image/png"),
    SVG("svg", "image/svg+xml"),
    JSON("json", "application/json"),
    APK("apk", "application/vnd.android.package-archive"),
    IPA("ipa", "application/iphone"),
    ZIP("zip", "application/zip");

    private final String extension;
    private final String mimeType;

    FileType(String extension, String mimeType) {
        this.extension = extension;
        this.mimeType = mimeType;
    }

    public String getExtension() {
        return extension;
    }

    public String getMimeType() {
        return mimeType;
    }

    public static boolean isValidExtension(String extension) {
        for (FileType type : FileType.values()) {
            if (type.getExtension().equals(extension)) {
                return true;
            }
        }
        return false;
    }

}