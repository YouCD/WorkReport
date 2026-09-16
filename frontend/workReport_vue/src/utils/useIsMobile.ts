import { ref, onMounted, onBeforeUnmount } from 'vue'

const query = typeof window !== 'undefined' ? window.matchMedia('(max-width: 768px)') : null

export function useIsMobile() {
  const isMobile = ref(query ? query.matches : false)

  function onChange(e: MediaQueryListEvent) {
    isMobile.value = e.matches
  }

  onMounted(() => {
    query?.addEventListener('change', onChange)
  })

  onBeforeUnmount(() => {
    query?.removeEventListener('change', onChange)
  })

  return { isMobile }
}
