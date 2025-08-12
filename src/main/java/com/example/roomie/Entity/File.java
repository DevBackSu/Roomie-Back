package com.example.roomie.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder
@Table(name = "File")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long fileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private Post post;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "orgin_name")
    private String originName;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String fileType;

    public String toString() {
        return "\n"+"fileId : " + fileId + "\nfilePath : " + filePath + "\noriginName : " + originName
                + "\npost : " + post + "\nfileName : " + fileName + "\nfileType : " + fileType;
    }
}
