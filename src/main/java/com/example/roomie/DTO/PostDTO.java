package com.example.roomie.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
//@AllArgsConstructor // JPQL에서는 적용되지 않음
public class PostDTO {
    private Long postId;
    private Long userId;
    private Long postCheckId;
    private String title;
    private String content;
    private String writeDtm;
    private String writerName;
    private List<FileDTO> files;


    // JPQL에서 사용할 생성자
    public PostDTO(Long postId, Long userId, Long postCheckId, String title, String content, String writeDtm, String writerName) {
        this.postId = postId;
        this.userId = userId;
        this.postCheckId = postCheckId;
        this.title = title;
        this.content = content;
        this.writeDtm = writeDtm;
        this.writerName = writerName;
    }

    public String toString() {
        return "PostDTO [postId=" + postId + ", userId=" + userId + ", postCheckId="
                + postCheckId + ", title=" + title + ", content=" + content + ", writeDtm="
                + writeDtm + ", writerName=" + writerName + "]"
                + "\n\tfiles=" + files;
    }
}
