import { useDarkMode } from '../hooks/useDarkMode'

export const Header = () => {
  const { isDark, toggleDarkMode } = useDarkMode()

  return (
    <header className="sticky top-0 z-50 bg-white/90 dark:bg-slate-900/90 backdrop-blur border-b border-slate-200/80 dark:border-slate-800 shadow-lg">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="relative flex items-center h-20">
          {/* Title */}
          <div className="absolute left-1/2 -translate-x-1/2 flex flex-col items-center text-center z-10">
            <span className="text-2xl sm:text-4xl font-extrabold text-slate-900 dark:text-white tracking-tight drop-shadow-sm">
              ShopHub
            </span>
            <span className="mt-1 text-xs sm:text-sm font-medium text-slate-500 dark:text-slate-400 tracking-wide">
              Curate. Manage. Deliver.
            </span>
          </div>

          {/* Dark mode toggle */}
          {/* <button
            onClick={toggleDarkMode}
            className="p-2 rounded-lg bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-300 hover:bg-slate-200 dark:hover:bg-slate-700 transition-colors"
            aria-label="Toggle dark mode"
            style={{ marginLeft: 'auto' }}
          >
            {isDark ? (
              <svg className="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
                <path fillRule="evenodd" d="M10 2a1 1 0 011 1v1a1 1 0 11-2 0V3a1 1 0 011-1zm4 8a4 4 0 11-8 0 4 4 0 018 0zm-.464 4.536l.707.707a1 1 0 001.414-1.414l-.707-.707a1 1 0 00-1.414 1.414zm2.12-10.607a1 1 0 010 1.414l-.707.707a1 1 0 11-1.414-1.414l.707-.707a1 1 0 011.414 0zM17 11a1 1 0 100-2h-1a1 1 0 100 2h1zm-7 4a1 1 0 011 1v1a1 1 0 11-2 0v-1a1 1 0 011-1zM5.05 6.464A1 1 0 106.464 5.05l-.707-.707a1 1 0 00-1.414 1.414l.707.707zm5.657-9.193a1 1 0 00-1.414 0l-.707.707A1 1 0 005.05 6.464l.707-.707a1 1 0 001.414-1.414l-.707-.707zM5 8a1 1 0 100-2H4a1 1 0 000 2h1z" clipRule="evenodd" />
              </svg>
            ) : (
              <svg className="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
                <path d="M17.293 13.293A8 8 0 016.707 2.707a8.001 8.001 0 1010.586 10.586z" />
              </svg>
            )}
          </button> */}
        </div>
      </div>
    </header>
  )
}
