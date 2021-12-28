package com.inhabas.api.repository.member;

import com.inhabas.api.domain.member.Major;
import com.inhabas.api.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class JpaMemberRepository implements MemberRepository {

    @PersistenceContext
    private EntityManager em;


    @Override
    public Member save(Member member) {
        em.persist(member);
        return em.find(Member.class, member.getId());
    }

    @Override
    public Member findById(Integer id) {
        return em.find(Member.class, id);
    }

    @Override
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    @Override
    public Member update(Member member) {
        return em.merge(member);
    }
}
