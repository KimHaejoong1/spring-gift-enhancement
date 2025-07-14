package gift.service;

import gift.common.code.CustomResponseCode;
import gift.common.exception.CustomException;
import gift.dto.WishRequest;
import gift.dto.WishResponse;
import gift.entity.Product;
import gift.entity.Wish;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishServiceImpl implements WishService {

    private final WishRepository wishRepository;
    private final ProductRepository productRepository;

    public WishServiceImpl(
        WishRepository wishRepository,
        @Qualifier("JDBC-Repo") ProductRepository productRepository
    ) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public WishResponse addWish(Long userId, WishRequest request) {
        Product product = productRepository.findById(request.productId())
            .orElseThrow(() -> new CustomException(CustomResponseCode.NOT_FOUND));

        boolean exists = wishRepository.existsByUserIdAndProductId(userId, request.productId());
        if (exists) {
            throw new CustomException(CustomResponseCode.ALREADY_EXISTS);
        }

        Wish savedWish = wishRepository.save(
            new Wish(null, userId, request.productId(), request.quantity()));

        return WishResponse.from(savedWish, product);
    }

    @Override
    @Transactional
    public void deleteWish(Long userId, Long productId) {
        boolean exists = wishRepository.existsByUserIdAndProductId(userId, productId);
        if (!exists) {
            throw new CustomException(CustomResponseCode.NOT_FOUND);
        }

        wishRepository.deleteByUserIdAndProductId(userId, productId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WishResponse> getWishes(Long userId) {
        List<Wish> wishes = wishRepository.findByUserId(userId);

        return wishes.stream()
            .map(wish -> {
                Product product = productRepository.findById(wish.getProductId())
                    .orElseThrow(() -> new CustomException(CustomResponseCode.NOT_FOUND));
                return WishResponse.from(wish, product);
            })
            .collect(Collectors.toList());
    }
}
