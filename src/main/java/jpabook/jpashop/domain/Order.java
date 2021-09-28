package jpabook.jpashop.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  //createOrder 메서드가 아닌 기본 생성은 막는다.
public class Order {

	@Id @GeneratedValue
	@Column(name = "order_id")
	private Long id;
	
	/**
	 * ManyToOne, OneToOne 같은 XToOne 매핑의 경우 기본 전략이 FetchType.EAGER 로 되어있다.
	 * 반드시 LAZY 전략으로 바꿔서 사용하자.
	 * 실무에서는 EAGER 전략으로 인해 성능이 나오지않는 문제가 발생한다.
	 * 
	 * OneToMany, MamyToMany 같은 XToMany 매핑의 경우는 기본 전략이 FetchType.LAZY 이다.
	 */
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;
	
	/**
	 * CascadeType.ALL 은 order를 영속성 컨텍스트에서 DB로 넣을때 연속적으로 orderItems도 같이 넣는다.
	 */
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderItem> orderItems = new ArrayList<>();
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "delivery_id")
	private Delivery delivery;
	
	private LocalDateTime orderDate;  //주문시간
	
	@Enumerated(EnumType.STRING)  //EnumType.ORDINARY 로 설정하면 숫자로 들어가는데 나중에 중간에 값이 추가되면 실무에서 장애로 이어진다. 반드시 STRING으로 사용하자.
	private OrderStatus status;  //주문상태 [ORDER, CANCEL]
	
	/**
	 * 연관관계 편의 메서드.
	 */
	public void setMember(Member member) {
		this.member = member;
		member.getOrders().add(this);
	}
	
	public void addOrderItem(OrderItem orderItem) {
		orderItems.add(orderItem);
		orderItem.setOrder(this);
	}
	
	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
		delivery.setOrder(this);
	}
	
	//==생성 메서드==//
	/**
	 * 생성 메서드를 구현하였기 때문에 변경에 대해 생성 메서드만 찾아 수정하면 된다.
	 * 다른 로직들을 찾아다니며 수정하지 않아도 되므로 유지보수가 편하다.
	 * @param member
	 * @param delivery
	 * @param orderItems
	 * @return
	 */
	public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
		Order order = new Order();
		order.setMember(member);
		order.setDelivery(delivery);
		for(OrderItem orderItem: orderItems) {
			order.addOrderItem(orderItem);
		}
		order.setStatus(OrderStatus.ORDER);
		order.setOrderDate(LocalDateTime.now());
		return order;
	}
	
	//==비즈니스 로직==//
	/**
	 * 주문 취소.
	 */
	public void cancel() {
		if(delivery.getStatus() == DeliveryStatus.COMP) {
			throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
		}
		
		this.setStatus(OrderStatus.CANCEL);
		for(OrderItem orderItem : orderItems) {
			orderItem.cancel();
		}
	}
	
	//==조회 로직==//
	/**
	 * 전체 주문 가격을 조회.
	 * @return
	 */
	public int getTotalPrice() {
		int totalPrice = 0;
		for(OrderItem orderItem : orderItems) {
			totalPrice += orderItem.getTotalPrice();
		}
		return totalPrice;
	}
}
