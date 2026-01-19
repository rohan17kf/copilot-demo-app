import { useEffect } from 'react'
import { useProductStore } from '../store/productStore'

export const useProducts = () => {
  const {
    products,
    currentProduct,
    loading,
    error,
    page,
    pageSize,
    totalPages,
    totalCount,
    sortBy,
    sortOrder,
    searchQuery,
    selectedCategory,
    minPrice,
    maxPrice,
    inStock,
    fetchProducts,
    fetchProductById,
    setSearchQuery,
    setCategory,
    setPriceRange,
    setStockFilter,
    setSorting,
    setPage,
    setPageSize,
    clearFilters,
    resetCurrentProduct,
  } = useProductStore()

  // Fetch products when filters change
  useEffect(() => {
    fetchProducts()
  }, [page, pageSize, sortBy, sortOrder, searchQuery, selectedCategory, minPrice, maxPrice, inStock])

  return {
    products,
    currentProduct,
    loading,
    error,
    page,
    pageSize,
    totalPages,
    totalCount,
    sortBy,
    sortOrder,
    searchQuery,
    selectedCategory,
    minPrice,
    maxPrice,
    inStock,
    fetchProducts,
    fetchProductById,
    setSearchQuery,
    setCategory,
    setPriceRange,
    setStockFilter,
    setSorting,
    setPage,
    setPageSize,
    clearFilters,
    resetCurrentProduct,
  }
}
