import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { useProducts } from '../hooks/useProducts'

export const ProductDetailPage = () => {
  const { productId } = useParams()
  const navigate = useNavigate()
  const { currentProduct, loading, error, fetchProductById, resetCurrentProduct } = useProducts()
  const [selectedImageIndex, setSelectedImageIndex] = useState(0)

  useEffect(() => {
    if (productId) {
      fetchProductById(productId)
    }
    return () => resetCurrentProduct()
  }, [productId, fetchProductById, resetCurrentProduct])

  const statusColors = {
    ACTIVE: 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200',
    INACTIVE: 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200',
    DISCONTINUED: 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200',
  }

  // Loading state
  if (loading) {
    return (
      <div className="min-h-screen bg-slate-50 dark:bg-slate-900">
        <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          <div className="animate-pulse">
            <div className="h-8 bg-slate-200 dark:bg-slate-700 rounded w-24 mb-8" />
            <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
              <div className="h-96 bg-slate-200 dark:bg-slate-700 rounded-lg" />
              <div className="space-y-4">
                <div className="h-8 bg-slate-200 dark:bg-slate-700 rounded w-3/4" />
                <div className="h-6 bg-slate-200 dark:bg-slate-700 rounded w-1/4" />
                <div className="h-40 bg-slate-200 dark:bg-slate-700 rounded" />
              </div>
            </div>
          </div>
        </div>
      </div>
    )
  }

  // Error state
  if (error || !currentProduct) {
    return (
      <div className="min-h-screen bg-slate-50 dark:bg-slate-900">
        <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          <button
            onClick={() => navigate('/')}
            className="inline-flex items-center gap-2 px-4 py-2 rounded-lg text-primary-600 dark:text-primary-400 hover:bg-primary-50 dark:hover:bg-primary-900/20 transition-colors mb-8 font-medium"
          >
            ← Back to products
          </button>
          <div className="text-center py-12">
            <p className="text-lg text-slate-600 dark:text-slate-400 mb-4">
              {error || 'Product not found'}
            </p>
            <button
              onClick={() => navigate('/')}
              className="inline-flex items-center gap-2 px-4 py-2 rounded-lg bg-primary-600 text-white hover:bg-primary-700 dark:hover:bg-primary-500 transition-colors font-medium"
            >
              Return to catalog
            </button>
          </div>
        </div>
      </div>
    )
  }

  const images = currentProduct.images || []
  const currentImage = images[selectedImageIndex] || null

  return (
    <div className="min-h-screen bg-slate-50 dark:bg-slate-900">
      <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Back button */}
        <button
          onClick={() => navigate('/')}
          className="inline-flex items-center gap-2 px-4 py-2 rounded-lg text-primary-600 dark:text-primary-400 hover:bg-primary-50 dark:hover:bg-primary-900/20 transition-colors mb-8 font-medium"
        >
          ← Back to products
        </button>

        {/* Product details */}
        <div className="bg-white dark:bg-slate-800 rounded-lg shadow-lg overflow-hidden">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-8 p-6 sm:p-8">
            {/* Images section */}
            <div>
              {/* Main image */}
              <div className="mb-4 bg-slate-100 dark:bg-slate-700 rounded-lg overflow-hidden flex items-center justify-center h-96 sm:h-[500px]">
                {currentImage?.url ? (
                  <img
                    src={currentImage.url}
                    alt={currentImage.alt || currentProduct.name}
                    className="w-full h-full object-cover"
                  />
                ) : (
                  <div className="w-full h-full flex flex-col items-center justify-center text-center px-4">
                    <svg className="w-20 h-20 text-slate-300 dark:text-slate-600 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                    </svg>
                    <p className="text-slate-500 dark:text-slate-400 font-medium">
                      {currentImage?.alt || 'No image available'}
                    </p>
                  </div>
                )}
              </div>

              {/* Thumbnail images */}
              {images.length > 1 && (
                <div className="grid grid-cols-4 gap-2">
                  {images.map((img, idx) => (
                    <button
                      key={idx}
                      onClick={() => setSelectedImageIndex(idx)}
                      className={`aspect-square rounded-lg overflow-hidden border-2 transition-colors ${
                        selectedImageIndex === idx
                          ? 'border-primary-600 dark:border-primary-500'
                          : 'border-slate-200 dark:border-slate-700 hover:border-slate-300 dark:hover:border-slate-600'
                      }`}
                    >
                      {img.url ? (
                        <img src={img.url} alt={img.alt} className="w-full h-full object-cover" />
                      ) : (
                        <div className="w-full h-full bg-slate-100 dark:bg-slate-700 flex items-center justify-center text-xs text-center text-slate-400 dark:text-slate-500 px-1">
                          {img.alt}
                        </div>
                      )}
                    </button>
                  ))}
                </div>
              )}
            </div>

            {/* Product info section */}
            <div>
              {/* Category and Status */}
              <div className="flex items-center gap-3 mb-4">
                <span className="text-sm font-semibold text-primary-600 dark:text-primary-400 uppercase tracking-wider">
                  {currentProduct.category}
                </span>
                <span className={`text-sm font-medium px-3 py-1 rounded-full ${statusColors[currentProduct.status] || statusColors.INACTIVE}`}>
                  {currentProduct.status}
                </span>
              </div>

              {/* Product name */}
              <h1 className="text-3xl sm:text-4xl font-bold text-slate-900 dark:text-white mb-4">
                {currentProduct.name}
              </h1>

              {/* Price and stock */}
              <div className="flex items-baseline gap-4 mb-6 pb-6 border-b border-slate-200 dark:border-slate-700">
                <div>
                  <span className="text-4xl font-bold text-slate-900 dark:text-white">
                    ${currentProduct.price?.toFixed(2)}
                  </span>
                </div>
                <div className={`text-lg font-semibold ${currentProduct.stockQuantity > 0 ? 'text-green-600 dark:text-green-400' : 'text-red-600 dark:text-red-400'}`}>
                  {currentProduct.stockQuantity > 0
                    ? `${currentProduct.stockQuantity} in stock`
                    : 'Out of stock'}
                </div>
              </div>

              {/* Description */}
              <div className="mb-6">
                <h2 className="text-lg font-semibold text-slate-900 dark:text-white mb-2">
                  Description
                </h2>
                <p className="text-slate-600 dark:text-slate-300 leading-relaxed">
                  {currentProduct.description || 'No description available'}
                </p>
              </div>

              {/* SKU */}
              <div className="mb-6">
                <h2 className="text-sm font-semibold text-slate-900 dark:text-white mb-2">
                  SKU
                </h2>
                <p className="text-slate-600 dark:text-slate-300 font-mono text-sm">
                  {currentProduct.sku || 'N/A'}
                </p>
              </div>

              {/* Attributes */}
              {currentProduct.attributes && Object.keys(currentProduct.attributes).length > 0 && (
                <div className="mb-6">
                  <h2 className="text-lg font-semibold text-slate-900 dark:text-white mb-3">
                    Attributes
                  </h2>
                  <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                    {Object.entries(currentProduct.attributes).map(([key, value]) => (
                      <div key={key} className="p-3 bg-slate-50 dark:bg-slate-700 rounded-lg">
                        <p className="text-xs font-semibold text-slate-600 dark:text-slate-400 uppercase tracking-wider mb-1">
                          {key}
                        </p>
                        <p className="text-slate-900 dark:text-white">
                          {value}
                        </p>
                      </div>
                    ))}
                  </div>
                </div>
              )}

              {/* Timestamps */}
              <div className="text-xs text-slate-500 dark:text-slate-400 space-y-1 pt-4 border-t border-slate-200 dark:border-slate-700">
                <p>Created: {new Date(currentProduct.createdAt).toLocaleDateString()}</p>
                {currentProduct.updatedAt && (
                  <p>Updated: {new Date(currentProduct.updatedAt).toLocaleDateString()}</p>
                )}
              </div>

              {/* Add to cart button */}
              <button
                disabled={currentProduct.stockQuantity === 0}
                className="w-full mt-6 px-6 py-3 rounded-lg bg-primary-600 text-white font-semibold hover:bg-primary-700 dark:hover:bg-primary-500 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
              >
                {currentProduct.stockQuantity > 0 ? 'Add to Cart' : 'Out of Stock'}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
