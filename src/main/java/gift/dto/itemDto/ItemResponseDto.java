package gift.dto.itemDto;

import gift.entity.Item;

/***
 * resposeDTO에도 굳이 Validation을 적용 시켜야하나?
 * 어차피 반환값일 뿐인데
 */
public record ItemResponseDto(Long id, String name, Integer price, String imageUrl) {

    public static ItemResponseDto from(Item item) {
        return new ItemResponseDto(item.getId(), item.getName(), item.getPrice(), item.getImageUrl());
    }
}
