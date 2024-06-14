/**
 * App component.
 *
 * Don't edit comments ending with '::',
 * they're used by MSN2 to update this file.
 *
 * (generated by MSN2) at
 * tests/next_2.0.400/my-app/pages/index.jsx
 *
 * @author {your name}
 * @date {current date}
 */

// imports ::
import { useEffect } from "react";
import { useState } from "react";

// default component export ::
export default function Index(props) {
  return (() => {
    function ApiResponse(key) {
      return (() => {
        const [responseLoading, setResponseloading] = useState(true);
        const [response, setResponse] = useState(`API response loading...`);
        useEffect(() => {
          (async () => {
            setResponse(
              await fetch("/api/getRandomFact").then((res) => res.json())
            );
            setResponseloading(false);
          })();
        }, []);
        return (
          <div
            style={{ display: "flex", flexDirection: "row" }}
            key={(() => {
              return key;
            })()}
          >
            <h4>API responded with:</h4>
            <h4 style={{ color: "lightgreen" }}>{response.text}</h4>
          </div>
        );
      })();
    }
    const apiResponseCount = 3;
    return (
      <div
        style={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          textAlign: "center",
        }}
      >
        <div style={{ display: "flex", flexDirection: "column" }}>
          <h1>
            MSN2 with{" "}
            <span style={{ fontWeight: "bold", color: "red" }}>NextJS</span>
          </h1>
          <div>
            {[
              ApiResponse(763369835545001),
              ApiResponse(763369837570002),
              ApiResponse(763369838526003),
            ]}
          </div>
        </div>
      </div>
    );
  })();
}
