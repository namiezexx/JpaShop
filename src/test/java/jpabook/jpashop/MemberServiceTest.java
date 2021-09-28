package jpabook.jpashop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.service.MemberService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

	@Autowired
	MemberService memberService;
	
	@Autowired
	MemberRepository memberRepository;
	
	@Test
	public void 회원가입() throws Exception {
		//given
		Member member = new Member();
		member.setName("lee");
		member.setAddress(new Address("서울", "구로구", "123-123"));
		
		//when
		Long saveId = memberService.join(member);
		
		//then
		assertEquals(member, memberRepository.findOne(saveId));
	}
	
	@Test(expected = IllegalStateException.class)  //발생하는 예외가 어떤 예외이다라고 미리 선언하면 catch를 안써도 되서 가독성이 좋아진다.
	public void 중복_회원_예외() throws Exception {
		//given
		Member member1 = new Member();
		member1.setName("lee");
		
		Member member2 = new Member();
		member2.setName("lee");
		
		//when
		memberService.join(member1);
		memberService.join(member2);
		
		//then
		fail("예외가 발생해야 한다.");  //fail은 테스트 케이스가 해당 라인에 들어오면 예외를 던진다.
		
	}
}
