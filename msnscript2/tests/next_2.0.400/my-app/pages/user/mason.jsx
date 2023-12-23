export default function Index(props) {
  return (() => {
    return <div style={{'display': 'flex', 'justifyContent': 'center', 'alignItems': 'center', 'textAlign': 'center'}}><div style={{'display': 'flex', 'flexDirection': 'column'}}><h1 style={{'color': 'red'}}>this is at users/mason.jsx</h1><Link href={`/`}>go back home</Link></div></div>
  })()
}