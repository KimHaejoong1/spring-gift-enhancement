package gift.view;

import gift.global.annotation.OnlyForAdmin;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Controller
public class ViewController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/products")
    public String products() {
        return  "all-product";
    }

    @GetMapping("/wishlist")
    public String wishlist() {
        return "wishlist";
    }

    @GetMapping("/my/products")
    public String myProducts() {
        return "my-product";
    }

    @GetMapping("/my/products/{id}")
    public String editProduct(@PathVariable UUID id, Model model){

        model.addAttribute("productId", id);

        return "edit-my-product";
    }

    @OnlyForAdmin
    @GetMapping("/admin/products")
    public String adminProducts(){
        return "admin-product";
    }

    @OnlyForAdmin
    @GetMapping("/admin/members")
    public String adminMembers(){
        return "admin-member";
    }

    @OnlyForAdmin
    @GetMapping("/admin/products/{id}")
    public String editProductForAdmin(@PathVariable UUID id, Model model){

        model.addAttribute("productId", id);

        return "edit-admin-product";
    }

    @OnlyForAdmin
    @GetMapping("/admin/members/add")
    public String addMember(){
        return "add-member";
    }

    @OnlyForAdmin
    @GetMapping("/admin/members/{id}")
    public String editMember(@PathVariable UUID id, Model model){
        model.addAttribute("memberId", id);

        return  "edit-member";
    }

}
