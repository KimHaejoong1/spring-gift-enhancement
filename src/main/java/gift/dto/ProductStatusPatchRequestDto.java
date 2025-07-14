package gift.dto;

import gift.domain.Product;

public record ProductStatusPatchRequestDto(Product.Status status) {
}
