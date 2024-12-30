package sit.int202.int202_project.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sit.int202.int202_project.entities.Product;
import sit.int202.int202_project.services.ProductService;
import sit.int202.int202_project.services.ProductlineService;


@Controller
public class ProductController {
    private final ProductService service;
    private final ProductlineService productlineService;

    public ProductController(ProductService service, ProductlineService productlineService) {
        this.service = service;
        this.productlineService = productlineService;
    }

    @GetMapping("")
    public String getAllProductLines(Model model) {
        model.addAttribute("productLines", productlineService.getAllProductline());
        return "index";
    }

    @GetMapping("/products")
    public String getAllProductsPaging(@RequestParam(defaultValue = "0") int pageNumber,
                                       @RequestParam(defaultValue = "10") int pageSize,
                                       @RequestParam(defaultValue = "") String productLine,
                                       @RequestParam(defaultValue = "productCode") String sortBy,
                                       @RequestParam(defaultValue = "desc") String sortDirection,
                                       Model model) {
        Page<?> page;
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        if (productLine.isEmpty()) {
            // Fetch all products when productLine is empty
            page = service.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(direction,sortBy)));
        } else {
            // Fetch products by productLine
            page = service.findByProductLine(productLine, PageRequest.of(pageNumber, pageSize, Sort.by(direction,sortBy)));
            model.addAttribute("productLine", productLine);
            model.addAttribute("product", productlineService.getProductline(productLine));
        }
        model.addAttribute("productLines", productlineService.getAllProductline());
        model.addAttribute("page", page);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDirection", sortDirection);
        return "product_list_paging";
    }
    @GetMapping("/create")
    public String createForm() {
        return "create_form";
    }
    @PostMapping("/create")
    public String createProduct(Product product, RedirectAttributes redirectAttributes) {
        Product p = service.addProduct(product);
        if (p == null) {
            redirectAttributes.addFlashAttribute("alertWarning", "flex");
            redirectAttributes.addFlashAttribute("message", "Product code " + product.getProductCode() + " already exists");
            redirectAttributes.addFlashAttribute("product", product);
            return "redirect:/create";
        }
        else {
            redirectAttributes.addFlashAttribute("alert", "flex");
            redirectAttributes.addFlashAttribute("message", "Product created successfully");
        }
        return "redirect:/products?productLine=" + product.getProductLine().getProductLine();
    }

    @GetMapping("/update")
    public String updateForm(@RequestParam String productCode,
                             Model model) {
        Product product = service.getProduct(productCode);
        model.addAttribute("product", product);
        return "update_form";
    }
    @PostMapping("/update")
    public String updateProduct(Product product,
                                RedirectAttributes redirectAttributes) {
        service.updateProduct(product);
        redirectAttributes.addFlashAttribute("alert", "flex");
        redirectAttributes.addFlashAttribute("message", "Product updated successfully");
        return "redirect:/products?productLine=" + product.getProductLine().getProductLine();
    }
    @GetMapping("/remove")
    public String removeProduct(@RequestParam String productCode, Model model,
                                RedirectAttributes redirectAttributes,
                                HttpServletRequest request) {
        try {
            Product product = service.deleteProduct(productCode);
            model.addAttribute("product", product);
            redirectAttributes.addFlashAttribute("alert", "flex");
            redirectAttributes.addFlashAttribute("message", "Product removed successfully");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("alertError", "flex");
            redirectAttributes.addFlashAttribute("error", "Cannot delete product: It is referenced in order details.");
        }
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/products");
    }
}

