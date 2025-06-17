package com.example.roomie.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class PostDTO {
    private Long postId;
    private Long userId;
    private String title;
    private String content;
    private String writeDtm;
    private String writerName;

    // JPQL에서 사용할 생성자 -> AllArgsConstructor 애너테이션으로 생성함
//    public PostDTO(Long postId, Long userId, String title, String content, String writeDtm, String writerName) {
//        this.postId = postId;
//        this.userId = userId;
//        this.title = title;
//        this.content = content;
//        this.writeDtm = writeDtm;
//        this.writerName = writerName;
//    }

    public String toString() {
        return "PostDTO [postId=" + postId + ", title=" + title + ", content=" + content
                + ", writeDtm=" + writeDtm + ", writerName=" + writerName + "]";
    }
}
