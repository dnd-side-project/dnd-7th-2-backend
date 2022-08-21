package com.dnd.niceteam.domain.review;

import com.dnd.niceteam.domain.project.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberReviewRepository extends JpaRepository<MemberReview, Long> {

    MemberReview findByReviewerAndReviewee(ProjectMember reviewer, ProjectMember reviewee);

}
