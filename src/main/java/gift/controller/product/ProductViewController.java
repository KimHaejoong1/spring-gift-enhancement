package gift.controller.product;

import gift.dto.product.ProductRequestDto;
import gift.dto.product.ProductResponseDto;
import gift.service.product.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/view")
public class ProductViewController {

    private final ProductService productService;

    public ProductViewController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String showProducts(Model model) {
        List<ProductResponseDto> productList = productService.findAll();

        model.addAttribute("productList", productList);

        if (!model.containsAttribute("requestDto")) {
            model.addAttribute("requestDto", new ProductRequestDto(null, 0, ""));
        }

        if (!model.containsAttribute("updateDto")) {
            model.addAttribute("updateDto", new ProductRequestDto(null, 0, ""));
        }

        if (!model.containsAttribute("productId")) {
            model.addAttribute("productId", null);
        }

        return "home";
    }

    @PostMapping("/products")
    public String createProduct(@ModelAttribute("requestDto") @Valid ProductRequestDto requestDto,
        BindingResult bindingResult,
        Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("openCreateModal", true);
            model.addAttribute("requestDto", requestDto);
            model.addAttribute("updateDto", new ProductRequestDto(null, 0, ""));
            model.addAttribute("productList", productService.findAll());
            return "home";
        }
        productService.create(requestDto);

        return "redirect:/view/products";
    }

    @PostMapping("/products/update/{id}")
    public String updateProduct(
        @PathVariable Long id,
        @ModelAttribute("updateDto") @Valid ProductRequestDto requestDto,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("openUpdateModal", true);
            redirectAttributes.addFlashAttribute("productId", id);
            redirectAttributes.addFlashAttribute("updateDto", requestDto);
            redirectAttributes.addFlashAttribute("productList", productService.findAll());
            redirectAttributes.addFlashAttribute(
                "org.springframework.validation.BindingResult.updateDto",
                bindingResult
            );

            if (!redirectAttributes.containsAttribute("requestDto")) {
                redirectAttributes.addFlashAttribute("requestDto",
                    new ProductRequestDto(null, 0, ""));
            }

            return "redirect:/view/products";
        }

        productService.update(id,
            new ProductRequestDto(requestDto.name(), requestDto.price(), requestDto.imageUrl()));

        return "redirect:/view/products";
    }

    @DeleteMapping("/products/{id}")
    public String deleteProduct(
        @PathVariable Long id,
        Model model
    ) {
        productService.delete(id);

        if (!model.containsAttribute("requestDto")) {
            model.addAttribute("requestDto", new ProductRequestDto(null, 0, ""));
        }

        if (!model.containsAttribute("updateDto")) {
            model.addAttribute("updateDto", new ProductRequestDto(null, 0, ""));
        }

        if (!model.containsAttribute("productId")) {
            model.addAttribute("productId", null);
        }

        return "home";
    }

}