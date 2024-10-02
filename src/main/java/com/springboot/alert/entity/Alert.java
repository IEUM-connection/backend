package com.springboot.alert.entity;

import com.springboot.member.entity.Member;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.security.Timestamp;

@Entity
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long alertId;

    @ManyToOne
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    private String content;

    private Timestamp sentDateTime;

    private Timestamp scheduledSentDateTime;

    @Column(columnDefinition = "varchar(50) default 'GENERAL'")
    private String notificationType;

    @Column(columnDefinition = "varchar(50) default 'INDIVIDUAL'")
    private String notificationScope;
}
