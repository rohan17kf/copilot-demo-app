import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import { Header } from './components/Header'
import { ProductListPage } from './pages/ProductListPage'
import { ProductDetailPage } from './pages/ProductDetailPage'

function App() {
  return (
    <Router>
      <div className="flex flex-col min-h-screen bg-slate-50 dark:bg-slate-900">
        <Header />
        <main className="flex-1">
          <Routes>
            <Route path="/" element={<ProductListPage />} />
            <Route path="/product/:productId" element={<ProductDetailPage />} />
          </Routes>
        </main>
        <footer className="bg-white dark:bg-slate-800 border-t border-slate-200 dark:border-slate-700 py-6 mt-12">
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center text-sm text-slate-600 dark:text-slate-400">
            <p>&copy; 2026 Product Catalog. All rights reserved.</p>
          </div>
        </footer>
      </div>
    </Router>
  )
}

export default App