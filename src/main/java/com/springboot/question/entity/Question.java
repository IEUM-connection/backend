package com.springboot.question.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.springboot.answer.entity.Answer;
import com.springboot.member.entity.Guardian;
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
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long questionId;

    @Column(nullable = false)
    private String questionTitle;

    @Column(nullable = false)
    private String questionContent;

    @Column(nullable = false)
    private LocalDateTime questionDate = LocalDateTime.now();

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private QuestionStatus questionStatus = QuestionStatus.PENDING;

    @Column(nullable = false, name = "LAST_MODIFIED_AT")
    LocalDateTime modifiedAt = LocalDateTime.now();

    public enum QuestionStatus {
        PENDING("답변 대기중"),
        ANSWERED("답변 완료");

        @Getter
        private String questionStatus;

        QuestionStatus(String questionStatus) {
            this.questionStatus = questionStatus;
        }
    }

    // guardian과 연결하기
    @JsonBackReference
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "GUARDIAN_ID")
    private Guardian guardian;


    @OneToOne(mappedBy = "question", orphanRemoval = true)
    private Answer answer;
}
