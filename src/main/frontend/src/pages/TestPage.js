import { api, authApi } from "api/api";
import { useEffect, useState } from "react";

function TestPage() {
  const [utc, setUtc] = useState("");
  const [local, setLocal] = useState("");

  const testApi = async () => {
    await authApi.get("/getDate").then((response) => {
      setUtc(response.data.UTC);
    });
  };

  useEffect(() => {
    testApi().then();
  });

  const utcToLocal = () => {
    const utcDate = new Date(utc);
    const offset = utcDate.getTimezoneOffset();
    const localDate = new Date(utcDate.getTime() + offset * 60000);
    setLocal(localDate.toISOString().replace("T", " ").slice(0, -5));
  };

  return (
    <div>
      {utc != "" ? (
        <div>
          <span>utc : {utc}</span>
          <button onClick={utcToLocal}>변환</button>
        </div>
      ) : null}
      <div>local : {local}</div>
    </div>
  );
}

export default TestPage;
