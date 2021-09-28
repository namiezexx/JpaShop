package jpabook.jpashop.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

	private final EntityManager em;
	
	public void save(Item item) {
		if(item.getId() == null) {
			/**
			 * entity의 id가 null이라는 의미는 db에 없는 데이터라는 의미이며 이때는 persist를 통해 영속성 컨텍스트에 영속화 시킨다.
			 */
			em.persist(item);
		} else {
			/**
			 * item값이 있다면 기존에 db에 있는 레코드를 가져온 데이터기 때문에 update가 수행된다.
			 */
			em.merge(item);
		}
	}
	
	public Item findOne(Long id) {
		return em.find(Item.class, id);
	}
	
	public List<Item> findAll() {
		return em.createQuery("select i from Item i", Item.class)
				.getResultList();
	}
}
