package com.kjms.repository;

import com.kjms.domain.EntityProductionDiscussionMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityProductionDiscussionMemberRepository extends JpaRepository<EntityProductionDiscussionMember, Long> {



}
