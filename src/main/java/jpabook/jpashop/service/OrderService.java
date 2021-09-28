package jpabook.jpashop.service;

import jpabook.jpashop.repository.OrderSearch;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final MemberRepository memberRepository;
	private final ItemRepository itemRepository;
	
	/**
	 * 주문 서비스의 주문과 주문 취소 메서드를 보면 비즈니스 로직 대부분이 엔티티에 있다.
	 * 서비스계층은 단순히 엔티티에 필요한 요청을 위임하는 역할을 한다.
	 * 이처럼 엔티티가 비즈니스 로직을 가지고 객체 지향의 특성을 적극 활용하는 것을 '도메인 모델 패턴'이라 한다.
	 * 반대로 엔티티에는 비즈니스 로직이 거의 없고 서비스 계층에서 대부분의 비즈니스 로직을 처리하는 것을 '트랜잭션 스크립트 패턴'이라 한다.
	 */
	
	/**
	 * 주문.
	 */
	@Transactional
	public Long order(Long memberId, Long itemId, int count) {
		
		//엔티티 조회.
		Member member = memberRepository.findOne(memberId);
		Item item = itemRepository.findOne(itemId);
		
		//배송정보 생성.
		Delivery delivery = new Delivery();
		delivery.setAddress(member.getAddress());
		
		//주문상품 생성.
		OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
		
		//주문 생성.
		Order order = Order.createOrder(member, delivery, orderItem);
		
		//주문 저장.
		orderRepository.save(order);
		
		/**
		 * Delivery와 Order를 세팅하지 않아도 연쇄적으로 persist가 되는 이유는 cascade가 걸려있기 때문인다.
		 * 이 부분은 나중에 공부를 하자.
		 */
		
		return order.getId();
	}
	
	/**
	 * 주문 취소.
	 */
	@Transactional
	public void cancelOrder(Long orderId) {
		Order order = orderRepository.findOne(orderId);
		
		/**
		 * order 객체의 cancel메서드만 호출하였지만 나머지 객체들도 영속성 컨텍스트에 변경된 내용이 자동으로 수정 반영된다. JPA의 강점.
		 */
		order.cancel();
	}
	
	/**
	 * 주문 검색.
	 */
	public List<Order> findOrders(OrderSearch orderSearch) {
		return orderRepository.findAllByString(orderSearch);
	}
}
