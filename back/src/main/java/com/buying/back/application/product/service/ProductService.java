package com.buying.back.application.product.service;

import com.buying.back.application.account.code.exception.BrandException;
import com.buying.back.application.account.code.exception.BrandException.BrandExceptionCode;
import com.buying.back.application.account.domain.Brand;
import com.buying.back.application.account.repository.BrandRepository;
import com.buying.back.application.category.code.exception.CategoryException;
import com.buying.back.application.category.domain.Category;
import com.buying.back.application.category.repository.CategoryRepository;
import com.buying.back.application.common.exception.code.AuthenticationException;
import com.buying.back.application.common.exception.code.AuthenticationException.AuthenticationExceptionCode;
import com.buying.back.application.product.code.ProductExceptionCode;
import com.buying.back.application.product.controller.dto.ItemDto;
import com.buying.back.application.product.controller.dto.ProductDto;
import com.buying.back.application.product.domain.Product;
import com.buying.back.application.product.exception.ProductException;
import com.buying.back.application.product.helper.ProductItemHelper;
import com.buying.back.application.product.helper.ProductOptionHelper;
import com.buying.back.application.product.repository.ProductRepository;
import com.buying.back.application.product.service.vo.ItemVO;
import com.buying.back.application.product.service.vo.OptionVO;
import com.buying.back.application.product.service.vo.ProductDefaultVO;
import com.buying.back.application.product.service.vo.ProductDetailVO;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProductService {
  private final CategoryRepository categoryRepository;
  private final BrandRepository brandRepository;
  private final ProductRepository productRepository;
  private final ProductItemHelper productItemHelper;
  private final ProductOptionHelper productOptionHelper;

  public ProductDefaultVO getProduct(Long productId) {
    Product product = productRepository.findById(productId)
      .orElseThrow(() -> new ProductException(ProductExceptionCode.NOT_FOUND_PRODUCT));

//    List<ItemDetailVO> itemsByProduct = productItemHelper.getItemsByProduct(product);
//    List<OptionVO> productOptions = productOptionHelper.getProductOptions(product);

    return new ProductDetailVO(); // product, itemsByProduct, productOptions
  }

  @Transactional
  public ProductDetailVO createProduct(ProductDto.Create dto, Long brandId) {
    Brand brand = brandRepository.findById(brandId)
      .orElseThrow(() -> new BrandException(BrandExceptionCode.NOT_FOUND_BRAND));

    Category category = categoryRepository.findById(dto.getCategoryId())
      .orElseThrow(() -> new CategoryException(
        CategoryException.CategoryExceptionCode.NOT_FOUND_CATEGORY));

    Product product = Product.create(dto, brand, category);
    productRepository.save(product);

    List<ItemVO> itemVOList = productItemHelper.createItem(product, dto.getItemsDto());

    return ProductDetailVO.valueOf(product, itemVOList);
  }

  @Transactional
  public ProductDefaultVO updateProduct(Long brandId, Long productId, ProductDto.Update dto) {
    Brand brand = brandRepository.findById(brandId)
      .orElseThrow(() -> new BrandException(BrandExceptionCode.NOT_FOUND_BRAND));

    Product product = productRepository.findById(productId)
      .orElseThrow(() -> new ProductException(ProductExceptionCode.NOT_FOUND_PRODUCT));

    // 브랜드 검증
    if (!brand.equals(product.getBrand())) {
      throw new AuthenticationException(AuthenticationExceptionCode.NOT_AUTHORIZED);
    }

    // 상품 정보 및 카테고리 변경
    Category originCategory = product.getCategory();
    Category targetCategory = null;
    // 같은 카테고리인 경우 변경할 필요없음
    if (!originCategory.getId().equals(dto.getCategoryId())) {
      targetCategory = categoryRepository.findById(dto.getCategoryId())
        .orElseThrow(() -> new CategoryException(CategoryException.CategoryExceptionCode.NOT_FOUND_CATEGORY));
    }
    product.update(dto, targetCategory);
    productRepository.save(product);
    return ProductDefaultVO.valueOf(product);
  }

  @Transactional
  public void deleteProduct(Long productId) {
    Product product = productRepository.findById(productId)
      .orElseThrow(() -> new RuntimeException("NOT FOND PRODUCT"));
    productItemHelper.deleteItemByProduct(product);
    productOptionHelper.deleteOptionByProduct(product);
    productRepository.delete(product);
  }
}

