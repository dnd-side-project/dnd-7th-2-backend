package com.dnd.niceteam.domain.project;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.JoinType;

public class ProjectSpecification {

    public static Specification<Project> equalMemberId(Long memberId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.join("projectMembers", JoinType.INNER).get("member").get("id"), memberId);
    }

    public static Specification<Project>  equalStatus(ProjectStatus status) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
    }

}
