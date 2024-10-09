package com.springboot.answer.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.springboot.member.entity.Admin;
import com.springboot.member.entity.Guardian;
import com.springboot.question.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long answerId;

    @Column(nullable = false)
    private String responseContent;

    @Column(nullable = false)
    private LocalDateTime answerDate = LocalDateTime.now();

    @Column(nullable = false, name = "LAST_MODIFIED_AT")
    private LocalDateTime modifiedAt = LocalDateTime.now();

    // admin과 연결하기
    @JsonBackReference
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ADMIN_ID")
    private Admin admin;


    @OneToOne
    @JoinColumn(name = "question_id")
    private Question question;

}
