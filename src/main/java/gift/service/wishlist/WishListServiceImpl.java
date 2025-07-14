package gift.service.wishlist;

import gift.dto.product.ProductResponseDto;
import gift.entity.Product;
import gift.entity.Wish;
import gift.exception.ResourceNotFoundException;
import gift.repository.product.ProductRepository;
import gift.repository.wishlist.WishListRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class WishListServiceImpl implements WishListService {

    private final WishListRepository wishListRepository;
    private final ProductRepository productRepository;

    public WishListServiceImpl(WishListRepository wishListRepository,
        ProductRepository productRepository) {
        this.wishListRepository = wishListRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Wish create(Long productId, Long memberId) {
        Wish wish = wishListRepository.create(
            new Wish(productId, memberId));

        return wish;
    }

    @Override
    public List<ProductResponseDto> findAll(Long memberId) {
        List<Wish> wishList = wishListRepository.findAll(memberId);
        List<Long> idList = wishList.stream()
            .map(Wish::getProductId)
            .toList();

        List<Product> productList = productRepository.findAllById(idList);
        List<ProductResponseDto> responseDtoList = productList.stream()
            .map(ProductResponseDto::from)
            .toList();

        return responseDtoList;
    }

    @Override
    public void delete(Long productId, Long memberId) {
        int deleteRow = wishListRepository.delete(productId, memberId);

        if (deleteRow <= 0) {
            throw new ResourceNotFoundException();
        }
    }
}
