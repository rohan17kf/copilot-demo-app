import { useEffect, useState } from 'react'

export const useDarkMode = () => {
  const [isDark, setIsDark] = useState(false)

  // Initialize from localStorage
  useEffect(() => {
    const saved = localStorage.getItem('darkMode')
    if (saved !== null) {
      setIsDark(JSON.parse(saved))
    } else {
      // Check system preference
      setIsDark(window.matchMedia('(prefers-color-scheme: dark)').matches)
    }
  }, [])

  // Apply theme to document
  useEffect(() => {
    if (isDark) {
      document.documentElement.classList.add('dark')
    } else {
      document.documentElement.classList.remove('dark')
    }
    localStorage.setItem('darkMode', JSON.stringify(isDark))
  }, [isDark])

  const toggleDarkMode = () => {
    setIsDark((prev) => !prev)
  }

  return { isDark, toggleDarkMode }
}
