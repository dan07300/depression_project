import defaultSettings from '@/settings'

const title = defaultSettings.title || '心理健康平台'

export default function getPageTitle(pageTitle) {
  if (pageTitle) {
    return `${pageTitle} - ${title}`
  }
  return `${title}`
}
