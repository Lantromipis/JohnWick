export function formatHour(hour: number): string {
  return (hour < 10 ? "0" + hour : hour) + ":00";
}

export function getCurrentWeekStartDay(): Date {
  const now = new Date();
  const currentDay = now.getDay();
  const diff = now.getDate() - currentDay + (currentDay == 0 ? -6 : 1);
  return new Date(now.setDate(diff));
}

export function getCurrentWeekEndDay(): Date {
  const startOfWeek = getCurrentWeekStartDay();
  const diff = startOfWeek.getDate() + 6;
  return new Date(startOfWeek.setDate(diff));
}

export function isSameDay(d1: Date, d2: Date) {
  return (
    d1.getFullYear() === d2.getFullYear() &&
    d1.getMonth() === d2.getMonth() &&
    d1.getDate() === d2.getDate()
  );
}

export function addDays(d: Date, daysToAdd: number): Date {
  const dateCopy = new Date(d.getTime());
  return new Date(dateCopy.setDate(d.getDate() + daysToAdd));
}
