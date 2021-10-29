package com.codegym.controller;

import com.codegym.exception.NotFoundException;
import com.codegym.model.Category;
import com.codegym.model.Product;
import com.codegym.model.ProductForm;
import com.codegym.service.category.ICategoryService;
import com.codegym.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    @Value("${file-upload}")
    private String fileUpload;

    @ModelAttribute(name = "categories")
    public Iterable<Category> categories() {
        return categoryService.findAll();
    }

    @GetMapping
    public ModelAndView showAll(@RequestParam(name = "q", required = false) String name, @PageableDefault(size = 5) Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("/product/list");
        Page<Product> products;
        if (name == null) {
            products = productService.findAll(pageable);
        } else {
            products = productService.findByNameContaining(name, pageable);
        }
        modelAndView.addObject("products", products);
        return modelAndView;
    }

//    @GetMapping("/search")
//    public ModelAndView findByName(@RequestParam(name = "q") Optional<String> name) {
//        if (name.isPresent()) {
//            ModelAndView modelAndView = new ModelAndView("/product/list");
//            List<Product> products = productService.fin(name.get());
//            modelAndView.addObject("products", products);
//            return modelAndView;
//        }
//        return new ModelAndView("/error-404");
//    }

    @GetMapping("/create")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("/product/create");
        modelAndView.addObject("product", new ProductForm());
        return modelAndView;
    }

    @PostMapping("/save")
    public String createProduct(Model model, @Validated @ModelAttribute(name = "product") ProductForm productForm, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("product", productForm);
            return "/product/create";
        }
        MultipartFile multipartFile = productForm.getImage();
        String fileName = multipartFile.getOriginalFilename();
        try {
            FileCopyUtils.copy(productForm.getImage().getBytes(), new File(fileUpload + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Product product = new Product();
        product.setId(productForm.getId());
        product.setName(productForm.getName());
        product.setPrice(productForm.getPrice());
        product.setDescription(productForm.getDescription());
        product.setImage(fileName);
        product.setCategory(productForm.getCategory());
        productService.save(product);
        return "redirect:/product";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable Long id) throws NotFoundException {
        Optional<Product> product = productService.findById(id);
        if (!product.isPresent()) {
            throw new NotFoundException();
        } else {
            ModelAndView modelAndView = new ModelAndView("/product/edit");
            modelAndView.addObject("product", product);
            return modelAndView;
        }
    }

    @PostMapping("/edit")
    public ModelAndView editProduct(@ModelAttribute(name = "product") ProductForm productForm) {
        MultipartFile multipartFile = productForm.getImage();
        String fileName = multipartFile.getOriginalFilename();
        try {
            FileCopyUtils.copy(productForm.getImage().getBytes(), new File(fileUpload + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Product product = new Product();
        product.setId(productForm.getId());
        product.setName(productForm.getName());
        product.setPrice(productForm.getPrice());
        product.setDescription(productForm.getDescription());
        product.setImage(fileName);
        product.setCategory(productForm.getCategory());
        productService.save(product);
        return new ModelAndView("redirect:/product");
    }

    @GetMapping("/delete/{id}")
    public ModelAndView showDeleteForm(@PathVariable Long id) throws NotFoundException {
        Optional<Product> product = productService.findById(id);
        if (!product.isPresent()) {
            throw new NotFoundException();
        } else {
            ModelAndView modelAndView = new ModelAndView("/product/delete");
            modelAndView.addObject("product", product);
            return modelAndView;
        }
    }

    @PostMapping("/delete")
    public ModelAndView deleteProduct(@ModelAttribute ProductForm productForm) {
        productService.remove(productForm.getId());
        return new ModelAndView("redirect:/product");
    }

//    @ExceptionHandler(NotFoundException.class)
//    public ModelAndView notFound() {
//        return new ModelAndView("error-404");
//    }

}
