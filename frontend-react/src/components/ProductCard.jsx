import { Link } from 'react-router-dom'

export const ProductCard = ({ product }) => {
  const image = product.images?.find((img) => img.primary) || product.images?.[0]
  const statusColors = {
    ACTIVE: 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200',
    INACTIVE: 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200',
    DISCONTINUED: 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200',
  }

  return (
    <Link to={`/product/${product.id}`}>
      <div className="bg-white dark:bg-slate-800 rounded-lg shadow-md hover:shadow-lg dark:hover:shadow-slate-700/50 transition-shadow h-full flex flex-col overflow-hidden group cursor-pointer">
        {/* Image Container */}
        <div className="relative w-full h-48 bg-slate-100 dark:bg-slate-700 overflow-hidden flex items-center justify-center">
          {image?.url ? (
            <img
              src={image.url}
              alt={image.alt || product.name}
              className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
            />
          ) : (
            <div className="w-full h-full flex flex-col items-center justify-center text-center px-4">
              <svg className="w-12 h-12 text-slate-300 dark:text-slate-600 mb-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
              </svg>
              <p className="text-sm text-slate-500 dark:text-slate-400 font-medium">
                {image?.alt || 'No image available'}
              </p>
            </div>
          )}
        </div>

        {/* Content */}
        <div className="flex-1 p-4 flex flex-col">
          {/* Category and Status */}
          <div className="flex items-center justify-between mb-2 gap-2">
            <span className="text-xs font-semibold text-primary-600 dark:text-primary-400 truncate">
              {product.category}
            </span>
            <span className={`text-xs font-medium px-2 py-1 rounded whitespace-nowrap ${statusColors[product.status] || statusColors.INACTIVE}`}>
              {product.status}
            </span>
          </div>

          {/* Product Name */}
          <h3 className="text-lg font-semibold text-slate-900 dark:text-white mb-2 line-clamp-2 group-hover:text-primary-600 dark:group-hover:text-primary-400 transition-colors">
            {product.name}
          </h3>

          {/* Description */}
          <p className="text-sm text-slate-600 dark:text-slate-400 line-clamp-2 mb-4 flex-1">
            {product.description || 'No description available'}
          </p>

          {/* Stock and Price */}
          <div className="flex items-center justify-between mt-auto pt-4 border-t border-slate-200 dark:border-slate-700">
            <div className="flex-1">
              <p className="text-xs text-slate-500 dark:text-slate-400 mb-1">Price</p>
              <p className="text-xl font-bold text-slate-900 dark:text-white">
                ${product.price?.toFixed(2)}
              </p>
            </div>
            <div className="text-right">
              <p className="text-xs text-slate-500 dark:text-slate-400 mb-1">Stock</p>
              <p className={`text-lg font-semibold ${product.stockQuantity > 0 ? 'text-green-600 dark:text-green-400' : 'text-red-600 dark:text-red-400'}`}>
                {product.stockQuantity}
              </p>
            </div>
          </div>
        </div>
      </div>
    </Link>
  )
}
