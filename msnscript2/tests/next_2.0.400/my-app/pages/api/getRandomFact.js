export default async function getRandomFact(req, res)          {
(() => {return fetch('https://uselessfacts.jsph.pl/api/v2/facts/random?language=en').then((response) => {response.json().then((data) => {res.json(data.text)})})})()
}