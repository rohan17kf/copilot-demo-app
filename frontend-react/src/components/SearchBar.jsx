import { useEffect, useState } from 'react'

export const SearchBar = ({
  searchQuery,
  onSearchChange,
  selectedCategory,
  onCategoryChange,
  minPrice,
  maxPrice,
  onPriceChange,
  inStock,
  onStockChange,
  isLoading,
}) => {
  const [showFilters, setShowFilters] = useState(false)
  const [localMinPrice, setLocalMinPrice] = useState(minPrice || '')
  const [localMaxPrice, setLocalMaxPrice] = useState(maxPrice || '')

  // Sync local state with props
  useEffect(() => {
    setLocalMinPrice(minPrice || '')
    setLocalMaxPrice(maxPrice || '')
  }, [minPrice, maxPrice])

  const handlePriceApply = () => {
    onPriceChange(
      localMinPrice ? parseFloat(localMinPrice) : null,
      localMaxPrice ? parseFloat(localMaxPrice) : null
    )
  }

  return (
    <div className="bg-white dark:bg-slate-800 rounded-lg shadow-md p-6 mb-6">
      {/* Search input */}
      <div className="mb-4">
        <div className="relative">
          <svg className="absolute left-3 top-3 w-5 h-5 text-slate-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
          </svg>
          <input
            type="text"
            placeholder="Search products..."
            value={searchQuery}
            onChange={(e) => onSearchChange(e.target.value)}
            disabled={isLoading}
            className="w-full pl-10 pr-4 py-2 rounded-lg border border-slate-300 dark:border-slate-600 bg-white dark:bg-slate-700 text-slate-900 dark:text-white placeholder-slate-400 dark:placeholder-slate-500 focus:outline-none focus:ring-2 focus:ring-primary-500 disabled:opacity-50 disabled:cursor-not-allowed transition-all"
          />
        </div>
      </div>

      {/* Filter toggle button */}
      <button
        onClick={() => setShowFilters(!showFilters)}
        className="inline-flex items-center gap-2 px-4 py-2 rounded-lg bg-slate-100 dark:bg-slate-700 text-slate-700 dark:text-slate-300 hover:bg-slate-200 dark:hover:bg-slate-600 transition-colors font-medium text-sm"
      >
        <svg className="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
          <path fillRule="evenodd" d="M3 3a1 1 0 011-1h12a1 1 0 011 1v2a1 1 0 01-.293.707L13 9.414V17a1 1 0 01-1.447.894l-4-2A1 1 0 007 15V9.414L3.293 5.707A1 1 0 013 5V3z" clipRule="evenodd" />
        </svg>
        {showFilters ? 'Hide Filters' : 'Show Filters'}
      </button>

      {/* Filters panel */}
      {showFilters && (
        <div className="mt-4 pt-4 border-t border-slate-200 dark:border-slate-700">
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
            {/* Category filter */}
            <div>
              <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-2">
                Category
              </label>
              <input
                type="text"
                placeholder="e.g., Electronics"
                value={selectedCategory || ''}
                onChange={(e) => onCategoryChange(e.target.value || null)}
                disabled={isLoading}
                className="w-full px-3 py-2 rounded-lg border border-slate-300 dark:border-slate-600 bg-white dark:bg-slate-700 text-slate-900 dark:text-white placeholder-slate-400 dark:placeholder-slate-500 focus:outline-none focus:ring-2 focus:ring-primary-500 disabled:opacity-50 transition-all"
              />
            </div>

            {/* Min Price */}
            <div>
              <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-2">
                Min Price ($)
              </label>
              <input
                type="number"
                placeholder="0"
                value={localMinPrice}
                onChange={(e) => setLocalMinPrice(e.target.value)}
                disabled={isLoading}
                className="w-full px-3 py-2 rounded-lg border border-slate-300 dark:border-slate-600 bg-white dark:bg-slate-700 text-slate-900 dark:text-white placeholder-slate-400 dark:placeholder-slate-500 focus:outline-none focus:ring-2 focus:ring-primary-500 disabled:opacity-50 transition-all"
              />
            </div>

            {/* Max Price */}
            <div>
              <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-2">
                Max Price ($)
              </label>
              <input
                type="number"
                placeholder="10000"
                value={localMaxPrice}
                onChange={(e) => setLocalMaxPrice(e.target.value)}
                disabled={isLoading}
                className="w-full px-3 py-2 rounded-lg border border-slate-300 dark:border-slate-600 bg-white dark:bg-slate-700 text-slate-900 dark:text-white placeholder-slate-400 dark:placeholder-slate-500 focus:outline-none focus:ring-2 focus:ring-primary-500 disabled:opacity-50 transition-all"
              />
            </div>

            {/* In Stock filter */}
            <div>
              <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-2">
                Stock Status
              </label>
              <select
                value={inStock === null ? 'all' : inStock ? 'in-stock' : 'out-of-stock'}
                onChange={(e) => {
                  if (e.target.value === 'all') onStockChange(null)
                  else if (e.target.value === 'in-stock') onStockChange(true)
                  else onStockChange(false)
                }}
                disabled={isLoading}
                className="w-full px-3 py-2 rounded-lg border border-slate-300 dark:border-slate-600 bg-white dark:bg-slate-700 text-slate-900 dark:text-white focus:outline-none focus:ring-2 focus:ring-primary-500 disabled:opacity-50 transition-all"
              >
                <option value="all">All</option>
                <option value="in-stock">In Stock</option>
                <option value="out-of-stock">Out of Stock</option>
              </select>
            </div>
          </div>

          {/* Apply button */}
          <button
            onClick={handlePriceApply}
            disabled={isLoading}
            className="mt-4 px-4 py-2 rounded-lg bg-primary-600 text-white hover:bg-primary-700 dark:hover:bg-primary-500 disabled:opacity-50 disabled:cursor-not-allowed transition-colors font-medium text-sm"
          >
            Apply Filters
          </button>
        </div>
      )}
    </div>
  )
}
