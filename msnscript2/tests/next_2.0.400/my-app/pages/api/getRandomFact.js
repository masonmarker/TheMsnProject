export default async function getRandomFact(req, res)          {
const ret = (await (await fetch('https://uselessfacts.jsph.pl/api/v2/facts/random?language=en')).json());res.status(200).json(ret)
}