package gift.service.product;

import gift.dto.product.ProductRequestDto;
import gift.dto.product.ProductResponseDto;
import gift.entity.Product;
import gift.repository.product.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductResponseDto> findAll() {
        List<ProductResponseDto> productResponseDtoList = new ArrayList<>();
        List<Product> productList = productRepository.findAll();

        for (Product product : productList) {
            productResponseDtoList.add(ProductResponseDto.from(product));
        }

        return productResponseDtoList;
    }

    @Override
    public ProductResponseDto create(ProductRequestDto requestDto) {
        Long id = productRepository.create(new Product(requestDto.name(), requestDto.price(), requestDto.imageUrl()));

        return new ProductResponseDto(id, requestDto.name(), requestDto.price(),
            requestDto.imageUrl());
    }

    @Override
    public ProductResponseDto findById(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ProductResponseDto.from(product);
    }

    @Override
    public ProductResponseDto update(Long id, ProductRequestDto requestDto) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        productRepository.update(id, requestDto);

        Product updatedProduct = productRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ProductResponseDto.from(updatedProduct);
    }

    @Override
    public void delete(Long id) {
        productRepository.delete(id);
    }
}
