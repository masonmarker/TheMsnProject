// Generated at 1720204289.0082545
// appwide styles
import '../styles/app.css'

// imports ::
import { ChakraProvider } from '@chakra-ui/react';

// main app component
function MyApp({ Component, pageProps }) {
return (
// return ::
<ChakraProvider>

<Component {...pageProps} />
{/** endreturn */}
</ChakraProvider>

)}

export default MyApp
