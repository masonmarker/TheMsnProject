export async function getstuff(body) {
return await fetch('/api/getstuff').then(res => res.json())
}