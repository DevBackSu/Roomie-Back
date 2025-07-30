package com.example.roomie.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FileDTO {
    private Long fileId;
    private Long postId;
    private String filePath;
    private String fileName;
    private String originName;
    private String fileType;

    public FileDTO(Long fileId, Long postId, String filePath, String fileName, String originName, String fileType) {
        this.fileId = fileId;
        this.postId = postId;
        this.filePath = filePath;
        this.fileName = fileName;
        this.originName = originName;
        this.fileType = fileType;
    }

    public String toString() {
        return "FileDTO [fileId=" + fileId + ", postId=" + postId + ", filePath=" + filePath
                + ", fileName=" + fileName + ", originName=" + originName + ", fileType=" + fileType + "]";
    }
}
