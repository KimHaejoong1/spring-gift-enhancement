package gift.service.itemService;

import gift.dto.itemDto.ItemCreateDto;
import gift.entity.Item;
import gift.repository.itemRepository.ItemRepositoryJdbc;

//@Service
public class ItemServiceImplV2 {
    private final ItemRepositoryJdbc itemRepository;

    public ItemServiceImplV2(ItemRepositoryJdbc itemRepository) {
        this.itemRepository = itemRepository;
    }

    public ItemCreateDto saveItem(ItemCreateDto dto) throws Exception {
        Item item = new Item(dto.name(), dto.price(), dto.imageUrl());
        Item saveditem = itemRepository.saveItem(item);
        return new ItemCreateDto(saveditem);
    }
}
