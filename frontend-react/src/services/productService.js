const BASE_URL = 'http://localhost:8080'

// Fetch all products with pagination and sorting
export const getAllProducts = async (page = 1, pageSize = 12, sortBy = 'name', sortOrder = 'asc') => {
  const response = await fetch(
    `${BASE_URL}/products?page=${page}&pageSize=${pageSize}&sortBy=${sortBy}&sortOrder=${sortOrder}`
  )

  if (!response.ok) {
    throw new Error(`Failed to fetch products: ${response.statusText}`)
  }

  return response.json()
}

// Fetch product by ID
export const getProductById = async (productId) => {
  const response = await fetch(`${BASE_URL}/products/${productId}`)

  if (!response.ok) {
    throw new Error(`Failed to fetch product: ${response.statusText}`)
  }

  return response.json()
}

// Search products with filters
export const searchProducts = async ({
  query = '',
  category = null,
  minPrice = null,
  maxPrice = null,
  inStock = null,
  page = 1,
  pageSize = 12,
} = {}) => {
  const params = new URLSearchParams()
  if (query) params.append('query', query)
  if (category) params.append('category', category)
  if (minPrice !== null) params.append('minPrice', minPrice)
  if (maxPrice !== null) params.append('maxPrice', maxPrice)
  if (inStock !== null) params.append('inStock', inStock)
  params.append('page', page)
  params.append('pageSize', pageSize)

  const response = await fetch(`${BASE_URL}/products/search?${params.toString()}`)

  if (!response.ok) {
    throw new Error(`Failed to search products: ${response.statusText}`)
  }

  return response.json()
}

// Fetch products by category
export const getByCategory = async (category, page = 1, pageSize = 12) => {
  const response = await fetch(
    `${BASE_URL}/products/category/${encodeURIComponent(category)}?page=${page}&pageSize=${pageSize}`
  )

  if (!response.ok) {
    throw new Error(`Failed to fetch products by category: ${response.statusText}`)
  }

  return response.json()
}

// Health check
export const getHealth = async () => {
  const response = await fetch(`${BASE_URL}/products/health`)

  if (!response.ok) {
    throw new Error(`Health check failed: ${response.statusText}`)
  }

  return response.json()
}
