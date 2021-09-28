package jpabook.jpashop.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)  //기본적으로 조회의 경우 readOnly 옵션을 걸어 사용하면 성능상 이점이 있음.
public class MemberService {

	private final MemberRepository memberRepository;
	
	//회원 가입.
	@Transactional  //데이터를 삽입하거나 수정, 삭제하는 메소드는 별도로 readOnly 속성을 갖지 않도록 어노테이션을 추가한다. Transactional의 기본값은 readOnly=false.
	public Long join(Member member) {
		validateDuplicateMember(member);
		memberRepository.save(member);
		return member.getId();
	}
	
	/**
	 * 서비스에서 Member의 등록 정보를 조회하지만 멀티스레드 환경에서 동시에 조회를하고 여러건이 등록될 수 있으므로 방어로직 뿐 아니라 테이블에 유니크 제약조건을 걸어두는것이 좋다.
	 * @param member
	 */
	private void validateDuplicateMember(Member member) {
		
		List<Member> findMembers = memberRepository.findByName(member.getName());
		
		if(!findMembers.isEmpty()) {
			throw new IllegalStateException("이미 존재하는 회원입니다.");
		}
	}
	
	//회원 전체 조회.
	public List<Member> findMembers() {
		return memberRepository.findAll();
	}

	public Member findOne(Long memberId) {
		return memberRepository.findOne(memberId);
	}

	@Transactional
	public void update(Long id, String name) {
		Member member = memberRepository.findOne(id);
		member.setName(name);
	}
}
