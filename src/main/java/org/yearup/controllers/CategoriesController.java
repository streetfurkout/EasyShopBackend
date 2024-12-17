package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@CrossOrigin
public class CategoriesController
{
    private CategoryDao categoryDao;
    private ProductDao productDao;

    @Autowired
    public CategoriesController(CategoryDao categoryDao, ProductDao productDao) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }

    @RequestMapping(path="/categories", method = RequestMethod.GET)
    public List<Category> getAll()
    {
        return categoryDao.getAllCategories();
    }

    @RequestMapping(path="/categories/{id}", method = RequestMethod.GET)
    public Category getById(@PathVariable(name="id") int id, HttpServletResponse response)
    {
        Category category = categoryDao.getById(id);
        if (category == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return category;
    }

    @RequestMapping(path="/categories/{categoryId}/products", method = RequestMethod.GET)
    public List<Product> getProductsById(@PathVariable(name="categoryId") int categoryId, HttpServletResponse response)
    {
        List<Product> p = productDao.listByCategoryId(categoryId);

        if (p == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return p;
    }

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(path = "/categories", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Category addCategory(@RequestBody Category category)
    {
        System.out.println("Incoming category: " + category);

        Category category1 = categoryDao.create(category);
        System.out.println("Returned category: " + category1);

        return category1;
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(path = "/categories/{id}", method = RequestMethod.PUT)
    public void updateCategory(@PathVariable int id, @RequestBody Category category)
    {

        categoryDao.update(id, category);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(path = "/categories/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable int id)
    {
        categoryDao.delete(id);
    }
}