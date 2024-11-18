export function isoStringToPrettyDateTime(isoDateTime?: string): string {
  if (!isoDateTime) {
    return "";
  }

  try {
    return new Date(isoDateTime).toLocaleString("ru-RU");
  } catch (err) {
    return isoDateTime;
  }
}
