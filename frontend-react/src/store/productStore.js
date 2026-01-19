import { create } from 'zustand'
import * as productService from '../services/productService'

export const useProductStore = create((set, get) => ({
  products: [],
  currentProduct: null,
  loading: false,
  error: null,
  page: 1,
  pageSize: 12,
  totalPages: 1,
  totalCount: 0,
  sortBy: 'name',
  sortOrder: 'asc',
  searchQuery: '',
  selectedCategory: null,
  minPrice: null,
  maxPrice: null,
  inStock: null,

  // Fetch all products with filters
  fetchProducts: async () => {
    const { page, pageSize, sortBy, sortOrder, searchQuery, selectedCategory, minPrice, maxPrice, inStock } = get()
    set({ loading: true, error: null })

    try {
      let data
      if (searchQuery) {
        data = await productService.searchProducts({
          query: searchQuery,
          category: selectedCategory,
          minPrice,
          maxPrice,
          inStock,
          page,
          pageSize,
        })
      } else if (selectedCategory) {
        data = await productService.getByCategory(selectedCategory, page, pageSize)
      } else {
        data = await productService.getAllProducts(page, pageSize, sortBy, sortOrder)
      }

      set({
        products: data.data,
        totalCount: data.pagination.totalCount,
        totalPages: data.pagination.totalPages,
        loading: false,
      })
    } catch (err) {
      set({ error: err.message, loading: false })
    }
  },

  // Fetch single product by ID
  fetchProductById: async (productId) => {
    set({ loading: true, error: null })

    try {
      const data = await productService.getProductById(productId)
      set({ currentProduct: data, loading: false })
    } catch (err) {
      set({ error: err.message, loading: false })
    }
  },

  // Update filters and reset to page 1
  setSearchQuery: (query) => {
    set({ searchQuery: query, page: 1 })
  },

  setCategory: (category) => {
    set({ selectedCategory: category, page: 1 })
  },

  setPriceRange: (min, max) => {
    set({ minPrice: min, maxPrice: max, page: 1 })
  },

  setStockFilter: (inStock) => {
    set({ inStock, page: 1 })
  },

  setSorting: (sortBy, sortOrder) => {
    set({ sortBy, sortOrder, page: 1 })
  },

  setPage: (page) => {
    set({ page })
  },

  setPageSize: (pageSize) => {
    set({ pageSize, page: 1 })
  },

  // Clear filters and search
  clearFilters: () => {
    set({
      searchQuery: '',
      selectedCategory: null,
      minPrice: null,
      maxPrice: null,
      inStock: null,
      page: 1,
      sortBy: 'name',
      sortOrder: 'asc',
    })
  },

  resetCurrentProduct: () => {
    set({ currentProduct: null })
  },
}))
