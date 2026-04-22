package com.aimailgo.repository;

import com.aimailgo.model.Mail;
import com.aimailgo.model.MailIntent;
import com.aimailgo.model.MailStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MailRepository extends JpaRepository<Mail, Long> {
    List<Mail> findByIntent(MailIntent intent);
    List<Mail> findByStatus(MailStatus status);
    List<Mail> findByStatusAndAutoExecutableTrue(MailStatus status);
    List<Mail> findByIntentOrderByPriorityDesc(MailIntent intent);
    List<Mail> findByStatusOrderByPriorityDesc(MailStatus status);
    List<Mail> findAllByOrderByPriorityDescCreatedAtDesc();
    List<Mail> findByToAddrOrderByPriorityDesc(String toAddr);
    List<Mail> findByIntentAndStatus(MailIntent intent, MailStatus status);
    long countByIntent(MailIntent intent);
    long countByStatus(MailStatus status);
    long countByAutoExecutableTrue();
}
