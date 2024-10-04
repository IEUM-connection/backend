package com.springboot.answer.entity;

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

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private AnswerStatus answerStatus = AnswerStatus.ANSWERED;

    public enum AnswerStatus {
        ANSWERED("답변 완료"),
        CLOSED("문의 완료???????"),
        ;

        @Getter
        private String answerStatus;

        AnswerStatus(String answerStatus) {
            this.answerStatus = answerStatus;
        }



    }

    // guardian, admin과 연결하기

    @OneToOne
    @JoinColumn(name = "question_id")
    private Question question;

}
