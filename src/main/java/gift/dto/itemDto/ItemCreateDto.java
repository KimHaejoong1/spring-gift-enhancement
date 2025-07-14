package gift.dto.itemDto;

import gift.entity.Item;
import gift.validation.itemPolicy.ItemFieldValid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@ItemFieldValid
public record ItemCreateDto(String name, @Min(0) Integer price, @NotNull @Size(max = 255) String imageUrl,
                            boolean useKakaoName) {


    public ItemCreateDto(Item saveditem) {
        this(saveditem.getName(), saveditem.getPrice(), saveditem.getImageUrl(), false);
    }
}
