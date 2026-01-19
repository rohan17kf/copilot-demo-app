export const Pagination = ({ page, totalPages, onPageChange, isLoading }) => {
  const startPage = Math.max(1, page - 2)
  const endPage = Math.min(totalPages, page + 2)
  const pages = []

  for (let i = startPage; i <= endPage; i++) {
    pages.push(i)
  }

  return (
    <div className="flex items-center justify-center gap-2 mt-8">
      {/* Previous button */}
      <button
        onClick={() => onPageChange(page - 1)}
        disabled={page === 1 || isLoading}
        className="px-3 py-2 rounded-lg border border-slate-300 dark:border-slate-600 text-slate-700 dark:text-slate-300 hover:bg-slate-100 dark:hover:bg-slate-800 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
      >
        ← Previous
      </button>

      {/* Page numbers */}
      <div className="flex items-center gap-1">
        {startPage > 1 && (
          <>
            <button
              onClick={() => onPageChange(1)}
              disabled={isLoading}
              className="px-3 py-2 rounded-lg border border-slate-300 dark:border-slate-600 text-slate-700 dark:text-slate-300 hover:bg-slate-100 dark:hover:bg-slate-800 disabled:opacity-50 transition-colors"
            >
              1
            </button>
            {startPage > 2 && <span className="text-slate-400 dark:text-slate-500">...</span>}
          </>
        )}

        {pages.map((p) => (
          <button
            key={p}
            onClick={() => onPageChange(p)}
            disabled={isLoading}
            className={`px-3 py-2 rounded-lg border transition-colors disabled:opacity-50 ${
              p === page
                ? 'bg-primary-600 text-white border-primary-600 dark:bg-primary-500 dark:border-primary-500'
                : 'border-slate-300 dark:border-slate-600 text-slate-700 dark:text-slate-300 hover:bg-slate-100 dark:hover:bg-slate-800'
            }`}
          >
            {p}
          </button>
        ))}

        {endPage < totalPages && (
          <>
            {endPage < totalPages - 1 && <span className="text-slate-400 dark:text-slate-500">...</span>}
            <button
              onClick={() => onPageChange(totalPages)}
              disabled={isLoading}
              className="px-3 py-2 rounded-lg border border-slate-300 dark:border-slate-600 text-slate-700 dark:text-slate-300 hover:bg-slate-100 dark:hover:bg-slate-800 disabled:opacity-50 transition-colors"
            >
              {totalPages}
            </button>
          </>
        )}
      </div>

      {/* Next button */}
      <button
        onClick={() => onPageChange(page + 1)}
        disabled={page === totalPages || isLoading}
        className="px-3 py-2 rounded-lg border border-slate-300 dark:border-slate-600 text-slate-700 dark:text-slate-300 hover:bg-slate-100 dark:hover:bg-slate-800 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
      >
        Next →
      </button>

      {/* Page info */}
      <div className="ml-4 text-sm text-slate-600 dark:text-slate-400">
        Page {page} of {totalPages}
      </div>
    </div>
  )
}
