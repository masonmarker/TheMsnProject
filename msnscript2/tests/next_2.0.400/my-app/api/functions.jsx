export async function getRandomFact(body) {
return await fetch('/api/getRandomFact').then(res => res.json())
}