package gift.controller.product;

import gift.domain.Product;
import gift.dto.product.ProductRequest;
import gift.service.product.ProductServiceAdmin;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/products")
public class ProductPageController {

    private final ProductServiceAdmin productServiceAdmin;

    public ProductPageController(ProductServiceAdmin productServiceAdmin) {
        this.productServiceAdmin = productServiceAdmin;
    }

    // 메인 페이지: 상품 목록 조회
    @GetMapping
    public String findAll(Model model) {
        List<Product> list = productServiceAdmin.getProductListAdmin();
        model.addAttribute("productList", list);
        return "product/productMain";
    }

    // 상품 등록 페이지 이동
    @GetMapping("/new")
    public String showNewProductForm(
        Model model
    ) {
        model.addAttribute("request", ProductRequest.NewForm());
        return "product/newProduct";
    }

    // 상품 등록
    @PostMapping
    public String createProduct(
        @Valid @ModelAttribute("request") ProductRequest request,
        BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return "product/newProduct";
        }

        productServiceAdmin.insertAdmin(Product.from(request));
        return "redirect:/admin/products";
    }

    // 상품 수정 페이지 이동
    @GetMapping("/update/{productId}")
    public String updateFormProduct(
        @PathVariable Long productId,
        Model model
    ) {
        Product product = productServiceAdmin.getProductByIdAdmin(productId);
        model.addAttribute("request", ProductRequest.from(product));
        return "product/updateProduct";
    }

    // 상품 수정(수정 처리)
    @PostMapping("/update")
    public String updateProduct(
        @Valid @ModelAttribute("request") ProductRequest request,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "product/updateProduct";
        }

        productServiceAdmin.updateAdmin(request);
        return "redirect:/admin/products";
    }

    // 상품 삭제
    @PostMapping("/delete/{productId}")
    public String deleteProduct(
        @PathVariable Long productId
    ) {
        productServiceAdmin.deleteByIdAdmin(productId);
        return "redirect:/admin/products";
    }
}
