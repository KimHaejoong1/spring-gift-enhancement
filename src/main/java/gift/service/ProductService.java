package gift.service;

import gift.domain.Product;
import gift.exception.ProductNotFoundException;
import gift.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Product create(String name, int price, String imageUrl) {
        validateNameContainKakao(name);
        return repository.save(name, price, imageUrl);
    }

    public Product findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public List<Product> findAllByPage(int page, int size, String sort, Long categoryId) {
        return repository.findAll().stream()
                .filter(product -> categoryId == null || product.getCategoryId().equals(categoryId))
                .sorted(createComparator(sort))
                .skip((long) page * size)
                .limit(size)
                .toList();
    }


    public List<Product> findAllProducts(String sort, String keyword) {
        return repository.findAll().stream()
                .filter(p -> matchesKeyword(p, keyword))
                .sorted(createComparator(sort))
                .toList();
    }

    private boolean matchesKeyword(Product p, String keyword) {
        if (keyword == null || keyword.isBlank()) return true;

        return p.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                String.valueOf(p.getId()).equals(keyword);
    }

    private Comparator<Product> createComparator(String sort) {
        String[] parts = sort.split(",");
        String key = parts[0];
        boolean ascending = parts.length < 2 || parts[1].equalsIgnoreCase("asc");

        Comparator<Product> comparator = switch (key) {
            case "name" -> Comparator.comparing(Product::getName);
            case "price" -> Comparator.comparingInt(Product::getPrice);
            default -> Comparator.comparing(Product::getId);
        };

        return ascending ? comparator : comparator.reversed();
    }


    public void delete(Long id) {

        int deletedCount = repository.deleteById(id);

        // 삭제된 항목이 없어도 예외 없이 정상 처리 (멱등성 보장)

        // 여러 상품이 삭제 되어버린 경우
        if (deletedCount > 1) {
            throw new IllegalStateException("ID 중복으로 인해 여러 상품이 삭제되었습니다.");
        }
    }

    public void update(Long id, String name, int price, String imageUrl) {

        validateNameContainKakao(name);

        // 상품 존재 여부 확인
        if (!repository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }

        int updatedCount = repository.update(id, name, price, imageUrl);

        // 상품이 존재 하지만 수정되지 않은 경우
        if (updatedCount == 0) {
            return;
        }

        // 여러 상품이 수정 되어버린 경우
        if (updatedCount > 1) {
            throw new IllegalStateException("ID 중복으로 인해 여러 상품이 수정되었습니다.");
        }
    }

    private void validateNameContainKakao(String name) {
        if (name.contains("카카오")) {
            throw new IllegalArgumentException("'카카오'가 포함된 상품명은 MD와 협의 후 등록 가능합니다.");
        }
    }

}
