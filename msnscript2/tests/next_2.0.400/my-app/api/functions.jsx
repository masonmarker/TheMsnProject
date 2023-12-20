export async function getstuff(body) {
return (async () => {return await fetch('/api/getstuff').then(res => res.json())})()
}