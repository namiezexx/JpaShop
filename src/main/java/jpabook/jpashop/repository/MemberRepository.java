package jpabook.jpashop.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

	private final EntityManager em;
	
	public void save(Member member) {
		em.persist(member);
	}
	
	public Member findOne(Long id) {
		return em.find(Member.class, id);
	}
	
	public List<Member> findAll() {
		
		//JPQL 을 통해서 쿼리를 동적으로 만들어 처리.
		return em.createQuery("select m from Member m", Member.class)
				.getResultList();
		
	}
	
	public List<Member> findByName(String name) {
		//JPQL 을 통해 쿼리 조회 시 파라미터를 비인딩하여 데이터 조회.
		return em.createQuery("select m from Member m where m.name = :name", Member.class)
				.setParameter("name", name)
				.getResultList();
	}
}
