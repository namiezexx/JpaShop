package jpabook.jpashop.service;

import java.util.List;

import jpabook.jpashop.domain.item.Book;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

	private final ItemRepository itemRepository;

	/**
	 * 영속상태가 아닌 준영속상태의 엔티티를 Insert/Update하는 작업
	 * 준영속상태란 DB에 존재하는 데이터를 기반으로 새롭게 생성된 객체를 말하며 영속상태가 아니기 때문에 Dirty Check가 지원되지 않는다.
	 * 준영속상태의 객체를 통해 merge를 실행하면 1차 캐시에 해당 엔티티가 있는지 조회하고 없으면 DB에서 가져와 1차 캐시에 등록한다.
	 * 이후 가져온 엔티티의 모든 값을 준영속객체의 값으로 전부 Set하여 변경하고 Dirty Check를 실행한다.
	 * 이 과정이 실무에서 매우 위험하다. 준영속 객체에 1개라도 값이 없다면 null로 update가 되기 때문에 실무에서는 merge를 사용하지 않도록한다.
	 * @param item
	 */
	@Transactional
	public void saveItem(Item item) {
		itemRepository.save(item);
	}

	/**
	 * 파라미터의 Id로 엔티티를 조회하면 영속성 컨텍스트에 영속상태로 올라간다.
	 * 영속상태의 엔티티의 값을 변경하면 변경감지(Dirty Check)를 통해 Update수행.
	 * @param itemId
	 * @param bookParam
	 */
	@Transactional
	public void updateItem(Long itemId, Book bookParam) {
		Item findItem = itemRepository.findOne(itemId);
		findItem.setPrice(bookParam.getPrice());
		findItem.setName(bookParam.getName());
		findItem.setStockQuantity(bookParam.getStockQuantity());
	}
	
	public List<Item> findItems() {
		return itemRepository.findAll();
	}
	
	public Item findOne(Long itemId) {
		return itemRepository.findOne(itemId);
	}
}
