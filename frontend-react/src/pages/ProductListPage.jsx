import { useEffect } from 'react'
import { useProducts } from '../hooks/useProducts'
import { ProductCard } from '../components/ProductCard'
import { SearchBar } from '../components/SearchBar'
import { Pagination } from '../components/Pagination'

export const ProductListPage = () => {
  const {
    products,
    loading,
    error,
    page,
    totalPages,
    searchQuery,
    selectedCategory,
    minPrice,
    maxPrice,
    inStock,
    setSearchQuery,
    setCategory,
    setPriceRange,
    setStockFilter,
    setPage,
    clearFilters,
  } = useProducts()

  useEffect(() => {
    // Initial fetch happens automatically through useProducts hook
  }, [])

  return (
    <div className="min-h-screen bg-slate-50 dark:bg-slate-900">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Header */}
        <div className="mb-8">
          <h1 className="text-2xl sm:text-3xl font-bold text-slate-900 dark:text-white mb-2">
            Our Products
          </h1>
          <p className="text-slate-600 dark:text-slate-400">
            Browse our extensive catalog of high-quality products
          </p>
        </div>

        {/* Search and filters */}
        <SearchBar
          searchQuery={searchQuery}
          onSearchChange={setSearchQuery}
          selectedCategory={selectedCategory}
          onCategoryChange={setCategory}
          minPrice={minPrice}
          maxPrice={maxPrice}
          onPriceChange={setPriceRange}
          inStock={inStock}
          onStockChange={setStockFilter}
          isLoading={loading}
        />

        {/* Active filters display */}
        {(searchQuery || selectedCategory || minPrice !== null || maxPrice !== null || inStock !== null) && (
          <div className="mb-6 flex flex-wrap items-center gap-2">
            <span className="text-sm font-medium text-slate-700 dark:text-slate-300">Active filters:</span>
            {searchQuery && (
              <span className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-primary-100 dark:bg-primary-900 text-primary-700 dark:text-primary-300 text-sm">
                Search: {searchQuery}
              </span>
            )}
            {selectedCategory && (
              <span className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-blue-100 dark:bg-blue-900 text-blue-700 dark:text-blue-300 text-sm">
                Category: {selectedCategory}
              </span>
            )}
            {(minPrice !== null || maxPrice !== null) && (
              <span className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-green-100 dark:bg-green-900 text-green-700 dark:text-green-300 text-sm">
                Price: ${minPrice || 0} - ${maxPrice || '∞'}
              </span>
            )}
            {inStock !== null && (
              <span className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-purple-100 dark:bg-purple-900 text-purple-700 dark:text-purple-300 text-sm">
                {inStock ? 'In Stock' : 'Out of Stock'}
              </span>
            )}
            <button
              onClick={clearFilters}
              className="ml-2 text-sm text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-white underline transition-colors"
            >
              Clear all
            </button>
          </div>
        )}

        {/* Error message */}
        {error && (
          <div className="mb-6 p-4 rounded-lg bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-800 text-red-700 dark:text-red-400">
            <p className="font-medium">Error loading products</p>
            <p className="text-sm mt-1">{error}</p>
          </div>
        )}

        {/* Loading state */}
        {loading && products.length === 0 && (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
            {[...Array(12)].map((_, i) => (
              <div key={i} className="bg-white dark:bg-slate-800 rounded-lg animate-pulse h-96">
                <div className="w-full h-48 bg-slate-200 dark:bg-slate-700" />
                <div className="p-4 space-y-3">
                  <div className="h-4 bg-slate-200 dark:bg-slate-700 rounded w-3/4" />
                  <div className="h-4 bg-slate-200 dark:bg-slate-700 rounded" />
                  <div className="h-4 bg-slate-200 dark:bg-slate-700 rounded w-1/2" />
                </div>
              </div>
            ))}
          </div>
        )}

        {/* Products grid */}
        {!loading && products.length > 0 && (
          <>
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6 mb-8">
              {products.map((product) => (
                <ProductCard key={product.id} product={product} />
              ))}
            </div>

            {/* Pagination */}
            {totalPages > 1 && (
              <Pagination
                page={page}
                totalPages={totalPages}
                onPageChange={setPage}
                isLoading={loading}
              />
            )}
          </>
        )}

        {/* Empty state */}
        {!loading && products.length === 0 && (
          <div className="text-center py-12">
            <svg className="w-16 h-16 text-slate-400 dark:text-slate-600 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4" />
            </svg>
            <h3 className="text-xl font-semibold text-slate-900 dark:text-white mb-2">
              No products found
            </h3>
            <p className="text-slate-600 dark:text-slate-400 mb-4">
              Try adjusting your filters or search terms
            </p>
            <button
              onClick={clearFilters}
              className="inline-flex items-center gap-2 px-4 py-2 rounded-lg bg-primary-600 text-white hover:bg-primary-700 dark:hover:bg-primary-500 transition-colors font-medium"
            >
              Clear filters
            </button>
          </div>
        )}
      </div>
    </div>
  )
}
